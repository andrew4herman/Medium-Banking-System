package banking.util;

/**
 * That class represents a validator of banking cards.
 */
public class CardValidator {

    /**
     * Given a string of numbers, return the check digit using a Luhn algorithm.
     *
     * @param number The credit card number you want to use.
     * @return The check sum.
     */
    public char getCheckSumFor(String number) {
        String[] numbers = number.split("");
        int sum = 0;

        for (int i = 0; i < numbers.length; i++) {
            int num = Integer.parseInt(numbers[i]);

            if (i % 2 == 0) num *= 2;
            if (num > 9) num -= 9;

            sum += num;
        }

        return String.valueOf((10 - (sum % 10)) % 10).charAt(0);
    }

    /**
     * Given a card number, return true if it is valid and false otherwise <br/>
     * Card considers valid if the last digit of the number corresponds of Luhn algorithm.
     *
     * @param number The card number as a string.
     * @return A boolean value.
     */
    public boolean isValidCard(String number) {
        return number.length() == 16 &&
                number.charAt(15) == getCheckSumFor(number.substring(0, number.length() - 1));
    }
}
