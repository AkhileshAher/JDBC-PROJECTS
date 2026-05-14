import java.io.*;
import java.sql.*;

public class ImageHandlingRead {
    static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/image_db";
        String username = "root";
        String password = "abcd1234";

        String folderPath = "<--FOLDER PATH-->";
        String query = "SELECT image_data FROM image_table WHERE image_id = (?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers Loaded Successfully");
        } catch(ClassNotFoundException e) {
            System.out.println(e);
        }

        try {

            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println("Connection Established Successsfully");

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                byte [] imageData = resultSet.getBytes("image_Data");
                String imgPath = folderPath + "extractedImage.jpg";
                OutputStream outputStream = new FileOutputStream(imgPath);
                outputStream.write(imageData);
                System.out.println("Image Read Success");
            } else {
                System.out.println("Image not FOUND !!!");
            }

        } catch(SQLException e) {
            System.out.println(e);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
