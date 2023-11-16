package databullet.domain.generate;

import databullet.domain.definition.GenerateDefinitions;
import databullet.domain.generate.generator.CachingGeneratorFactory;
import databullet.domain.definition.generate.GenerateColumn;
import databullet.domain.definition.generate.GenerateTable;
import databullet.domain.generate.persistance.Persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GenerateProcessor {

    GenerateStore store;

    static long maxMemory = Runtime.getRuntime().maxMemory();

    static long batchSize = maxMemory / 100;

    public GenerateProcessor(GenerateStore store) {
        // TODO スレッド数などの定義
        this.store = store;
    }

    public void generate(GenerateDefinitions definitions, Persistence persistence) throws ExecutionException, InterruptedException {
        for (GenerateTable table : definitions.getGenTables()) {
            generate(table, persistence);
        }
    }

    public void generate(GenerateTable generateTable, Persistence persistence) throws ExecutionException, InterruptedException {

        // 並列処理用
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<String>[] futures = new Future[generateTable.getColumnCount()];

        long rowCount = generateTable.getRowCount();
        List<GenerateRecord> records = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {

            int index = 0;
            for (GenerateColumn column : generateTable.getColumns()) {
                futures[index++] = executorService.submit(() -> generate(column).toString());
            }

            GenerateRecord record = new GenerateRecord();
            for (Future<String> future : futures) {
                record.append(future.get());
            }

            records.add(record);

            if (records.size() >= batchSize) {
                System.out.println(generateTable.getName() + " table : " + i + "/" + rowCount);
                persistence.persist(generateTable, records);
                records.clear();
            }
        }

        if (records.size() > 0) {
            System.out.println(generateTable.getName() + " table : " + rowCount + "/" + rowCount);
            persistence.persist(generateTable, records);
        }

        executorService.shutdown();
    }

    public Object generate(GenerateColumn generateColumn) {
        return CachingGeneratorFactory.create(generateColumn).generate();
    }
}
