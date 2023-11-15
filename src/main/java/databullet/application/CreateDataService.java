package databullet.application;

import databullet.domain.definition.data.DataSpecification;
import databullet.domain.definition.table.RelationInfo;
import databullet.domain.definition.table.TableDefinition;
import databullet.infrastructure.JsonMapper;
import lombok.SneakyThrows;

import java.nio.file.Path;

public class CreateDataService {

    @SneakyThrows
    public void write(Path tableDefsPath, Path relationInfoPath, Path dataSpecPath) {

        JsonMapper mapper = new JsonMapper();
        TableDefinition tableDef = mapper.mapJsonToObject(tableDefsPath, TableDefinition.class);
        RelationInfo relationInfo = mapper.mapJsonToObject(relationInfoPath, RelationInfo.class);
        DataSpecification dataSpec = mapper.mapJsonToObject(dataSpecPath, DataSpecification.class);
        System.out.println(tableDef);
        System.out.println(relationInfo);
        System.out.println(dataSpec);
    }
}
