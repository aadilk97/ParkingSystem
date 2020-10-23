package parkingpackage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;


public class VisitorPermit extends Permit{
	String lotname;
	int spaceNum;
	Connection conn;
	String phoneNumber;
	int duration;
	
	VisitorPermit(String phoneNumber,String licenseNumber, String startDate, String startTime, String expirationDate, String expirationTime,int duration,String spaceType, String lotname, int spaceNum, String zone, Connection conn ) {
		super(licenseNumber, startDate, startTime, expirationDate, expirationTime, spaceType, zone, conn);
		this.lotname=lotname;
		this.spaceNum=spaceNum;
		this.phoneNumber=phoneNumber;
		this.duration=duration;
		this.conn=conn;
	}
	
	public VisitorPermit(Connection conn) {
		// TODO Auto-generated constructor stub
		this.conn=conn;
	}
	public String getOgTimeStamp(String expDate, String expTime) {
		String input=expDate+" "+expTime;
		DateFormat ip = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
		DateFormat op = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss");
		java.util.Date date2 = null;
		try
		{
			date2 = ip.parse(input);
		}
		catch ( ParseException e )
		{
			e.printStackTrace();
		}

		String newDate = op.format(date2);
		return newDate;
	}
	
	void getVisitorPermit() {
		String permit_id=super.getRandomString();
		PreparedStatement stmt;
		try {
			stmt=this.conn.prepareStatement("INSERT INTO VisitorPermits "
					+ "(PermitId, PhoneNumber, LicenseNumber, StartDate,StartTime,ExpirationDate,ExpirationTime,Duration,SpaceType,LotName,SpaceNumber,Zone) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, permit_id);
			stmt.setString(2, phoneNumber);
			stmt.setString(3, super.licenseNumber);
			stmt.setString(4, super.startDate);
			stmt.setString(5, super.startTime);
			stmt.setString(6, super.expirationDate);
			stmt.setString(7, super.expirationTime);
			stmt.setString(8, String.valueOf(duration) + " h");
			stmt.setString(9, super.spaceType);
			stmt.setString(10, lotname);
			stmt.setInt(11, spaceNum);
			stmt.setString(12,"V");
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
		String licenseno ="";
		try {
			stmt=this.conn.prepareStatement("SELECT * from VisitorPermits "
					+ "WHERE PermitId=?");
			stmt.setString(1, permit_id);
			
			ResultSet result=stmt.executeQuery();
			
			//get lotname and spaceNumber to update
			while(result.next()) {
				licenseno= result.getString("LicenseNumber");
				lotname=result.getString("LotName");
				spaceNumber=result.getInt("SpaceNumber");
				expDate = result.getString("ExpirationDate");
				expTimeStr=result.getString("ExpirationTime");
				
			}
			expTime = Timestamp.valueOf(getOgTimeStamp(expDate, expTimeStr));
			Space space=new Space(lotname,spaceNumber,conn);
			space.updateAvailable("Yes");
			Timestamp curtime = new Timestamp(new java.util.Date().getTime());
			
			//calculate extra charges if any
			
			if(expTime.getTime()-curtime.getTime()<0) {
				ResultSet rs1=null;
				String model="";
				String color="";
				try {

					stmt = this.conn.prepareStatement("SELECT * FROM Vehicle "
							+ "WHERE LicenseNumber = ?"
					);

					stmt.setString(1, licenseno);
					rs1 = stmt.executeQuery();

					while (rs1.next()) {
						model = rs1.getString("Model");
						color = rs1.getString("Color");
					}

					String[] var = new Timestamp(new java.util.Date().getTime()).toString().split(" ");
					String startDate = var[0];
					String citationTime = var[1];
					String violationCategory = "Expired Permit";
					int fee = 25;

					java.util.Date currDate = new java.util.Date();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(currDate);
					calendar.add(Calendar.DATE, 30);
					Timestamp time1 = new Timestamp(calendar.getTime().getTime());
					String dtime[] = time1.toString().split(" ");
					String dueDate = dtime[0];
					String paidStatus = "Unpaid";
					Citation citation = new Citation(licenseno, model, color, startDate, lotname, citationTime, violationCategory, fee, dueDate, paidStatus, conn);
					citation.IssueCitation();
					System.out.println("Your permit has expired and you have been charged $25");
				}catch(SQLException e) {
					System.out.println("Failed to get user with the given licenseNumber " + e.getMessage());
				}

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
