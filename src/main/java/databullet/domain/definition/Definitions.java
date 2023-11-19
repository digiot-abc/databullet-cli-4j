package databullet.domain.definition;

import databullet.domain.definition.dataspec.DataSpecDefinition;
import databullet.domain.definition.table.TableDefinition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Definitions {

    private TableDefinition tableDef;
    private DataSpecDefinition dataSpec;
}
