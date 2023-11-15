package databullet.domain.generate;

import databullet.domain.definition.table.Column;
import databullet.domain.definition.table.Table;
import databullet.domain.definition.table.TableDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenerateProcessor {

    GenerateStore store;

    public GenerateProcessor(GenerateStore store) {
        // TODO スレッド数などの定義
        this.store = store;
    }

    public void generate(TableDefinition tableDef) {
        long maxMemory = Runtime.getRuntime().maxMemory(); // JVMヒープ最大サイズ
        // バッチサイズの計算（例：ヒープの一部を使用）
        long batchSize = maxMemory / 100; // 仮の計算式

        List<Table> tables = tableDef.getTables();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (Table table : tables) {
            databullet.domain.definition.data.Table dataTable = store.getDataTable(table);
            double rowCount = dataTable.getRowCount() * store.dataSpecDefinition.getScale();

            executorService.submit(() -> {
                StringBuilder localResults = new StringBuilder();
                for (int i = 0; i < rowCount; i++) {
                    localResults.append(generate(table));
                    if (localResults.length() >= batchSize) {
                        writeData(localResults.toString()); // データの書き込み
                        localResults = new StringBuilder(); // ビルダーをリセット
                    }
                }
                if (localResults.length() > 0) {
                    writeData(localResults.toString()); // 残りのデータの書き込み
                }
            });
        }

        executorService.shutdown();
        // ここで全てのタスクの完了を待つ（省略）
    }

    private void writeData(String data) {
        // 実際のデータ書き込み処理（ファイルへの書き込み、データベースへの挿入など）
    }


    public String generate(Table table) {

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<String>[] futures = new Future[table.getColumnCount()];

        int index = 0;
        for (Column column : table.getColumns()) {
            futures[index++] = executorService.submit(() -> generate(column).toString());
        }

        executorService.shutdown();

        StringBuilder result = new StringBuilder();
        for (Future<String> future : futures) {
            try {
                result.append(future.get());
            } catch (InterruptedException | ExecutionException e) {;
                e.printStackTrace();
            }
            result.append(",");
        }

        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }

    public Object generate(Column tableColumn) {
        databullet.domain.definition.data.Column dataColumn = store.getDataColumn(tableColumn);
        return dataColumn.getType().getOptions().generate();
    }
}
