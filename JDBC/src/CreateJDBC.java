import java.sql.*;

public class CreateJDBC {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/jdbc";
        String username = "root";
        String password = "abcd1234";
        String sql = "INSERT INTO employee(ename,role,salary) VALUES('Niraj','Intern',9000)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Class Loaded Successfully");

        } catch (ClassNotFoundException e) {
            System.out.println("Failed to Load Class " + e.getMessage() );
        }

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Established Success");
            Statement stmt = conn.createStatement();
            int rowAffected = stmt.executeUpdate(sql);

            if(rowAffected > 0) {
                System.out.println("INSERTION SUCCESS");
            } else {
                System.out.println("INSERTION FAILED");
            }

            stmt.close();
            conn.close();
            System.out.println("Connection Closed Success");

        } catch (SQLException e) {
            System.out.println("Connection Failed " + e.getMessage());
        }


    }
}
