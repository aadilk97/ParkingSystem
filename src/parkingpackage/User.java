package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
	String univid;
	String password;
	String type;
	Connection conn;
	
	
	User(String univid, String password, String type, Connection conn){
		this.univid = univid;
		this.password = password;
		this.type = type;
		this.conn = conn;
		
		createUser();
	}
	
	User(String univid, String password, String type){
		this.univid = univid;
		this.password = password;
		this.type = type;
	}
	
	User(Connection conn){
		this.conn = conn;
	}
	
	public void createUser() {
		Scanner sc = new Scanner(System.in);
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement("INSERT INTO Users "
					+ "(Univid, Password, Type)	"
					+ "VALUES (?, ?, ?)"
					);
			
			stmt.setString(1, univid);
			stmt.setString(2, password);
			stmt.setString(3, type);
			
			stmt.executeUpdate();
			System.out.println("User created successfully");
			
			if(!type.equalsIgnoreCase("Admin")) {
				System.out.println("Enter the license number");
				String licenseNumber = sc.next();
				System.out.println("Enter the manufacturer");
				String manufacturer = sc.next();
				System.out.println("Enter the car's model");
				String model = sc.next();
				System.out.println("Enter the car's color");
				String color = sc.next();
				System.out.println("Enter the year");
				int year = sc.nextInt();
				
				Vehicle vehicle = new Vehicle(licenseNumber, manufacturer, model, color, year, conn);
				vehicle.addVehicle();
				System.out.println("Vehicle added sucessfully");
			}
			
		} catch(SQLException e) {
			System.out.println("Failed to create user or add vehicle " + e.getMessage());
		}
		
	}
	
	public boolean checkLogin(String univid, String password, String type) {
		boolean status = false;
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement("SELECT Univid, Password, Type from Users "
					+ "WHERE Univid = ? AND Password = ?");
					
			stmt.setString(1, univid);
			stmt.setString(2, password);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String expectedPassword = rs.getString("Password");
				String expectedType = rs.getString("Type");
				if(password.equals(expectedPassword) && type.equals(expectedType)){
					status = true;
				}
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
}
