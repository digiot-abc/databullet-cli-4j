package databullet.domain.definition.generate;

import databullet.domain.definition.Definitions;
import databullet.domain.definition.dataspec.DataSpecColumn;
import databullet.domain.definition.dataspec.DataSpecDefinition;
import databullet.domain.definition.dataspec.DataSpecTable;
import databullet.domain.definition.dataspec.types.ReferenceType;
import databullet.domain.definition.table.Column;
import databullet.domain.definition.table.Table;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GenerateDefinitionFactory {

  public static List<GenerateRelationGroup> create(Definitions definitions) {

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
      if (dataSpecTable.getRowCount() == null) {
        dataSpecTable.setRowCount(dataSpec.getDefaultRowCount());
      }
      int rowCount = (int) (dataSpecTable.getRowCount() * dataSpecTable.getScale() * dataSpec.getScale());
      generateTable.setRowCount(rowCount);
      generateTable.setColumns(new ArrayList<>(table.getColumns().size()));

      Map<String, DataSpecColumn> dataColumnMap = dataSpecTable.getColumns()
          .stream()
          .collect(Collectors.toMap(DataSpecColumn::getName, Function.identity()));

      for (Column column : table.getColumns()) {
        String columnName = column.getName();
        DataSpecColumn dataDataSpecColumn = dataColumnMap.getOrDefault(columnName, DataSpecColumn.empty(columnName));

        GenerateColumn generateColumn = new GenerateColumn(column, dataDataSpecColumn);
        generateColumn.setOwnerTable(generateTable);
        generateTable.getColumns().add(generateColumn);
        generateColumnMap.put(String.join(".", tableName, columnName), generateColumn);

        if (!dataDataSpecColumn.isEmpty() && dataDataSpecColumn.getType() instanceof ReferenceType) {
          relationColumns.add(generateColumn);
        }
      }

      generateTableMap.put(tableName, generateTable);
    }

    relationColumns.forEach(relationColumn -> {

      ReferenceType options = (ReferenceType) relationColumn.getDataSpecColumn().getType();
      GenerateTable childTable = relationColumn.getOwnerTable();
      GenerateTable parentTable = generateTableMap.get(options.getReferencedTable());
      childTable.setParentTable(parentTable);

      parentTable.getChildTables().add(childTable);
      GenerateColumn parentColumn = generateColumnMap.get(options.getFQDNReferencedColumnName());
      relationColumn.setRelationParent(parentColumn);

      parentColumn.getRelationChildren().add(relationColumn);

//      DataSpecTable ownerTable = relationColumn.getOwnerTable().getDataSpecTable();
//      int rowCount = ownerTable.getRowCount();
//      ownerTable.setRowCount(rowCount);
    });

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

    List<GenerateRelationGroup> relationGroups = List.of(singleGroup, familyGroup);
    return relationGroups;
  }
}
