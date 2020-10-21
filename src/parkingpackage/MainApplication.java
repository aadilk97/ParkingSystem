package parkingpackage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class MainApplication {

	public static void signup(Connection conn, String type, Scanner sc) {
		System.out.println("Enter the University ID: ");
		String univid = sc.next();

		System.out.println("Enter the Password: ");
		String password = sc.next();

		User user = new User(univid, password, type, conn);

	}

	public static void employeeScreen() {
		System.out.println("This is the employee screen");
	}

	public static void studentScreen() {
		System.out.println("This is student screen");
	}

	public static void main(String args[]) throws SQLException {
		DatabaseConnection dbConnection = new DatabaseConnection();
		Connection conn = dbConnection.createConnection();

		if (dbConnection.testConnection(conn)) {
			System.out.println("\nConnection to the DB is Successful\n");
		} else {
			System.out.println("\nConnection to the DB is Failed\n");
		}

		Scanner sc = new Scanner(System.in);
		sc.useDelimiter("\n");

		while (true) {
			System.out.println("Choose an Option\n" +
					"1. UPS Admin Role\n" +
					"2. Employee Role\n" +
					"3. Student Role\n" +
					"4. Visitor Role\n" +
					"5. List of Zones in each Lot\n" +
					"6. Permit information for a given employee with UnivID: 1006020\n" +
					"7. Vehicle information for a Non-Visitor with UnivID: 1006003\n" +
					"8. Available space# for Visitor for an electric vehicle in a Justice parking lot\n" +
					"9. Cars that are currently in violation\n" +
					"10. No. of Employees with Parking Zone D permits\n" +
					"11. Total No. of Citations given in all Zones for each lot during 07/01/2020 - 09/30/2020 period\n" +
					"12. No. of Visitor permits (during 08/12/2020 - 08/20/2020) grouped by permit type in Justice Lot\n" +
					"13. Total amount of revenue generated (including pending citation fines) for each day of August 2020 in each Visitor's parking zone\n" +
					"14. Exit\n");

			System.out.print("Enter the option: ");
			int role = sc.nextInt();

			if (role < 4) {
				String type = "";
				switch (role) {
					case 1:
						type = "Admin";
						break;

					case 2:
						type = "Employee";
						break;

					case 3:
						type = "Student";
						break;
				}

				System.out.println("\n1. Login\n2. Sign Up\n");
				int choice = sc.nextInt();

				if (choice == 1) {
					System.out.println("Enter the UnivID: ");
					String univid = sc.next();

					System.out.println("Enter the Password: ");
					String password = sc.next();

					User user = new User(conn);
					if (user.checkLogin(univid, password, type)) {
						System.out.println("\nLogin Successful\n");

						if (type.equalsIgnoreCase("Employee")) {
							Employee employee = new Employee(conn);
							employee.employeeScreen();
						}

						else if (type.equalsIgnoreCase("Admin")) {
							Admin admin = new Admin(conn);
							admin.adminScreen();
						}

						else {
							studentScreen();
						}

					} else {
						System.out.println("\nLogin Failure !!!\nReason: Invalid Credentials or Invalid User Type\nTry logging in again with appropriate options\n");
					}
				}

				else if (choice == 2) {
					signup(conn, type, sc);
				}

			}

			else if (role == 4) {
				while (true) {
					System.out.println("\nEnter your choice:\n1. Entry\n2. Exit\n");
					int ch = sc.nextInt();
					if (ch == 1) {
						// Entry WorkFlow

						System.out.println("Hello Visitor!!\nWelcome to University Parking Lot!\n");
						System.out.println("\nEnter a valid contact number without spaces (Ex: 1234567890)\n");
						String phoneNumber = sc.next();
						System.out.println("Enter the license number\n");
						String licenseNumber = sc.next();

						System.out.println("Entry");
						Visitor visitor = new Visitor(phoneNumber, licenseNumber, conn);
						visitor.addVisitor();
						System.out.println("Visitor added successfully");

						System.out.println("Enter your Vehicle details below: \n\n");
						System.out.println("Enter the Vehicle Manufacturer:\n");
						String manufacturer = sc.next();
						System.out.println("Enter the Vehicle Model:\n");
						String model = sc.next();
						System.out.println("Enter the Vehicle Color:\n");
						String color = sc.next();
						System.out.println("Enter the Vehicle Year:\n");
						int year = Integer.parseInt(sc.next());

						Vehicle vv = new Vehicle(licenseNumber, manufacturer, model, color, year, conn);
						vv.addVehicle();

						System.out.println("Vehicle added successfully");

						while (true) {
							System.out.print("Enter the name of a parking lot:\n");
							String lotname = sc.next();

							System.out.print("Enter the desired space number:\n");
							int spaceNumber = Integer.parseInt(sc.next());

							System.out.println("Enter the space type:\n");
							String spaceType = sc.next();

							Space space = new Space(lotname, spaceNumber, "V", spaceType, conn);
							if (space.isSpaceAvailableVisitor().equals("Yes")) {
								space.updateAvailable("No");
								while (true) {
									System.out.println("Enter the Entry Time Value as instructed below:");
									System.out.println("Enter the value of hour between 9-20 inclusive (assuming visitor entry is allowed from 9a-8p)"); //assuming visitors are allowed only from 9am-8pm
									String hr=sc.next();
									System.out.println("Enter the value of mins between 00-59 inclusive");
									String mins=sc.next();
									
									//get current date
									Timestamp time = new Timestamp(new java.util.Date().getTime());
									String t = new SimpleDateFormat("MM/dd/yyyy_hh:mm aa").format(time);
									String datetime[] = time.toString().split(" ");
									String format_datetime[]=t.split("_");
									
									//today's date + entry time
									Timestamp time1=Timestamp.valueOf(datetime[0]+" "+hr+":"+mins+":00.000");
									Timestamp entry_time=time1;
									String entry=new SimpleDateFormat("MM/dd/yyyy_hh:mm aa").format(entry_time);
									
									System.out.println("Your Entry Time is "+ entry);
									if(entry_time.after(time)) {
										System.out.println("Enter the permit duration required ranging between 1-4 hours (inclusive)\n");
										int duration = Integer.parseInt(sc.next());
										
										//compute expiration time
										time1.setTime(time1.getTime()+duration*60*60*1000);
										String expt = new SimpleDateFormat("MM/dd/yyyy_hh:mm aa").format(time1);
										
										String expdt[] = expt.split("_");
										System.out.println(expdt[0]);// exp date
										System.out.println(expdt[1]);// exp time

										String startDate = datetime[0];

										// pass to visitorpermit class and create visitor permit
										System.out.println(format_datetime[0]);
										VisitorPermit vpermit = new VisitorPermit(phoneNumber,licenseNumber, format_datetime[0], expdt[0],
												expdt[1], spaceType, lotname, spaceNumber, "V", conn);
										vpermit.getVisitorPermit();
										break;
									}
									else {
										System.out.println("Enter a valid time and try again\n");
									}
								}
								break;
							} else {
								System.out.println("Cannot park here, try some other values");
							}
						}
						break;
					} else if (ch == 2) {
						// Exit Workflow
						System.out.println("Exit\n");

						System.out.println("Enter your permit number:\n");
						String permitID = sc.next();

						VisitorPermit vpermit = new VisitorPermit(conn);
						vpermit.ExitLot(permitID);
						break;
					} else {
						System.out.println("Enter a valid choice and try again\n");
					}
				}

//				System.out.println("I'm outside");
			}
			else if (role == 5) {
				PreparedStatement stmt;
				ResultSet rs = null;
				try {

					stmt = conn.prepareStatement("SELECT L.NAME, L.DESIGNATION FROM LOTS L");
					rs = stmt.executeQuery();


					System.out.println("\n(Lot Name, Zone Designation)");
					while(rs.next()) {
						String lotName = rs.getString("NAME");
						String designation = rs.getString("DESIGNATION");
						System.out.println("(" + lotName + ", " + designation + ")");
					}
					System.out.println();
					stmt.close();

				} catch(SQLException e) {
					System.out.println("Query failed to run " + e.getMessage());
				}
			}

			else if (role == 6) {
				PreparedStatement stmt;
				ResultSet rs = null;
				try {

					stmt = conn.prepareStatement("SELECT * FROM NONVISITORPERMITS WHERE UNIVID = '1006020'");
					rs = stmt.executeQuery();

					// Need to add "start time" to the table and to the print statement below
					System.out.println("\n(PermitID, Univ ID, License#, StartDate, ExpirationDate, ExpirationTime, SpaceType, Zone, Manufacturer, Model, Color, Year)");
					while(rs.next()) {
						String permitID = rs.getString("PERMITID");
						String univID = rs.getString("UNIVID");
						String licenseNo = rs.getString("LICENSENUMBER");
						String startDate = rs.getString("STARTDATE");
						String expDate = rs.getString("EXPIRATIONDATE");
						String expTime = rs.getString("EXPIRATIONTIME");
						String spaceType = rs.getString("SPACETYPE");
						String zone = rs.getString("ZONE");
						String manu = rs.getString("MANUFACTURER");
						String model = rs.getString("MODEL");
						String color = rs.getString("COLOR");
						String year = rs.getString("YEAR");
						System.out.println("(" + permitID + ", " + univID + ", " + licenseNo + ", " + startDate + ", " + expDate + ", " + expTime + ", " + spaceType + ", " + zone + ", " + manu + ", " + model + ", " + color + ", " + year + ")");
					}
					System.out.println();
					stmt.close();

				} catch(SQLException e) {
					System.out.println("Query failed to run " + e.getMessage());
				}
			}

			else if (role == 7) {
				PreparedStatement stmt;
				ResultSet rs = null;
				try {

					stmt = conn.prepareStatement("SELECT * FROM VEHICLE WHERE LICENSENUMBER IN (SELECT LICENSENUMBER FROM NONVISITORPERMITS WHERE UNIVID = '1006003')");
					rs = stmt.executeQuery();

					// Need to add "start time" to the table and to the print statement below
					System.out.println("\n(License#, Manufacturer, Model, Color, Year)");
					while(rs.next()) {
						String licenseNo = rs.getString("LICENSENUMBER");
						String manu = rs.getString("MANUFACTURER");
						String model = rs.getString("MODEL");
						String color = rs.getString("COLOR");
						String year = rs.getString("YEAR");
						System.out.println("(" + licenseNo + ", " + manu + ", " + model + ", " + color + ", " + year + ")");
					}
					System.out.println();
					stmt.close();

				} catch(SQLException e) {
					System.out.println("Query failed to run " + e.getMessage());
				}
			}

			else if (role == 8) {
				PreparedStatement stmt;
				ResultSet rs = null;
				try {

					stmt = conn.prepareStatement("SELECT SPACENUMBER FROM SPACES WHERE NAME = 'Justice Lot' AND ZONE = 'V' AND TYPE = 'Electric' AND AVAILABLE = 'Yes'");
					rs = stmt.executeQuery();

					// Need to add "start time" to the table and to the print statement below
					System.out.println("\n(Space#)");
					while(rs.next()) {
						String spaceNo = rs.getString("SPACENUMBER");
						System.out.println("(" + spaceNo + ")");
					}
					System.out.println();
					stmt.close();

				} catch(SQLException e) {
					System.out.println("Query failed to run " + e.getMessage());
				}
			}

			else if (role == 9) {
				PreparedStatement stmt;
				ResultSet rs = null;
				try {

					stmt = conn.prepareStatement("SELECT * FROM CITATION WHERE PAIDSTATUS = 'Unpaid'");
					rs = stmt.executeQuery();

					// Need to add "start time" to the table and to the print statement below
					System.out.println("\n(Citation#, License#, Model, Color, StartDate, LotName, CitationTime, ViolationCategory, Fee, DueDate, PaidStatus)");
					while(rs.next()) {
						String citationNo = rs.getString("CITATIONNUMBER");
						String licenseNo = rs.getString("LICENSENUMBER");
						String model = rs.getString("model");
						String color = rs.getString("COLOR");
						String startDate = rs.getString("STARTDATE");
						String lotName = rs.getString("LOTNAME");
						String citationTime = rs.getString("CITATIONTIME");
						String violationCategory = rs.getString("VIOLATIONCATEGORY");
						String fee = rs.getString("FEE");
						String dueDate = rs.getString("DUEDATE");
						String status = rs.getString("PAIDSTATUS");
						System.out.println("(" + citationNo + ", " + licenseNo + ", " + model + ", " + color + ", " + startDate + ", " + lotName + ", " + citationTime + ", " + violationCategory + ", " + fee + ", " + dueDate + ", " + status + ")");
					}
					System.out.println();
					stmt.close();

				} catch(SQLException e) {
					System.out.println("Query failed to run " + e.getMessage());
				}
			}

			else if (role == 10) {
				PreparedStatement stmt;
				ResultSet rs = null;
				try {

					stmt = conn.prepareStatement("SELECT COUNT(UNIVID) AS COUNT FROM NONVISITORPERMITS WHERE ZONE = 'D'");
					rs = stmt.executeQuery();

					// Need to add "start time" to the table and to the print statement below
					System.out.println("\n(Count)");
					while(rs.next()) {
						String count = rs.getString("COUNT");
						System.out.println("(" + count + ")");
					}
					System.out.println();
					stmt.close();

				} catch(SQLException e) {
					System.out.println("Query failed to run " + e.getMessage());
				}
			}

			else if (role == 14) {
				break;
			}

			else {
				System.out.println("\nInvalid choice !!!\nChoose one from below options\n");
			}
		}

		sc.close();
		conn.close();

	}
}