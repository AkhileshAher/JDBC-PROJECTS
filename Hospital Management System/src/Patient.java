import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection,Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {
        System.out.print("Enter Patient Name : ");
        String name = scanner.next();
        scanner.nextLine();
        System.out.print("Enter Patient Age : ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Patient Gender : ");
        String gender = scanner.next();;

        try {
            String query = "INSERT INTO patients(name,age,gender) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0) {
                System.out.println("Patient Added Successfully");
            } else {
                System.out.println("Failed to Add Patient");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatients() {
       try {
           String query = "SELECT * FROM patients";
           PreparedStatement preparedStatement = connection.prepareStatement(query);
           ResultSet resultSet = preparedStatement.executeQuery();
           while(resultSet.next()) {
               int id =  resultSet.getInt("id");
               String patientName = resultSet.getString("name");
               int patientAge = resultSet.getInt("age");
               String patientGender = resultSet.getString("gender");
               System.out.println("+-----------------------------------------+");
               System.out.println("ID : " +id);
               System.out.println("Patient Name : " + patientName);
               System.out.println("Age : " + patientAge);
               System.out.println("Gender : "+ patientGender);
               System.out.println("+-----------------------------------------+");
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
    }

    public boolean getPatientById(int id) {
        String query = "SELECT name FROM patients WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() == true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
