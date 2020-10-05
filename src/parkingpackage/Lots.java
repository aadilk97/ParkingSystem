package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
			
			stmt.executeQuery();
			System.out.println("Added lot successfully");
			
		} catch(SQLException e) {
			System.out.println("Failed to add Lot " + e.getMessage());
		}
	}
}
