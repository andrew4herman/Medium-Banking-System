package banking.database;

import banking.model.Account;
import banking.model.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Optional;

/**
 * The class is responsible for managing the accounts in the database
 */
public class AccountDao {

    public static final String GET_BY_CARD_NUMBER =
            "SELECT id, cardNumber, cardPIN, balance FROM account WHERE cardNumber = ?;";
    public static final String GET_BY_CREDENTIALS =
            "SELECT id, cardNumber, cardPIN, balance FROM account WHERE cardNumber = ? AND cardPin = ?;";
    public static final String INSERT_CARD = "INSERT INTO account(cardNumber, cardPin) VALUES(?, ?);";
    public static final String SQL_UPDATE_BALANCE = "UPDATE account SET balance = balance + ? WHERE cardNumber = ?";
    public static final String SQL_DELETE = "DELETE FROM account WHERE id = ?;";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CARD_NUMBER = "cardNumber";
    private static final String COLUMN_CARD_PIN = "cardPIN";
    private static final String COLUMN_BALANCE = "balance";

    private final DBConnector dbConnector;

    public AccountDao(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    /**
     * Given a card number, return the account associated with that card number
     *
     * @param cardNumber the card number of the account to get
     * @return An Optional of Account object or empty one if there is no account with that card number.
     */
    public Optional<Account> get(String cardNumber) {
        try (PreparedStatement statement =
                     dbConnector.getConnection().prepareStatement(GET_BY_CARD_NUMBER)) {
            statement.setString(1, cardNumber);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new Account(
                        rs.getInt(COLUMN_ID),
                        new Card(rs.getString(COLUMN_CARD_NUMBER), rs.getString(COLUMN_CARD_PIN)),
                        rs.getInt(COLUMN_BALANCE)
                ));
            }

            dbConnector.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get account with card number " + cardNumber, e);
        }
        return Optional.empty();
    }

    /**
     * Given a card number and PIN, return the account with that card number and PIN
     *
     * @param cardNumber the card number of the account to get
     * @param cardPIN    the PIN of the card
     * @return An Optional of Account object or empty one if credentials does not match.
     */
    public Optional<Account> get(String cardNumber, String cardPIN) {
        try (PreparedStatement statement =
                     dbConnector.getConnection().prepareStatement(GET_BY_CREDENTIALS)) {
            statement.setString(1, cardNumber);
            statement.setString(2, cardPIN);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new Account(
                        rs.getInt(COLUMN_ID),
                        new Card(rs.getString(COLUMN_CARD_NUMBER), rs.getString(COLUMN_CARD_PIN)),
                        rs.getInt(COLUMN_BALANCE)));
            }

            dbConnector.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get account with card number " + cardNumber, e);
        }
        return Optional.empty();
    }

    /**
     * Takes a Card object as a parameter and saves it to the database
     *
     * @param card the card to be saved
     */
    public void save(Card card) {
        try (PreparedStatement statement =
                     dbConnector.getConnection().prepareStatement(INSERT_CARD)) {
            statement.setString(1, card.cardNumber());
            statement.setString(2, card.PIN());

            statement.executeUpdate();
            dbConnector.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot save card " + card.cardNumber(), e);
        }
    }

    /**
     * Given a card number and an amount of money, update the balance of the card
     *
     * @param cardNumber the card number of the card to update
     * @param income     the amount of money to add to the card
     */
    public void update(String cardNumber, int income) {
        try (PreparedStatement statement =
                     dbConnector.getConnection().prepareStatement(SQL_UPDATE_BALANCE)) {
            statement.setInt(1, income);
            statement.setString(2, cardNumber);

            statement.executeUpdate();
            dbConnector.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot add money to card " + cardNumber, e);
        }
    }

    /**
     * Given an account id, delete the account with that id
     *
     * @param id The id of the account to delete.
     */
    public void delete(int id) {
        try (PreparedStatement statement =
                     dbConnector.getConnection().prepareStatement(SQL_DELETE)) {
            statement.setInt(1, id);

            statement.executeUpdate();
            dbConnector.getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot delete account with id " + id, e);
        }
    }

    /**
     * It transfers money from one account to another.
     *
     * @param from  the account number of the account from which money is to be transferred.
     * @param to    The account to transfer money to.
     * @param money the amount of money to transfer
     */
    public void executeTransferTransaction(String from, String to, int money) {
        try {
            Connection connection = dbConnector.getConnection();
            Savepoint savepoint = connection.setSavepoint();

            try {
                update(from, -money);
                update(to, money);
                connection.commit();
            } catch (RuntimeException | SQLException e) {
                connection.rollback(savepoint);
                throw new RuntimeException("Cannot transfer money. Trying to rollback...", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot rollback to the last savepoint!", e);
        }
    }
}
