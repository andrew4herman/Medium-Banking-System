package banking.database;

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

    private final DBConnector dbConnector;

    public DBManager(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void migrateUp() {
        try (Statement statement = dbConnector.getConnection().createStatement()) {
            statement.executeUpdate(SQL_CREATE_TABLE);
            dbConnector.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create table 'account'", e);
        }
    }
}
