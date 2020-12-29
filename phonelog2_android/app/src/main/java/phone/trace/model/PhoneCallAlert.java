package phone.trace.model;

public class PhoneCallAlert extends Event {

	
	
	
	public PhoneCallAlert(int type, long date, Contact contact, AppAccount account) {
		super(type, date, contact, account);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PhoneCallAlert [type=" + type + ", date=" + date + ", contact="
				+ contact + ", account=" + account + ", getType()=" + getType()
				+ ", getDate()=" + getDate() + ", getContact()=" + getContact()
				+ ", getAccount()=" + getAccount() + ", getDateAsHour()="
				+ getDateAsHour() + ", getDateAsDay()=" + getDateAsDay()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	@Override
	public String getMessageText() {
			return "";
	}

	
	
}
