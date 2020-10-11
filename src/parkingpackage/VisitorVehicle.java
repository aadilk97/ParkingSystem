package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VisitorVehicle extends Vehicle{
	Connection conn;
	String phoneNumber;
	
	VisitorVehicle(String phoneNumber,String licenseplate, String manufacturer, String model,String color,int year, Connection conn){
		super(licenseplate,manufacturer,model,color,year,conn);
		this.phoneNumber = phoneNumber;
		this.conn = conn;
	}

	public void addVisitor() {
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement("INSERT INTO VisitorVehicle "
										+ "(Phone_no,LicensePlate,Manufacturer,Model,Color,Year) "
										+ "VALUES (?,?,?,?,?,?)"
										);
			
			stmt.setString(1, phoneNumber);
			stmt.setString(2, licenseplate);
			stmt.setString(3, manufacturer);
			stmt.setString(4, model);
			stmt.setString(5, color);
			stmt.setInt(6, year);
			
			stmt.executeUpdate();
			System.out.println("VisitorVehicle entry added successfully");
			
		} catch(SQLException e) {
			System.out.println("Failed to add phone number to VisitorVehicle " + e.getMessage());
		}
	}
	
}