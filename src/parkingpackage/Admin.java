package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
	

	public String checkVVisitorParking(String licenseNumber, String lName, int sNum, Timestamp curtime) {
		PreparedStatement stmt;
		String lotName="";
		int spaceNumber=0;
		String expTimeStr = "";
		String expDate = "";
		
		try {
			stmt=this.conn.prepareStatement("SELECT * from VisitorPermits "
					+ "WHERE LicenseNumber=?");
			stmt.setString(1, licenseNumber);
			
			ResultSet res=stmt.executeQuery();
			
			while(res.next()) {
				lotName=res.getString("LotName");
				spaceNumber=res.getInt("SpaceNumber");
				expDate = res.getString("ExpirationDate");
				expTimeStr=res.getString("ExpirationTime");
			}
			if (lotName.equalsIgnoreCase("unassigned") && spaceNumber==-1) {
				return "Vehicle "+licenseNumber+ " has already left the parking lot";
			}
			else if(lName.equalsIgnoreCase(lotName) && spaceNumber==sNum) {
				Timestamp expTime = Timestamp.valueOf(expDate + " " + expTimeStr.toString());
				if(expTime.getTime()-curtime.getTime()<0) {
					return "Vehicle "+licenseNumber+" has an expired permit";
				}
				else {
					return "Vehicle "+licenseNumber+" has a valid permit";
				}
			}
			else {
				return "Vehicle "+licenseNumber+" has been parked in an unauthorized zone";
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}	
		return null;	
	}
	
	public void adminScreen() {

		Scanner sc = new Scanner(System.in);
		PreparedStatement stmt;
		ResultSet rs = null;
		while (true) {

			System.out.println("Enter a choice 1. Add Lot  2. Add Zone  3. Add type  "
					+ "4. Assign Permit  5. CheckVValidParking  6. CheckNVValidParking 7. Issue Citation M. Main menu");

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
				Timestamp start=new Timestamp(new Date().getTime());
				String startDate =new SimpleDateFormat("MM/dd/yyyy_hh:mm aa").format(start); 
				String expDate = "";
				String expTime = "11:59 PM";
				
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
				String t = new SimpleDateFormat("MM/dd/yyyy_hh:mm aa").format(time);
				String datetime[]=t.split("_");
				expDate = datetime[0];
				
				System.out.println(expDate);
				System.out.println(expTime);
				System.out.println(startDate.split("_")[0]);
				
				NonVisitorPermit nvpermit = new NonVisitorPermit(licenseNumber, startDate.split("_")[0], expDate, expTime, type, zone, univid, this.conn);
				nvpermit.getNonVisitorPermit("");
				
			}

			else if(choice.equalsIgnoreCase("7")) {
					System.out.println("Enter a citation category 1. Invalid Permit  2. Expired Permit  3. No Permit ");
					String category = sc.next();

					if (category.equalsIgnoreCase("1") || category.equalsIgnoreCase("2")) {
						System.out.println("Enter License Number of the vehicle");
						String licenseNumber = sc.next();
						String model="";
						String color="";
						try{
							stmt = this.conn.prepareStatement("SELECT * FROM Vehicle "
									+ "WHERE LicenseNumber = ?"
							);

							stmt.setString(1, licenseNumber);
							rs = stmt.executeQuery();

							while (rs.next()) {
								model = rs.getString("Model");
								color = rs.getString("Color");
							}

//							String[] var= new Timestamp(new Date().getTime()).toString().split(" ");
							Timestamp start=new Timestamp(new Date().getTime());
							String startDate =new SimpleDateFormat("MM/dd/yyyy_hh:mm aa").format(start); 
							System.out.println("Enter Lot name vehicle was parked in");
							String lotName = sc.next();
							String citationTime = startDate.split("_")[1];
							String violationCategory="";
							int fee=0;
							if (category.equalsIgnoreCase("1")) {
								violationCategory = "1. Invalid Permit";
								fee = 20;
							}
							else if(category.equalsIgnoreCase("2")){
								violationCategory = "2. Expired Permit";
								fee = 25;
							}
							Date currDate = new Date();
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(currDate);
							calendar.add(Calendar.DATE, 30);
							Timestamp time = new Timestamp(calendar.getTime().getTime());
							String duetime =new SimpleDateFormat("MM/dd/yyyy_hh:mm aa").format(time);
							String dtime[]=duetime.split("_");
							String dueDate=dtime[0];
							String paidStatus="UNPAID";
							Citation citation = new Citation(licenseNumber, model, color, startDate.split("_")[0], lotName, citationTime, violationCategory, fee, dueDate, paidStatus, this.conn);
							citation.IssueCitation();

					}catch(SQLException e) {
							System.out.println("Failed to get user with the given licenseNumber " + e.getMessage());
						}
					}
					else if (category.equalsIgnoreCase("3")) {
						System.out.println("Enter License Number of the vehicle");
						String licenseNumber = sc.next();
						System.out.println("Enter Model of the vehicle");
						String model = sc.next();
						System.out.println("Enter Color of the vehicle");
						String color = sc.next();
//						String[] var= new Timestamp(new Date().getTime()).toString().split(" ");
//						String startDate = var[0];
						Timestamp start=new Timestamp(new Date().getTime());
						String startDate =new SimpleDateFormat("MM/dd/yyyy_hh:mm aa").format(start);
						System.out.println("Enter Lot name vehicle was parked in");
						String lotName = sc.next();
						String citationTime = startDate.split("_")[1];
						String violationCategory="3. No Permit";
						int fee=40;
						Date currDate = new Date();
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(currDate);
						calendar.add(Calendar.DATE, 30);
						Timestamp time = new Timestamp(calendar.getTime().getTime());
						String duetime =new SimpleDateFormat("MM/dd/yyyy_hh:mm aa").format(time);
						String dtime[]=duetime.split("_");
						String dueDate=dtime[0];
						String paidStatus="UNPAID";
						Citation citation = new Citation(licenseNumber, model, color, startDate.split("_")[0], lotName, citationTime, violationCategory, fee, dueDate, paidStatus, this.conn);
						citation.IssueCitation();


					}
			}

			
			else if(choice.equalsIgnoreCase("5")) {
				System.out.println("Enter a valid Visitor license plate number");
				String licenseNumber=sc.next();
				
				System.out.println("Enter lotname and spacenumber for the parked vehicle"); //would always be valid
				String lotName=sc.next();
				int spaceNumber=sc.nextInt();
				Timestamp curtime = new Timestamp(new java.util.Date().getTime());
				System.out.println("STATUS: "+checkVVisitorParking(licenseNumber,lotName,spaceNumber,curtime));
				
			}
			
			else if(choice.equalsIgnoreCase("6")) {
				System.out.println("Enter the permitID");
				String permitId = sc.next();
				System.out.println("Enter the time eg: 06:PM");
				String time = sc.next();
				System.out.println("Enter the lot in which the vehicle has been parked");
				String lotParked = sc.next();
				System.out.println("Enter the zone in which the vehicle has been parked");
				String zoneParked = sc.next();
				System.out.println("Enter the space number in which the vehicle is parked");
				String spaceNumParked = sc.next();
				
				NonVisitorPermit nvpermit = new NonVisitorPermit(conn);
				if (nvpermit.checkNonVisitorPermit(permitId, lotParked, zoneParked, spaceNumParked, time)) {
					System.out.println("Valid parking");
				}
				
				else {
					System.out.println("Invalid parking");
				}
			}
			
			else if(choice.equalsIgnoreCase("M")) {
				// Going back to main menu
				break;
			}
		
		}
	}
}
