//1. Load and register Driver
//2. Create Connection
//3. Create Statement
//4. Execute the Query
//5. Store and Process Result
//6. Close the Connection

import java.sql.*;

public class DBConnection {
	public static void main(String [] args) {
		
		String url = "jdbc:mysql://localhost:3306/jdbc";
		String username = "root";
		String password = "abcd1234";

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
			ResultSet rs = stmt.executeQuery("SELECT * FROM <tablename>");

			rs.close();
			stmt.close();
			conn.close();
			System.out.println("Connection Closed");
		} catch (SQLException e) {
			System.out.println("Connection Failed " + e.getMessage());
		}
		
		
	}
}
