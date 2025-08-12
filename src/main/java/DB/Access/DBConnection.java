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
                String dbURL = properties.getProperty("db.url");
                String dbUser = properties.getProperty("db.user");
                String dbPassword = properties.getProperty("db.pw");

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
