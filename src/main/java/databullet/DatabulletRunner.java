package databullet;

import databullet.application.GenerateService;
import databullet.application.SchemaService;
import databullet.domain.definition.DefinitionLoader;
import databullet.domain.definition.Definitions;
import databullet.domain.definition.generate.GenerateDefinition;
import databullet.domain.definition.generate.GenerateDefinitionFactory;
import databullet.domain.schema.ConnectionInfo;
import databullet.domain.schema.SchemaConverter;
import databullet.infrastructure.Database;
import databullet.infrastructure.DatabaseAccessor;
import schemacrawler.schema.Table;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DatabulletRunner {

    public static void main(String... args) throws Exception {
        System.exit(new DatabulletRunner().execute());
    }

    public int execute() throws Exception {

        ConnectionInfo connectionInfo = new ConnectionInfo(Database.H2, "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");

        SchemaService schemaService = new SchemaService(connectionInfo);
        Definitions definitions = schemaService.parseFromSQL("PUBLIC", Paths.get("./src/test/resources/databullet/domain/parse/SchemaParserTest/tables.sql"));
//        Definitions definitions = DefinitionLoader.load4generate(Paths.get("./src/test/resources");

        GenerateService generateService = new GenerateService();
        generateService.generateCsv(new GenerateDefinition(definitions), Path.of("./target/output"));

        return 0;
    }
}
