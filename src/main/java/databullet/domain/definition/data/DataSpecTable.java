package databullet.domain.definition.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DataSpecTable {
    private String name;
    private int rowCount;
    private List<DataSpecColumn> columns = new ArrayList<>();
}