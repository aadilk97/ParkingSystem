package parkingpackage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

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
				String phone=sc.next();
				
				System.out.println("Enter your car details in the order mentioned below\n\n");
				System.out.println("License Plate Manufacturer Model Color Year");			
				
				String licenseplate=sc.next();
				String manufacturer=sc.next();
				String model=sc.next();
				String color=sc.next();
				int year=Integer.parseInt(sc.next());
				
				Vehicle v = new Vehicle(licenseplate, manufacturer,model,color,year,conn);
				
//				System.out.println(v);
				VisitorVehicle vv=new VisitorVehicle(phone,conn,v);
				
				vv.addVisitor();
				
				System.out.println("Visitor entry added");
				
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
