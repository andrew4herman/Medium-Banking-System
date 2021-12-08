package banking;

import banking.database.DatabaseController;
import banking.model.Account;
import banking.model.Card;
import banking.util.CardGenerator;

public class Bank {

    private final DatabaseController database;
    private static final String BINumber = "400000";

    public Bank(DatabaseController database) {
        this.database = database;
    }

    public Account registerAccount() {
        Account account;
        Card card;

        do {
            card = CardGenerator.generate(BINumber);
        } while (exists(card.cardNumber()));

        account = new Account(card, 0);
        database.insertValues(card.cardNumber(), card.PIN());

        return account;
    }

    public Account signInAccount(String cardNumber, String PIN) {
        for (Account account : database.getAll()) {
            if (account.card().equals(new Card(cardNumber, PIN))) {
                return account;
            }
        }

        return null;
    }

    public boolean exists(String cardNumber) {
        for (Account account : database.getAll()) {
            if (account.card().cardNumber().equals(cardNumber))
                return true;
        }

        return false;
    }

    public int getBalance(String cardNumber) {
        return Integer.parseInt(database.getValue(cardNumber, "balance"));
    }

    public void addIncome(String cardNumber, int sum) {
        sum += getBalance(cardNumber);
        database.updateValue(cardNumber, "balance", sum);
    }

    public void doTransfer(String from, String to, int sum) {
        addIncome(from, -sum);
        addIncome(to, sum);
    }

    public void deleteUser(String cardNumber) {
        database.deleteRow(cardNumber);
    }
}
