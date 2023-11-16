package databullet.domain.definition.generate;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GenerateRelationGroup {

    private List<GenerateTable> genTables = new ArrayList<>();
}
