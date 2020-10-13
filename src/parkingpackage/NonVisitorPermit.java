package parkingpackage;
import java.sql.Connection;

public class NonVisitorPermit extends Permit {
	String univid;
	Connection conn;
	NonVisitorPermit(String licenseNumber, String startDate, String expirationDate, String expirationTime, String spaceType, String univid,String zone, Connection conn){
		super(licenseNumber, startDate, expirationDate, expirationTime, spaceType,zone,conn);
		this.univid = univid;
	}

}
