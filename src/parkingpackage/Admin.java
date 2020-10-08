package parkingpackage;

import java.sql.Connection;
import java.util.Scanner;


public class Admin extends User{
	Admin(Connection conn){
		super(conn);
	}
	
	public void adminScreen() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("Enter a choice 1. Add Lot  2. Add Zone  Q. Main menu");
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
			}
			
			else if(choice.equalsIgnoreCase("Q")) {
				// Going back to main menu
				break;
			}
		
		}
	}
}
