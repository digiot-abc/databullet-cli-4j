package databullet.domain.definition.generate;

import databullet.domain.definition.Definitions;
import databullet.domain.definition.data.DataSpecColumn;
import databullet.domain.definition.data.DataSpecDefinition;
import databullet.domain.definition.data.DataSpecTable;
import databullet.domain.definition.table.*;

import java.util.*;
import java.util.stream.Collectors;

public class GenerateTableFactory {

    public static List<GenerateTable> create(Definitions definitions) {

        DataSpecDefinition dataSpec = definitions.getDataSpec();

        Map<String, Table> tableNameMap =
                definitions.getTableDef().getTables().stream().collect(Collectors.toMap(t -> t.getName(), t -> t));


        // 生成するテーブルは、データ仕様に記載されているもののみ
        List<GenerateTable> generateTables = new ArrayList<>();
        for (DataSpecTable dataSpecTable : dataSpec.getTables()) {

            Table table = tableNameMap.get(dataSpecTable.getName());
            GenerateTable generateTable = new GenerateTable(table, dataSpecTable);
            generateTable.setRowCount((long) (dataSpecTable.getRowCount() * dataSpec.getScale()));
            generateTable.setColumns(new ArrayList<>());

            Map<String, DataSpecColumn> dataColumnMap =
                    dataSpecTable.getColumns().stream().collect(Collectors.toMap(c -> c.getName(), c -> c));

            for (Column column : table.getColumns()) {
                DataSpecColumn dataDataSpecColumn = dataColumnMap.getOrDefault(column.getName(), null);
                GenerateColumn generateColumn = new GenerateColumn(column, dataDataSpecColumn);
                generateColumn.setOwnerTable(generateTable);
                generateTable.getColumns().add(generateColumn);
            }

            generateTables.add(generateTable);
        }

        // 依存関係グラフの構築
        Map<String, GenerateTable> generateTableMap = new HashMap<>();
        for (GenerateTable genTable : generateTables) {
            generateTableMap.put(genTable.getName(), genTable);
        }

        // 依存関係の解析
        Map<GenerateTable, Set<GenerateTable>> dependencies = new HashMap<>();
        for (Relation relation : definitions.getRelationInfo().getRelations()) {
            GenerateTable fromTable = generateTableMap.get(relation.getFromTable());
            GenerateTable toTable = generateTableMap.get(relation.getToTable());
            dependencies.computeIfAbsent(toTable, k -> new HashSet<>()).add(fromTable);
        }

        // トポロジカルソートを実施して順序付け
        List<GenerateTable> sortedGenerateTables = topologicalSort(generateTables, dependencies);

        // カラムのリレーションシップマッピングを構築する
        Map<String, GenerateColumn> generateColumnMap = new HashMap<>();
        for (GenerateTable genTable : generateTables) {
            for (GenerateColumn genColumn : genTable.getColumns()) {
                generateColumnMap.put(genColumn.getName(), genColumn);
            }
        }

        // GenerateColumnインスタンスに親子関係を設定する
        for (Relation relation : definitions.getRelationInfo().getRelations()) {
            GenerateColumn childColumn = generateColumnMap.get(relation.getFromColumn());
            GenerateColumn parentColumn = generateColumnMap.get(relation.getToColumn());

            // 子カラムのリレーション親を設定
            childColumn.setRelationParent(parentColumn);

            // 親カラムのリレーション子リストに子カラムを追加
            List<GenerateColumn> children = parentColumn.getRelationChildren();
            if (children == null) {
                children = new ArrayList<>();
                parentColumn.setRelationChildren(children);
            }
            children.add(childColumn);
        }

        return sortedGenerateTables;
    }

    private static List<GenerateTable> topologicalSort(List<GenerateTable> tables,
                                                       Map<GenerateTable, Set<GenerateTable>> dependencies) {
        List<GenerateTable> sortedList = new ArrayList<>();
        Set<GenerateTable> visited = new HashSet<>();
        Set<GenerateTable> expanded = new HashSet<>();

        for (GenerateTable table : tables) {
            visit(table, visited, expanded, sortedList, dependencies);
        }

        Collections.reverse(sortedList); // 依存関係が前に来るようにリストを逆にする
        return sortedList;
    }

    private static void visit(GenerateTable table, Set<GenerateTable> visited,
                              Set<GenerateTable> expanded, List<GenerateTable> sortedList,
                              Map<GenerateTable, Set<GenerateTable>> dependencies) {
        if (visited.contains(table)) {
            if (!expanded.contains(table)) {
                throw new IllegalArgumentException("Dependency cycle detected!");
            }
            return;
        }

        visited.add(table);

        for (GenerateTable dep : dependencies.getOrDefault(table, Collections.emptySet())) {
            visit(dep, visited, expanded, sortedList, dependencies);
        }

        expanded.add(table);
        sortedList.add(table);
    }
}
