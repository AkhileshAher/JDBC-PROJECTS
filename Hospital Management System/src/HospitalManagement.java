import java.sql.*;
import java.util.Scanner;

public class HospitalManagement {

    private static final String url="jdbc:mysql://localhost:3306/HospitalDB";
    private static final String username="root";
    private static final String password = "abcd1234";

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully");
        } catch(ClassNotFoundException e) {
            System.out.println(e);
        }

        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println("Connection Established Successfully.");
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while(true) {
                System.out.println("**-- HOSPITAL MANAGEMENT SYSTEM --**");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your Choice : ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(patient,doctor,connection,scanner);
                        break;
                    case 5:
                        System.out.println("THANK FOR VISITING US !! TAKE CARE ...");
                        return;
                    default:
                        System.out.println("Enter Valid Choice !! ");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scanner) {
        System.out.print("Enter Patient ID : ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor ID : ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter Appointment Date (YYYY-MM-DD) : ");
        String appointmentDate = scanner.next();
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId,appointmentDate,connection)) {
                String appointmentQuery = "INSERT INTO appointments(patient_id,doctor_id,appointment_date) VALUES (?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected > 0) {
                        System.out.println("Appointment Booked");
                    } else {
                        System.out.println("Failed to Book Appointment");
                    }
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor Not Available on this Date");
            }
        } else {
            System.out.println("Either Doctor or Patient Doesn't Exist");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId,String appointmentDate,Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date= ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
