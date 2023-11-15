package databullet.domain.generate;

import databullet.domain.definition.data.DataSpecTable;
import databullet.domain.definition.table.Column;
import databullet.domain.definition.table.Table;
import databullet.domain.definition.table.TableDefinition;
import databullet.domain.generate.generator.GeneratorFactory;
import databullet.domain.generate.table.GenerateColumn;
import databullet.domain.generate.table.GenerateTable;
import databullet.domain.write.DataRecord;
import databullet.domain.write.DataWriter;
import lombok.SneakyThrows;

import java.nio.file.Paths;
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

    @SneakyThrows
    public void generate(TableDefinition tableDef, DataWriter<?> writer) {
        List<Table> tables = tableDef.getTables();
        for (Table table : tables) {
            generate(table, store.getGenerateTable(table), writer);
        }
    }

    public void generate(Table table, GenerateTable generateTable, DataWriter<?> writer) {

        // 並列処理用
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<String>[] futures = new Future[table.getColumnCount()];

        // 生成行数の計算
        long rowCount = (long) (generateTable.getDataSpecTable().getRowCount() * store.dataSpecDefinition.getScale());

        List<DataRecord> records = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {

            int index = 0;
            for (Column column : table.getColumns()) {
                futures[index++] = executorService.submit(() -> generate(column).toString());
            }

            DataRecord record = new DataRecord();
            for (Future<String> future : futures) {
                try {
                    record.append(future.get());
                } catch (InterruptedException | ExecutionException e) {;
                    e.printStackTrace();
                }
            }

            records.add(record);

            if (records.size() >= batchSize) {
                System.out.println(table.getName() + " table : " + i + "/" + rowCount);
                writer.write(Paths.get("TODO"), records);
                records.clear();
            }
        }

        if (records.size() > 0) {
            System.out.println(table.getName() + " table : " + rowCount + "/" + rowCount);
            writer.write(Paths.get("TODO"), records);
        }

        executorService.shutdown();
    }

    public Object generate(Column tableColumn) {
        GenerateColumn generateColumn = store.getGenerateColumn(tableColumn);
        return GeneratorFactory.create(generateColumn).generate();
    }
}
