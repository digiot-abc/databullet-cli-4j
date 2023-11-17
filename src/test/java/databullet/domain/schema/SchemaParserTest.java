package databullet.domain.schema;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

class SchemaParserTest {

    @Test
    public void test() {
        Path path = Paths.get("./src/test/resources/databullet/domain/parse/SchemaParserTest/tables.sql").toAbsolutePath().normalize();
        System.out.println(path);
        SchemaParser.parse(path);
    }
}