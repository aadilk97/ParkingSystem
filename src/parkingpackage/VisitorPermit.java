package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
	
	public VisitorPermit(Connection conn) {
		// TODO Auto-generated constructor stub
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
	
	void ExitLot(String permit_id) {
		PreparedStatement stmt;
		System.out.println(permit_id);
		String lotname="";
		int spaceNumber=0;
		Timestamp expTime = null;
		String expTimeStr = "";
		String expDate = "";
		try {
			stmt=this.conn.prepareStatement("SELECT * from VisitorPermits "
					+ "WHERE PermitId=?");
			stmt.setString(1, permit_id);
			
			ResultSet result=stmt.executeQuery();
			
			//get lotname and spaceNumber to update
			while(result.next()) {
				lotname=result.getString("LotName");
				spaceNumber=result.getInt("SpaceNumber");
				expDate = result.getString("ExpirationDate");
				expTimeStr=result.getString("ExpirationTime");
				
			}
			expTime = Timestamp.valueOf(expDate + " " + expTimeStr);
			Space space=new Space(lotname,spaceNumber,conn);
			space.updateAvailable("Yes");
			Timestamp curtime = new Timestamp(new java.util.Date().getTime());
			
			//calculate extra charges if any
			
			if(expTime.getTime()-curtime.getTime()<0) {
				System.out.println("Your permit has expired and you have been charged $25");
			}
			
			PreparedStatement stmt1;
			try {
				stmt1=this.conn.prepareStatement("UPDATE VisitorPermits "
						+"SET LotName=?,SpaceNumber=? "
						+"WHERE PermitId=?");
				stmt1.setString(1, "Unassigned");
				stmt1.setInt(2, -1);
				stmt1.setString(3, permit_id);
				stmt1.executeUpdate();
				System.out.println("Visitor has left the System");
			}
			catch(SQLException e) {
				System.out.println("Failed to update space availability, permit is invalid try again "+e.getMessage());
			}

		}
		catch(SQLException e) {
			System.out.println("Error updating query " + e);
		}
	}
}
