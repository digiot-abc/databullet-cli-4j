package databullet.domain.generate;

import databullet.domain.definition.table.Column;
import databullet.domain.definition.data.DataSpecDefinition;
import databullet.domain.definition.table.RelationInfo;
import databullet.domain.definition.table.Table;
import databullet.domain.definition.table.TableDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GenerateStore {

    Map<Table, databullet.domain.definition.data.Table> tableMap = new HashMap<>();
    Map<Column, databullet.domain.definition.data.Column> columnMap = new HashMap<>();

    public DataSpecDefinition dataSpecDefinition;

    public GenerateStore(TableDefinition tableDef, RelationInfo relInfo, DataSpecDefinition dataSpec) {

        this.dataSpecDefinition = dataSpec;

        Map<String, databullet.domain.definition.data.Table> tableNameMap =
                dataSpec.getTables().stream().collect(Collectors.toMap(t -> t.getName(), t -> t));

        for (Table table : tableDef.getTables()) {

            databullet.domain.definition.data.Table dataTable = tableNameMap.getOrDefault(table.getName(), null);
            tableMap.put(table, dataTable);

            if (dataTable != null) {
                Map<String, databullet.domain.definition.data.Column> dataColumnMap =
                        dataTable.getColumns().stream().collect(Collectors.toMap(c -> c.getName(), c -> c));

                for (Column column : table.getColumns()) {
                    databullet.domain.definition.data.Column dataColumn = dataColumnMap.getOrDefault(column.getName(), null);
                    columnMap.put(column, dataColumn);
                }
            }
        }
    }

    public databullet.domain.definition.data.Table getDataTable(Table tableTable) {
        return tableMap.get(tableTable);
    }

    public databullet.domain.definition.data.Column getDataColumn(Column tableColumn) {
        return columnMap.get(tableColumn);
    }

    class DefinitionChain {


    }
}
