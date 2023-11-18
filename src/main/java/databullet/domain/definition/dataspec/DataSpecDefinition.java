package databullet.domain.definition.dataspec;

import lombok.Data;

import java.util.List;

@Data
public class DataSpecDefinition {

    private Double scale;
    private Integer defaultRowCount = 10;
    private List<DataSpecTable> tables;
}
