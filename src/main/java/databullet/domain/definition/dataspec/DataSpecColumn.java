package databullet.domain.definition.dataspec;

import lombok.Data;

@Data
public class DataSpecColumn {

    private String name;
    private DataSpecType type;

    public static DataSpecColumn empty(String name) {
        DataSpecColumn dataSpecColumn = new DataSpecColumn();
        dataSpecColumn.setName(name);
        return dataSpecColumn;
    }

    public boolean isEmpty() {
        return type == null;
    }
}