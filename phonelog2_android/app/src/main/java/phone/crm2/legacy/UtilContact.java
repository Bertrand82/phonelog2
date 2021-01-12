
package phone.crm2.legacy;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import phone.crm2.model.Contact;

public class UtilContact {

	private static final String TAG = "UtilContact";


	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void getNormalizedNumber(Context context, String phoneNumber) {
		class TestContact {
			String lk;
			String nn;

			@Override
			public String toString() {
				return "Lookupkey : " + lk + " normalized number : " + nn;
			}
		}
		try {

			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
			Cursor cursor = context.getContentResolver().query(uri, new String[]{PhoneLookup.LOOKUP_KEY, PhoneLookup.NORMALIZED_NUMBER}, null, null, null);


			List<TestContact> list = new ArrayList<TestContact>();
			if (cursor.moveToFirst()) {
				do {
					TestContact tc = new TestContact();
					tc.lk = cursor.getString(cursor.getColumnIndex(PhoneLookup.LOOKUP_KEY));
					tc.nn = cursor.getString(cursor.getColumnIndex(PhoneLookup.NORMALIZED_NUMBER));
					// Adding PhoneCall to list
					Log.i(TAG, tc.toString());
					list.add(tc);
				} while (cursor.moveToNext());
			}


			return;
		} catch (Exception e) {
			Log.w(TAG, "UtilContact excep", e);
//			return new Contact_LEGACY_DEPRECATED(phoneNumber, null, null, 0l, null,0l);
		}
	}


	public static void updateContact(Activity context, Contact contact) {
		if (contact == null) {
			return;
		}
		Log.i("bg2", " ------------------------------------------ updateContact  isInContacts : " + contact.isInContacts(context) + "  " + contact);

		if (!contact.isInContacts(context)) {

			insertNewContact(context, contact);
		} else {
			editContact(context, contact);
		}
	}


	// The following snippet shows how to construct and send an intent that
	// inserts a new raw contact and data:
	private static void editContact(Activity activity, Contact contact) {
		// Gets values from the UI tact phone : " + phone + " | name :
		// " + name+" lookup "+lookup);
		// A content URI pointing to the contact
		Log.i("bg2", "editContact_ " + contact);

		// Intent editIntent = new Intent(Intents.Insert.ACTION);
		Uri mSelectedContactUri = Contacts.getLookupUri(contact.getId(), "" + contact.getExtra(activity).getLookup_key());

		Intent editIntent = new Intent(Intent.ACTION_EDIT);
		editIntent.setDataAndType(mSelectedContactUri, Contacts.CONTENT_ITEM_TYPE);
		editIntent.putExtra("finishActivityOnSaveCompleted", true);

		activity.startActivity(editIntent);
	}

	private static Account getSelectedAccount_(Activity activity) {
		Account[] accounts = AccountManager.get(activity).getAccounts();
		if (accounts == null) {
			return null;
		}
		if (accounts.length == 0) {
			return null;
		}
		return accounts[0];
	}

	private static void insertNewContact(Activity activity, Contact contact) {
		// Gets values from the UI
		Log.i("bg", "insertNewContact phone : " + contact.getNumber());

		// Gets values from the UI

		// Creates a new intent for sending to the device's contacts application
		Intent insertIntent = new Intent(ContactsContract.Intents.Insert.ACTION);

		// Sets the MIME type to the one expected by the insertion activity
		insertIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

		// Sets the new contact name
		// insertIntent.putExtra(ContactsContract.Intents.Insert.NAME, name);

		// Sets the new company and job title
		insertIntent.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getNumber());
		insertIntent.putExtra("finishActivityOnSaveCompleted", true);
		/*
		 * Demonstrates adding data rows as an array list associated with the
		 * DATA key
		 */

		// Defines an array list to contain the ContentValues objects for each
		// row
		ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();

		/*
		 * Defines the raw contact row
		 */

		// Sets up the row as a ContentValues object
		ContentValues rawContactRow = new ContentValues();

		// Adds the account type and name to the row
		rawContactRow.put(ContactsContract.RawContacts.ACCOUNT_TYPE, getSelectedAccount_(activity).type);
		rawContactRow.put(ContactsContract.RawContacts.ACCOUNT_NAME, getSelectedAccount_(activity).name);
		rawContactRow.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber());
		// Adds the row to the array
		contactData.add(rawContactRow);

		/*
		 * Sets up the phone number data row
		 */

		// Sets up the row as a ContentValues object
		ContentValues phoneRow_ = new ContentValues();

		// Specifies the MIME type for this data row (all data rows must be
		// marked by their type)
		phoneRow_.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);

		// Adds the phone number and its type to the row
		phoneRow_.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber());

		// Adds the row to the array
		contactData.add(phoneRow_);

		/*
		 * Sets up the email data row
		 */

		/*
		 * Adds the array to the intent's extras. It must be a parcelable object
		 * in order to travel between processes. The device's contacts app
		 * expects its key to be Intents.Insert.DATA
		 */
		insertIntent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);

		// Send out the intent to start the device's contacts app in its add
		// contact activity.
		activity.startActivity(insertIntent);
	}


	/**
	 * @return the photo URI
	 */
	public static Uri getPhotoUri(Context ctx, String contact_id) {
		if (contact_id == null) {
			return null;
		}
		if (contact_id.trim().length() == 0) {
			return null;
		}

		Uri person = ContentUris.withAppendedId(Contacts.CONTENT_URI, Long.parseLong(contact_id));
		return Uri.withAppendedPath(person, Contacts.Photo.CONTENT_DIRECTORY);
	}




	
	

}