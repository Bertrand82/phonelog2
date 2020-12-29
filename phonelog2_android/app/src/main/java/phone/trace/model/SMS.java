package phone.trace.model;

public class SMS extends Event {

	
	private String message;
	
	public SMS() {
		// TODO Auto-generated constructor stub
	}

	public SMS(int type, long date, Contact contact, AppAccount account) {
		super(type, date, contact, account);
		// TODO Auto-generated constructor stub
	}

	public SMS(int type, long date, Contact contact, AppAccount account, long id,
               String message) {
		super(type, date, contact, account);
		this.id = id;
		this.message = message;
	}

	

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getMessageText() {
		return getMessage();
	}

	public boolean isConsistent() {
		if (this.message == null){
			return false;
		}
		return true;
	}

	
	
	

}
