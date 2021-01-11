package phone.crm2.model;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import phone.crm2.BgCalendar;

public abstract class Event implements Serializable {

	public static final int TYPE_ALERT_CALL = 0;
	public static final int TYPE_INCOMING_CALL = 1;
	public static final int TYPE_OUTGOING_CALL = 2;
	public static final int TYPE_MISSED_CALL = 3;
	public static final int TYPE_INCOMING_SMS = 4;
	public static final int TYPE_OUTGOING_SMS = 5;
	public static final int TYPE_CRM = 6;

	public static String[] TYPE_COMMENT = { "Alert Call", "Incoming Call",
			"Outgoing Call", "Missed Call", "Incoming Sms", "Outgoing Sms",
			"CRM", "", "" };

	public static final SimpleDateFormat simpleDateFormatHour = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("dd/MM/yyyy");

	public long id;
	public int type;
	protected long date;
	public Contact contact;
	public AppAccount account;
	private String bddId;
	private HashMap<BgCalendar, Long> hIds = new HashMap<BgCalendar, Long>();

	public Event() {
	}

	public Event(int type, long date, Contact contact, AppAccount account) {
		super();
		this.type = type;
		this.date = date;
		this.contact = contact;
		this.account = account;
	}

	/**
	 * @return the id
	 */
	public final long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	public String getTypeStr() {
		return TYPE_COMMENT[getType()];
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the date
	 */
	public long getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(long date) {
		this.date = date;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}
	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * @return the account
	 */
	public AppAccount getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(AppAccount account) {
		this.account = account;
	}

	public String getDateAsHour() {
		return simpleDateFormatHour.format(new Date(this.date));
	}

	public String getDateAsDay() {
		return simpleDateFormatDay.format(new Date(this.date));
	}

	public abstract String getMessageText();

	public String getBddId() {
		return bddId;
	}

	public void setBddId(String bddId) {
		this.bddId = bddId;
	}
	
	public final String toStringDigest(Context context) {
		String s="";
		s +=  getContact().getExtra(context).getDisplayName();
		s += " "+getContact().getNormalizedNumber(context);		
		s += "    " + getMessageText() + " " + getTypeStr();
		return s;
	}

	public HashMap<BgCalendar, Long> gethIds() {
		return hIds;
	}

	public void sethIds(HashMap<BgCalendar, Long> hIds) {
		this.hIds = hIds;
	}

}
