import java.sql.*;

public class ReadJDBC {

        public static void main(String [] args) {

            String url = "jdbc:mysql://localhost:3306/jdbc";
            String username = "root";
            String password = "abcd1234";
            String sql = "SELECT * FROM employee;";

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
                ResultSet rs = stmt.executeQuery(sql);

                while(rs.next()) {
                    int id = rs.getInt(1);
                    String empName = rs.getString(2);
                    String role = rs.getString(3);
                    int salary = rs.getInt("salary");
                    System.out.println("ID : "+id);
                    System.out.println("Name : "+empName);
                    System.out.println("Role : "+role);
                    System.out.println("Salary : "+salary);
                    System.out.println("===============");
                }

                rs.close();
                stmt.close();
                conn.close();
                System.out.println("Connection Closed Success");

            } catch (SQLException e) {
                System.out.println("Connection Failed " + e.getMessage());
            }


        }
    }


