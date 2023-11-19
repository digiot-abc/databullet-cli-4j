package databullet.domain.generate;

import databullet.domain.definition.generate.GenerateColumn;
import databullet.domain.definition.generate.GenerateDefinition;
import databullet.domain.definition.generate.GenerateTable;
import databullet.domain.generate.generators.Generator;
import databullet.domain.generate.persistance.Persistence;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class GenerateProcessor {

  private static final Logger logger = LogManager.getLogger(GenerateProcessor.class);

  GenerateStore store;

  public GenerateProcessor(GenerateStore store) {
    this.store = store;
  }

  public void generate(GenerateDefinition definitions, Persistence persistence) {

    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    try {
      definitions.getRelationGroups().stream().parallel().forEach(defGroup -> {
        for (GenerateTable table : defGroup.getGenTables()) {
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

  @SneakyThrows
  public void generate(GenerateTable generateTable, Persistence persistence,
                       ExecutorService executorService, final int rowOffsetIndex, final int rowLimit) {

    if (store.isFinished(generateTable)) {
      return;
    }

    if (!store.isInitialized(generateTable)) {
      persistence.persist(generateTable, List.of(GenerateRecord.header(generateTable)));
      store.initialize(generateTable);
    }

    List<GenerateRecord> records = new ArrayList<>();
    for (int i = rowOffsetIndex; i < rowLimit; i++) {

      final int index = i;

      List<CompletableFuture<String>> futureList = new ArrayList<>();
      for (GenerateColumn column : generateTable.getColumns()) {
        futureList.add(CompletableFuture.supplyAsync(() -> {
          store.setCurrentProcessing(column, index);

          // 自身の親カラムの値が生成されていなかった場合、親カラムとそのカラムに紐づく子カラムすべてに同じ値を設定する
          if (column.hasParent() &&
                  !store.isFinished(column.getRelationParent().getOwnerTable()) &&
                  !store.existsColumnValue(column.getRelationParent(), index)) {

            Object value = generate(column.getRelationParent(), store);
            logger.debug(() -> generateDataLogSupplier(index, rowLimit, column, column.getRelationParent(), "関連親カラムの値が不明", value).get());

            store.registerColumnValue(column.getRelationParent(), value);
            // カラムの値生成後、自身の子カラムすべてに再帰的に値を設定する
            store.setRelationValueRecursiveChildren(column.getRelationParent(), value);
          }

          // 自身のカラムの値生成後、自身の子カラムすべてに再帰的に値を設定する
          Object value = generate(column, store);
          logger.debug(() -> generateDataLogSupplier(index, rowLimit, column, column, "Generatorによる生成", value).get());
          store.setRelationValueRecursiveChildren(column, value);

          return value.toString();
        }, executorService));
      }

      GenerateRecord record = new GenerateRecord();
      for (CompletableFuture<String> future : futureList) {
        record.append(generateTable.getColumns().get(futureList.indexOf(future)), future.get());
      }

      records.add(record);
      handleBatchPersistence(index, records, generateTable, persistence);
    }

    finalBatchPersistence(rowLimit, records, generateTable, persistence);
  }

  public Object generate(GenerateColumn generateColumn, GenerateStore store) {

    Generator<?, ?> generator = GeneratorRepository.findByColumn(generateColumn).or(() -> {
      Generator<?, ?> gen = GeneratorFactory.create(generateColumn);
      GeneratorRepository.save(generateColumn, gen);
      return Optional.of(gen);
    }).get();

    return generator.generate(store);
  }

  private void handleBatchPersistence(int currentIndex, List<GenerateRecord> records, GenerateTable generateTable, Persistence persistence) {

    int batchSize = store.getBatchSize();
    if (records.size() >= batchSize) {
      persistence.persist(generateTable, records);
      records.clear();

      int startIndex = currentIndex - (currentIndex % batchSize);
      int rowLimit = startIndex + batchSize;
      generateTable.getChildTables().stream().parallel()
              .forEach(c -> generate(c, persistence, Executors.newFixedThreadPool(c.getColumnCount()), startIndex, c.getRowCount() < rowLimit ? c.getRowCount() : rowLimit));
    }
  }

  private void finalBatchPersistence(int rowLimit, List<GenerateRecord> records, GenerateTable generateTable, Persistence persistence) {

    store.finish(generateTable);

    if (!records.isEmpty()) {
      persistence.persist(generateTable, records);
      records.clear();
    }

    int batchSize = store.getBatchSize();
    int startIndex = batchSize > rowLimit ? 0 : rowLimit + 1 - batchSize;
    generateTable.getChildTables().stream().parallel()
            .forEach(c -> generate(c, persistence, Executors.newFixedThreadPool(c.getColumnCount()), startIndex, c.getRowCount() > rowLimit ? c.getRowCount() : rowLimit));
  }

  static Supplier<String> generateDataLogSupplier(int index, int rowLimit, GenerateColumn triggerColumn, GenerateColumn generatedColumn, String message, Object value) {
    return () -> "行[" + (index + 1) + "/" + rowLimit  + "]\t" + "契機[" + triggerColumn.getFullName() + "](" + message + "), " + "生成[" + generatedColumn.getFullName() + "], " + "値[" + value.toString() + "]";
  }
}
