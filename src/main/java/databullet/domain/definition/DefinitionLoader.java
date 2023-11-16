package databullet.domain.definition;

import databullet.domain.definition.data.DataSpecDefinition;
import databullet.domain.definition.generate.GenerateDefinition;
import databullet.domain.definition.table.TableDefinition;
import databullet.infrastructure.JsonMapper;

import java.nio.file.Path;

public class DefinitionLoader {

    static final String dataSpecJson = "data_spec.json";
    static final String tableDefsJson = "table_defs.json";

    public static Definitions load(Path basePath) {

        JsonMapper mapper = new JsonMapper();
        TableDefinition tableDef = mapper.mapJsonToObject(basePath.resolve(tableDefsJson), TableDefinition.class);
        DataSpecDefinition dataSpec = mapper.mapJsonToObject(basePath.resolve(dataSpecJson), DataSpecDefinition.class);

        // テーブル生成用オブジェクト
        return new Definitions(tableDef, dataSpec);
    }

    public static GenerateDefinition load4generate(Path basePath) {
        return new GenerateDefinition(load(basePath));
    }
}
