package databullet.domain.definition.generate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateRelationGroup {

    private List<GenerateTable> genTables = new ArrayList<>();
}
