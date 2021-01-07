package phone.crm2.legacy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import phone.crm2.DataBaseHandlerBg;
import phone.crm2.model.PhoneCall;

public class PhoneCallHandler {

	private static final int MAX_PHONECAL = 4000;

	// PhoneCalls table name
	private static final String TABLE_PHONE_CALL = "phoneCallsBg";

	// PhoneCalls Table Columns names

	private static final String KEY_ID = "id";

	private static final String KEY_number = "number";
	private static final String KEY_contact_id= "contact_id";
	private static final String KEY_type = "type";
	private static final String KEY_date = "date";
	private static final String KEY_duration = "duration";
	private static final String KEY_contactName = "contactName";
	private static final String KEY_comment = "comment";
	private static final String KEY_photoid = "photoid";
	private static final String KEY_message_sms = "message_sms";

	private static final String CREATE_PHONECALL_TABLE = "CREATE TABLE " + TABLE_PHONE_CALL + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_number + " TEXT," + KEY_type + " INTEGER, " + KEY_date + " LONG, " + KEY_duration + " INTEGER, " + KEY_contactName + " TEXT, " + KEY_comment + " TEXT, "+KEY_contact_id + " LONG, " +KEY_photoid+" LONG, "+KEY_message_sms+" TEXT)";

	private DataBaseHandlerBg dataBaseHandlerBg;

	public PhoneCallHandler(DataBaseHandlerBg dataBaseHandlerBg) {
		this.dataBaseHandlerBg = dataBaseHandlerBg;
		Log.i("bg", "phoneCall PhoneCallHandler construct !!! ");
	}

	// Creating Tables

	public void onCreate(SQLiteDatabase db) {
		Log.i("bg", "PhoneCallHandler OnCreate db table contact ");

		Log.i("bg", "OnCreate db table create  start!!! ");
		db.execSQL(CREATE_PHONECALL_TABLE);
		Log.i("bg", "OnCreate db table create  done!!! ");
	}

