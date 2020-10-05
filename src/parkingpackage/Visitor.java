package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Visitor {
	Connection conn;
	String phoneNumber;
	
	Visitor(String phoneNumber, Connection conn){
		this.phoneNumber = phoneNumber;
		this.conn = conn;
	}
	
	
	public void addVisitor() {
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement("INSERT INTO Visitor "
										+ "(phone_number) "
										+ "VALUES (?)");
			
			stmt.setString(1, phoneNumber);
			stmt.executeQuery();
			System.out.println("Visitor added successfully");
			
		} catch(SQLException e) {
			System.out.println("Failed to add phone number to vistor " + e.getMessage());
		}
	}
	
}
