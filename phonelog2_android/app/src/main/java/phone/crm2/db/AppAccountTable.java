package phone.crm2.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import phone.crm2.model.AppAccount;

/**
 * @author antoineguiral
 *
 */
public class AppAccountTable {

	private static final String TAG = "AccountTable";
	
	// Contacts table name
	private static final String TABLE_NAME = "accounts";

	// Contacts Table Columns names
	public static final String KEY_ID = "id";
	public static final String KEY_MAIL = "mail";
	public static final String KEY_PASSWORD = "password";
	private static final String[] ALL_KEYS = { KEY_ID, KEY_MAIL, KEY_PASSWORD };
	
	// create table
	private static final String REQUEST_CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MAIL + " TEXT,"
            + KEY_PASSWORD + " TEXT)";
	
	// create table
	private static final String REQUEST_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;


	// db
	private final DbHelper dbHelper;

	public AppAccountTable(DbHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public void onCreate(SQLiteDatabase database) {
		database.execSQL(REQUEST_CREATE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(REQUEST_DELETE);
		onCreate(db);
	}

	
    /**
     * Insert a new AppAccount in local SQLite DB and return the appAccount with id
     * 
     * @param appAccount AppAccount
     * @return appAccount
     */
    public AppAccount insert(AppAccount appAccount) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_MAIL, appAccount.getMail()); // Contact_LEGACY_DEPRECATED Name
        values.put(KEY_PASSWORD, appAccount.getPassword()); // Contact_LEGACY_DEPRECATED Phone
 
        // Inserting Row
        long id = db.insert(TABLE_NAME, null, values);
        appAccount.setId(id);
        db.close(); // Closing database connection
		return appAccount;
    }
    
    
	public AppAccount getBy(String column, String value) {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
 
        Cursor c = db.query(TABLE_NAME, ALL_KEYS , column + "=?",
                new String[] { value }, null, null, null, null);
        
        if (c != null){
            c.moveToFirst();
        }
 
        AppAccount o = hydrate(c);
        // return contact
        return o;
    }
    
    
   
	
    /**
     * Delete an AppAccount
     * 
     * @param appAccount AppAccount
     */
    public void delete(AppAccount appAccount) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(appAccount.getId()) });
        db.close();
    }
	
    
    /**
     * Create a new AppAccount from SQLite cursor
     * 
     * @param cursor
     * @return appAccount
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private AppAccount hydrate(Cursor cursor) {
		AppAccount appAccount = new AppAccount();
		appAccount.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
        appAccount.setMail(cursor.getString(cursor.getColumnIndex(KEY_MAIL))); 
        appAccount.setPassword(cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
		return appAccount;
	}

}
