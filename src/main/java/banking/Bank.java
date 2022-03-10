package banking;

import banking.database.AccountDao;
import banking.model.Account;
import banking.model.Card;
import banking.util.CardGenerator;
import banking.util.CardValidator;

import java.util.Optional;

/**
 * The Bank class is responsible for managing the accounts of the bank
 */
public class Bank {

    private final AccountDao accountDao;
    private final CardValidator cardValidator;
    private final CardGenerator cardGenerator;

    public Bank(AccountDao accountDao, CardValidator cardValidator, CardGenerator cardGenerator) {
        this.accountDao = accountDao;
        this.cardValidator = cardValidator;
        this.cardGenerator = cardGenerator;
    }

    /**
     * Generate a card number that doesn't already exist in the database
     *
     * @return Generated Card object.
     */
    public Card registerAccount() {
        Card card;

        do {
            card = cardGenerator.generate();
        } while (exists(card.cardNumber()));

        accountDao.save(card);
        return card;
    }

    /**
     * It returns an optional account object.
     *
     * @param cardNumber The card number of the account to sign in.
     * @param cardPIN    the PIN of the card that is being used to sign in.
     * @return Optional of Account object or empty one if credentials does not match.
     */
    public Optional<Account> signIn(String cardNumber, String cardPIN) {
        return accountDao.get(cardNumber, cardPIN);
    }

    /**
     * Return true if the card exists in the database, false otherwise.
     *
     * @param cardNumber the card number of the account to be checked
     * @return A boolean value.
     */
    public boolean exists(String cardNumber) {
        return accountDao.get(cardNumber).isPresent();
    }

    /**
     * It adds income to the account.
     *
     * @param cardNumber the card number of the account to update
     * @param income     the amount of money that will be added to the account.
     */
    public void addIncome(String cardNumber, int income) {
        accountDao.update(cardNumber, income);
    }

    /**
     * It transfers money from one account to another.
     *
     * @param from   the account number of the account to transfer from
     * @param to     the account number of the account to transfer money to.
     * @param amount The amount of money to transfer.
     */
    public void doTransfer(String from, String to, int amount) {
        accountDao.executeTransferTransaction(from, to, amount);
    }

    /**
     * Remove an account from the database
     *
     * @param id The id of the account to be deleted.
     */
    public void removeAccount(int id) {
        accountDao.delete(id);
    }

    /**
     * Checks if the card number is valid, otherwise it throws an exception.
     *
     * @param cardNumber the card number to check
     */
    public void checkCardNumber(String cardNumber) {
        String errorMessage = "";

        if (!cardValidator.isValidCard(cardNumber)) {
            errorMessage = "Probably you made mistake in the card number. Please try again!";
        } else if (!exists(cardNumber)) {
            errorMessage = "Such a card does not exist.";
        }

        if (!errorMessage.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
