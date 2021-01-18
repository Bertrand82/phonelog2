package phone.crm2.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract.Data;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import phone.crm2.model.Contact;

public class ContactTable {

	private static final String TAG = "ContactTable";

	// Contacts table name
	private static final String TABLE_NAME = "contacts";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NUMBER = "number";
	private static final String KEY_IS_PRIVATE = "is_private";
	private static final String[] ALL_KEYS = { KEY_ID, KEY_NUMBER, KEY_IS_PRIVATE };

	// create table
	private static final String REQUEST_CREATE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMBER + " TEXT," + KEY_IS_PRIVATE + " INTEGER)";

	// create table
	private static final String REQUEST_DROP_TABLE_IF_E = "DROP TABLE IF EXISTS " + TABLE_NAME;

	// db
	private final DbHelper dbHelper;

	public ContactTable(DbHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public void onCreate(SQLiteDatabase database) {
		Log.i(TAG, "ContactTAble OnCreate");
		Log.i(TAG, "ContactTAble OnCreate " + REQUEST_CREATE);
		database.execSQL(REQUEST_CREATE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "ContactTAble OnUpgrade " + oldVersion + " " + newVersion);
		db.execSQL(REQUEST_DROP_TABLE_IF_E);
		onCreate(db);
	}

	/**
	 * CRUD
	 */

	// Adding new contact
	public Contact insert(Contact contact) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NUMBER, contact.getNumber()); // Contact_LEGACY_DEPRECATED
														// Name
		Boolean privateValue = 	contact.isPrivate2();
		if (privateValue == null){
			privateValue = false;
		}
		values.put(KEY_IS_PRIVATE, (privateValue) ? 1 : 0); // Contact_LEGACY_DEPRECATED
																	// Phone

		// Inserting Row
		long id = db.insert(TABLE_NAME, null, values);
		contact.setId(id);
		Log.i("bg2", "insert contact  done " + id);
		db.close(); // Closing database connection
		return contact;
	}

	// Getting single contact
	public Contact getById(long id) {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();

		Cursor c = db.query(TABLE_NAME, ALL_KEYS, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

		c.moveToFirst();

		Contact contact = new Contact();
		contact.setId(c.getLong(c.getColumnIndex(KEY_ID)));
		contact.setNumber(c.getString(c.getColumnIndex(KEY_NUMBER)));
		contact.setPrivate(c.getInt(c.getColumnIndex(KEY_IS_PRIVATE)) == 1);
		// return contact
		return contact;
	}

	public Contact getByNumber(String number) {

		Contact contact = null;
		Log.i("bg2", "ContactTable getByNumber contact " + number);
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();

		Cursor c = db.query(TABLE_NAME, ALL_KEYS, KEY_NUMBER + "=?", new String[] { number }, null, null, null, null);

		if (c.getCount() > 0) {

			if (c != null) {
				c.moveToFirst();
			}

			contact = new Contact();
			contact.setId(c.getLong(c.getColumnIndex(KEY_ID)));
			contact.setNumber(c.getString(c.getColumnIndex(KEY_NUMBER)));
			contact.setPrivate(c.getInt(c.getColumnIndex(KEY_IS_PRIVATE)) == 1);
		}
		// return contact
		Log.i("bg2", "ContactTable getByNumber done contact " + contact);

		return contact;
	}

	// Getting All Contacts
	public List<Contact> getAllPrivate() {
		String selection = KEY_IS_PRIVATE + "=?";
		String[] selectionArgs = { "1" };
		return getAll(selection, selectionArgs);
	}

	private List<Contact> getAll(String selection, String[] selectionArgs) {
		List<Contact> contactList = new ArrayList<Contact>();

		SQLiteDatabase db = this.dbHelper.getReadableDatabase();

		Cursor c = db.query(TABLE_NAME, ALL_KEYS, selection, selectionArgs, null, null, null, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Contact contact = new Contact();
				contact.setId(c.getLong(c.getColumnIndex(KEY_ID)));
				contact.setNumber(c.getString(c.getColumnIndex(KEY_NUMBER)));
				contact.setPrivate(c.getInt(c.getColumnIndex(KEY_IS_PRIVATE)) == 1);
				// Adding contact to list
				contactList.add(contact);
			} while (c.moveToNext());
		}

		// return contact list
		return contactList;
	}

	public Contact update(Contact contact) {
		try {
			Log.i("bg2", "update Contact start ----- contact.getId "+contact.getId());
			if (contact.getId() == 0) {
				Contact contactByNumber = this.getByNumber(contact.getNumber());
				contact.setId(contactByNumber.getId());
			}
			if (contact.getId() == 0) {
				return insert(contact);
			}

			SQLiteDatabase db = this.dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_NUMBER, contact.getNumber()); // Contact_LEGACY_DEPRECATED
			Boolean privateValue = 	contact.isPrivate2();
			if (privateValue == null){
				privateValue = false;
			}
			values.put(KEY_IS_PRIVATE, (privateValue) ? 1 : 0); // Contact_LEGACY_DEPRECATED
																		// Phone

			// updating row
			int result = db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(contact.getId()) });

			if (result != 1) {
				Log.e(TAG, "Contact No row or more than one row modified");
				throw new Exception("Contact No row or more than one row modified " + contact);
			}else {
				Log.i("bg2", "update contact done "+contact);
				
			}

		} catch (Exception e) {
			Log.e("bg2", "exception ", e);
		}
		return contact;
	}

	// Deleting single contact
	public void delete(Contact contact) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(contact.getId()) });
		db.close();
	}

	public Contact getByNumberOrCreate(String number) {
		Contact contact = getByNumber(number);
		if (contact == null) {
			contact = new Contact();
			contact.setNumber(number);
			insert(contact);
		}
		return contact;
	}

	public static String getCustomField3(Context context, long raw_contact_id) {
		Cursor cursor = null;
		// if (cursor.moveToFirst()) {
		String r = null;
		try {
			ContentResolver cr = context.getContentResolver();
			String[] projection = { Data.DATA2 };
			String selection = (Data.MIMETYPE + " = ? AND " + Data.RAW_CONTACT_ID + " = ?");
			String custom = "vnd.com.google.cursor.item/contact_user_defined_field";
			String[] selectionParams = new String[] { custom, "" + raw_contact_id };

			cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionParams, null);
			r = null;
			boolean hasResult = cursor.moveToFirst();
			Log.i("bg2", "getCustomField2 hasResult " + hasResult);
			if (hasResult) {
				// DATA1 : Nom du champs (ClienId par exemple ...)
				int columnIndex = cursor.getColumnIndex(Data.DATA2);
				r = cursor.getString(columnIndex);
				Log.i("bg2", "getCustomField raw_contact_id  :" + raw_contact_id + " custom field : " + r);
			}
		} catch (Exception e) {
			Log.w("bg2", "getCustomField2 ", e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return r;
	}

	public boolean isPrivateByNumber(String number) {
		Contact contact = getByNumber(number);
		if (contact == null) {
			return false;
		} else {
			return contact.isPrivate2();
		}
	}

	public void getIsPrivate(Contact contact) {
		Contact contact2 = getByNumber(contact.getNumber());
		contact.setPrivate(contact2.isPrivate2());
		contact.setId(contact2.getId());
	}

}
