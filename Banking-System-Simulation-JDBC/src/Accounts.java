import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection,Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public long open_account(String email) {
        if(!account_exist(email)) {
            String accOpenQuery = "INSERT INTO accounts(account_number,full_name,email,balance,security_pin) VALUES (?,?,?,?,?);";
            scanner.nextLine();
            System.out.print("Enter Full Name : ");
            String full_name = scanner.nextLine();
            System.out.print("Enter Initial Amount : ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter Security Pin : ");
            String securityPin = scanner.nextLine();
            try {
                long accNum = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(accOpenQuery);
                preparedStatement.setLong(1,accNum);
                preparedStatement.setString(2,full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,securityPin);
                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected > 0) {
                    return accNum;
                } else {
                    throw new RuntimeException("Account Creation Failed");
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        throw new RuntimeException("Account Already Exist");
    }

    public long getAccount_number(String email) {
        String query = "SELECT account_number FROM accounts WHERE email = ?;";
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw  new RuntimeException("Account Number Doesn't Exist!");
    }

    private long generateAccountNumber() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1");
            if(resultSet.next()) {
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number+1;
            } else {
                return 1000100;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        throw new RuntimeException("Cannot Generate Account Number !!");
    }



    public boolean account_exist(String email) {
        String query = "SELECT account_number FROM accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }
}
