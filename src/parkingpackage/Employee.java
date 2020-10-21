package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Employee extends User {
	Employee(Connection conn) {
		super(conn);
	}

	public void addToNonVisitorPermits(String permitId, String licenseNumber, String univid) {
		PreparedStatement stmt;
		String startDate, expDate, expTime, zone, type;
		startDate = expDate = expTime = type = zone = "";
		ResultSet rs = null;

		try {
			stmt = this.conn.prepareStatement("SELECT * FROM NonvisitorPermits "
					+ "WHERE permitId = ?"
			);

			stmt.setString(1, permitId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				startDate = rs.getString("StartDate");
				expDate = rs.getString("ExpirationDate");
				expTime = rs.getString("ExpirationTime");
				zone = rs.getString("Zone");
				type = rs.getString("SpaceType");
			}

			NonVisitorPermit nvpermit = new NonVisitorPermit(licenseNumber, startDate, expDate, expTime, type, zone, univid, this.conn);
			nvpermit.getNonVisitorPermit(permitId);

		} catch (SQLException e) {
			System.out.println("Failed to add to nonvisitor permits " + e.getMessage());
		}

	}

	public void changeVehicleList(String permitId, String univid, Scanner sc) {
		System.out.println("Enter the license number of the vehicle that needs to be removed from permit");
		String oldLicenseNumber = sc.next();

		System.out.println("Enter the license number");
		String licenseNumber = sc.next();
		System.out.println("Enter the manufacturer");
		String manufacturer = sc.next();
		System.out.println("Enter the car's model");
		String model = sc.next();
		System.out.println("Enter the car's color");
		String color = sc.next();
		System.out.println("Enter the year");
		int year = sc.nextInt();

		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement("DELETE FROM NonvisitorPermits "
					+ "WHERE LicenseNumber = ?"
			);

			stmt.setString(1, oldLicenseNumber);
			stmt.executeUpdate();

			stmt = this.conn.prepareStatement("DELETE Vehicle "
					+ "WHERE LicenseNumber = ?"
			);

			stmt.setString(1, oldLicenseNumber);
			stmt.executeUpdate();

			Vehicle vehicle = new Vehicle(licenseNumber, manufacturer, model, color, year, conn);
			vehicle.addVehicle();

			addToNonVisitorPermits(permitId, licenseNumber, univid);


			System.out.println("Vehicle list updated successfully");

		} catch(Exception e) {
			System.out.println("Failed to update vehicle list " + e.getMessage());
		}
	}

	public void addVehicleToPermit(String permitId, String univid, Scanner sc) {
		PreparedStatement stmt;
		int vehicleCount = 0;

		try {

			stmt = this.conn.prepareStatement("SELECT Count(*) AS Count "
					+ "FROM NonvisitorPermits "
					+ "WHERE Univid = ? "
					+ ""
			);

			stmt.setString(1,  univid);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				vehicleCount = Integer.parseInt(rs.getString("Count"));
			}

			if(vehicleCount > 1) {
				System.out.println("There are already 2 vehciles assiciated with this permit. Can not add more vehciles");
				return;
			}

			System.out.println("Enter the license number");
			String licenseNumber = sc.next();
			System.out.println("Enter the manufacturer");
			String manufacturer = sc.next();
			System.out.println("Enter the car's model");
			String model = sc.next();
			System.out.println("Enter the car's color");
			String color = sc.next();
			System.out.println("Enter the year");
			int year = sc.nextInt();

			Vehicle vehicle = new Vehicle(licenseNumber, manufacturer, model, color, year, conn);
			vehicle.addVehicle();

			addToNonVisitorPermits(permitId, licenseNumber, univid);


		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void employeeScreen() {
		Scanner sc = new Scanner(System.in);

		while(true) {
			System.out.println("Enter a choice. 1. Add vehicle to permit  2. Change vehicle  3. Pay Citation  M. Main menu");
			String choice = sc.next();

			if(choice.equalsIgnoreCase("1")) {
				System.out.println("Enter the permitId");
				String permitId = sc.next();
				System.out.println("Enter the university id");
				String univid = sc.next();

				addVehicleToPermit(permitId, univid, sc);
			}

			else if(choice.equalsIgnoreCase("2")) {
				System.out.println("Enter the permitId");
				String permitId = sc.next();
				System.out.println("Enter the university id");
				String univid = sc.next();

				changeVehicleList(permitId, univid, sc);
			}
			else if(choice.equalsIgnoreCase("3")) {
				System.out.println("Enter the License Number");
				String licenseno = sc.next();
				PreparedStatement stmt;
				ResultSet rs=null;
				try {
					stmt = this.conn.prepareStatement("SELECT * FROM Citation "
							+ "WHERE LicenseNumber =? "
							+ "AND PaidStatus=?");
					stmt.setString(1, licenseno);
					stmt.setString(2,"UNPAID");
					rs = stmt.executeQuery();
					if (!rs.next()) {
						System.out.println("There is no fee charged!!!");
					} else {
						String citationNum= rs.getString("CitationNumber");
						String model= rs.getString("Model");
						String color= rs.getString("Color");
						String startDate= rs.getString("StartDate");
						String lotName= rs.getString("LotName");
						String citationTime= rs.getString("CitationTime");
						String violationCategory= rs.getString("ViolationCategory");
						int fee= rs.getInt("Fee");
						String dueDate= rs.getString("DueDate");
						Citation citation = new Citation(licenseno, model, color, startDate, lotName, citationTime, violationCategory, fee, dueDate, "UNPAID", this.conn);
						citation.PayCitation(citationNum);
					}
				}catch(SQLException e) {
					System.out.println("Citation couldn't be found" + e.getMessage());
				}
			}
			else if(choice.equalsIgnoreCase("M")) {
				break;
			}
		}
	}

}
