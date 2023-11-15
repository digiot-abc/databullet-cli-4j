package databullet.domain.definition.data;

import databullet.domain.definition.data.type.DataType;
import lombok.Data;

@Data
public class Column {

    private String name;
    private DataType type;
    private Boolean primaryKey;
    private Boolean nullable;

    public boolean is(databullet.domain.definition.table.Column column) {
        return this.name.equals(column.getName());
    }

    public static Column empty() {
        Column column = new Column();
        column.setName("");
        column.setType(DataType.empty());
        return column;
    }
}