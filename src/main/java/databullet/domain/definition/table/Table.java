package databullet.domain.definition.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import databullet.domain.definition.data.DataSpecTable;
import lombok.Data;

import java.util.List;

@Data
public class Table {

    private String name;
    private List<Column> columns;

    @JsonIgnore
    private Integer columnCount;

    public boolean is(DataSpecTable dataSpecTable) {
        return this.name.equals(dataSpecTable.getName());
    }

    public Integer getColumnCount() {
        if (columnCount == null) {
            this.columnCount = columns.size();
        }
        return columnCount;
    }
}