package parkingpackage;
import java.sql.Connection;
import java.util.Random;
public class Permit {
	String licenseNumber;
	String startDate;
	String startTime;
	String expirationDate;
	String expirationTime;
	String spaceType;
	Connection conn;
	String zone;
	
	Permit(String licenseNumber, String startDate, String startTime, String expirationDate, String expirationTime, String spaceType, String zone, Connection conn) {
		this.licenseNumber = licenseNumber;
		this.startDate = startDate;
		this.expirationDate = expirationDate;
		this.spaceType = spaceType;
		this.startTime=startTime;
		this.expirationTime = expirationTime;
		this.zone=zone;
		this.conn=conn;
	}
	
	Permit(){}
	
	public String getRandomString() {
		final String LOWER = "abcdefghijklmnopqrstuvwxyz";
	    final String UPPER = LOWER.toUpperCase();
	    final String NUMS = "0123456789";
	    final String DATA_FOR_RANDOM_STRING = LOWER + UPPER + NUMS;
	    
	    String getDateID=startDate.substring(0,2);
	    
	    String permit_id=getDateID+zone;
	    
	    int remaining=8-permit_id.length(); 
	    StringBuilder sb=new StringBuilder(remaining);
	    for(int i=0;i<remaining;i++) {
	    	Random rand=new Random();
	    	int randomIndex=rand.nextInt(DATA_FOR_RANDOM_STRING.length());
	    	char randChr=DATA_FOR_RANDOM_STRING.charAt(randomIndex);
	    	sb.append(randChr);
	    }
	    
//	    System.out.println(permit_id+sb.toString());
	    return permit_id + sb.toString();
	}
	

}
