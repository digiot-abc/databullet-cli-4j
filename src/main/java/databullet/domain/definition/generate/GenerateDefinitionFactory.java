package databullet.domain.definition.generate;

import databullet.domain.definition.Definitions;
import databullet.domain.definition.data.DataSpecColumn;
import databullet.domain.definition.data.DataSpecDefinition;
import databullet.domain.definition.data.DataSpecTable;
import databullet.domain.definition.data.options.ReferenceOptions;
import databullet.domain.definition.table.*;

import java.util.*;
import java.util.stream.Collectors;

public class GenerateDefinitionFactory {

    public static List<GenerateRelationGroup> create(Definitions definitions) {

        DataSpecDefinition dataSpec = definitions.getDataSpec();

        Map<String, DataSpecTable> dataSpecTableNameMap =
                dataSpec.getTables().stream().collect(Collectors.toMap(t -> t.getName(), t -> t));

        // リレーションカラムリスト
        List<GenerateColumn> relationColumns = new ArrayList<>();

        // 依存関係グラフ
        Map<String, GenerateTable> generateTableMap = new HashMap<>();
        // 生成用カラムマップ
        Map<String, GenerateColumn> generateColumnMap = new HashMap<>();

        // 生成するテーブルは、データ仕様に記載されているもののみ
        List<GenerateTable> generateTables = new ArrayList<>();
        for (Table table : definitions.getTableDef().getTables()) {

            DataSpecTable dataSpecTable = dataSpecTableNameMap.getOrDefault(table.getName(), DataSpecTable.empty(table.getName()));
            GenerateTable generateTable = new GenerateTable(table, dataSpecTable);
            generateTable.setRowCount((int) (dataSpecTable.getRowCount() * dataSpec.getScale()));
            generateTable.setColumns(new ArrayList<>());

            Map<String, DataSpecColumn> dataColumnMap =
                    dataSpecTable.getColumns().stream().collect(Collectors.toMap(c -> c.getName(), c -> c));

            for (Column column : table.getColumns()) {
                DataSpecColumn dataDataSpecColumn = dataColumnMap.getOrDefault(column.getName(), DataSpecColumn.empty(column.getName()));
                GenerateColumn generateColumn = new GenerateColumn(column, dataDataSpecColumn);
                generateColumn.setOwnerTable(generateTable);
                generateTable.getColumns().add(generateColumn);
                generateColumnMap.put(String.join(".", table.getName(), column.getName()), generateColumn);

                // リレーションカラムを保持
                if (!dataDataSpecColumn.isEmpty() &&
                        dataDataSpecColumn.getType().getOptions() instanceof ReferenceOptions) {
                    relationColumns.add(generateColumn);
                }
            }

            generateTables.add(generateTable);
            generateTableMap.put(generateTable.getName(), generateTable);
        }

        // 依存関係の解析
        Map<GenerateTable, Set<GenerateTable>> dependencies = new HashMap<>();
        for (GenerateColumn relationColumn : relationColumns) {

            ReferenceOptions options = (ReferenceOptions) relationColumn.getDataSpecColumn().getType().getOptions();
            GenerateTable childTable = relationColumn.getOwnerTable();
            GenerateTable parentTable = generateTableMap.get(options.getReferencedTable());
            childTable.setParentTable(parentTable);
            parentTable.getChildTables().add(childTable);

            GenerateColumn parentColumn = generateColumnMap.get(options.getFQDNReferencedColumnName());
            relationColumn.setRelationParent(parentColumn);

            // 親カラムのリレーション子リストに子カラムを追加
            List<GenerateColumn> children = parentColumn.getRelationChildren();
            children.add(relationColumn);

            // 子と親の情報を連携
            long rowCount = parentColumn.getOwnerTable().getDataSpecTable().getRowCount();
            relationColumn.getOwnerTable().getDataSpecTable().setRowCount(rowCount);

            dependencies.computeIfAbsent(parentTable, k -> new HashSet<>()).add(relationColumn.getOwnerTable());
        }

        // トポロジカルソートを実施して順序付け
        List<GenerateTable> sortedGenerateTables = topologicalSort(generateTables, dependencies);

        // リレーショングループのリストを作成
        List<GenerateRelationGroup> relationGroups = new ArrayList<>();
        relationGroups.add(new GenerateRelationGroup(sortedGenerateTables));

        return relationGroups;
    }

    private static List<GenerateTable> topologicalSort(List<GenerateTable> tables,
                                                       Map<GenerateTable, Set<GenerateTable>> dependencies) {
        List<GenerateTable> sortedList = new ArrayList<>();
        Set<GenerateTable> visited = new HashSet<>();
        Set<GenerateTable> expanded = new HashSet<>();

        for (GenerateTable table : tables) {
            visit(table, visited, expanded, sortedList, dependencies);
        }

        Collections.reverse(sortedList);
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
