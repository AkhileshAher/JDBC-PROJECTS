import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class PreparedStmt {
    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver Load Failed " + e.getMessage());
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc","root","abcd1234");
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employee(ename,role,salary) VALUES (?,?,?)");
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter EMP NAME , ROLE and Salary : ");
            String ename = sc.nextLine();
            String role = sc.nextLine();
            int salary = sc.nextInt();

            pstmt.setString(1,ename);
            pstmt.setString(2,role);
            pstmt.setInt(3,salary);
            int rows = pstmt.executeUpdate();

            if(rows > 0) {
                System.out.println("INSERTION SUCCESS");
            } else {
                System.out.println("Failed to Insert");
            }

            sc.close();
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception Error Occurred");
        }

    }
}
