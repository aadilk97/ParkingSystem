package parkingpackage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MainApplication {
	
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
		while (true) {
			System.out.println("Enter a choice.\n 1. Add visitor  2. Add Lot  Q. Quit");
			String choice = sc.next();
			
			if(choice.equalsIgnoreCase("1")) {
				System.out.println("Enter the phone number of the visitor");
				String phoneNumber = sc.next();
				
				Visitor v = new Visitor(phoneNumber, conn);
				v.addVisitor();
			}
			
			else if(choice.equalsIgnoreCase("2")) {
				 System.out.println("Enter name, address, designation, startnum, numspaces");
				 String name = sc.next();
				 String address = sc.next();
				 String designation = sc.next();
				 int startSpaceNumber = sc.nextInt();
				 int numSpaces = sc.nextInt();
				 
				 Lots lot = new Lots(name, address, designation, startSpaceNumber, numSpaces, conn);
				 lot.addLot();
				 
			}
			
			if(choice.equalsIgnoreCase("Q")) {
				break;
			}
			
		}
		sc.close();
		conn.close();
		
	}
}
