package banking.database;

import banking.model.Account;
import banking.model.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Optional;

public class AccountDao {

    public static final String GET_BY_CARD_NUMBER = "SELECT * FROM account WHERE cardNumber = ?;";
    public static final String GET_BY_CREDENTIALS = "SELECT * FROM account WHERE cardNumber = ? AND cardPin = ?;";
    public static final String INSERT_CARD = "INSERT INTO account(cardNumber, cardPin) VALUES(?, ?);";
    public static final String SQL_UPDATE_BALANCE = "UPDATE account SET balance = balance + ? WHERE cardNumber = ?";
    public static final String SQL_DELETE = "DELETE FROM account WHERE id = ?;";

    private final DBManager dbManager;

    public AccountDao(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public Optional<Account> get(String cardNumber) {
        try (PreparedStatement statement =
                     dbManager.getConnection().prepareStatement(GET_BY_CARD_NUMBER)) {
            statement.setString(1, cardNumber);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new Account(
                        rs.getInt("id"),
                        new Card(rs.getString("cardNumber"), rs.getString("cardPIN")),
                        rs.getInt("balance")
                ));
            }

            dbManager.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get account with card number " + cardNumber, e);
        }
        return Optional.empty();
    }

    public Optional<Account> get(String cardNumber, String cardPIN) {
        try (PreparedStatement statement =
                     dbManager.getConnection().prepareStatement(GET_BY_CREDENTIALS)) {
            statement.setString(1, cardNumber);
            statement.setString(2, cardPIN);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new Account(
                        rs.getInt("id"),
                        new Card(rs.getString("cardNumber"), rs.getString("cardPIN")),
                        rs.getInt("balance")));
            }

            dbManager.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get account with card number " + cardNumber, e);
        }
        return Optional.empty();
    }

    public void save(Card card) {
        try (PreparedStatement statement =
                     dbManager.getConnection().prepareStatement(INSERT_CARD)) {
            statement.setString(1, card.cardNumber());
            statement.setString(2, card.PIN());

            statement.executeUpdate();
            dbManager.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot save card " + card.cardNumber(), e);
        }
    }

    public void update(String cardNumber, int income) {
        try (PreparedStatement statement =
                     dbManager.getConnection().prepareStatement(SQL_UPDATE_BALANCE)) {
            statement.setInt(1, income);
            statement.setString(2, cardNumber);

            statement.executeUpdate();
            dbManager.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot add money to card " + cardNumber, e);
        }
    }

    public void delete(int id) {
        try (PreparedStatement statement =
                     dbManager.getConnection().prepareStatement(SQL_DELETE)) {
            statement.setInt(1, id);

            statement.executeUpdate();
            dbManager.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot delete account with id " + id, e);
        }
    }

    public void executeTransferTransaction(String from, String to, int money) {
        try {
            Connection connection = dbManager.getConnection();
            Savepoint savepoint = connection.setSavepoint();

            try {
                update(from, -money);
                update(to, money);
                connection.commit();
            } catch (RuntimeException | SQLException e) {
                System.out.println("Cannot transfer money. Trying to rollback...");
                connection.rollback(savepoint);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot rollback to the last savepoint!", e);
        }
    }
}
