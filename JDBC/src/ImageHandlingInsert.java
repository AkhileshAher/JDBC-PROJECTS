import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImageHandlingInsert {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/image_db";
        String username = "root";
        String password = "abcd1234";

        String image_path = "<--FILE PATH-->";
        String query = "INSERT INTO image_table(image_data) VALUES(?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers Loaded Successfully");
        } catch(ClassNotFoundException e) {
            System.out.println(e);
        }

        try {

            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println("Connection Established Successsfully");

            FileInputStream fileInputStream = new FileInputStream(image_path);
            byte [] imageData = new byte[fileInputStream.available()];
            fileInputStream.read(imageData);

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setBytes(1,imageData);
            int affectedRows = ps.executeUpdate();

            if(affectedRows > 0) {
                System.out.println("Image Inserted Successfully");
            } else {
                System.out.println("Image Not Inserted");
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
