import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateJDBC {
    public static void main(String [] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class Loading Error " + e.getMessage());
        }

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc","root","abcd1234");
            Statement stmt = con.createStatement();
            int rowsAffected = stmt.executeUpdate("UPDATE employee SET ename='Anurag' WHERE id=1");
            if(rowsAffected > 0) {
                System.out.println("UPDATION SUCCESS");
            } else {
                System.out.println("UPDATION FAILED");
            }

            stmt.close();
            con.close();
        } catch(SQLException e) {
            System.out.println("SQL Exception Occurred " + e.getMessage());
        }

    }
}
