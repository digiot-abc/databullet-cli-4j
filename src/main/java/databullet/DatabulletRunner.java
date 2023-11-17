package databullet;

import databullet.application.GenerateService;
import databullet.domain.definition.DefinitionLoader;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabulletRunner {

    public static void main(String... args) {
        GenerateService service = new GenerateService();
//        service.generatePrint(DefinitionLoader.load4generate(Paths.get("./src/test/resources")));
        service.generateCsv(DefinitionLoader.load4generate(Paths.get("./src/test/resources")), Path.of("./target/output"));
    }
}
