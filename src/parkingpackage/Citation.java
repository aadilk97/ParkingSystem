package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;

public class Citation {
    String citationNumber;
    String licenseNumber;
    String model;
    String color;
    String startDate;
    String lotName;
    String citationTime;
    String violationCategory;
    int fee;
    String dueDate;
    String paidStatus;
    Connection conn;

    Citation(String licenseNumber, String model, String color, String startDate, String lotName, String citationTime, String violationCategory, int fee, String dueDate, String paidStatus, Connection conn) {
//        this.citationNumber = citationNumber;
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.color = color;
        this.startDate = startDate;
        this.lotName = lotName;
        this.citationTime = citationTime;
        this.violationCategory = violationCategory;
        this.fee = fee;
        this.dueDate = dueDate;
        this.paidStatus = paidStatus;
        this.conn = conn;
    }

    public Citation(Connection conn, String citationNumber) {
        // TODO Auto-generated constructor stub

        this.conn = conn;
        this.citationNumber = citationNumber;
    }

    void PayCitation(String citationNumber){
        PreparedStatement stmt;
        ResultSet rs=null;
        try{
            stmt=this.conn.prepareStatement("UPDATE Citation "
                    + "set PaidStatus =?" +
                    " WHERE CitationNumber =?");
            stmt.setString(1,"PAID");
            stmt.setString(2,citationNumber);
            stmt.executeUpdate();
            Notification notify = new Notification(citationNumber, licenseNumber, violationCategory, startDate, fee, dueDate, conn);
            notify.DeleteNotification(citationNumber);
        } catch(SQLException e) {
            System.out.println("Citation couldn't be paid" + e.getMessage());
        }
    }


    void IssueCitation(){
        PreparedStatement stmt;
        citationNumber= getRandomCitation();
        try {
            stmt=this.conn.prepareStatement("INSERT INTO Citation "
                    + "(CitationNumber,LicenseNumber, Model,Color,StartDate,LotName,CitationTime,ViolationCategory,Fee,DueDate,PaidStatus) "
                    + "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?,?,?)");
            stmt.setString(1, citationNumber);
            stmt.setString(2, licenseNumber);
            stmt.setString(3, model);
            stmt.setString(4, color);
            stmt.setString(5, startDate);
            stmt.setString(6, lotName);
            stmt.setString(7, citationTime);
            stmt.setString(8, violationCategory);
            stmt.setInt(9, fee);
            stmt.setString(10, dueDate);
            stmt.setString(11, paidStatus);
            stmt.executeUpdate();

            System.out.println("Citation issued Successfully");
            Notification notify = new Notification(citationNumber, licenseNumber, violationCategory, startDate, fee, dueDate, conn);
            notify.AddNotification();
        }
        catch(SQLException e) {
            System.out.println("Citation couldn't be issued" + e.getMessage());
        }

    }

    public String getRandomCitation() {
        final String LOWER = "abcdefghijklmnopqrstuvwxyz";
        final String UPPER = LOWER.toUpperCase();
        final String NUMS = "0123456789";
        final String DATA_FOR_RANDOM_STRING = LOWER + UPPER + NUMS;

        String getDateID=startDate.substring(0,2);

        String citation_id=getDateID+lotName;

        int remaining=8-citation_id.length();
        StringBuilder sb=new StringBuilder(remaining);
        for(int i=0;i<remaining;i++) {
            Random rand=new Random();
            int randomIndex=rand.nextInt(DATA_FOR_RANDOM_STRING.length());
            char randChr=DATA_FOR_RANDOM_STRING.charAt(randomIndex);
            sb.append(randChr);
        }

//	    System.out.println(permit_id+sb.toString());
        return citation_id + sb.toString();
    }
}
