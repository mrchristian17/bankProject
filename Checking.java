public class Checking {
    private String firstName;
    private String lastName;
    private int accountNumber;
    private boolean checking;
    private boolean savings;
    private double balance;
    private double interest;

    //Constructors
    public Checking(String firstName, String lastName, int accountNumber, double balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Checking(String firstName, String lastName, int accountNumber, boolean checking, boolean savings, double balance, double interest) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountNumber = accountNumber;
        this.checking = checking;
        this.savings = savings;
        this.balance = balance;
        this.interest = interest;
    }

    public String withdraw(double withdrawAmount) {
        double currBalance = getBalance();
        if (currBalance < withdrawAmount) {
            return getFullName() +" failed to withdraw: " + withdrawAmount + " from checking account.  Balance: " +
                    currBalance + " - insufficient funds.";
        }
        double updatedBalance = currBalance - withdrawAmount;
        setBalance(updatedBalance);
        return getFullName() +" withdrew: " + withdrawAmount + " from checking account.";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName(){
        return getFirstName() + " " + getLastName();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public boolean isChecking() {
        return checking;
    }

    public void setChecking(boolean checking) {
        this.checking = checking;
    }

    public boolean isSavings() {
        return savings;
    }

    public void setSavings(boolean savings) {
        this.savings = savings;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

}
