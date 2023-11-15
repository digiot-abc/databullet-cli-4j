package databullet.domain.generate;

import databullet.domain.definition.data.DataSpecColumn;
import databullet.domain.definition.data.DataSpecTable;
import databullet.domain.definition.table.Column;
import databullet.domain.definition.table.Table;
import databullet.domain.definition.table.TableDefinition;
import databullet.domain.generate.generator.DataGeneratorFactory;
import databullet.domain.generate.table.GenerateColumn;
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

    public GenerateProcessor(GenerateStore store) {
        // TODO スレッド数などの定義
        this.store = store;
    }

    @SneakyThrows
    public void generate(TableDefinition tableDef, DataWriter<?> writer) {

        List<Table> tables = tableDef.getTables();
        for (Table table : tables) {
            generate(table, store.getDataTable(table), writer);
        }
    }


    public void generate(Table table, DataSpecTable dataDataSpecTable, DataWriter<?> writer) {

        long maxMemory = Runtime.getRuntime().maxMemory();
        long batchSize = maxMemory / 100; // 仮の計算式

        // 並列処理用
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<String>[] futures = new Future[table.getColumnCount()];

        // 生成行数の計算
        double rowCount = dataDataSpecTable.getRowCount() * store.dataSpecDefinition.getScale();

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
                writer.write(Paths.get("TODO"), records);
                records.clear();
            }
        }

        if (records.size() > 0) {
            writer.write(Paths.get("TODO"), records);
        }

        executorService.shutdown();
    }

    public Object generate(Column tableColumn) {
        GenerateColumn generateColumn = store.getGenerateColumn(tableColumn);
        return DataGeneratorFactory.create(generateColumn).generate();
    }
}
