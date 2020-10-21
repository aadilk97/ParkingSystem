package parkingpackage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class NonVisitorPermit extends Permit {
	String univid;
	Connection conn;
	NonVisitorPermit(String licenseNumber, String startDate, String expirationDate, String expirationTime, String spaceType, String zone, String univid, Connection conn){
		super(licenseNumber, startDate, expirationDate, expirationTime, spaceType, zone, conn);
		this.univid = univid;
		this.conn = conn;
	}
	
	NonVisitorPermit(Connection conn) {
		this.conn = conn;
	}
	
	public ResultSet getVehicle(String licenseNumber) {
		PreparedStatement stmt;
		ResultSet rs = null;
		try {
			stmt = this.conn.prepareStatement("SELECT * FROM Vehicle "
					+ "WHERE LicenseNumber = ?"
					);
			
			stmt.setString(1, licenseNumber);
			rs = stmt.executeQuery();
			
		} catch(SQLException e) {
			System.out.println("Failed to get any vehicle with the given license number " + e.getMessage());
		}
		
		return rs;
	}
	
	void getNonVisitorPermit(String permit_id) {
		if (permit_id.equalsIgnoreCase("")) {
			permit_id = super.getRandomString();
		}
		String manufacturer, model, color, year;
		manufacturer = "";
		model = "";
		color = "";
		year = "";
		
		ResultSet vehicle = getVehicle(licenseNumber);
		try {
			while(vehicle.next()) {
				manufacturer = vehicle.getString("Manufacturer");
				model = vehicle.getString("Model");
				color = vehicle.getString("Color");
				year = vehicle.getString("Year");
				break;
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		PreparedStatement stmt;
		try {
			stmt=this.conn.prepareStatement("INSERT INTO NonvisitorPermits "
					+ "(PermitId,Univid,LicenseNumber,StartDate,ExpirationDate,ExpirationTime,"
					+ "SpaceType,Zone,Manufacturer,Model,Color,Year) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, permit_id);
			stmt.setString(2, this.univid);
			stmt.setString(3, super.licenseNumber);
			stmt.setString(4, super.startDate);
			stmt.setString(5, super.expirationDate);
			stmt.setString(6, super.expirationTime);
			stmt.setString(7, super.spaceType);
			stmt.setString(8, super.zone);
			stmt.setString(9, manufacturer);
			stmt.setString(10, model);
			stmt.setString(11, color);
			stmt.setString(12, year);
			
			stmt.executeUpdate();
			
			System.out.println("Non Visitor Permit Granted Successfully");
		}
		catch(SQLException e) {
			System.out.println("Non Visitor Permit couldn't be added" + e.getMessage());
		}
	}
	
	public boolean checkNonVisitorPermit(String permitId, String lotParked, String zoneParked, String spaceTypeParked, String time) {
		PreparedStatement stmt;
		String lot, zone, designation, spaceType;
		lot = zone = designation = spaceType = "";
		try {
			stmt = this.conn.prepareStatement("SELECT * FROM NonvisitorPermits "
					+ "WHERE PermitId = ?"
				);
			
			stmt.setString(1, permitId);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				zone = rs.getString("Zone");
				spaceType = rs.getString("SpaceType");
			}
			
			stmt = this.conn.prepareStatement("SELECT * FROM Lots "
					+ "WHERE Name = ?"
				);
			
			stmt.setString(1, lotParked);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				designation = rs.getString("Designation");
			}
			
			String zones[] = designation.split("/");
			
			// Parked zone not in designation of parked lot
			if(!Arrays.asList(zones).contains(zoneParked)) {
				return false;
			}
			
			// Permit associated with an employee
			if(zone.length() == 1 && !zone.equalsIgnoreCase("V")) {
				if((zone.equalsIgnoreCase(zoneParked) || zoneParked.length() == 2) 
						&& spaceType.equalsIgnoreCase(spaceTypeParked)) {
					return true;
				}
			}
			
			// Permit associated with a student
			if(zone.length() == 2) {
				String timeParts[] = time.split(":");
				int numericTime = Integer.parseInt(timeParts[0]);
				String meridian = timeParts[1];
				
				if(!spaceType.equalsIgnoreCase(spaceTypeParked)) {
					return false;
				}
				
				if(zoneParked.length() == 2 && zoneParked.equalsIgnoreCase(zone))
					return true;
				
				if(zoneParked.length() == 1 && numericTime > 5 && meridian.equalsIgnoreCase("PM")) {
					return true;
				}
				
				return false;
			}
			

		} catch(SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

}
