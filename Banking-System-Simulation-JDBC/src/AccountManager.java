import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;

    public AccountManager(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    };


    public void credit_money(long account_number,String email) {
        scanner.nextLine();
        System.out.println("Enter Amount : ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin : ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if(account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE security_pin = ? AND email= ?;");
                preparedStatement.setString(1,security_pin);
                preparedStatement.setString(2,email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1,amount);
                    preparedStatement1.setLong(2,account_number);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if(rowsAffected > 0) {
                        System.out.println("Rs. " + amount + " credited Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed !");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Invalid Security PIN");
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void debit_money(long account_number,String email) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Amount : ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin :");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if(account_number!=0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE security_pin = ? AND email = ?;");
                preparedStatement.setString(1,security_pin);
                preparedStatement.setString(2,email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if(amount<=current_balance) {
                        String credit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?;";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if(rowsAffected > 0) {
                            System.out.println("Rs. "+amount+" debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                        } else {
                            System.out.println("Transaction Failed ");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else  {
                        System.out.println("Insufficient Funds");
                    }
                } else {
                    System.out.println("Invalid Security Pin");
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_acc_no) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number :");
        long reciever_acc_no = scanner.nextLong();
        System.out.print("Enter Amount : ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin : ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if(sender_acc_no!=0 && reciever_acc_no!=0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1,sender_acc_no);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()) {
                    double curr_bal = resultSet.getDouble("balance");
                    if(amount<=curr_bal) {
                        String deb_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        String cred_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(cred_query);
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(deb_query);
                        creditPreparedStatement.setDouble(1,amount);
                        creditPreparedStatement.setLong(2,reciever_acc_no);
                        debitPreparedStatement.setDouble(1,amount);
                        debitPreparedStatement.setLong(2,sender_acc_no);
                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
                        if(rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("Transaction Successfull!");
                            System.out.println("Rs. " + amount + " Transferred Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }

                    } else {
                        System.out.println("Insufficient Balance");
                    }
                } else {
                    System.out.println("Invalid Security Pin");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void getBalance(long account_number) {
        scanner.nextLine();
        System.out.print("Enter Security Pin : ");
        String security_pin = scanner.nextLine();
        try {
            String bal_query = "SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(bal_query);
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                System.out.println("Account Balance is : " + resultSet.getDouble("balance"));
            } else {
                System.out.println("Invalid Security Pin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
