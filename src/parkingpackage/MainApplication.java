package parkingpackage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import java.sql.Timestamp;

public class MainApplication {
	
	public static void signup(Connection conn, String type, Scanner sc) {
		System.out.println("Enter the univesity id");
		String univid = sc.next();
		
		System.out.println("Enter a password");
		String password = sc.next();
		
		User user = new User(univid, password, type, conn);
		
	}
	
	public static void employeeScreen() {
		System.out.println("This is the employee screen");
	}
	
	public static void studentScreen() {
		System.out.println("This is student screen");
	}
	
	
	public static void main(String args[]) throws SQLException{
		DatabaseConnection dbConnection = new DatabaseConnection();
		Connection conn = dbConnection.createConnection();
		
		if(dbConnection.testConnection(conn)) {
			System.out.println("Connection successful");
		}
		else {
			System.out.println("Connection Failed");
		}
		
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("Enter a role 1.Admin 2.Employee 3.Student 4.Visitor 5.Exit");
			int role = sc.nextInt();
			
			if(role < 4) {
				String type = "";
				switch(role) {
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

				
				System.out.println("1.Login  2.SignUp");
				int choice = sc.nextInt();
				
				if(choice == 1) {
					System.out.println("Enter the univid");
					String univid = sc.next();
					
					System.out.println("Enter the password");
					String password = sc.next();
					
					
					User user = new User(conn);
					if (user.checkLogin(univid, password, type)) {
						System.out.println("Login successful");
						
						if(type.equalsIgnoreCase("Employee")) {
							employeeScreen();
						}
						
						else if(type.equalsIgnoreCase("Admin")) {
							Admin admin = new Admin(conn);
							admin.adminScreen();
						}
						
						else {
							studentScreen();
						}
						
					}
					else {
						System.out.println("Invalid credentials, login failed");
					}
				}
				
				else if(choice == 2) {
					signup(conn, type, sc);
				}
				
			}
			
			else if(role == 4) {
				System.out.println("Hi Visitor welcome to our Parking Lot!");
				System.out.println("Please enter a valid contact number without spaces");
				String phoneNumber=sc.next();
				System.out.println("Enter the license number");
				String licenseNumber=sc.next();
				
				Visitor visitor = new Visitor(phoneNumber, licenseNumber, conn);
				visitor.addVisitor();
				System.out.println("Visitor added successfully");
				
				System.out.println("Enter your car details in the order mentioned below\n\n");
				System.out.println("Manufacturer Model Color Year");			
				
				String manufacturer=sc.next();
				String model=sc.next();
				String color=sc.next();
				int year=Integer.parseInt(sc.next());
				
				Vehicle vv=new Vehicle(licenseNumber,manufacturer,model,color,year,conn);
				vv.addVehicle();
				
				System.out.println("Vehicle added successfully");
	
				while (true) {
					System.out.print("Enter the value for a parking lot:\n");
					String lotname=sc.next();
					
					System.out.print("Enter the desired space number:\n");
					int spaceNumber=Integer.parseInt(sc.next());
					
					System.out.println("Enter the space type:\n");
					String spaceType=sc.next();
					
					Space space=new Space(lotname,spaceNumber,"V",spaceType,conn);
					if(space.isSpaceAvailableVisitor().equals("Yes")) {
						space.updateAvailable();
						System.out.println("Enter the permit duration required ranging between 1-4 hours (inclusive)\n");
						int duration=Integer.parseInt(sc.next());
						
						Timestamp time=new Timestamp(new java.util.Date().getTime());
						String datetime[]=time.toString().split(" ");
						System.out.println(datetime[0]);// start date
						
						Timestamp exp = new Timestamp(System.currentTimeMillis() + duration *60*60);
						String expdt[]=exp.toString().split(" ");
						System.out.println(expdt[0]);//exp date
						System.out.println(expdt[1]);//exp time
						
						String startDate=datetime[0];
						
						//pass to visitorpermit class and create visitor permit
						VisitorPermit vpermit=new VisitorPermit(licenseNumber,datetime[0],expdt[0],expdt[1],spaceType,lotname,spaceNumber,"V",conn);
						vpermit.getVisitorPermit();
						break;
					}
					else {
						System.out.println("Cannot park here, try some other values");
					}
				}
//				System.out.println("I'm outside");
			}
			
			else if(role == 5) {
				break;
			}
			
			else {
				System.out.println("Invalid choice");
			}
		}
	

		sc.close();
		conn.close();
		
	}
}