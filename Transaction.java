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

    public static void fileWriter(List<Checking> accounts) {
        //Write transactions to file
        try {
            File transactionReport = new File("transactionReport.txt");
            if (transactionReport.createNewFile()) {
                System.out.println("File created: " + transactionReport.getName());
            } else {
                System.out.println("Overwriting previous file: transactionReport.txt");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

            try {
                FileWriter myWriter = new FileWriter("transactionReport.txt");
                for(int i = 0; i < accounts.size(); i++) {
                    myWriter.write(accounts.get(i).getFirstName() + "\n");
                }
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
    }

    public static void main(String[] args) {
        List<Checking> accounts= fileReader();

        checkTransactionTypeInput();

//        fileWriter(accounts);

    }
}
