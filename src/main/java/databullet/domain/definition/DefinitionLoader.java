package databullet.domain.definition;

import databullet.domain.definition.dataspec.DataSpecDefinition;
import databullet.domain.definition.generate.GenerateDefinition;
import databullet.domain.definition.table.TableDefinition;
import databullet.infrastructure.JsonMapper;

import java.nio.file.Path;

public class DefinitionLoader {

    static final String dataspecsJson = "dataspecs.json";
    static final String tablesJson = "tables.json";

    public static Definitions load(Path basePath) {
        JsonMapper mapper = new JsonMapper();
        TableDefinition tableDef = mapper.mapJsonToObject(basePath.resolve(tablesJson), TableDefinition.class);
        DataSpecDefinition dataSpec = mapper.mapJsonToObject(basePath.resolve(dataspecsJson), DataSpecDefinition.class);
        return new Definitions(tableDef, dataSpec);
    }

    public static GenerateDefinition load4generate(Path basePath) {
        return new GenerateDefinition(load(basePath));
    }
}
