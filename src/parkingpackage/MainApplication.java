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
			System.out.println("Enter a choice.\n 1. Add visitor  Q. Quit");
			String choice = sc.next();
			
			if(choice.equalsIgnoreCase("1")) {
				System.out.println("Enter the phone number of the visitor");
				String phoneNumber = sc.next();
				
				Visitor v = new Visitor(phoneNumber, conn);
				v.addVisitor();
			}
			
			if(choice.equalsIgnoreCase("Q")) {
				break;
			}
			
		}
		sc.close();
		conn.close();
		
	}
}
