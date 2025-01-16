package DB.Access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection implements IDBConnection {

    private static Connection connection = null;

    @Override
    public IDBConnection openConnection(Properties properties) {
        if(connection == null) {
            try {
                String systemUser = System.getProperty("user.name");
                String dbURL = properties.getProperty(systemUser + ".db.url");
                String dbUser = properties.getProperty(systemUser + ".db.user");
                String dbPassword = properties.getProperty(systemUser + ".db.pw");

                connection = DriverManager.getConnection(dbURL,dbUser,dbPassword);
            } catch (SQLException e){
                System.err.println(e.getMessage());
            }
        }
        return this;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
