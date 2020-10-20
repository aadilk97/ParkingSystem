package parkingpackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
	static final String jdbcURL = "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:orcl01";
	private String userName = "akhan23";
	private String password = "abcd1234";
	
	public Connection createConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(jdbcURL, userName, password);
		} catch(SQLException e) {
			System.out.println("Failed to create connection " + e.getMessage());
		}
		
		return conn;
	}
	
	public boolean testConnection(Connection conn){
		Statement stmt = null;
		
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM Test";
			ResultSet result = stmt.executeQuery(sql);
			int testId = -1;
			String testString = "";
			
			
			while(result.next()) {
				testId = result.getInt("testId");
				testString = result.getString("testString");	
				break;
			}
			
			if(testId == 1 && testString.equals("Proteas")) {
				return true;
			}
			return false;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
}