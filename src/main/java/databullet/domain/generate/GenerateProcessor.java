package databullet.domain.generate;

import databullet.domain.definition.generate.GenerateDefinition;
import databullet.domain.definition.generate.GenerateRelationGroup;
import databullet.domain.generate.generator.Generator;
import databullet.domain.generate.generator.GeneratorFactory;
import databullet.domain.definition.generate.GenerateColumn;
import databullet.domain.definition.generate.GenerateTable;
import databullet.domain.generate.generator.GeneratorRepository;
import databullet.domain.generate.persistance.Persistence;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public class GenerateProcessor {

    GenerateStore store;

    static long maxMemory = Runtime.getRuntime().maxMemory();

    static long batchSize = maxMemory / 100;

    public GenerateProcessor(GenerateStore store) {
        this.store = store;
    }

    public void generate(GenerateDefinition definitions, Persistence persistence) {

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try {
            definitions.getRelationGroups().stream().parallel().forEach(defGroup -> {
                for (GenerateTable table : defGroup.getGenTables()) {
                    try {
                        generate(table, persistence, executorService);
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        store.finish(table);
                    }
                }
            });
        } finally {
            if (!executorService.isShutdown()) {
                executorService.shutdown();
            }
        }
    }

    public void generate(GenerateTable generateTable, Persistence persistence, ExecutorService executorService)
            throws ExecutionException, InterruptedException {
        generate(generateTable, persistence, executorService, 0, generateTable.getRowCount());
    }

    public void generate(GenerateTable generateTable, Persistence persistence,
                         ExecutorService executorService, int rowOffsetIndex, int rowLimit)
            throws ExecutionException, InterruptedException {

        if (store.isFinished(generateTable)) {
            return;
        }

        persistence.persist(generateTable, List.of(GenerateRecord.header(generateTable)));

        List<GenerateRecord> records = store.getRecords(generateTable);
        int rowCount = generateTable.getRowCount();

        try {
            for (int i = rowOffsetIndex; i < rowLimit; i++) {
                List<CompletableFuture<String>> futureList = new ArrayList<>();

                final int index = i;
                for (GenerateColumn column : generateTable.getColumns()) {
                    futureList.add(CompletableFuture.supplyAsync(() -> {
                        Object value = column.hasParent() ?
                                store.getRecords(generateTable.getParentTable()).get(index).getData().get(column.getRelationParent()) :
                                generate(column);
                        return value.toString();
                    }, executorService));
                }

                GenerateRecord record = new GenerateRecord();
                for (CompletableFuture<String> future : futureList) {
                    record.append(generateTable.getColumns().get(futureList.indexOf(future)), future.get());
                }

                records.add(record);

                // 子テーブルの生成
                for (GenerateTable childTable : generateTable.getChildTables()) {
                    generate(childTable, persistence, executorService, i, i + 1);
                }

                handleBatchPersistence(i, rowCount, records, generateTable, persistence);
            }

            for (GenerateTable childTable : generateTable.getChildTables()) {
                generate(childTable, persistence, executorService, rowOffsetIndex, rowLimit);
            }
            finalBatchPersistence(rowCount, records, generateTable, persistence);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rowLimit >= rowCount) {
                store.finish(generateTable);
            }
        }
    }

    private void handleBatchPersistence(int currentIndex, int totalRowCount,
                                        List<GenerateRecord> records, GenerateTable generateTable,
                                        Persistence persistence) {
        if (records.size() >= batchSize || currentIndex == totalRowCount - 1) {
            persistence.persist(generateTable, records);
            records.clear();
        }
    }

    private void finalBatchPersistence(int totalRowCount, List<GenerateRecord> records,
                                       GenerateTable generateTable, Persistence persistence) {
        if (!records.isEmpty()) {
            persistence.persist(generateTable, records);
            records.clear();
        }
    }

    public Object generate(GenerateColumn generateColumn) {
        return GeneratorRepository.findByColumn(generateColumn).or(() -> {
            Generator<?, ?> generator = GeneratorFactory.create(generateColumn);
            GeneratorRepository.save(generateColumn, generator);
            return Optional.of(generator);
        }).get();
    }
}
