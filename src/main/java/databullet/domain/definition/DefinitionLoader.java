package databullet.domain.definition;

import databullet.domain.definition.data.DataSpecDefinition;
import databullet.domain.definition.table.RelationInfo;
import databullet.domain.definition.table.TableDefinition;
import databullet.infrastructure.JsonMapper;

import java.nio.file.Path;

public class DefinitionLoader {

    static final String dataSpecJson = "data_spec.json";
    static final String relationInfoJson = "relation_info.json";
    static final String tableDefsJson = "table_defs.json";

    public static Definitions load(Path basePath) {

        JsonMapper mapper = new JsonMapper();
        TableDefinition tableDef = mapper.mapJsonToObject(basePath.resolve(tableDefsJson), TableDefinition.class);
        RelationInfo relationInfo = mapper.mapJsonToObject(basePath.resolve(relationInfoJson), RelationInfo.class);
        DataSpecDefinition dataSpec = mapper.mapJsonToObject(basePath.resolve(dataSpecJson), DataSpecDefinition.class);

        // テーブル生成用オブジェクト
        return new Definitions(tableDef, relationInfo, dataSpec);
    }

    public static GenerateDefinitions load4generate(Path basePath) {
        return new GenerateDefinitions(load(basePath));
    }
}
