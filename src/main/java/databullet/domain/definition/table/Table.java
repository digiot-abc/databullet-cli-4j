package databullet.domain.definition.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class Table {

    private String name;
    private List<Column> columns;

    @JsonIgnore
    private Integer columnCount;

    public boolean is(databullet.domain.definition.data.Table table) {
        return this.name.equals(table.getName());
    }

    public Integer getColumnCount() {
        if (columnCount == null) {
            this.columnCount = columns.size();
        }
        return columnCount;
    }
}