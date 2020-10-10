package parkingpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Space extends Lots{
	int spaceNum;
	String zone;
	String type;
	Connection conn;
	
	Space(String name, int spaceNum, String zone, String type, Connection conn){
		super(name, conn);
		this.spaceNum = spaceNum;
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
	
	public void addSpace() {
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement("INSERT INTO Spaces "
					+ "(SpaceNumber, Zone, Type, Available, Name) "
					+ "VALUES (?, ?, 'regular', 'Yes', ?)"
				);
			
			stmt.setInt(1, spaceNum);
			stmt.setString(2, zone);
			stmt.setString(3, super.name);
			
			stmt.executeQuery();
			
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
	
	
	
}
