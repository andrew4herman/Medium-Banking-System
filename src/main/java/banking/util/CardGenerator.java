package banking.util;

import banking.model.Card;

import java.util.Random;

/**
 * Class for generating a Card objects with number and a PIN
 */
public class CardGenerator {

    private final CardValidator cardValidator;
    private final String BINumber;
    private final Random random;

    private static final int PIN_SIZE = 4;
    private static final int ACC_IDENTIFIER_SIZE = 9;

    public CardGenerator(CardValidator cardValidator, String BINumber, Random random) {
        this.cardValidator = cardValidator;
        this.BINumber = BINumber;
        this.random = random;
    }

    /**
     * Create a card number and PIN
     *
     * @return A Card object.
     */
    public Card generate() {
        return new Card(createCardNumber(), createPIN());
    }

    private String createCardNumber() {
        String accIdentifier = generateNum(ACC_IDENTIFIER_SIZE);
        String checksum = String.valueOf(cardValidator.getCheckSumFor(BINumber + accIdentifier));

        return (BINumber + accIdentifier + checksum);
    }

    private String createPIN() {
        return generateNum(PIN_SIZE);
    }

    private String generateNum(int size) {
        StringBuilder number = new StringBuilder();
        int randomBound = 10;

        for (int i = 0; i < size; i++) {
            number.append(random.nextInt(randomBound));
        }
        return number.toString();
    }
}
