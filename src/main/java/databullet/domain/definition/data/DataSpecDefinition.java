package databullet.domain.definition.data;

import lombok.Data;

import java.util.List;

@Data
public class DataSpecDefinition {

    private Double scale;
    private List<DataSpecTable> tables;
}
