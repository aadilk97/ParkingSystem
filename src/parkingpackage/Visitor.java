package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Visitor {
	String phoneNumber;
	String licenseNumber;
	Connection conn;
	
	Visitor(String phoneNumber, String licenseNumber, Connection conn) {
		this.phoneNumber = phoneNumber;
		this.licenseNumber = licenseNumber;
		this.conn = conn;
	}
	
	public void addVisitor() {
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement("INSERT Into Visitor "
					+ "(PhoneNumber, LicenseNumber) "
					+ "VALUES (?, ?)");
			stmt.setString(1, this.phoneNumber);
			stmt.setString(2, this.licenseNumber);
			
			stmt.executeQuery();
			System.out.println("Visitor with license added successfully");
			
		} catch(SQLException e) {
			System.out.println("Failed to add visitor " + e.getMessage());
		}
	}
}
