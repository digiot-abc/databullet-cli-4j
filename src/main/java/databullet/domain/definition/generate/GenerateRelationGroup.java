package databullet.domain.definition.generate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateRelationGroup {

    private Collection<GenerateTable> genTables = new TreeSet<>();
}
