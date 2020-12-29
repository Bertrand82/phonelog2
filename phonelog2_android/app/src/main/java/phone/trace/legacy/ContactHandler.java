package phone.trace.legacy;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import phone.trace.DataBaseHandlerBg;
import phone.trace.model.Contact;

//public class ContactHandler extends SQLiteOpenHelper {
public class ContactHandler {
 
   
    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private DataBaseHandlerBg dataBaseHandlerBg;
    
    public ContactHandler( DataBaseHandlerBg dataBaseHandlerBg) {
       this.dataBaseHandlerBg =dataBaseHandlerBg;
    }
 
    // Creating Tables
  
    public void onCreate(SQLiteDatabase db) {
    	Log.i("bg", "ContactHandle OnCreate db table contact ");
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," 
        		+ KEY_NAME + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
   
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new contact
    void addContact(Contact contact) {
        SQLiteDatabase db = this.dataBaseHandlerBg.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact_LEGACY_DEPRECATED Name
        values.put(KEY_PHONE_NUMBER, contact.getNumber()); // Contact_LEGACY_DEPRECATED Phone
 
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.dataBaseHandlerBg.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                KEY_NAME, KEY_PHONE_NUMBER }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        int id_contact = Integer.parseInt(cursor.getString(0));
        String name = cursor.getString(1);
        String phone = cursor.getString(2);
        Contact contact = new Contact(id_contact,name, phone);
        // return contact
        cursor.close();
        return contact;
    }
     
    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
 
        SQLiteDatabase db = this.dataBaseHandlerBg.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }
 
    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.dataBaseHandlerBg.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE_NUMBER, contact.getNumber());
 
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
    }
 
    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.dataBaseHandlerBg.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }
 
 
    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.dataBaseHandlerBg.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }

	public HashMap<String, String> getMap() {
		Log.i("BG","getMap start");
		List<Contact> list = getAllContacts();
		HashMap<String, String> hMap = new HashMap<String, String>();
		for(Contact contact : list){
			hMap.put(contact.getNumber(),contact.getName());
		}
		Log.i("BG","getMap done "+hMap.size());
		
		return hMap;
	}

	public void addContact(String numero, String contactName) {
		addContact(new Contact(contactName,numero));
	}

	public void clear() {
		List<Contact> list = getAllContacts();
		for(Contact c : list){
			this.deleteContact(c);
		}
	}
 
}