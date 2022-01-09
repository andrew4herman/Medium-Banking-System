package banking.database;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    public static final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS account(
                id INTEGER PRIMARY KEY,
                cardNumber TEXT UNIQUE,
                cardPIN TEXT,
                balance INTEGER DEFAULT 0
            );
            """;

    private final SQLiteDataSource dataSource;
    private Connection connection;

    public DBManager(String fileName) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + fileName);
        tryToCreateConnection();
        executeQuery();
    }

    private void executeQuery() {
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void tryToCreateConnection() {
        try {
            this.connection = dataSource.getConnection();
            this.connection.setAutoCommit(false);
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
