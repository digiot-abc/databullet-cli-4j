package databullet.domain.definition;

import databullet.domain.definition.data.DataSpecDefinition;
import databullet.domain.definition.table.TableDefinition;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Definitions {

    private TableDefinition tableDef;
    private DataSpecDefinition dataSpec;
}
