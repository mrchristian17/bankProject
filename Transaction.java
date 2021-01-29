import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Transaction {

    private static Scanner in = new Scanner(System.in);

    enum TransactionType {
        BALANCE, PAY, DEPOSIT, WITHDRAW,
    }
    enum Yes_No {
        YES, NO,
    }

    public static String checkTransactionTypeInput() {
        boolean validInput = false;
        TransactionType transactionTypeEnum = null;

        while(!validInput) {
            System.out.println("What transaction type would you like to execute?");
            System.out.println("Options: balance, pay, deposit, or withdraw.");

            try{
                String inputTransaction = in.nextLine();
                transactionTypeEnum = TransactionType.valueOf(inputTransaction.toUpperCase());
            }
            catch(IllegalArgumentException e) {
                System.out.println("Input is not a valid transaction type: ");
                System.out.println();
                continue;
            }
            validInput = true;
        }
        return transactionTypeEnum.name();
    }

    public static String check_yes_no(String text_input) {
        Yes_No yes_no = null;
        boolean valid_input = false;

        while(!valid_input) {
            System.out.println(text_input);
            System.out.println("Yes or No?");
            try{
                String input = in.nextLine();
                yes_no = Yes_No.valueOf(input.toUpperCase());
            }
            catch(IllegalArgumentException e) {
                System.out.println("This is NOT a valid response.");
                continue;
            }
            valid_input = true;

        }
        return yes_no.name();
    }


    public static String getUserNameInput() {

        String inputName = in.nextLine();
        inputName.replaceAll("\\s","");
        return inputName;
    }

    public static Checking findUserAccount(List<Checking> accounts, String userFirstName, String userLastName) {
        Checking currAccount = null;
        for(int i = 0; i < accounts.size(); i++) {
            currAccount = accounts.get(i);
            String currFirstName = currAccount.getFirstName();
            String currLastName = currAccount.getLastName();
            if (currFirstName.equals(userFirstName) && currLastName.equals(userLastName)) {
                return currAccount;
            }
        }
        return null;
    }


    //Currently allocates: firstName, lastName, accountNumber, startingBalance
    public static List<Checking> fileReader() {
        List<Checking> accounts = new ArrayList<Checking>();
        try {
            File bankAccountsFile = new File("src/CS 3331 - Bank Users.csv");
            Scanner file_reader= new Scanner(bankAccountsFile);
            int count = 0;
            while (file_reader.hasNextLine()) {
                String current_line = file_reader.nextLine();
                String[] currentUserData = current_line.trim().split("\\s*,\\s*");
                if(count == 0) {
                    count++;
                    continue;
                }
                accounts.add(new Checking(
                        currentUserData[0],
                        currentUserData[1],
                        Integer.parseInt(currentUserData[2]),
                        Double.parseDouble(currentUserData[5])));
            }
            file_reader.close();
            System.out.println("User account information has been downloaded.");
        }
        catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        }
        return accounts;
    }


    public static void fileWriter(String currTransaction) {

        try {
            FileWriter myWriter = new FileWriter("transactionReport.txt", true);
            myWriter.write(currTransaction+ "\n");

            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void createFile() {
        //Creates transactions file
        try {
            File transactionReport = new File("transactionReport.txt");
            if (transactionReport.createNewFile()) {
                System.out.println("File created: " + transactionReport.getName());
            } else {
                System.out.println("Overwriting previous file: transactionReport.txt");
                FileWriter myWriter = new FileWriter("transactionReport.txt", false);
                myWriter.write("");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String deposit(Checking currUser, double depositAmount) {
        double currBalance = currUser.getBalance();
        double updatedBalance = currBalance + depositAmount;
        currUser.setBalance(updatedBalance);
        return "Deposit Successful - " + depositAmount + " deposited into " +
                currUser.getFirstName() + " " + currUser.getLastName() +
                "'s account." + currUser.getBalance() + " in checking account.";
    }

    public static String pay(Checking currUser, Checking userToPay, double paymentAmount) {
        double currUserbalance = currUser.getBalance();
        double userToPaybalance = userToPay.getBalance();
        if (currUserbalance < paymentAmount) {
            return "Payment Failed - Insufficient Funds.  Amount requested: " + paymentAmount + " Amount in account: " + currUserbalance;
        }
        double currUserUpdatedBalance = currUserbalance - paymentAmount;
        double userToPayUpdatedBalance = userToPaybalance + paymentAmount;
        currUser.setBalance(currUserUpdatedBalance);
        userToPay.setBalance(userToPayUpdatedBalance);
        return "Payment Successful - " + paymentAmount + " withdrawn from " +
                currUser.getFirstName() + " " + currUser.getLastName() +
                "'s account." + currUser.getBalance() + " remaining in checking account.\n" +
                paymentAmount + " deposited into " +
                userToPay.getFirstName() + " " + userToPay.getLastName() +
                "'s account." + userToPay.getBalance() + " remaining in checking account. ";
    }

    public static String executeTransaction(List<Checking> accounts, Checking currUser, String transType) {
        String transactionDescription = "";
        if(transType.equals("WITHDRAW")) {
            System.out.println("How much money would you like to withdraw?");
            double withdrawAmount = Double.parseDouble(in.nextLine());
            transactionDescription = currUser.withdraw(withdrawAmount);
        }
        else if(transType.equals("DEPOSIT")) {
            System.out.println("How much money would you like to deposit");
            double depositAmount = Double.parseDouble(in.nextLine());
            transactionDescription = deposit(currUser, depositAmount);
        }
        else if(transType.equals("PAY")) {
            System.out.println("Who would you like to pay?");
            Checking userToPay = findAuthorizedUser(accounts);
            System.out.println("How much money would you like to pay "
                    + userToPay.getFirstName() + " " + userToPay.getLastName());
            double paymentAmount = Double.parseDouble(in.nextLine());
            transactionDescription = pay(currUser, userToPay, paymentAmount);
        }
        return transactionDescription;
    }

    public static Checking findAuthorizedUser(List<Checking> accounts) {
        Checking currUser = null;
        while (currUser == null) {
            //Gets user first and last name
            System.out.println("Please input the following information:");
            System.out.println("First name?");
            String currUserFirstName = getUserNameInput();
            System.out.println("Last name?");
            String currUserLastName = getUserNameInput();

            //Based on input, checks if the user exists then returns the appropriate account
            currUser = findUserAccount(accounts, currUserFirstName, currUserLastName);
            if (currUser == null){
                System.out.println("User does not exist");
            }
        }
        return currUser;
    }



    public static void main(String[] args) {
        List<Checking> accounts= fileReader();
        Checking currUser = findAuthorizedUser(accounts);
        createFile();
        boolean resume = true;
        while(resume) {
            fileWriter(executeTransaction(accounts, currUser, checkTransactionTypeInput()));
            String resumeInput = check_yes_no("Would you like to make another transaction?");
            if(resumeInput.equals("NO"))
                resume = false;
        }
        System.out.println("Active session closed....  Thank you, come again.");
    }
}
