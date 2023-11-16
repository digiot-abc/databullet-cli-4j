package databullet.domain.definition;

import databullet.domain.definition.generate.GenerateTable;
import databullet.domain.definition.generate.GenerateTableFactory;
import databullet.domain.definition.data.DataSpecDefinition;
import databullet.domain.definition.table.RelationInfo;
import databullet.domain.definition.table.TableDefinition;
import lombok.Getter;

import java.util.List;

@Getter
public class GenerateDefinitions extends Definitions {

    private Double scale;

    private List<GenerateTable> genTables;

    public GenerateDefinitions(Definitions definitions) {
        this(definitions.getTableDef(), definitions.getRelationInfo(), definitions.getDataSpec());
    }

    public GenerateDefinitions(TableDefinition tableDef, RelationInfo relationInfo, DataSpecDefinition dataSpec) {
        super(tableDef, relationInfo, dataSpec);
        this.scale = dataSpec.getScale();
        this.genTables = GenerateTableFactory.create(this);
    }
}
