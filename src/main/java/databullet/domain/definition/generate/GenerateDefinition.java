package databullet.domain.definition.generate;

import databullet.domain.definition.Definitions;
import databullet.domain.definition.dataspec.DataSpecDefinition;
import databullet.domain.definition.table.TableDefinition;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GenerateDefinition extends Definitions {

    private Double scale;

    private List<GenerateRelationGroup> relationGroups;

    public GenerateDefinition(Definitions definitions) {
        this(definitions.getTableDef(), definitions.getDataSpec());
    }

    public GenerateDefinition(TableDefinition tableDef, DataSpecDefinition dataSpec) {
        super(tableDef, dataSpec);
        this.scale = dataSpec.getScale();
        this.relationGroups = GenerateDefinitionFactory.createRelationGroups(this);
    }
}
