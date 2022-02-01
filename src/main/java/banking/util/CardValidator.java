package banking.util;

public class CardValidator {

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

    public boolean isValidCard(String number) {
        return number.length() == 16 &&
                number.charAt(15) == getCheckSumFor(number.substring(0, number.length() - 1));
    }
}
