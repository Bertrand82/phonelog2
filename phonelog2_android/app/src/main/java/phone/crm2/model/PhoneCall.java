package phone.crm2.model;

import java.util.Date;

public class PhoneCall extends Event{

	
	private int duration_ms;
	private String comment;
	
	public PhoneCall() {
		super();
	}
	
	public PhoneCall(int type, long date, Contact contact, AppAccount account) {
		super(type, date, contact, account);
		// TODO Auto-generated constructor stub
	}
	
/*	public PhoneCall(int type, long date, Contact contact, AppAccount account,	long id, int duration, String comment) {
		super(type, date, contact, account);
		this.id = id;
		this.duration_ms = duration;
		this.comment = comment;
	}*/
	
	

	/**
	 * @return the duration 
	 */
	public int getDuration_ms() {
		return duration_ms;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration_ms(int duration) {
		this.duration_ms = duration;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "PhoneCall [duration=" + duration_ms + ", comment=" + comment + ", id=" + id + " type=" + type + ", date=" + date +" "+new Date(date)+ ", contact=" + contact + ", account=" + account + "]";
	}

	@Override
	public String getMessageText() {
		if (comment == null){
			return "";
		}
		return comment;
	}

	

	public boolean isConsistent() {
		if (this.contact == null){
			return false;
		}
		if (this.contact.getNumber()==null){
			return false;
		}
		if (this.contact.getNumber().length()<2){
			return false;
		}
		return true;
	}



	public boolean equals2(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhoneCall other = (PhoneCall) obj;
		if (duration_ms != other.duration_ms){
			return false;
		}
		if (getType() != other.getType()){
			return false;
		}
		if (Math.abs(getDate() - other.getDate())>500 ){
			return false;
		}
		return contact.equals2(other.getContact());
	}
	
	public String getNameOrNumber() {
		if (contact== null){
			return " ";
		}else {
			return contact.getContactNameOrNumber();
		}
	}
}
