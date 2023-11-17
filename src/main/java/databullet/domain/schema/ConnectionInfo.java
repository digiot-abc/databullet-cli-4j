package databullet.domain.schema;

import databullet.infrastructure.Database;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectionInfo {

    private Database database;

    private String url;

    private String user;

    private String password;
}
