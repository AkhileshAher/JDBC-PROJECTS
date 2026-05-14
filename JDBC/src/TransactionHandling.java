import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionHandling {
    static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/BankingDB";
        String username = "root";
        String password = "abcd1234";

        String withdrawQuery = "UPDATE accounts SET balance = balance - ? WHERE acc_no = ?";
        String depositQuery = "UPDATE accounts SET balance = balance + ? WHERE acc_no = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully");
        } catch(ClassNotFoundException e) {
            System.out.println(e);
        }

        try {
            Connection conn = DriverManager.getConnection(url,username,password);
            System.out.println("Connection Established Successfully");
            conn.setAutoCommit(false);

            try {
                PreparedStatement withdrawStatement = conn.prepareStatement(withdrawQuery);
                PreparedStatement depositStatement = conn.prepareStatement(depositQuery);
                withdrawStatement.setDouble(1, 500);
                withdrawStatement.setString(2, "xyz123");
                depositStatement.setDouble(1, 500);
                depositStatement.setString(2, "pqr1234");
                int rowsAffectedWithdrawl = withdrawStatement.executeUpdate();
                int rowsAffectedDeposit = depositStatement.executeUpdate();

                if(rowsAffectedWithdrawl > 0 && rowsAffectedDeposit > 0) {
                    conn.commit();
                    System.out.println("Transaction Successfull!");
                } else {
                    conn.rollback();
                    System.out.println("Transaction Failed");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch(SQLException e) {
            System.out.println(e);
        }

    }
}
