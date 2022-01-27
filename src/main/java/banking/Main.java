package banking;

import banking.database.AccountDao;
import banking.database.DBManager;
import banking.util.CardGenerator;
import banking.util.CardValidator;
import banking.util.CliParser;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String BINumber = "400000";
        String fileName = new CliParser(args).optionOf("-fileName")
                .orElseThrow(() -> new IllegalArgumentException("Incorrect option for -fileName"));

        DBManager dbManager = new DBManager(fileName);
        AccountDao accountDao = new AccountDao(dbManager);

        CardValidator cardValidator = new CardValidator();
        CardGenerator cardGenerator = new CardGenerator(cardValidator, BINumber);

        Bank bank = new Bank(accountDao, cardValidator, cardGenerator);
        BankingSystem system = new BankingSystem(bank, new Scanner(System.in));

        system.start();
        dbManager.closeConnection();
    }
}
