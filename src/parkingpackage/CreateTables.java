package parkingpackage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTables {
	
	public static void createLotsTable(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("CREATE TABLE Lots( "
					+ "Name varchar(255) NOT NULL PRIMARY KEY,"
					+ "Address varchar(255), "
					+ "Designation varchar(255),"
					+ "StartSpaceNum integer, "
					+ "NumSpaces integer)"
					);
			
		} catch(SQLException e) {
			System.out.println("Failed to create table " + e.getMessage());
		}
	}
	
	public static void main(String args[]) {
		DatabaseConnection dbConnection = new DatabaseConnection();
		Connection conn = dbConnection.createConnection();
		
//		createLotsTable(conn);
	
	}
	
}
