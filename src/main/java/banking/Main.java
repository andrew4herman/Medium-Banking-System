package banking;

import banking.database.AccountDao;
import banking.database.DBManager;
import banking.util.CliParser;

import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Optional<String> fileName = new CliParser(args).optionOf("-fileName");

        if (fileName.isPresent()) {
            DBManager dbManager = new DBManager(fileName.get());
            AccountDao accountDao = new AccountDao(dbManager);
            Bank bank = new Bank(accountDao, "400000");
            BankingSystem system = new BankingSystem(bank, new Scanner(System.in));

            system.start();
        } else {
            throw new IllegalArgumentException("Incorrect option for -fileName");
        }
    }
}
