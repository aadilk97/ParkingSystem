package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Vehicle {
	String licenseplate;
	String manufacturer;
	String model;
	String color;
	int year;
	Connection conn;
	
	Vehicle(String licenseplate, String manufacturer, String model,String color,int year,Connection conn){
		this.conn=conn;
		this.licenseplate=licenseplate;
		this.manufacturer=manufacturer;
		this.model=model;
		this.color=color;
		this.year=year;
	}
	
	
	
}
