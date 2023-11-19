package databullet.domain.definition.generate;

import databullet.domain.definition.Definitions;
import databullet.domain.definition.dataspec.DataSpecColumn;
import databullet.domain.definition.dataspec.DataSpecDefinition;
import databullet.domain.definition.dataspec.DataSpecTable;
import databullet.domain.definition.dataspec.DataSpecType;
import databullet.domain.definition.dataspec.types.ReferenceType;
import databullet.domain.definition.table.Column;
import databullet.domain.definition.table.Table;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GenerateDefinitionFactory {

  public static GenerateDefinition create(Definitions definitions) {
    GenerateDefinition definition = new GenerateDefinition();
    definition.setScale(definitions.getDataSpec().getScale());
    definition.setRelationGroups(createRelationGroups(definitions));
    return definition;
  }

  public static List<GenerateRelationGroup> createRelationGroups(Definitions definitions) {

    DataSpecDefinition dataSpec = definitions.getDataSpec();

    Map<String, DataSpecTable> dataSpecTableNameMap = dataSpec.getTables()
            .stream()
            .collect(Collectors.toMap(DataSpecTable::getName, Function.identity()));

    List<GenerateColumn> relationColumns = new ArrayList<>();
    Map<String, GenerateTable> generateTableMap = new LinkedHashMap<>();
    Map<String, GenerateColumn> generateColumnMap = new LinkedHashMap<>();

    for (Table table : definitions.getTableDef().getTables()) {
      String tableName = table.getName();
      DataSpecTable dataSpecTable = dataSpecTableNameMap.getOrDefault(tableName, DataSpecTable.empty(tableName));

      GenerateTable generateTable = new GenerateTable(table, dataSpecTable);
      setRowCount(dataSpec, dataSpecTable, generateTable);
      generateTable.setColumns(new ArrayList<>(table.getColumns().size()));

      processColumns(table, dataSpecTable, generateTable, generateColumnMap, relationColumns);

      generateTableMap.put(tableName, generateTable);
    }

    buildRelations(generateTableMap, generateColumnMap);
    return createRelationGroups(generateTableMap);
  }

  private static void setRowCount(DataSpecDefinition dataSpec, DataSpecTable dataSpecTable, GenerateTable generateTable) {
    if (dataSpecTable.getRowCount() == null) {
      dataSpecTable.setRowCount(dataSpec.getDefaultRowCount());
    }
    int rowCount = (int) (dataSpecTable.getRowCount() * dataSpecTable.getScale() * dataSpec.getScale());
    generateTable.setRowCount(rowCount);
  }

  private static void processColumns(Table table, DataSpecTable dataSpecTable, GenerateTable generateTable,
                                     Map<String, GenerateColumn> generateColumnMap, List<GenerateColumn> relationColumns) {
    Map<String, DataSpecColumn> dataColumnMap = dataSpecTable.getColumns()
            .stream()
            .collect(Collectors.toMap(DataSpecColumn::getName, Function.identity()));

    for (Column column : table.getColumns()) {
      String columnName = column.getName();
      DataSpecColumn dataSpecColumn = dataColumnMap.getOrDefault(columnName, DataSpecColumn.empty(columnName));

      GenerateColumn generateColumn = new GenerateColumn(column, dataSpecColumn);
      generateColumn.setOwnerTable(generateTable);
      generateTable.getColumns().add(generateColumn);
      generateColumnMap.put(generateTable.getName() + "." + columnName, generateColumn);

      if (dataSpecColumn.getType() instanceof ReferenceType) {
        relationColumns.add(generateColumn);
      }
    }
  }

  private static void buildRelations(Map<String, GenerateTable> generateTableMap, Map<String, GenerateColumn> generateColumnMap) {
    for (GenerateTable table : generateTableMap.values()) {
      for (GenerateColumn column : table.getColumns()) {

        DataSpecType type = column.getDataSpecColumn().getType();
        if (!(type instanceof ReferenceType)) {
          continue;
        }

        ReferenceType refType = (ReferenceType) type;
        GenerateTable childTable = column.getOwnerTable();
        GenerateTable parentTable = generateTableMap.get(refType.getReferencedTable());

        Set<GenerateTable> visited = new HashSet<>();
        int sinkLevel = calculateSinkLevel(parentTable, generateTableMap, 1, visited);

        if (sinkLevel == -1) {
          // 循環参照が検出された場合の処理
          continue;
        }

        if (sinkLevel > childTable.getRelationSinkLevel()) {
          childTable.setRelationSinkLevel(sinkLevel);

          if (childTable.getParentTable() != null) {
            childTable.getParentTable().getChildTables().remove(childTable);
          }

          childTable.setParentTable(parentTable);
          parentTable.getChildTables().add(childTable);

          GenerateColumn parentColumn = generateColumnMap.get(refType.getFQDNReferencedColumnName());
          column.setRelationParent(parentColumn);
          parentColumn.getRelationChildren().add(column);
        }
      }
    }
  }

  private static int calculateSinkLevel(GenerateTable table, Map<String, GenerateTable> generateTableMap, int currentLevel, Set<GenerateTable> visited) {
    if (table == null || visited.contains(table)) {
      return -1; // 循環参照が検出された場合
    }

    visited.add(table);
    int maxLevel = currentLevel;
    for (GenerateColumn column : table.getColumns()) {
      DataSpecType type = column.getDataSpecColumn().getType();
      if (type instanceof ReferenceType) {
        ReferenceType refType = (ReferenceType) type;
        GenerateTable parentTable = generateTableMap.get(refType.getReferencedTable());
        int parentLevel = calculateSinkLevel(parentTable, generateTableMap, currentLevel + 1, visited);
        if (parentLevel != -1) {
          maxLevel = Math.max(maxLevel, parentLevel);
        }
      }
    }
    visited.remove(table);
    return maxLevel;
  }

  private static List<GenerateRelationGroup> createRelationGroups(Map<String, GenerateTable> generateTableMap) {
    GenerateRelationGroup singleGroup = new GenerateRelationGroup();
    GenerateRelationGroup familyGroup = new GenerateRelationGroup();

    generateTableMap.values()
            .stream()
            .sorted()
            .forEachOrdered(t -> {
              if (t.getParentTable() == null && t.getChildTables().isEmpty()) {
                singleGroup.getGenTables().add(t);
              } else if (t.getParentTable() == null) {
                familyGroup.getGenTables().add(t);
              }
            });

    return List.of(singleGroup, familyGroup);
  }
}
