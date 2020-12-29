package phone.trace.model;

public class EventCRM extends Event {

	
	String message ;
	
	public EventCRM() {
		super();
	}

	public EventCRM(long date,String message) {
		super(Event.TYPE_CRM, date, null, null);
		this.message = message;
	}

	public EventCRM(long time, String comment, Contact contact_) {
		this(time,comment);
		this.contact = contact_;
	}

	@Override
	public String getMessageText() {
		return message;
	}

}
