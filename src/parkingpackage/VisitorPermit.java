package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class VisitorPermit extends Permit{
	String lotname;
	int spaceNum;
	Connection conn;
	
	VisitorPermit(String licenseNumber, String startDate, String expirationDate, String expirationTime,String spaceType, String lotname, int spaceNum, String zone, Connection conn ) {
		super(licenseNumber, startDate, expirationDate, expirationTime, spaceType, zone, conn);
		this.lotname=lotname;
		this.spaceNum=spaceNum;
		this.conn=conn;
	}
	
	
	void getVisitorPermit() {
		String permit_id=super.getRandomString();
		PreparedStatement stmt;
		try {
			stmt=this.conn.prepareStatement("INSERT INTO VisitorPermits "
					+ "(PermitId, LicenseNumber, StartDate,ExpirationDate,ExpirationTime,SpaceType,LotName,SpaceNumber) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, permit_id);
			stmt.setString(2, super.licenseNumber);
			stmt.setString(3, super.startDate);
			stmt.setString(4, super.expirationDate);
			stmt.setString(5, super.expirationTime);
			stmt.setString(6, super.spaceType);
			stmt.setString(7, lotname);
			stmt.setInt(8, spaceNum);
			stmt.executeUpdate();
			
			System.out.println("Visitor Permit Granted Successfully");
		}
		catch(SQLException e) {
			System.out.println("Visitor Permit couldn't be added" + e.getMessage());
		}
	}
}
