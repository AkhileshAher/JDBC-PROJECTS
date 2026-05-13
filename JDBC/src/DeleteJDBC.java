import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteJDBC {
    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver load Failed" + e.getMessage());
        }

        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc","root","abcd1234");
            Statement stmt = con.createStatement();
            int row = stmt.executeUpdate("DELETE FROM employee WHERE id=4");

            if(row > 0) {
                System.out.println("Deleted SuccessFully");
            } else {
                System.out.println("Deletion Failed");
            }

            stmt.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception Occurred" + e.getMessage());
        }

    }
}
