package databullet.domain.definition.dataspec;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataSpecColumn {

    private String name;
    private DataSpecType type;

    public DataSpecColumn(String name) {
        this.name = name;
    }

    public static DataSpecColumn empty(String name) {
        DataSpecColumn dataSpecColumn = new DataSpecColumn();
        dataSpecColumn.setName(name);
        return dataSpecColumn;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return type == null;
    }
}