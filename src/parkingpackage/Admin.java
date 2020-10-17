package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;


public class Admin extends User{
	Admin(Connection conn){
		super(conn);
	}
	
	public String getUserType(String univid) {
		PreparedStatement stmt;
		ResultSet rs = null;
		try {
			stmt = this.conn.prepareStatement("SELECT Type FROM Users "
					+ "WHERE Univid = ?"
					);
			
			stmt.setString(1, univid);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				return rs.getString("Type");
			}
			
		} catch(SQLException e) {
			System.out.println("Failed to get user with the given univid " + e.getMessage());
		}
		
		return "";
	}
	
	public void adminScreen() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("Enter a choice 1. Add Lot  2. Add Zone  3. Add type  4. Assign Permit  M. Main menu");
			String choice = sc.next();
			
			
			if(choice.equalsIgnoreCase("1")) {
				 System.out.println("Enter name");
				 String name = sc.next();
				 System.out.println("Enter address");
				 String address = sc.next();
				 System.out.println("Enter designation");
				 String designation = sc.next();
				 System.out.println("Enter startnumber");
				 int startSpaceNumber = sc.nextInt();
				 System.out.println("Enter number of spaces");
				 int numSpaces = sc.nextInt();
				 
				 Lots lot = new Lots(name, address, designation, startSpaceNumber, numSpaces, conn);
				 lot.addLot();
				 
			}
			
			else if(choice.equalsIgnoreCase("2")) {
				// Logic for adding new zone to a lot.
				System.out.println("Enter the lot to which zone has to be assigned");
				String name = sc.next();
				System.out.println("Enter the zone");
				String zone = sc.next();
				System.out.println("Enter the start space number");
				int startSpaceNumber = sc.nextInt();
				System.out.println("Enter number of spaces");
				int numSpaces = sc.nextInt();
				
				
				Lots lotObject = new Lots(name, conn);
				lotObject.assignZoneToLot(name, zone);
				
				for(int i=startSpaceNumber; i < startSpaceNumber + numSpaces; i++) {
					Space space = new Space(name, i, zone, conn);
					space.addSpace();
				}
			}
			
			else if(choice.equalsIgnoreCase("3")) {
				System.out.println("Enter the lot to which the space belongs to");
				String name = sc.next();
				System.out.println("Enter the space number");
				int spaceNum = sc.nextInt();
				System.out.println("Enter the type");
				String type = sc.next();
				
				Space space = new Space(name, conn);
				space.setType(type);
				space.setSpaceNum(spaceNum);
				space.addTypeToSpace();
				
			}
			
			else if(choice.equalsIgnoreCase("4")) {
				System.out.println("Enter the univid");
				String univid = sc.next();
				System.out.println("Enter the zone");
				String zone = sc.next();
				String type = "regular";
				System.out.println("Do you want to request a specific type. Enter Yes to do so");
				String typeChoice = sc.next();
				
				if(typeChoice.equalsIgnoreCase("Yes")) {
					System.out.println("Enter the type");
					type = sc.next();
				}
				
				System.out.println("Enter the license number");
				String licenseNumber = sc.next();
				String userType = getUserType(univid);
				String startDate = new Timestamp(new Date().getTime()).toString().split(" ")[0];
				String expDate = "";
				String expTime = "23:59:59";
				
				Date currDate = new Date();
				Calendar calendar = Calendar.getInstance(); 
				calendar.setTime(currDate);
				
				
				if(userType.equalsIgnoreCase("Student")) {
					calendar.add(Calendar.MONTH, 4);	
				}
				
				else if(userType.equalsIgnoreCase("Employee")) {
					calendar.add(Calendar.MONTH, 12);	
				}
				
				Timestamp time = new Timestamp(calendar.getTime().getTime());
				String datetime[]=time.toString().split(" ");
				expDate = datetime[0];
				
				NonVisitorPermit nvpermit = new NonVisitorPermit(licenseNumber, startDate, expDate, expTime, type, zone, univid, this.conn);
				nvpermit.getNonVisitorPermit();
				
			}
			
			else if(choice.equalsIgnoreCase("M")) {
				// Going back to main menu
				break;
			}
		
		}
	}
}
