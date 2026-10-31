import java.util.ArrayList;
import java.util.Scanner;

/*
 * Banking Information System
 * Core Java Internship Project - upskill Campus / UCT
 *
 * A simple console based program to manage bank accounts.
 * Features: create account, deposit, withdraw, transfer,
 * check balance, view account details, view all accounts.
 */

// This class stores the data of one customer account
class Account {
    private int accountNumber;
    private String holderName;
    private String phone;
    private double balance;

    public Account(int accountNumber, String holderName, String phone, double openingBalance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.phone = phone;
        this.balance = openingBalance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getPhone() {
        return phone;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance = balance + amount;
    }

    // returns true if withdrawal worked, false if balance is not enough
    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance = balance - amount;
        return true;
    }

    public void showDetails() {
        System.out.println("----------------------------------");
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Holder Name    : " + holderName);
        System.out.println("Phone          : " + phone);
        System.out.println("Balance        : Rs. " + String.format("%.2f", balance));
        System.out.println("----------------------------------");
    }
}

// This class handles the bank work and the menu
public class BankingInformationSystem {

    private static ArrayList<Account> accounts = new ArrayList<Account>();
    private static Scanner sc = new Scanner(System.in);
    private static int nextAccountNumber = 1001; // account numbers start from 1001

    public static void main(String[] args) {
        int choice = 0;

        System.out.println("=====================================");
        System.out.println("     BANKING INFORMATION SYSTEM");
        System.out.println("=====================================");

        do {
            showMenu();
            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    withdrawMoney();
                    break;
                case 4:
                    transferMoney();
                    break;
                case 5:
                    checkBalance();
                    break;
                case 6:
                    viewAccountDetails();
                    break;
                case 7:
                    viewAllAccounts();
                    break;
                case 8:
                    System.out.println("Thank you for using the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, please enter a number from 1 to 8.");
            }
        } while (choice != 8);

        sc.close();
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("1. Create new account");
        System.out.println("2. Deposit money");
        System.out.println("3. Withdraw money");
        System.out.println("4. Transfer money");
        System.out.println("5. Check balance");
        System.out.println("6. View account details");
        System.out.println("7. View all accounts");
        System.out.println("8. Exit");
    }

    // ---------- helper methods to read input safely ----------

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readAmount(String message) {
        while (true) {
            System.out.print(message);
            String line = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value <= 0) {
                    System.out.println("Amount must be greater than zero.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    // looks for an account in the list, returns null if not found
    private static Account findAccount(int accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accountNumber) {
                return acc;
            }
        }
        return null;
    }

    // asks the user for an account number and returns that account
    private static Account askForAccount(String message) {
        int number = readInt(message);
        Account acc = findAccount(number);
        if (acc == null) {
            System.out.println("No account found with number " + number + ".");
        }
        return acc;
    }

    // ---------- main features ----------

    private static void createAccount() {
        System.out.println("\n--- Create New Account ---");

        String name = "";
        while (name.isEmpty()) {
            System.out.print("Enter holder name: ");
            name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
            }
        }

        String phone = "";
        while (!phone.matches("\\d{10}")) {
            System.out.print("Enter 10 digit phone number: ");
            phone = sc.nextLine().trim();
            if (!phone.matches("\\d{10}")) {
                System.out.println("Phone number must have exactly 10 digits.");
            }
        }

        double openingBalance = -1;
        while (openingBalance < 500) {
            openingBalance = readAmount("Enter opening balance (minimum Rs. 500): ");
            if (openingBalance < 500) {
                System.out.println("Minimum opening balance is Rs. 500.");
            }
        }

        Account acc = new Account(nextAccountNumber, name, phone, openingBalance);
        accounts.add(acc);
        nextAccountNumber++;

        System.out.println("Account created successfully!");
        System.out.println("Your account number is " + acc.getAccountNumber());
    }

    private static void depositMoney() {
        System.out.println("\n--- Deposit Money ---");
        Account acc = askForAccount("Enter account number: ");
        if (acc == null) {
            return;
        }

        double amount = readAmount("Enter amount to deposit: ");
        acc.deposit(amount);
        System.out.println("Rs. " + String.format("%.2f", amount) + " deposited successfully.");
        System.out.println("Updated balance: Rs. " + String.format("%.2f", acc.getBalance()));
    }

    private static void withdrawMoney() {
        System.out.println("\n--- Withdraw Money ---");
        Account acc = askForAccount("Enter account number: ");
        if (acc == null) {
            return;
        }

        double amount = readAmount("Enter amount to withdraw: ");
        if (acc.withdraw(amount)) {
            System.out.println("Rs. " + String.format("%.2f", amount) + " withdrawn successfully.");
            System.out.println("Remaining balance: Rs. " + String.format("%.2f", acc.getBalance()));
        } else {
            System.out.println("Insufficient balance! Your balance is Rs. "
                    + String.format("%.2f", acc.getBalance()));
        }
    }

    private static void transferMoney() {
        System.out.println("\n--- Transfer Money ---");
        Account from = askForAccount("Enter sender account number: ");
        if (from == null) {
            return;
        }

        Account to = askForAccount("Enter receiver account number: ");
        if (to == null) {
            return;
        }

        if (from.getAccountNumber() == to.getAccountNumber()) {
            System.out.println("Sender and receiver cannot be the same account.");
            return;
        }

        double amount = readAmount("Enter amount to transfer: ");
        if (from.withdraw(amount)) {
            to.deposit(amount);
            System.out.println("Rs. " + String.format("%.2f", amount) + " transferred from "
                    + from.getAccountNumber() + " to " + to.getAccountNumber() + ".");
        } else {
            System.out.println("Transfer failed. Insufficient balance in sender account.");
        }
    }

    private static void checkBalance() {
        System.out.println("\n--- Check Balance ---");
        Account acc = askForAccount("Enter account number: ");
        if (acc != null) {
            System.out.println("Current balance: Rs. " + String.format("%.2f", acc.getBalance()));
        }
    }

    private static void viewAccountDetails() {
        System.out.println("\n--- Account Details ---");
        Account acc = askForAccount("Enter account number: ");
        if (acc != null) {
            acc.showDetails();
        }
    }

    private static void viewAllAccounts() {
        System.out.println("\n--- All Accounts ---");
        if (accounts.isEmpty()) {
            System.out.println("No accounts have been created yet.");
            return;
        }
        for (Account acc : accounts) {
            acc.showDetails();
        }
        System.out.println("Total accounts: " + accounts.size());
    }
}
