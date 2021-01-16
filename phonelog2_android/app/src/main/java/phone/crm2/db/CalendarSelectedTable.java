package phone.crm2.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import phone.crm2.BgCalendar;

public class CalendarSelectedTable {

	private static final String TAG = "CalendarsTable";

	// Contacts table name
	public static final String TABLE_NAME = "calendar3";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_Selected = "selected";
	private static final String KEY_ownerName = "ownerName";
	private static final String KEY_accountName = "accountName";
	private static final String KEY_accountType = "accountType";
	private static final String[] ALL_KEYS = { KEY_ID, KEY_Selected, KEY_ownerName,KEY_accountName, KEY_accountType };

	// create table
		private static final String REQUEST_CREATE_ = "CREATE TABLE " + TABLE_NAME
				+ "(" + KEY_ID + " STRING PRIMARY KEY," + KEY_Selected + " INTEGER,"
				+ KEY_ownerName + " STRING, " + KEY_accountType + " STRING, "+KEY_accountName+" STRING"+ " )";

		// create table
		private static final String REQUEST_CREATE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
				+ "(" + KEY_ID + " STRING PRIMARY KEY," + KEY_Selected + " INTEGER,"
				+ KEY_ownerName + " STRING, " + KEY_accountType + " STRING,"+KEY_accountName+" STRING "+ " )";

	// 
	// create table
	private static final String REQUEST_DELETE = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	// db
	private final DbHelper dbHelper;

	public CalendarSelectedTable(DbHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public void onCreate(SQLiteDatabase database) {
		Log.i(TAG, "CalendarSelectedTable OnCreate");
		Log.i(TAG, REQUEST_CREATE_IF_NOT_EXISTS);
		database.execSQL(REQUEST_CREATE_IF_NOT_EXISTS);
	
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "OnUpgrade oldVersion: "+oldVersion+"  newVersion :"+newVersion);
		db.execSQL(REQUEST_DELETE);
		onCreate(db);
	}

	/**
	 * CRUD
	 */

	// Adding new contact
	public BgCalendar insert(BgCalendar o) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		ContentValues values = serialize(o);
		// Inserting Row
		db.insert(TABLE_NAME, null, values);
		
//		db.close(); // Closing database connection
		return o;
	}

	// Getting single contact
	public BgCalendar getById(BgCalendar bgc) {
		
		
		//
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();

		Cursor c = db.query(TABLE_NAME, ALL_KEYS, KEY_ID + "=?",
				new String[] { String.valueOf(bgc.getId()) }, null, null, null, null);

		if (c == null) {
			return null;
		}
		BgCalendar o= null;
		if (c.moveToFirst())  {		
			 o = hydrate(this.dbHelper,c);
		}
		return o;
	}

	

	
	
	

	// Getting All Contacts
	public List<BgCalendar> getAll() {
		List<BgCalendar> eventList = new ArrayList<BgCalendar>();

		SQLiteDatabase db = this.dbHelper.getReadableDatabase();

		String request = "SELECT * FROM "
				+ CalendarSelectedTable.TABLE_NAME;
				
		
		Cursor c = db.rawQuery(request, null);
		

		Log.i(TAG,"cursor size : " + c.getCount());
		Log.i(TAG,"cursor request : " + request);
		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				BgCalendar o = hydrate(this.dbHelper,c);
				// Adding contact to list
				eventList.add(o);
			} while (c.moveToNext());
		}

		// return contact list
		return eventList;
	}

	public BgCalendar update(BgCalendar calendar) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();

		
		
		ContentValues values = serialize(calendar);
		BgCalendar calendarOld  = getById(calendar);
		if (calendarOld== null){
			insert(calendar);
		} else {
		// updating row
		int result = db.update(TABLE_NAME, values, KEY_ID + " = ?",
				new String[] { calendar.getId() });

		}
		return calendar;
	}

	// Deleting single contact
	public void delete(BgCalendar o) {
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		db.delete(TABLE_NAME, KEY_ID + " = ?",
				new String[] { String.valueOf(o.getId()) });
		db.close();
	}

	/******************************
	 * 
	 * HELPER
	 * 
	 ******************************/

	public ContentValues serialize(BgCalendar o) {
		ContentValues values = new ContentValues();
		values.put(KEY_ID, o.getId());
		values.put(KEY_accountName, o.getAccountName()); 
		values.put(KEY_accountType, o.getAccountType()); 
		values.put(KEY_ownerName, o.getOwnerName()); 
		if (o.isSelected()){
			values.put(KEY_Selected, 1); 
		}else {
			values.put(KEY_Selected, 2);   
		} 
		return values;
	}
	
	public static BgCalendar hydrate(DbHelper dbHelper, Cursor c) {
		BgCalendar o = new BgCalendar();
		o.setOwnerName(c.getString(c.getColumnIndex(KEY_ownerName)));
		o.setAccountType(c.getString(c.getColumnIndex(KEY_accountType)));
		o.setAccountName(c.getString(c.getColumnIndex(KEY_accountName)));
		o.setSelected(1==c.getInt(c.getColumnIndex(KEY_Selected)));
		return o;
	}

	public void setSelectedParam(List<BgCalendar> listCalendars) {
		// Create table if doen't exist
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		db.execSQL(REQUEST_CREATE_IF_NOT_EXISTS);
		//
		for(BgCalendar calendar : listCalendars){
			BgCalendar bc = getById(calendar);
			if (bc != null){
				calendar.setSelected(bc.isSelected());
			}
		}
	}
	
}
