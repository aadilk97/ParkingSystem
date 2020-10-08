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
	
	public static void adminScreen() {
		System.out.println("This is the ADMIN screen");
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
							adminScreen();
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
				
			}
			
			else if(role == 5) {
				break;
			}
			
			else {
				System.out.println("Invalid choice");
			}
		}
	
			
		
//		while (true) {
//			System.out.println("Enter a choice.\n 1. Add visitor  2. Add Lot  Q. Quit");
//			String choice = sc.next();
//			
//			if(choice.equalsIgnoreCase("1")) {
//				System.out.println("Enter the phone number of the visitor");
//				String phoneNumber = sc.next();
//				
//				Visitor v = new Visitor(phoneNumber, conn);
//				v.addVisitor();
//			}
//			
//			else if(choice.equalsIgnoreCase("2")) {
//				 System.out.println("Enter name, address, designation, startnum, numspaces");
//				 String name = sc.next();
//				 String address = sc.next();
//				 String designation = sc.next();
//				 int startSpaceNumber = sc.nextInt();
//				 int numSpaces = sc.nextInt();
//				 
//				 Lots lot = new Lots(name, address, designation, startSpaceNumber, numSpaces, conn);
//				 lot.addLot();
//				 
//			}
//			
//			if(choice.equalsIgnoreCase("Q")) {
//				break;
//			}
//			
//		}
		sc.close();
		conn.close();
		
	}
}
