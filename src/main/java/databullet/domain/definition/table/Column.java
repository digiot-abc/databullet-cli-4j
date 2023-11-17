package databullet.domain.definition.table;

import databullet.domain.definition.dataspec.DataSpecColumn;
import lombok.Data;

@Data
public class Column {

    private String name;
    private String type;
    private ColumnDigit digit;
    private Boolean primaryKey;
    private Boolean nullable;

    public void setDigit(Object size) {
        digit = new ColumnDigit(size);
    }

    public boolean is(DataSpecColumn dataSpecColumn) {
        return this.name.equals(dataSpecColumn.getName());
    }
}