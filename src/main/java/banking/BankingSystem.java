package banking;

import banking.database.DatabaseController;
import banking.model.Account;
import banking.util.CardGenerator;

import java.util.Scanner;

public class BankingSystem {

    private Bank bank;
    private Account currentUser;
    private Scanner scanner;

    public BankingSystem(String dataFile) {
        bank = new Bank(new DatabaseController(dataFile));
        scanner = new Scanner(System.in);
    }

    public void start() {
        int option;

        do {
            showMenu();
            option = scanner.nextInt();

            if (currentUser == null) {
                switch (option) {
                    case 1:
                        createAccountOption();
                        break;
                    case 2:
                        logIntoOption();
                        break;
                }

            } else {
                switch (option) {
                    case 1:
                        System.out.printf(
                                "\nBalance: %d\n", currentUser.balance());
                        break;
                    case 2:
                        addIncomeOption();
                        break;
                    case 3:
                        doTransferOption();
                        break;
                    case 4:
                        closeAccountOption();
                        break;
                    case 5:
                        logOutOption();
                        break;
                }
            }
        } while (option != 0);

        System.out.println("Bye!");
    }

    private void showMenu() {
        if (currentUser == null) {
            System.out.println("\n1. Create an account");
            System.out.println("2. Log into account");
        } else {
            System.out.println("\n1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
        }
        System.out.println("0. Exit");
    }

    private void createAccountOption() {
        Account account = bank.registerAccount();

        System.out.print("\nYour card has been created\n");
        System.out.print("Your card number:\n");
        System.out.printf("%s\n", account.card().cardNumber());
        System.out.print("Your card PIN:\n");
        System.out.printf("%s\n", account.card().PIN());
    }

    private void logIntoOption() {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.next();

        System.out.println("Enter your PIN:");
        String PIN = scanner.next();

        currentUser = bank.signInAccount(cardNumber, PIN);
        if (currentUser != null)
            System.out.println("You have successfully logged in!");
        else
            System.out.println("Wrong card number or PIN!");
    }

    private void addIncomeOption() {
        System.out.print("\nEnter income:\n");
        int income = scanner.nextInt();

        bank.addIncome(currentUser.card().cardNumber(), income);
        currentUser = new Account(
                currentUser.card(),
                bank.getBalance(currentUser.card().cardNumber())
        );

        System.out.print("Income was added!\n");
    }

    private void doTransferOption() {
        System.out.print("\nTransfer\n");
        System.out.print("Enter card number:\n");
        String receiverCardNumber = scanner.next();

        if (CardGenerator.isLuhnCorrect(receiverCardNumber)) {
            if (!receiverCardNumber.equals(currentUser.card().cardNumber())) {
                if (bank.exists(receiverCardNumber)) {

                    System.out.println("Enter how much money you want to transfer:");
                    int money = scanner.nextInt();

                    if (money <= currentUser.balance()) {

                        bank.doTransfer(currentUser.card().cardNumber(), receiverCardNumber, money);
                        currentUser = new Account(
                                currentUser.card(),
                                bank.getBalance(currentUser.card().cardNumber())
                        );
                        System.out.print("Success!\n");

                    } else System.out.print("Not enough money!\n");
                } else
                    System.out.println("Such a card does not exist.\\n");
            } else
                System.out.println("You can't transfer money to the same account!\\n");
        } else
            System.out.println("Probably you made mistake in the card number. Please try again!\\n");
    }

    private void closeAccountOption() {
        bank.deleteUser(currentUser.card().cardNumber());
        currentUser = null;

        System.out.println("The account has been closed!");
    }

    private void logOutOption() {
        currentUser = null;
        System.out.println("You have successfully logged out!");
    }

}
