package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Student extends User {
    Student(Connection conn) {
        super(conn);
    }

    public void studentScreen() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("You are logged in as student");
            System.out.println("Enter a choice. 1. Pay Citation  M. Main menu");
            String choice = sc.next();
            if (choice.equalsIgnoreCase("1")) {
                System.out.println("Enter the License Number");
                String licenseno = sc.next();
                PreparedStatement stmt;
                ResultSet rs = null;
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
                        String citationNum = rs.getString("CitationNumber");
                        String model = rs.getString("Model");
                        String color = rs.getString("Color");
                        String startDate = rs.getString("StartDate");
                        String lotName = rs.getString("LotName");
                        String citationTime = rs.getString("CitationTime");
                        String violationCategory = rs.getString("ViolationCategory");
                        int fee = rs.getInt("Fee");
                        String dueDate = rs.getString("DueDate");
                        Citation citation = new Citation(licenseno, model, color, startDate, lotName, citationTime, violationCategory, fee, dueDate, "UNPAID", this.conn);
                        citation.PayCitation(citationNum);
                    }
                } catch (SQLException e) {
                    System.out.println("Citation couldn't be found" + e.getMessage());
                }
            } else if (choice.equalsIgnoreCase("M")) {
                break;
            }
        }
    }
}