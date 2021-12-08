package banking;

import banking.util.CliParser;

public class Main {

    public static void main(String[] args) {
        CliParser parser = new CliParser(args);

        BankingSystem bankingSystem = new BankingSystem(
                parser.optionOrDefault("-fileName", "default.db")
        );

        bankingSystem.start();
    }
}
