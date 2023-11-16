package databullet.domain.definition.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DataSpecTable {
    private String name;
    private Long rowCount;
    private List<DataSpecColumn> columns = new ArrayList<>();

    public static DataSpecTable empty(String name) {
        DataSpecTable table = new DataSpecTable();
        table.setName(name);
        table.setColumns(new ArrayList<>());
        table.setRowCount(0L);
        return table;
    }
}