package databullet.application;

import databullet.domain.definition.Definitions;
import databullet.domain.schema.ConnectionInfo;
import databullet.domain.schema.SchemaConverter;
import databullet.infrastructure.DatabaseAccessor;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

public class SchemaService {

    ConnectionInfo connectionInfo;

    public SchemaService(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public Definitions parseFromSQL(String schema, Path sql) throws Exception {

        DatabaseAccessor accessor = new DatabaseAccessor(connectionInfo.getUrl(), connectionInfo.getUser(), connectionInfo.getPassword());
        accessor.executeUpdate("set schema ".concat(schema));
        accessor.executeUpdate(Files.readString(sql));

        return parseFromDB(schema);
    }

    public Definitions parseFromDB(String schema) throws Exception {

        DatabaseAccessor accessor = new DatabaseAccessor(connectionInfo.getUrl(), connectionInfo.getUser(), connectionInfo.getPassword());
        List<Table> tables = accessor.fetchTables(schema);

        return SchemaConverter.convertTablesToDefinitions(tables);
    }
}
