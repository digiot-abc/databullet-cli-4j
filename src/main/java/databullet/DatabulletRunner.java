package databullet;

import databullet.application.CreateDataService;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabulletRunner {

    public static void main(String... args) {

        Path dataSpec = Paths.get("./src/test/resources/data_spec.json").toAbsolutePath().normalize();
        Path relInfo = Paths.get("./src/test/resources/relation_info.json").toAbsolutePath().normalize();
        Path tableDefs = Paths.get("./src/test/resources/table_defs.json").toAbsolutePath().normalize();

        new CreateDataService().write(tableDefs, relInfo, dataSpec);
    }
}
