package banking;

import banking.database.AccountDao;
import banking.database.DBManager;
import banking.util.CardGenerator;
import banking.util.CardValidator;
import banking.util.CliParser;

import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String BINumber = "400000";
        Optional<String> fileName = new CliParser(args).optionOf("-fileName");

        if (fileName.isPresent()) {
            DBManager dbManager = new DBManager(fileName.get());
            AccountDao accountDao = new AccountDao(dbManager);

            CardValidator cardValidator = new CardValidator();
            CardGenerator cardGenerator = new CardGenerator(cardValidator, BINumber);

            Bank bank = new Bank(accountDao, cardValidator, cardGenerator);
            BankingSystem system = new BankingSystem(bank, new Scanner(System.in));

            system.start();
            dbManager.closeConnection();
        } else {
            throw new IllegalArgumentException("Incorrect option for -fileName");
        }
    }
}
