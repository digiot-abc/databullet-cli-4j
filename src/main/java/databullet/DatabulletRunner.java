package databullet;

import databullet.application.GenerateService;
import databullet.application.SchemaService;
import databullet.domain.definition.DefinitionLoader;
import databullet.domain.definition.Definitions;
import databullet.domain.definition.generate.GenerateDefinition;
import databullet.domain.schema.ConnectionInfo;
import databullet.infrastructure.Database;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabulletRunner {

  public static void main(String... args) throws Exception {
    System.exit(new DatabulletRunner().execute2());
  }

  public int execute() throws Exception {
    Definitions definitions = DefinitionLoader.load4generate(Paths.get("./src/test/resources"));
    GenerateService generateService = new GenerateService();
    generateService.generateCsv(new GenerateDefinition(definitions), Path.of("./target/output"));

    return 0;
  }

  public int execute2() throws Exception {

    String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String password = "postgres";

    ConnectionInfo connectionInfo = new ConnectionInfo(Database.PostgreSQL, url, user, password);

    SchemaService schemaService = new SchemaService(connectionInfo);
    Definitions definitions = schemaService.parseFromDB("app");

    GenerateService generateService = new GenerateService();
    generateService.generateCsv(new GenerateDefinition(definitions), Path.of("./target/output"));

    return 0;
  }

  public int execute3() throws Exception {

    String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    String user = "sa";
    String password = "";

    ConnectionInfo connectionInfo = new ConnectionInfo(Database.PostgreSQL, url, user, password);

    SchemaService schemaService = new SchemaService(connectionInfo);
        Definitions definitions = schemaService.parseFromSQL("PUBLIC", Paths.get("./src/test/resources/databullet/domain/parse/SchemaParserTest/tables.sql"));

    GenerateService generateService = new GenerateService();
    generateService.generateCsv(new GenerateDefinition(definitions), Path.of("./target/output"));

    return 0;
  }
}