	// Upgrading database

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("bg", "onUpgrade newVersion " + newVersion + "  oldVersion " + oldVersion);
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONE_CALL);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new PhoneCall
	public void insertPhoneCall(PhoneCall phoneCall) {
		SQLiteDatabase db = this.dataBaseHandlerBg.getWritableDatabase();
		ContentValues values = getContentValues(phoneCall);
		// Inserting Row
		long id = db.insert(TABLE_PHONE_CALL, null, values);
		phoneCall.setId(id);
		db.close(); // Closing database connection
	}

	private ContentValues getContentValues(PhoneCall phoneCall) {
		ContentValues values = new ContentValues();
//		values.put(KEY_number, phoneCall.getNumber());
//		values.put(KEY_type, phoneCall.getType());
//		values.put(KEY_date, phoneCall.getDate());
//		values.put(KEY_duration, phoneCall.getDuration());
//		values.put(KEY_contactName, phoneCall.getContactName());
//		values.put(KEY_comment, phoneCall.getComment());
//		values.put(KEY_contact_id, phoneCall.getContact().get_idAsLong());
//		values.put(KEY_photoid, phoneCall.getContact().getPhotoId());
//		values.put(KEY_message_sms, phoneCall.getMessageSms());
		return values;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	PhoneCall getPhoneCall(int id) {
		SQLiteDatabase db = this.dataBaseHandlerBg.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PHONE_CALL, new String[] { KEY_ID, KEY_number, KEY_type, KEY_date, KEY_duration, KEY_contactName, KEY_comment, KEY_contact_id ,KEY_photoid}, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		PhoneCall phoneCall = getPhoneCallFromCursor(cursor);
		cursor.close();
		return phoneCall;
	}

	private PhoneCall getPhoneCallFromCursor(Cursor cursor) {
		PhoneCall phoneCall = new PhoneCall();
//		phoneCall.setId(Integer.parseInt(cursor.getString(0)));
//		phoneCall.setNumber(cursor.getString(1));
//		phoneCall.setType(cursor.getInt(2));
//		phoneCall.setDate(cursor.getLong(3));
//		phoneCall.setDuration(cursor.getInt(4));
//		phoneCall.setContactName(cursor.getString(5));
//		phoneCall.setComment(cursor.getString(6));
//		phoneCall.getContact().set_id(cursor.getLong(7));
//		phoneCall.getContact().setPhotoId(cursor.getLong(8));
//		phoneCall.setMessageSms(cursor.getString(9));
		return phoneCall;
	}

	// Getting All PhoneCalls
	public List<PhoneCall> getListPhoneCalls(int max) {
		List<PhoneCall> phoneCallList = new ArrayList<PhoneCall>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PHONE_CALL + " ORDER BY " + KEY_date + " DESC " + " LIMIT " + max;
		if (max <= 0) {
			selectQuery = "SELECT  * FROM " + TABLE_PHONE_CALL + " ORDER BY " + KEY_date + " DESC ";
		}
		SQLiteDatabase db = this.dataBaseHandlerBg.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				PhoneCall phoneCall = getPhoneCallFromCursor(cursor);
				// Adding PhoneCall to list
				phoneCallList.add(phoneCall);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// return PhoneCall list
		return phoneCallList;
	}
	
	private List<PhoneCall> getListPhoneCallsByNumber(String number, int max) {
		List<PhoneCall> phoneCallList = new ArrayList<PhoneCall>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PHONE_CALL+" WHERE "+KEY_number+"=\""+number + "\" ORDER BY " + KEY_date + " DESC " + " LIMIT " + max;
		if (max <= 0) {
			selectQuery = "SELECT  * FROM " + TABLE_PHONE_CALL +" WHERE "+KEY_number+"=\""+number + "\" ORDER BY " + KEY_date + " DESC ";
		}
		SQLiteDatabase db = this.dataBaseHandlerBg.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				PhoneCall phoneCall = getPhoneCallFromCursor(cursor);
				// Adding PhoneCall to list
				phoneCallList.add(phoneCall);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// return PhoneCall list
		return phoneCallList;
	}


	// Getting All PhoneCalls
	public List<PhoneCall> getAllPhoneCalls() {
		return getListPhoneCalls(-1);
	}

	// Updating single PhoneCall
	public int updatePhoneCall(PhoneCall phoneCall) {
		Log.i("bg", "update phonecall in db | id :" + phoneCall.getId() + " | comment :" + phoneCall.getComment());
		SQLiteDatabase db = this.dataBaseHandlerBg.getWritableDatabase();
		ContentValues values = getContentValues(phoneCall);
		// updating row

		return db.update(TABLE_PHONE_CALL, values, KEY_ID + " = ?", new String[] { String.valueOf(phoneCall.getId()) });

	}

	// Deleting single PhoneCall
	public void deletePhoneCall(PhoneCall phoneCall) {
		SQLiteDatabase db = this.dataBaseHandlerBg.getWritableDatabase();
		db.delete(TABLE_PHONE_CALL, KEY_ID + " = ?", new String[] { String.valueOf(phoneCall.getId()) });
		db.close();
	}

	public int getPhoneCallsCount_() {
		String countQuery = "SELECT Count(*) FROM " + TABLE_PHONE_CALL;
		SQLiteDatabase db = this.dataBaseHandlerBg.getReadableDatabase();
		Cursor mCount = db.rawQuery(countQuery, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public void clear() {
		List<PhoneCall> list = getAllPhoneCalls();
		for (PhoneCall pc : list) {
			this.deletePhoneCall(pc);
		}
	}

	public void checkMax() {
		int count = this.getPhoneCallsCount_();
		Log.i("bg"," start checkMax ... count : "+count+"  MAX_PHONECAL "+MAX_PHONECAL);
		
		if (count >= MAX_PHONECAL) {
			int n = MAX_PHONECAL / 2;
			String query ="DELETE FROM "+TABLE_PHONE_CALL+" WHERE "+KEY_ID+"= (SELECT "+KEY_ID+" FROM "+TABLE_PHONE_CALL+" ORDER BY "+KEY_date+" ASC LIMIT "+n+")";
					
			SQLiteDatabase db = this.dataBaseHandlerBg.getWritableDatabase();
			db.execSQL(query);
			Log.i("bg",query);
		}
	}
	
	public PhoneCall[] getPhoneCallsArray() {
		List<PhoneCall> phoneCallList = this.getListPhoneCalls(50);
		PhoneCall[] phoneCalls = new PhoneCall[phoneCallList.size()];
		phoneCallList.toArray(phoneCalls);
		return phoneCalls;
	}

	public PhoneCall[] getPhoneCallsArrayByNumber(String number) {
		List<PhoneCall> phoneCallList = this.getListPhoneCallsByNumber(number,50);
		PhoneCall[] phoneCalls = new PhoneCall[phoneCallList.size()];
		phoneCallList.toArray(phoneCalls);
		return phoneCalls;
	}

	
}