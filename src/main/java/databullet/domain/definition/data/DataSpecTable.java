package databullet.domain.definition.data;

import databullet.domain.definition.table.Table;
import lombok.Data;

import java.util.List;

@Data
public class DataSpecTable {
    private String name;
    private int rowCount;
    private List<DataSpecColumn> columns;

    public boolean is(Table table) {
        return this.name.equals(table.getName());
    }
}