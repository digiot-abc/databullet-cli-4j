package databullet.application;

import databullet.domain.definition.data.DataSpecDefinition;
import databullet.domain.definition.table.RelationInfo;
import databullet.domain.definition.table.TableDefinition;
import databullet.domain.generate.GenerateProcessor;
import databullet.domain.generate.GenerateStore;
import databullet.domain.write.CSVWriter;
import databullet.infrastructure.JsonMapper;
import lombok.SneakyThrows;

import java.nio.file.Path;

public class CreateDataService {

    @SneakyThrows
    public void write(Path tableDefsPath, Path relationInfoPath, Path dataSpecPath) {

        JsonMapper mapper = new JsonMapper();
        TableDefinition tableDef = mapper.mapJsonToObject(tableDefsPath, TableDefinition.class);
        RelationInfo relationInfo = mapper.mapJsonToObject(relationInfoPath, RelationInfo.class);
        DataSpecDefinition dataSpec = mapper.mapJsonToObject(dataSpecPath, DataSpecDefinition.class);
        System.out.println(tableDef);
        System.out.println(relationInfo);
        System.out.println(dataSpec);

        // ストアに情報を保管する
        GenerateStore store = new GenerateStore(tableDef, relationInfo, dataSpec);

        long st = System.currentTimeMillis();

        // 生成を行う
        GenerateProcessor processor = new GenerateProcessor(store);

        processor.generate(tableDef, new CSVWriter());

        System.out.println(System.currentTimeMillis() - st);
    }
}
