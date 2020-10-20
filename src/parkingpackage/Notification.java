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


    void AddNotification() throws SQLException {
        PreparedStatement stmt;
        stmt=this.conn.prepareStatement("SELECT LicenseNumber FROM NonVisitorPermits ");
        ArrayList<String> nvarr=new ArrayList<String>();

        ResultSet rs = null;
        rs = stmt.executeQuery();

        while (rs.next()){
            nvarr.add(rs.getString("LicenseNumber"));
        }
        stmt=this.conn.prepareStatement("SELECT LicenseNumber FROM VisitorPermits ");
        ArrayList<String> varr=new ArrayList<String>();
        rs = stmt.executeQuery();
        while (rs.next()){

            varr.add(rs.getString("LicenseNumber"));
        }
        if (nvarr.contains(licenseNumber)){
            stmt=this.conn.prepareStatement("SELECT * FROM NonVisitorPermits "
                    +"WHERE LicenseNumber="+licenseNumber);
            userID = rs.getString("Univid");
        }
        else if (varr.contains(licenseNumber)){
            stmt=this.conn.prepareStatement("SELECT * FROM VisitorPermits "
                    +"WHERE LicenseNumber="+licenseNumber);
            userID = rs.getString("PhoneNumber");
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