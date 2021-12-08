package banking.util;

import banking.model.Card;

import java.util.Random;

public class CardGenerator {

    private static String BINumber;

    public static Card generate(String BINumber) {
        CardGenerator.BINumber = BINumber;

        Card card = new Card(
                createCardNumber(),
                createPIN()
        );

        return card;
    }

    private static String createCardNumber() {
        String accIdentifier = generateNum(9);
        String checksum = getCheckSumFor(BINumber + accIdentifier);

        return (BINumber + accIdentifier + checksum);
    }

    private static String createPIN() {
        return generateNum(4);
    }

    private static String generateNum(int size) {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < size; i++) {
            number.append((new Random()).nextInt(10));
        }
        return number.toString();
    }

    private static String getCheckSumFor(String number) {
        String[] numbers = number.split("");
        int sum = 0;

        for (int i = 0; i < numbers.length; i++) {
            int num = Integer.parseInt(numbers[i]);

            if (i % 2 == 0) num *= 2;
            if (num > 9) num -= 9;

            sum += num;
        }

        return ((10 - (sum % 10)) % 10) + "";
    }

    public static boolean isLuhnCorrect(String number) {
        return getCheckSumFor(number.substring(0, number.length() - 1))
                .equals(number.substring(number.length() - 1));
    }
}
