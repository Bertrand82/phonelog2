package phone.crm2.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

import java.io.Serializable;

public class ContactExtra implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	public String displayName;
	// UTI is not always serializable : java.io.NotSerializableException: android.net.Uri$StringUri
	public  Uri photoUri;	
	public String normalizedNumber;
	public Long raw_contact_id;
	private Long _id;
	private String lookup_key;
	

	public ContactExtra(Context context, String phoneNumber) {
		 
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		String[] params = new String[]{PhoneLookup.DISPLAY_NAME, PhoneLookup.PHOTO_URI, PhoneLookup.NORMALIZED_NUMBER,PhoneLookup.LOOKUP_KEY,PhoneLookup._ID};
		Cursor cursor = context.getContentResolver().query(uri, params ,null,null,null);
		// TODO Caused by:android 4.2.2 java.lang.IllegalArgumentException: Invalid column raw_contact_id
		if (cursor.moveToFirst()) {
				this.displayName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
				String uriString = cursor.getString(cursor.getColumnIndex(PhoneLookup.PHOTO_URI));
				if (uriString == null){
					this.photoUri = null;
				}else {
					this.photoUri =  Uri.parse(uriString);
				}
				this.normalizedNumber = cursor.getString(cursor.getColumnIndex(PhoneLookup.NORMALIZED_NUMBER));
				
				//raw_contact_id = cursor.getInt(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
				this.lookup_key = cursor.getString(cursor.getColumnIndex(PhoneLookup.LOOKUP_KEY));
				this._id = cursor.getLong(cursor.getColumnIndex(PhoneLookup._ID));
				// Adding PhoneCall to list
		}
		cursor.close();
	}
	
	private void initRawContactId(Context context) {
		
		Cursor c =context. getContentResolver().query(RawContacts.CONTENT_URI,
		    new String[]{RawContacts._ID},
		    RawContacts.CONTACT_ID + "=?",
		    new String[]{String.valueOf(_id)}, null);
		try {
		    if (c.moveToFirst()) {
		        raw_contact_id = c.getLong(0);
		    }
		} finally {
		    c.close();
		}
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		if (displayName  == null){
			return "";
		}
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the photoUri
	 */
	public Uri getPhotoUri() {
		return photoUri;
	}

	/**
	 * @param photoUri the photoUri to set
	 */
	public void setPhotoUri(Uri photoUri) {
		this.photoUri = photoUri;
	}

	/**
	 * @return the normalizedNumber
	 */
	public String getNormalizedNumber() {
		return normalizedNumber;
	}

	/**
	 * @param normalizedNumber the normalizedNumber to set
	 */
	public void setNormalizedNumber(String normalizedNumber) {
		this.normalizedNumber = normalizedNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	

	public Long getRaw_contact_id(Context context) {
		if (raw_contact_id == null){
			if (_id == null){
				return null;
			}
			initRawContactId(context);
		}
		return raw_contact_id;
	}

	

	

	@Override
	public String toString() {
		return "ContactExtra [displayName=" + displayName + ", photoUri=" + photoUri + ", normalizedNumber=" + normalizedNumber + ", raw_contact_id=" + raw_contact_id + ", lookup_key=" + lookup_key + "]";
	}

	public void setRaw_contact_id(Long raw_contact_id) {
		this.raw_contact_id = raw_contact_id;
	}

	public String getLookup_key() {
		return lookup_key;
	}

	
	
	
	
}
