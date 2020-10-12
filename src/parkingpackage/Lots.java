package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Lots {
	String name;
	String address;
	String designation;
	int startSpaceNumber;
	int numSpaces;
	Connection conn;
	
	Lots(String name, String address, String designation, int startSpaceNumber, int numSpaces, Connection conn) {
		this.name = name;
		this.address = address;
		this.designation = designation;
		this.startSpaceNumber = startSpaceNumber;
		this.numSpaces = numSpaces;
		this.conn = conn;
	}
	
	Lots(String name, Connection conn){
		this.name = name;
		this.conn = conn;
	}
	
	public void addLot() {
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement("INSERT INTO Lots "
					+ "(Name, Address, Designation, StartSpaceNum, NumSpaces) "
					+ "VALUES (?, ?, ?, ?, ?)"
					);
			stmt.setString(1, name);
			stmt.setString(2, address);
			stmt.setString(3, designation);
			stmt.setInt(4, startSpaceNumber);
			stmt.setInt(5, numSpaces);
			
			stmt.executeUpdate();
			
			for(int i=startSpaceNumber; i < startSpaceNumber + numSpaces; i++) {
				Space space = new Space(name, i, designation, conn);
				space.addSpace();
			}
			
			System.out.println("Added lot successfully");
			
		} catch(SQLException e) {
			System.out.println("Failed to add Lot " + e.getMessage());
		}
	}
	
	public void assignZoneToLot(String lot, String newZone) {
		PreparedStatement stmt;
		String newDesignation = "";
		try {
			stmt = this.conn.prepareStatement("SELECT Designation FROM Lots "
					+ "WHERE Name = ?"
					);
			
			stmt.setString(1, lot);
			ResultSet result = stmt.executeQuery();
			
			while(result.next()) {
				newDesignation = result.getString("Designation");
			}
			if(newDesignation.equalsIgnoreCase("")) {
				throw new SQLException("Given lot not found");
			}
			
			newDesignation += "/" + newZone;
			
			stmt = this.conn.prepareStatement("UPDATE Lots "
					+ "SET Designation = ? "
					+ "WHERE Name = ?"
					);
			
			stmt.setString(1, newDesignation);
			stmt.setString(2, lot);
			
			stmt.executeQuery();
			System.out.println("Zone added to lot successfully");
			
		} catch(SQLException e) {
			System.out.println("Failed to add zone to lot " + e.getMessage());
		}
	}
}
