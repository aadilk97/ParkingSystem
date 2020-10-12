package parkingpackage;

public class NonVisitorPermit extends Permit {
	String univid;
	
	NonVisitorPermit(String licenseNumber, String startDate, String expirationDate, String expirationTime, String spaceType, String univid){
		super(licenseNumber, startDate, expirationDate, expirationTime, spaceType);
		this.univid = univid;
	}

}
