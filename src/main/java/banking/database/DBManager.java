package banking.database;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private final SQLiteDataSource dataSource;

    public DBManager(String fileName) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + fileName);

        executeQuery(AccountQuery.CREATE_TABLE.getQuery());
    }

    private void executeQuery(String sqlQuery) {
        try (Connection connection = createConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection createConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
