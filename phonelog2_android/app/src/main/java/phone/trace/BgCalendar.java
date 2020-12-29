package phone.trace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BgCalendar implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String displayName ;
	private  String   accountName ;
	private  String    ownerName ;
	private  long calID;
	private  String accountType;
	 private boolean isSelected=false;
	 
	
	
	public BgCalendar(String displayName, String accountName, String ownerName, long calID, String accountType) {
		super();
		this.displayName = displayName;
		this.accountName = accountName;
		this.ownerName = ownerName;
		this.calID = calID;
		this.accountType = accountType;
	}

	public BgCalendar() {
		// TODO Auto-generated constructor stub
	}

	public String getDisplayName() {
		
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public long getCalID() {
		return calID;
	}
	public void setCalID(long calID) {
		this.calID = calID;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public boolean isSelected() { 
		return isSelected;  
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getId() {
		return ""+ownerName+":"+accountName+":"+accountType;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((ownerName == null) ? 0 : ownerName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BgCalendar other = (BgCalendar) obj;
		if (accountName == null) {
			if (other.accountName != null)
				return false;
		} else if (!accountName.equals(other.accountName))
			return false;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (ownerName == null) {
			if (other.ownerName != null)
				return false;
		} else if (!ownerName.equals(other.ownerName))
			return false;
		return true;
	}

	public long getEventId(long startMillis) {
		for(EventId eventId : listEventId){
			if (eventId.timeStart == startMillis){
				return eventId.eventId;
			}
		}
		return 0;
	}

	List<EventId> listEventId = new ArrayList<EventId>();
	public void setEventId(long startMillis, long eventID) {
		EventId eventId2 = new EventId(startMillis, eventID);
		listEventId.add(0,eventId2);
		while(listEventId.size() > 20){
			listEventId.remove(listEventId.size()-1);
		}
		
	}
	static private class EventId implements Serializable{
		EventId(long timeStart, long eventId){
			this.timeStart = timeStart;
			this.eventId = eventId;
		}
		long timeStart;
		long eventId;
	}
	@Override
	public String toString() {
		return "Calendar " + displayName +" "+  ownerName + "  "+ accountType;
	}
	
	
	
}
