package DB;

import java.sql.Connection;
import java.util.Properties;

public interface IDBConnection {
    IDBConnection openConnection(Properties properties);

    Connection getConnection();

}
