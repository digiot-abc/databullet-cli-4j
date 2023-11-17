package databullet.infrastructure;

import lombok.AllArgsConstructor;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.*;
import schemacrawler.tools.utility.SchemaCrawlerUtility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DatabaseAccessor {

    private String url;

    private String user;

    private String password;

    public Connection connect() throws SQLException {
        return  DriverManager.getConnection(url, user, password);
    }

    public int executeUpdate(String sql) throws SQLException {
        try (Connection con = connect()) {
            return con.createStatement().executeUpdate(sql);
        }
    }

    public Catalog fetchCatalog() throws SQLException, SchemaCrawlerException {

        final LoadOptionsBuilder loadOptionsBuilder = LoadOptionsBuilder.builder()
                .withSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
        final SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
                .withLoadOptions(loadOptionsBuilder.toOptions());

        try (Connection con = connect()) {
            return SchemaCrawlerUtility.getCatalog(con, options);
        }
    }

    public List<Table> fetchTables(String schema) throws SQLException, SchemaCrawlerException {
        Catalog catalog = fetchCatalog();
        return fetchCatalog().getSchemas().stream().filter(s -> s.getName().equals(schema))
                .map(s -> catalog.getTables(s))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
