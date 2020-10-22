package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

public class Notification extends Citation {

    String userID;
    String citationNumber;
    String licenseNumber;
    String violationCategory;
    String citationDate;
    int fee;
    String dueDate;
    Connection conn;

    Notification(String citationNumber, String licenseNumber, String violationCategory,String citationDate, int fee, String dueDate, Connection conn) {
        super(conn,citationNumber);
        this.citationNumber = super.citationNumber;
        this.violationCategory= violationCategory;
        this.licenseNumber = licenseNumber;
        this.citationDate = citationDate;
        this.fee = fee;
        this.dueDate = dueDate;
        this.conn = conn;
    }
    void DeleteNotification( String citationNum) throws SQLException{
        PreparedStatement stmt;
        stmt=this.conn.prepareStatement("DELETE FROM Notification "
                +"WHERE CitationNumber=?");
        stmt.setString(1,citationNum);
        stmt.executeUpdate();
        System.out.println("Notification deleted successfully");
    }

    void AddNotification() {
        PreparedStatement stmt;
        ResultSet rs = null;

        try {
            stmt=this.conn.prepareStatement("SELECT * FROM NonVisitorPermits "
                    +"WHERE LicenseNumber=?");
            stmt.setString(1,licenseNumber);
            rs = stmt.executeQuery();
            //        userID="54545454";
            if (rs.next()) {
                userID = rs.getString("Univid");
            }
            else{
                stmt=this.conn.prepareStatement("SELECT * FROM VisitorPermits "
                        +"WHERE LicenseNumber=?");
                stmt.setString(1,licenseNumber);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    userID = rs.getString("PhoneNumber");
                }
            }
        }catch(SQLException e) {
            System.out.println("Cannot be notified as user doesn't exits in UPS system " + e.getMessage());
        }

        try {
            stmt=this.conn.prepareStatement("INSERT INTO Notification "
                    + "(UserID, CitationNumber,LicenseNumber, ViolationCategory, CitationDate,Fee,DueDate) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, userID);
            stmt.setString(2, citationNumber);
            stmt.setString(3, licenseNumber);
            stmt.setString(4, violationCategory);
            stmt.setString(5, citationDate);
            stmt.setInt(6, fee);
            stmt.setString(7, dueDate);
            stmt.executeUpdate();

            System.out.println("Notification added Successfully");
        }
        catch(SQLException e) {
            System.out.println("Notification couldn't be added" + e.getMessage());
        }
    }
}
