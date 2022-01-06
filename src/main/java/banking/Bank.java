package banking;

import banking.database.AccountDao;
import banking.model.Account;
import banking.model.Card;
import banking.util.CardGenerator;
import banking.util.CardValidator;

import java.util.Optional;

public class Bank {

    private final AccountDao accountDao;
    private final CardValidator cardValidator;
    private final CardGenerator cardGenerator;

    public Bank(AccountDao accountDao, String BINumber) {
        this.accountDao = accountDao;
        this.cardValidator = new CardValidator();
        this.cardGenerator = new CardGenerator(cardValidator, BINumber);
    }

    public Card registerAccount() {
        Card card;

        do {
            card = cardGenerator.generate();
        } while (exists(card.cardNumber()));

        accountDao.save(card);
        return card;
    }

    public Optional<Account> signIn(String cardNumber, String cardPIN) {
        return accountDao.get(cardNumber, cardPIN);
    }

    public boolean exists(String cardNumber) {
        return accountDao.get(cardNumber).isPresent();
    }

    public void addIncome(String cardNumber, int income) {
        accountDao.update(cardNumber, income);
    }

    public void doTransfer(String from, String to, int amount) {
        addIncome(from, -amount);
        addIncome(to, amount);
    }

    public void removeAccount(int id) {
        accountDao.delete(id);
    }

    public void checkCardNumber(String cardNumber) throws Exception {
        String errorMessage = "";

        if (!cardValidator.isValidCard(cardNumber)) {
            errorMessage = "Probably you made mistake in the card number. Please try again!";
        } else if (!exists(cardNumber)) {
            errorMessage = "Such a card does not exist.";
        }

        if (!errorMessage.isEmpty()) {
            throw new Exception(errorMessage);
        }
    }
}
