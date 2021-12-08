package banking.database;

import banking.model.Account;
import banking.model.Card;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {

    private final SQLiteDataSource dataSource;

    public DatabaseController(String dataPath) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:test.db" + dataPath);

        createIfNotExists();
    }

    private void createIfNotExists() {
        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {

            statement.executeUpdate(DBQuery.CREATE_TABLE.getQuery());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertValues(String number, String pin) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     DBQuery.INSERT_INTO.getQuery())
        ) {
            statement.setString(1, number);
            statement.setString(2, pin);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement()) {

            ResultSet result = statement.executeQuery(DBQuery.SELECT_ALL.getQuery());

            while (result.next()) {
                accounts.add(new Account(
                        new Card(result.getString("number"),
                                result.getString("pin")),
                        result.getInt("balance")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    public String getValue(String cardNumber, String columnLabel) {
        StringBuilder result = new StringBuilder();
        String sql = String.format(
                DBQuery.SELECT_VALUE.getQuery(),
                columnLabel
        );

        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(sql)) {

            statement.setString(1, cardNumber);
            ResultSet cardInfo = statement.executeQuery();

            if (cardInfo.next()) {
                result.append(cardInfo.getString(columnLabel));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public void updateValue(String cardNumber, String columnLabel, int newValue) {
        String sqlUpdate = String.format(
                DBQuery.UPDATE_VALUE.getQuery(),
                columnLabel);

        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(sqlUpdate)) {

            statement.setInt(1, newValue);
            statement.setString(2, cardNumber);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(String cardNumber) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(DBQuery.DELETE_ROW.getQuery())) {

            statement.setString(1, cardNumber);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
