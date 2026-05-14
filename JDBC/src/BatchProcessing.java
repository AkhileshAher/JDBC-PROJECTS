import java.sql.*;
import java.util.Scanner;

public class BatchProcessing {
    static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/jdbc";
        String username = "root";
        String password = "abcd1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully");
        } catch(ClassNotFoundException e) {
            System.out.println(e);
        }

        try {
            Connection conn = DriverManager.getConnection(url,username,password);
            System.out.println("Connection Established Successfully !!");
            conn.setAutoCommit(false);

            // Using Statement Interface
//            Statement statement = conn.createStatement();
//            statement.addBatch("INSERT INTO employee(ename,role,salary) VALUES ('Karan','HR',56000)");
//            statement.addBatch("INSERT INTO employee(ename,role,salary) VALUES ('Ashu','Civil Engg',96000)");
//            statement.addBatch("INSERT INTO employee(ename,role,salary) VALUES ('Abhay','AI Engg',46000)");
//
//            int [] batchResult = statement.executeBatch();
//            conn.commit();
//            System.out.println("BATCH EXECUTED SUCCESSFULLY !!");

            String query = "INSERT INTO employee (ename,role,salary) VALUES (?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            Scanner scanner = new Scanner(System.in);

            while(true) {
                System.out.println("Enter Name : ");
                String empName = scanner.nextLine();
                System.out.println("Enter Job Role : ");
                String jobTitle = scanner.nextLine();
                System.out.println("Salary : ");
                int salary = scanner.nextInt();
                scanner.nextLine();

                preparedStatement.setString(1,empName);
                preparedStatement.setString(2,jobTitle);
                preparedStatement.setInt(3,salary);
                preparedStatement.addBatch();
                System.out.println("Add more Values Y/N : ");
                String decision = scanner.nextLine();
                if(decision.toUpperCase().equals("N")) {
                    break;
                }

            }

            int [] batchResult = preparedStatement.executeBatch();
            conn.commit();
            System.out.println("BATCH EXECUTED SUCCESSFULLY !! ");


        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
