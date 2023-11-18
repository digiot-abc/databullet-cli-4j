package databullet.domain.generate;

import databullet.domain.definition.generate.GenerateColumn;
import databullet.domain.definition.generate.GenerateDefinition;
import databullet.domain.definition.generate.GenerateTable;
import databullet.domain.generate.generators.Generator;
import databullet.domain.generate.generators.GeneratorRepository;
import databullet.domain.generate.persistance.Persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GenerateProcessor {

  static long maxMemory = Runtime.getRuntime().maxMemory();
  static long batchSize = 1;//maxMemory / 1000;
  GenerateStore store;

  public GenerateProcessor(GenerateStore store) {
    this.store = store;
  }

  public void generate(GenerateDefinition definitions, Persistence persistence) {

    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    try {
      definitions.getRelationGroups().stream().parallel().forEach(defGroup -> {
        for (GenerateTable table : defGroup.getGenTables()) {
          System.out.println(table.getName());
          generate(table, persistence, executorService);
        }
      });
    } finally {
      if (!executorService.isShutdown()) {
        executorService.shutdown();
      }
    }
  }

  public void generate(GenerateTable generateTable, Persistence persistence, ExecutorService executorService) {
    generate(generateTable, persistence, executorService, 0, generateTable.getRowCount());
  }

  public void generate(GenerateTable generateTable, Persistence persistence,
                       ExecutorService executorService, int rowOffsetIndex, int rowLimit) {

    if (store.isFinished(generateTable)) {
      return;
    }

    // ヘッダ書き込み
    persistence.persist(generateTable, List.of(GenerateRecord.header(generateTable)));

    List<GenerateRecord> records = new ArrayList<>();
    int rowCount = generateTable.getRowCount();
    try {

      for (int i = rowOffsetIndex; i < rowLimit; i++) {

        List<CompletableFuture<String>> futureList = new ArrayList<>();
        for (GenerateColumn column : generateTable.getColumns()) {
          futureList.add(CompletableFuture.supplyAsync(() -> {

            Object value = store.relationColumnValue.getOrDefault(column, generate(column));
            setRelationValueRecursiveChildren(column, value);

            // DEBUGING
            if (value == null) {
              System.out.println("---");
              System.out.println(column.hasParent() ? column.getRelationParent().getOwnerTable().getName().concat(".").concat(column.getRelationParent().getName()) : "");
              System.out.println(column.getOwnerTable().getName().concat(".").concat(column.getName()));
              System.out.println("---");
              return "";
            }
            return value.toString();
          }, executorService));
        }

        GenerateRecord record = new GenerateRecord();
        for (CompletableFuture<String> future : futureList) {
          record.append(generateTable.getColumns().get(futureList.indexOf(future)), future.get());
        }

        records.add(record);
        handleBatchPersistence(i, rowCount, records, generateTable, persistence);
      }
      finalBatchPersistence(rowCount, records, generateTable, persistence);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Object generate(GenerateColumn generateColumn) {
    Generator<?, ?> generator = GeneratorRepository.findByColumn(generateColumn).or(() -> {
      Generator<?, ?> gen = GeneratorFactory.create(generateColumn);
      GeneratorRepository.save(generateColumn, gen);
      return Optional.of(gen);
    }).get();
    return generator.generate();
  }

  public void setRelationValueRecursiveChildren(GenerateColumn column, Object value) {
    if (!column.getRelationChildren().isEmpty()) {
      for (GenerateColumn child : column.getRelationChildren()) {
        store.relationColumnValue.put(child, value);
        setRelationValueRecursiveChildren(child, value);
      }
    }
  }

  private void handleBatchPersistence(int currentIndex, int totalRowCount,
                                      List<GenerateRecord> records, GenerateTable generateTable,
                                      Persistence persistence) {
    if (records.size() >= batchSize || currentIndex == totalRowCount - 1) {
      generateTable.getChildTables().stream().parallel().forEach(c -> generate(c, persistence, Executors.newFixedThreadPool(c.getColumnCount())));
      persistence.persist(generateTable, records);
      records.clear();
    }
  }

  private void finalBatchPersistence(int totalRowCount, List<GenerateRecord> records,
                                     GenerateTable generateTable, Persistence persistence) {
    if (!records.isEmpty()) {
      generateTable.getChildTables().stream().parallel().forEach(c -> generate(c, persistence, Executors.newFixedThreadPool(c.getColumnCount())));
      persistence.persist(generateTable, records);
      records.clear();
    }

    store.finish(generateTable);
  }
}
