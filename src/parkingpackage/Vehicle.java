package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Vehicle {
	String licenseNumber;
	String manufacturer;
	String model;
	String color;
	int year;
	Connection conn;
	
	Vehicle(String licenseNumber, String manufacturer, String model,String color,int year, Connection conn){
		this.licenseNumber=licenseNumber;
		this.manufacturer=manufacturer;
		this.model=model;
		this.color=color;
		this.year=year;
		this.conn = conn;
	}

	public void addVehicle() {
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement("INSERT INTO Vehicle "
										+ "(LicenseNumber,Manufacturer,Model,Color,Year) "
										+ "VALUES (?,?,?,?,?)"
										);
			
			stmt.setString(1, licenseNumber);
			stmt.setString(2, manufacturer);
			stmt.setString(3, model);
			stmt.setString(4, color);
			stmt.setInt(5, year);
			
			stmt.executeUpdate();
			
		} catch(SQLException e) {
			System.out.println("Failed to add Vehicle " + e.getMessage());
		}
	}
	
}