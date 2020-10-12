package parkingpackage;

public class Permit {
	String licenseNumber;
	String startDate;
	String expirationDate;
	String expirationTime;
	String spaceType;
	
	Permit(String licenseNumber, String startDate, String expirationDate, String expirationTime, String spaceType) {
		this.licenseNumber = licenseNumber;
		this.startDate = startDate;
		this.expirationDate = expirationDate;
		this.spaceType = spaceType;
		this.expirationTime = expirationTime;
	}
	
	
}
