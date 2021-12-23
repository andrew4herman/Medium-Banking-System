package banking.util;

import banking.model.Card;

import java.util.Random;

public class CardGenerator {

    private final CardValidator cardValidator;
    private final String BINumber;

    public CardGenerator(CardValidator cardValidator, String BINumber) {
        this.cardValidator = cardValidator;
        this.BINumber = BINumber;
    }

    public Card generate() {
        return new Card(
                createCardNumber(),
                createPIN()
        );
    }

    private String createCardNumber() {
        String accIdentifier = generateNum(9);
        String checksum = String.valueOf(cardValidator.getCheckSumFor(BINumber + accIdentifier));

        return (BINumber + accIdentifier + checksum);
    }

    private String createPIN() {
        return generateNum(4);
    }

    private static String generateNum(int size) {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < size; i++) {
            number.append((new Random()).nextInt(10));
        }
        return number.toString();
    }
}
