package databullet.domain.schema;

import lombok.SneakyThrows;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.LoadOptionsBuilder;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.utility.SchemaCrawlerUtility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SchemaParser {

    @SneakyThrows
    public static void parse(Path sqlPath) {
        final LoadOptionsBuilder loadOptionsBuilder = LoadOptionsBuilder.builder()
                // スキーマの詳細情報をロードするためのオプションを設定
                .withSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
        final SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
                .withLoadOptions(loadOptionsBuilder.toOptions());

        // データベース接続設定
        final String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
        final String user = "sa";
        final String password = "";
        final Connection connection = DriverManager.getConnection(url, user, password);

        String sqlString = Files.readString(sqlPath);
        System.out.println(sqlString);
        connection.createStatement().executeUpdate(sqlString);

        final Catalog catalog = SchemaCrawlerUtility.getCatalog(connection, options);
        List<Table> tables =  catalog.getSchemas().stream()
                .filter(s -> s.getName().equals("PUBLIC"))
                .map(catalog::getTables)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        System.out.println(SchemaConverter.convertTablesToTableDefinitionJson(tables));
        System.out.println(SchemaConverter.convertTablesToDataSpecDefinitionJson(tables));
    }

    public static String readFileAndRemoveInvisibleChars(Path path) throws IOException {
        // ファイルの内容を読み込み、不可視の制御文字を取り除く
        String content = Files.lines(path)
                .map(line -> line.replaceAll("\\p{Cntrl}", "")) // 制御文字を削除
                .collect(Collectors.joining(System.lineSeparator()));

        return content;
    }
}
