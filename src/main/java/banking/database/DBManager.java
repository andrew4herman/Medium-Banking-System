package banking.database;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private final SQLiteDataSource dataSource;
    private Connection connection;

    public DBManager(String fileName) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + fileName);
        tryToCreateConnection();
        executeQuery(AccountQuery.CREATE_TABLE.getQuery());
    }

    private void executeQuery(String sqlQuery) {
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void tryToCreateConnection() {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            System.err.println("Cannot create a connection with database!");
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                System.err.println("Cannot close a connection with database!");
            }
        }
    }
}
