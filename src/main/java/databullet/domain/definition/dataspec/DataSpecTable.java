package databullet.domain.definition.dataspec;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DataSpecTable {

    private String name;
    private Integer rowCount;
    private Double scale = 1.0;
    private List<DataSpecColumn> columns = new ArrayList<>();

    public static DataSpecTable empty(String name) {
        DataSpecTable table = new DataSpecTable();
        table.setName(name);
        table.setColumns(new ArrayList<>());
        table.setScale(1.0);
        table.setRowCount(0);
        return table;
    }
}