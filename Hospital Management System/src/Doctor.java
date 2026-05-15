import java.sql.*;

public class Doctor {

    private Connection connection;


    public Doctor(Connection connection) {
        this.connection = connection;
    }

    public void viewDoctors() {
        String query = "SELECT * FROM doctors;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                System.out.println("---------------------------------------");
                System.out.println("Doctor ID : " + resultSet.getInt("id"));
                System.out.println("Doctor Name : " + resultSet.getString("name"));
                System.out.println("Doctor Specialization : " + resultSet.getString("specialization"));
                System.out.println("---------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id) {
        String query = "SELECT name FROM doctors WHERE id = ?";
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
