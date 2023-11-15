package databullet.domain.generate;

import databullet.domain.definition.data.DataSpecColumn;
import databullet.domain.definition.data.DataSpecTable;
import databullet.domain.definition.table.Column;
import databullet.domain.definition.data.DataSpecDefinition;
import databullet.domain.definition.table.RelationInfo;
import databullet.domain.definition.table.Table;
import databullet.domain.definition.table.TableDefinition;
import databullet.domain.generate.table.GenerateColumn;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GenerateStore {

    Map<Table, DataSpecTable> tableMap = new HashMap<>();
    Map<Column, GenerateColumn> generateColumnMap = new HashMap<>();

    public DataSpecDefinition dataSpecDefinition;

    public GenerateStore(TableDefinition tableDef, RelationInfo relInfo, DataSpecDefinition dataSpec) {

        this.dataSpecDefinition = dataSpec;

        Map<String, DataSpecTable> tableNameMap =
                dataSpec.getTables().stream().collect(Collectors.toMap(t -> t.getName(), t -> t));

        for (Table table : tableDef.getTables()) {

            DataSpecTable dataDataSpecTable = tableNameMap.getOrDefault(table.getName(), null);
            tableMap.put(table, dataDataSpecTable);

            if (dataDataSpecTable != null) {
                Map<String, DataSpecColumn> dataColumnMap =
                        dataDataSpecTable.getColumns().stream().collect(Collectors.toMap(c -> c.getName(), c -> c));

                for (Column column : table.getColumns()) {
                    DataSpecColumn dataDataSpecColumn = dataColumnMap.getOrDefault(column.getName(), null);
                    generateColumnMap.put(column, new GenerateColumn(column, dataDataSpecColumn));
                }
            }
        }
    }

    public GenerateColumn getGenerateColumn(Column column) {
        return generateColumnMap.get(column);
    }

    public DataSpecTable getDataTable(Table tableTable) {
        return tableMap.get(tableTable);
    }

    public DataSpecColumn getDataColumn(Column tableColumn) {
        return generateColumnMap.get(tableColumn).getDataSpecColumn();
    }

    class DefinitionChain {


    }
}
