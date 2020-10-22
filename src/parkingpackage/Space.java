package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Space extends Lots{
	int spaceNum;
	String zone;
	String type;
	Connection conn;
	
	Space(String name, int spaceNum, String zone, String type, Connection conn){
		super(name, conn);
		this.spaceNum = spaceNum;
		this.zone=zone;
		this.type = type;
		this.conn = conn;
	}
	
	Space(String name, int spaceNum, String zone, Connection conn){
		super(name, conn);
		this.spaceNum = spaceNum;
		this.zone = zone;
		this.conn = conn;
	}
	
	Space(String name, Connection conn){
		super(name, conn);
		this.conn = conn;
	}
	
	Space(String name, int spaceNum, Connection conn){
		super(name,conn);
		this.spaceNum=spaceNum;
		this.conn=conn;
	}

	public void addSpace() {
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement("INSERT INTO Spaces "
					+ "(SpaceNumber, Zone, Type, Available, Name) "
					+ "VALUES (?, ?, 'Regular', 'Yes', ?)"
				);
			
			stmt.setInt(1, spaceNum);
			stmt.setString(2, zone);
			stmt.setString(3, super.name);
			
			stmt.executeUpdate();
			stmt.close();
			
			
		} catch(SQLException e) {
			System.out.println("Failed to add space to lot " + e.getMessage());
		}	
	}
	
	public void addTypeToSpace() {
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement("UPDATE Spaces "
					+ "SET Type = ? "
					+ "WHERE Name = ? AND SpaceNumber = ?"
				);
			
			stmt.setString(1, this.type);
			stmt.setString(2, this.name);
			stmt.setInt(3, this.spaceNum);
			
			System.out.println(this.type + " " + this.name + " " + this.spaceNum);
			
			stmt.executeUpdate();
			System.out.println("Type added to space successfully");
			
		} catch(SQLException e) {
			System.out.println("Failed to add type to zone " + e.getMessage());
		}
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setZone(String zone) {
		this.zone = zone;
	}
	
	public void setSpaceNum(int spaceNum) {
		this.spaceNum = spaceNum;
	}
	
	public String isSpaceAvailableVisitor() {
		PreparedStatement stmt;
		String avail="";
		String getZone="";
		String getType="";
		try {
			stmt=this.conn.prepareStatement("SELECT * from Spaces "
					+ "WHERE Name=? AND SpaceNumber=?");
			stmt.setString(1, super.name);
			stmt.setInt(2,spaceNum);
			ResultSet result=stmt.executeQuery();
			
			while(result.next()) {
				avail=result.getString("Available");
				getZone=result.getString("Zone");
				getType=result.getString("Type");
			}
		}
		catch(SQLException e) {
			System.out.println("Desired space doesn't exist " + e.getMessage());
		}
//		System.out.println(getType);
//		System.out.println(getZone);
//		System.out.println(avail);
		if (getZone.equals(zone) && avail.equals("Yes") && type.equals(getType)) {
			return "Yes";
		}
		return "No";
	}
	
	public void updateAvailable(String updateAs) {
		PreparedStatement stmt;
		try {
			stmt=this.conn.prepareStatement("UPDATE Spaces "
					+"SET Available=? "
					+"WHERE Name=? AND SpaceNumber=?");
			stmt.setString(1, updateAs);
			stmt.setString(2, super.name);
			stmt.setInt(3, spaceNum);
//			System.out.println(stmt);
			stmt.executeUpdate();
			System.out.println("Space Availability updated");
		}
		catch(SQLException e) {
			System.out.println("Failed to update space availability "+e.getMessage());
		}
	}
	
}
