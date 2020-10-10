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
	
}
