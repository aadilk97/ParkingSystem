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
			System.out.println("Enter a choice 1. Add Lot  2. Add Zone  3. Add type   M. Main menu");
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
				System.out.println("Ebnter the start space number");
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
			
			else if(choice.equalsIgnoreCase("M")) {
				// Going back to main menu
				break;
			}
		
		}
	}
}
