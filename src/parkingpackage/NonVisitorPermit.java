package parkingpackage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NonVisitorPermit extends Permit {
	String univid;
	Connection conn;
	NonVisitorPermit(String licenseNumber, String startDate, String expirationDate, String expirationTime, String spaceType, String zone, String univid, Connection conn){
		super(licenseNumber, startDate, expirationDate, expirationTime, spaceType, zone, conn);
		this.univid = univid;
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

}
