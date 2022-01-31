package banking;

import banking.database.AccountDao;
import banking.database.DBConnector;
import banking.database.DBManager;
import banking.util.CardGenerator;
import banking.util.CardValidator;
import banking.util.CliParser;
import org.sqlite.SQLiteDataSource;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String BINumber = "400000";
        String JDBCDriverName = "jdbc:sqlite:";
        String fileName = new CliParser(args).optionOf("-fileName")
                .orElseThrow(() -> new IllegalArgumentException("Incorrect option for -fileName"));

        String url = JDBCDriverName + fileName;
        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
        sqLiteDataSource.setUrl(url);

        Random random = new Random();
        CardValidator cardValidator = new CardValidator();
        CardGenerator cardGenerator = new CardGenerator(cardValidator, BINumber, random);

        try (DBConnector dbConnector = new DBConnector(sqLiteDataSource);
             Scanner scanner = new Scanner(System.in)) {

            DBManager dbManager = new DBManager(dbConnector);
            dbManager.migrateUp();

            AccountDao accountDao = new AccountDao(dbConnector);
            Bank bank = new Bank(accountDao, cardValidator, cardGenerator);
            BankingSystem system = new BankingSystem(bank, scanner);

            system.start();
        } catch (RuntimeException e) {
            System.out.println("Execution of this application has stopped due to an error: ");
            System.out.println(e.getMessage());

            e.printStackTrace();
        }
    }
}
