package banking.database;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector implements AutoCloseable {

    private final SQLiteDataSource dataSource;
    private Connection connection;

    public DBConnector(SQLiteDataSource dataSource) {
        this.dataSource = dataSource;
        attemptConnection();
    }

    private void attemptConnection() {
        try {
            this.connection = dataSource.getConnection();
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create a connection with database!", e);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void close() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Cannot close a connection with database!", e);
            }
        }
    }
}
