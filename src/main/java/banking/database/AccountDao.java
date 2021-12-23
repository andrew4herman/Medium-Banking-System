package banking.database;

import banking.model.Account;
import banking.model.Card;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDao {

    private final DBManager dbManager;

    public AccountDao(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public Optional<Account> get(String cardNumber, String cardPIN) {
        try (Connection connection = dbManager.createConnection();
             PreparedStatement statement = connection.prepareStatement(
                     AccountQuery.GET.getQuery()
             )) {
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

        try (Connection connection = dbManager.createConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(AccountQuery.GET_ALL.getQuery());

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
        try (Connection connection = dbManager.createConnection();
             PreparedStatement statement = connection.prepareStatement(
                     AccountQuery.SAVE.getQuery()
             )) {
            statement.setString(1, card.cardNumber());
            statement.setString(2, card.PIN());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String cardNumber, int income) {
        try (Connection connection = dbManager.createConnection();
             PreparedStatement statement = connection.prepareStatement(
                     AccountQuery.UPDATE_BALANCE.getQuery()
             )) {
            statement.setInt(1, income);
            statement.setString(2, cardNumber);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection connection = dbManager.createConnection();
             PreparedStatement statement = connection.prepareStatement(
                     AccountQuery.DELETE.getQuery()
             )) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
