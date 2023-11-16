package databullet.domain.definition.data;

import databullet.domain.definition.table.Column;
import lombok.Data;

@Data
public class DataSpecColumn {

    private String name;
    private DataSpecType type;

    public static DataSpecColumn empty() {
        DataSpecColumn dataSpecColumn = new DataSpecColumn();
        dataSpecColumn.setName("");
        dataSpecColumn.setType(DataSpecType.empty());
        return dataSpecColumn;
    }
}