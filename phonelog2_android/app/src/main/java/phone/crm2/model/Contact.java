package phone.crm2.model;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;

import phone.crm2.ApplicationBg;
import phone.crm2.db.ContactTable;
import phone.crm2.legacy.UtilContact;

public final class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String number;
	private Boolean isPrivate = null;
	private transient ContactExtra extra = null;
	private String name;
	// private String raw_contact_id;

	/**
	 * First Custom field in gmail contact
	 */
	private String clientId;

	public Contact(String contactName, String numero) {
		this.name = contactName;
		this.number = numero;
	}

	public Contact(long id_contact, String name2, String phone) {
		this.id = id_contact;
		this.name = name2;
		this.number = phone;
	}

	public Contact() {
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the isPrivate
	 */
	public Boolean isPrivate(ApplicationBg applicationBg) {
		try {
			if (this.isPrivate == null){
				applicationBg.getDb().getContact().getIsPrivate(this);
			}
			return isPrivate;
		} catch (Exception e) {
			Log.w("bg2","isPrivate Oups ",e);
			return false;
		}
	}
	public Boolean isPrivate2(){
		return isPrivate;
	}
	/**
	 * @param isPrivate
	 *            the isPrivate to set
	 */
	
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	

	public String getName() {
		if (name == null) {
			return "";
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNameOrNumber() {
		if ((name != null) && name.trim().length() > 0) {
			return name;
		} else {
			return number;
		}
	}

	public String getContactNumbberAndNAme() {
		if ((name != null) && name.trim().length() > 0) {
			return number + " " + name;
		} else {
			return number;
		}
	}

	public String getNumberOrNoName() {
		if ((name != null) && name.trim().length() > 0) {
			return name;
		} else {
			return number;
		}
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", number=" + number + ", isPrivate=" + isPrivate + ", extra=" + extra + ", name=" + name + ", clientId=" + clientId + "]";
	}

	public ContactExtra getExtra(Context context) {
		
		if (extra == null) {
			extra = new ContactExtra(context, number);
		}
		return extra;
	}

	public boolean isInsideContacts(Context context) {
		getExtra(context);
		if (this.extra == null) {
			return false;
		} else {
			Long raw_contact_id = this.extra.getRaw_contact_id(context);
			if (raw_contact_id == null) {
				return false;
			} else {
				return true;
			}
		}

	}

	public String getClientId(Context context) {
		if (this.clientId == null) {
			getExtra(context);
			if (this.extra == null) {
					return null;
			}else {
				Long raw_contact_id = this.extra.getRaw_contact_id(context);
				if (raw_contact_id == null) {
					// PAs de contact !!!
					return null;
				} else {
					Log.i("bg2", "Contact getClientId  raw_contact_id " + raw_contact_id);
					this.clientId = ContactTable.getCustomField3(context, raw_contact_id);
					Log.i("bg2", "Contact getClientId  raw_contact_id " + raw_contact_id+" clientId :"+clientId);
				}
			}
		}
		return this.clientId;
	}

	public String getClientId() {
		if (clientId!=null){
			return clientId+"-"+name;
		}
		if (number!=null){
			return number+"-"+name;
		}
		if (name != null){
			return name;
		}
		return "xyz";
	}

	public boolean isInContacts(Context context) {
		if (this.getExtra(context) != null) {
			if (this.extra.getRaw_contact_id(context) == null) {
				;
				return false;
			} else if (this.extra.raw_contact_id != 0) {
				return true;
			}
		}
		return false;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public String  getNormalizedNumber(Context context){
		String normalizedNumber = this.getExtra(context).getNormalizedNumber();
		if (normalizedNumber  ==null){
			normalizedNumber = number;
		}
		return normalizedNumber;
	}

	public  String getNameRemember() {
		String displayName2 =null;
		if (extra!= null){
			displayName2 = extra.getDisplayName();
		}
		if ((displayName2!= null)  && (displayName2.length() > 1)){
			return displayName2.substring(0,2);
		}else {
			String number = getNumber().trim();
			return number.substring(number.length()-2);
		}
	}



	public boolean equals2(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}

	public String getEmailFromContact(Context context){
		String email = null;
		
		ContactExtra contactExtra_ = this.getExtra(context);
		if (contactExtra_ == null) {
		} else {
			Long raw_contact_id = contactExtra_.getRaw_contact_id(context);
			if (raw_contact_id != null) {
				email = UtilContact.getEmail(raw_contact_id, context);
			}
		}
		return email;
	}

}
