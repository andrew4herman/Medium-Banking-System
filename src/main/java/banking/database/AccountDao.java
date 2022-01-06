package banking.database;

import banking.model.Account;
import banking.model.Card;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDao {

    public static final String SQL_GET = "SELECT * FROM account WHERE cardNumber = ? AND cardPin = ?;";
    public static final String SQL_GET_ALL = "SELECT * FROM account;";
    public static final String SQL_SAVE = "INSERT INTO account(cardNumber, cardPin) VALUES(?, ?);";
    public static final String SQL_UPDATE_BALANCE = "UPDATE account SET balance = balance + ? WHERE cardNumber = ?";
    public static final String SQL_DELETE = "DELETE FROM account WHERE id = ?;";

    private final DBManager dbManager;

    public AccountDao(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public Optional<Account> get(String cardNumber, String cardPIN) {
        try (PreparedStatement statement =
                     dbManager.getConnection().prepareStatement(SQL_GET)) {
            statement.setString(1, cardNumber);
            statement.setString(2, cardPIN);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new Account(
                        rs.getInt("id"),
                        new Card(rs.getString("cardNumber"), rs.getString("cardPIN")),
                        rs.getInt("balance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Account> getAll() {
        List<Account> result = new ArrayList<>();

        try (Statement statement = dbManager.getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery(SQL_GET_ALL);

            while (rs.next()) {
                result.add(new Account(
                        rs.getInt("id"),
                        new Card(rs.getString("cardNumber"), rs.getString("cardPIN")),
                        rs.getInt("balance"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void save(Card card) {
        try (PreparedStatement statement =
                     dbManager.getConnection().prepareStatement(SQL_SAVE)) {
            statement.setString(1, card.cardNumber());
            statement.setString(2, card.PIN());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String cardNumber, int income) {
        try (PreparedStatement statement =
                     dbManager.getConnection().prepareStatement(SQL_UPDATE_BALANCE)) {
            statement.setInt(1, income);
            statement.setString(2, cardNumber);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (PreparedStatement statement =
                     dbManager.getConnection().prepareStatement(SQL_DELETE)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
