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
			
			System.out.println("Table created successfully");
		} catch(SQLException e) {
			System.out.println("Failed to create table " + e.getMessage());
		}
	}
	
	public static void createUserTable(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("CREATE TABLE Users( "
					+ "Univid varchar(10) NOT NULL PRIMARY KEY, "
					+ "Password varchar(20) NOT NULL, "
					+ "Type varchar(20) NOT NULL)"
				);
			
			System.out.println("Table created successfully");
		} catch(SQLException e) {
			System.out.println("Failed to create table " + e.getMessage());
		}
	}
	
	public static void createSpacesTable(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("CREATE TABLE Spaces( "
					+ "SpaceNumber integer, "
					+ "Zone varchar(2), "
					+ "Type varchar(20), "
					+ "Available varchar(10), "
					+ "Name varchar(255), "
					+ "FOREIGN KEY (Name) REFERENCES Lots(Name), "
					+ "PRIMARY KEY (Name, SpaceNumber))"
				);
			
			System.out.println("Table created successfully");
		} catch(SQLException e) {
			System.out.println("Failed to create table " + e.getMessage());
		}
	}
	
	public static void main(String args[]) {
		DatabaseConnection dbConnection = new DatabaseConnection();
		Connection conn = dbConnection.createConnection();
		
//		createLotsTable(conn);
//		createSpacesTable(conn);
	
	}
	
}
