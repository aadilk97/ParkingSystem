package parkingpackage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class NonVisitorPermit extends Permit {
	String univid;
	Connection conn;
	NonVisitorPermit(String licenseNumber, String startDate, String startTime, String expirationDate, String expirationTime, String spaceType, String zone, String univid, Connection conn){
		super(licenseNumber, startDate, startTime, expirationDate, expirationTime, spaceType, zone, conn);
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
					+ "(PermitId,Univid,LicenseNumber,StartDate,startTime,ExpirationDate,ExpirationTime,"
					+ "SpaceType,Zone,Manufacturer,Model,Color,Year) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, permit_id);
			stmt.setString(2, this.univid);
			stmt.setString(3, super.licenseNumber);
			stmt.setString(4, super.startDate);
			stmt.setString(5, super.startTime);
			stmt.setString(6, super.expirationDate);
			stmt.setString(7, super.expirationTime);
			stmt.setString(8, super.spaceType);
			stmt.setString(9, super.zone);
			stmt.setString(10, manufacturer);
			stmt.setString(11, model);
			stmt.setString(12, color);
			stmt.setString(13, year);
			
			stmt.executeUpdate();
			
			System.out.println("Non Visitor Permit Granted Successfully");
		}
		catch(SQLException e) {
			System.out.println("Non Visitor Permit couldn't be added" + e.getMessage());
		}
	}
	
	public boolean checkNonVisitorPermit(String permitId, String lotParked, String zoneParked, String spaceNumParked, String time) {
		PreparedStatement stmt;
		String lot, zone, designation, spaceZone, spaceType, spaceTypeParked;
		lot = zone = designation = spaceZone = spaceType = spaceTypeParked = "";
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
			
			stmt = this.conn.prepareStatement("SELECT * FROM Spaces "
					+ "WHERE Name = ? AND SpaceNumber = ?"
				);
			
			stmt.setString(1, lotParked);
			stmt.setString(2, spaceNumParked);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				spaceZone = rs.getString("Zone");
				spaceTypeParked = rs.getString("Type");
			}
			String zones[] = designation.split("/");
			
			// Parked zone not in designation of parked lot
			if(!Arrays.asList(zones).contains(zoneParked) || !spaceZone.equalsIgnoreCase(zone)) {
				return false;
			}
			
			// Permit associated with an employee
			if(zone.length() == 1 && !zone.equalsIgnoreCase("V")) {
				if((zone.equalsIgnoreCase(zoneParked) || zoneParked.length() == 2) 
						&& spaceType.equalsIgnoreCase(spaceTypeParked)) {
					return true;
				}
				return false;
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
