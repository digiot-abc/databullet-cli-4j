package databullet.domain.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import databullet.domain.definition.Definitions;
import databullet.domain.definition.dataspec.DataSpecColumn;
import databullet.domain.definition.dataspec.DataSpecDefinition;
import databullet.domain.definition.dataspec.DataSpecTable;
import databullet.domain.definition.dataspec.types.ReferenceType;
import databullet.domain.definition.table.TableDefinition;
import databullet.infrastructure.JsonMapper;
import schemacrawler.schema.Column;
import schemacrawler.schema.ColumnReference;
import schemacrawler.schema.ForeignKey;
import schemacrawler.schema.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SchemaConverter {

  public static String convertTablesToDataSpecDefinitionJson(List<Table> tables) throws JsonProcessingException {
    return new JsonMapper().mapObjectToJson(convertTablesToDataSpecDefinition(tables));
  }

  public static String convertTablesToTableDefinitionJson(List<Table> tables) throws Exception {
    return new JsonMapper().mapObjectToJson(convertTablesToTableDefinition(tables));
  }

  public static Definitions convertTablesToDefinitions(List<Table> tables) throws Exception {
    TableDefinition tableDefinition = SchemaConverter.convertTablesToTableDefinition(tables);
    DataSpecDefinition dataSpecDefinition = SchemaConverter.convertTablesToDataSpecDefinition(tables);
    return new Definitions(tableDefinition, dataSpecDefinition);
  }

  public static DataSpecDefinition convertTablesToDataSpecDefinition(List<Table> tables) throws JsonProcessingException {

    DataSpecDefinition dataSpecDefinition = new DataSpecDefinition();
    dataSpecDefinition.setScale(1.0);
    dataSpecDefinition.setTables(new ArrayList<>());

    for (Table table : tables) {

      DataSpecTable dataSpecTable = new DataSpecTable();
      dataSpecTable.setName(table.getName());
      dataSpecTable.setColumns(new ArrayList<>());

      Map<String, DataSpecColumn> dataSpecColumnMap = table.getColumns().stream()
          .map(c -> new DataSpecColumn(c.getName()))
          .peek(c -> dataSpecTable.getColumns().add(c))
          .collect(Collectors.toMap(c -> c.getName(), c -> c));

      for (ForeignKey foreignKey : table.getForeignKeys()) {
        for (ColumnReference columnReference : foreignKey.getColumnReferences()) {

          if (table.getName().equals(columnReference.getPrimaryKeyColumn().getParent().getName())) {
            continue;
          }

          String columnName = columnReference.getForeignKeyColumn().getName();
          DataSpecColumn dataSpecColumn = dataSpecColumnMap.get(columnName);

          if (dataSpecColumn.getType() == null) {
            ReferenceType dataSpecType = new ReferenceType();
            dataSpecType.setName("reference");
            dataSpecType.setReferencedTable(foreignKey.getReferencedTable().getName());
            dataSpecType.setReferencedColumn(columnReference.getPrimaryKeyColumn().getName());
            dataSpecColumn.setType(dataSpecType);
          }
        }
      }

      dataSpecDefinition.getTables().add(dataSpecTable);
    }

    return dataSpecDefinition;
  }

  public static TableDefinition convertTablesToTableDefinition(List<Table> tables) throws Exception {

    TableDefinition tableDefinition = new TableDefinition();
    tableDefinition.setTables(new ArrayList<>());

    for (Table table : tables) {

      databullet.domain.definition.table.Table defTable = new databullet.domain.definition.table.Table();
      defTable.setName(table.getName());
      defTable.setColumnCount(table.getColumns().size());
      defTable.setColumns(new ArrayList<>());

      for (Column column : table.getColumns()) {
        databullet.domain.definition.table.Column defColumn = new databullet.domain.definition.table.Column();
        defColumn.setName(column.getName());
        defColumn.setNullable(column.isNullable());
        defColumn.setPrimaryKey(column.isPartOfPrimaryKey());
        defColumn.setType(column.getColumnDataType().getName());
        List<Integer> digits = List.of(column.getSize(), column.getDecimalDigits());
        defColumn.setDigit(digits.get(1) == 0 ? digits.get(0) : digits);
        defTable.getColumns().add(defColumn);
      }

      tableDefinition.getTables().add(defTable);
    }

    return tableDefinition;
  }

}

