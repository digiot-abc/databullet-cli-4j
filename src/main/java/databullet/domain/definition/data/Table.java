package databullet.domain.definition.data;

import lombok.Data;

import java.util.List;

@Data
public class Table {
    private String name;
    private int rowCount;
    private List<Column> columns;

    public boolean is(databullet.domain.definition.table.Table table) {
        return this.name.equals(table.getName());
    }
}