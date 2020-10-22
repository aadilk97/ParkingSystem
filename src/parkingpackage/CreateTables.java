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
	
	public static void createVehicleTable(Connection conn) {
		try {
			Statement stmt=conn.createStatement();
			stmt.execute("CREATE TABLE Vehicle("
					+ "LicenseNumber varchar(10) NOT NULL,"
					+ "Manufacturer varchar(20),"
					+ "Model varchar(20),"
					+ "Color varchar(20),"
					+ "Year varchar(4),"
					+ "CONSTRAINT pk_vehicle PRIMARY KEY (LicenseNumber))"
				);
			System.out.println("Vehicle Table created successfully");
		}
		catch(SQLException e) {
			System.out.println("Failed to create table" +e.getMessage());
		}
	}
	
	public static void createVisitorTable(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("CREATE TABLE Visitor( "
					+ "PhoneNumber varchar(10) NOT NULL,"
					+ "LicenseNumber varchar(10) NOT NULL, "
					+ "CONSTRAINT pk_visitor PRIMARY KEY(PhoneNumber))"
				);
			System.out.println("Visitor table created successfully");
		} catch(SQLException e) {
			System.out.println("Failed to create visitor table " + e.getMessage());
		}
	}
	
	public static void createNonVisitorPermitsTable(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("CREATE TABLE NonvisitorPermits( "
					+ "PermitId varchar(8) NOT NULL, "
					+ "Univid varchar(10) NOT NULL, "
					+ "LicenseNumber varchar(10) NOT NULL, "
					+ "StartDate varchar(15), "
					+ "ExpirationDate varchar(15), "
					+ "ExpirationTime varchar(15), "
					+ "SpaceType varchar(20), "
					+ "Zone varchar(2), "
					+ "Manufacturer varchar(20), "
					+ "Model varchar(20), "
					+ "Color varchar(20), "
					+ "Year varchar(4), "
					+ "FOREIGN KEY (Univid) REFERENCES Users(Univid), "
					+ "FOREIGN KEY (LicenseNumber) REFERENCES Vehicle(LicenseNumber), "
					+ "PRIMARY KEY (PermitId, LicenseNumber))" 
				);
			System.out.println("NonVisitor Permits table created successfully");
		} catch(SQLException e) {
			System.out.println("Failed to create non visitor permits table " + e.getMessage());
		}
	}
	
	public static void createVisitorPermitsTable(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("CREATE TABLE VisitorPermits( "
					+ "PermitId varchar(8), "
					+ "PhoneNumber varchar(10) NOT NULL, "
					+ "LicenseNumber varchar(10) NOT NULL, "
					+ "StartDate varchar(15), "
					+ "ExpirationDate varchar(15), "
					+ "ExpirationTime varchar(15), "
					+ "SpaceType varchar(20), "
					+ "LotName varchar(20), "
					+ "SpaceNumber varchar(20), "
					+ "Zone varchar(2), "
					+ "FOREIGN KEY (LicenseNumber) REFERENCES Vehicle(LicenseNumber), "
					+ "FOREIGN KEY (PhoneNumber) REFERENCES Visitor(PhoneNumber), "
					+ "PRIMARY KEY (PermitId))" 
				);
			System.out.println("Visitor Permits table created successfully");
		} catch(SQLException e) {
			System.out.println("Failed to create visitor permits table " + e.getMessage());
		}
	}

	public static void createCitationTable(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("CREATE TABLE Citation( "
					+ "CitationNumber varchar(8), "
					+ "LicenseNumber varchar(10), "
					+ "Model varchar(20), "
					+ "Color varchar(20), "
					+ "StartDate varchar(15), "
					+ "LotName varchar(20), "
					+ "CitationTime varchar(15), "
					+ "ViolationCategory varchar(20), "
					+ "Fee integer, "
					+ "DueDate varchar(20), "
					+ "PaidStatus varchar(10), "
					+ "PRIMARY KEY (CitationNumber))"
			);
			System.out.println("Citations table created successfully");
		} catch(SQLException e) {
			System.out.println("Failed to create citations table " + e.getMessage());
		}

	}

	public static void createNotificationTable(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("CREATE TABLE Notification( "
					+ "UserID varchar(10) NOT NULL, "
					+ "CitationNumber varchar(8) NOT NULL, "
					+ "LicenseNumber varchar(10), "
					+ "ViolationCategory varchar(20), "
					+ "CitationDate varchar(15), "
					+ "Fee integer , "
					+ "DueDate varchar(20), "
					+ "FOREIGN KEY (CitationNumber) REFERENCES Citation(CitationNumber), "
					+ "PRIMARY KEY (UserID))"
			);
			System.out.println("Notifications table created successfully");
		} catch (SQLException e) {
			System.out.println("Failed to create notifications table " + e.getMessage());
		}
	}
  
	public static void main(String args[]) {
		DatabaseConnection dbConnection = new DatabaseConnection();
		Connection conn = dbConnection.createConnection();
		
//		createVehicleTable(conn);
//		createNonVisitorPermitsTable(conn);
//		createVisitorPermitsTable(conn);
//		createLotsTable(conn);
//		createUserTable(conn);
//		createSpacesTable(conn);
//		createVehicleTable(conn);
		createVisitorTable(conn);
//		createNonVisitorPermitsTable(conn);
		createVisitorPermitsTable(conn);
//		createCitationTable(conn);
//		createNotificationTable(conn);
	}
	
}
