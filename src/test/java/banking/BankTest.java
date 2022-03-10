package banking;

import banking.database.AccountDao;
import banking.model.Account;
import banking.model.Card;
import banking.util.CardGenerator;
import banking.util.CardValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Random;

import static banking.factory.AccountFactory.ACCOUNT_ID;
import static banking.factory.AccountFactory.createAccount;
import static banking.factory.CardFactory.CARD_NUMBER;
import static banking.factory.CardFactory.CARD_PIN;
import static banking.factory.CardFactory.createCard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BankTest {

    private AccountDao accountDao;
    private Bank bank;

    @BeforeEach
    void setUp() {
        CardValidator cardValidator = new CardValidator();
        String BINumber = "400000";
        Random random = new Random(100);

        accountDao = mock(AccountDao.class);
        CardGenerator cardGenerator = new CardGenerator(cardValidator, BINumber, random);
        bank = new Bank(accountDao, cardValidator, cardGenerator);
    }

    @Test
    void registerAccount() {
        //given
        Card expectedCard = createCard();
        when(accountDao.get(CARD_NUMBER)).thenReturn(Optional.empty());

        //when
        Card createdCard = bank.registerAccount();

        //then
        verify(accountDao).get(CARD_NUMBER);
        assertNotNull(createdCard);
        assertEquals(expectedCard, createdCard);
    }

    @Test
    void signIn() {
        //given
        Account expectedAccount = createAccount();
        when(accountDao.get(CARD_NUMBER, CARD_PIN)).thenReturn(Optional.of(expectedAccount));

        //when
        Account returnedAccount = bank.signIn(CARD_NUMBER, CARD_PIN).orElseThrow();

        //then
        verify(accountDao).get(CARD_NUMBER, CARD_PIN);
        assertNotNull(returnedAccount);
        assertEquals(expectedAccount, returnedAccount);
    }

    @Test
    void exists() {
        //given
        Account account = createAccount();
        when(accountDao.get(CARD_NUMBER)).thenReturn(Optional.of(account));

        //when
        boolean result = bank.exists(CARD_NUMBER);

        //then
        verify(accountDao).get(CARD_NUMBER);
        assertTrue(result);
    }

    @Test
    void addIncome() {
        //given

        //when
        bank.addIncome(CARD_NUMBER, 100);

        //then
        verify(accountDao).update(CARD_NUMBER, 100);
    }

    @Test
    void doTransfer() {
        //given
        String CARD_NUMBER_TO = "4000001234567890";

        //when
        bank.doTransfer(CARD_NUMBER, CARD_NUMBER_TO, 100);

        //then
        verify(accountDao).executeTransferTransaction(CARD_NUMBER, CARD_NUMBER_TO, 100);
    }

    @Test
    void removeAccount() {
        //given

        //when
        bank.removeAccount(ACCOUNT_ID);

        //then
        verify(accountDao).delete(ACCOUNT_ID);
    }

    @Test
    void checkValidCardNumber() {
        //given
        when(accountDao.get(CARD_NUMBER)).thenReturn(Optional.of(createAccount()));

        //when
        bank.checkCardNumber(CARD_NUMBER);

        //then
        verify(accountDao).get(CARD_NUMBER);
    }

    @Test
    void checkInvalidCardNumber() {
        //given
        String WRONG_CARD_NUMBER = "4000009999999999";

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bank.checkCardNumber(WRONG_CARD_NUMBER));

        //then
        assertEquals("Probably you made mistake in the card number. Please try again!",
                exception.getMessage());
    }

    @Test
    void checkNonExistentCardNumber() {
        //given
        when(accountDao.get(CARD_NUMBER)).thenReturn(Optional.empty());

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bank.checkCardNumber(CARD_NUMBER));

        //then
        verify(accountDao).get(CARD_NUMBER);
        assertEquals("Such a card does not exist.",
                exception.getMessage());
    }


}
