package phone.crm2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import phone.crm2.legacy.ContactHandler;
import phone.crm2.legacy.PhoneCallHandler;


@Deprecated
public class DataBaseHandlerBg extends SQLiteOpenHelper {
	

	// All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 10;
 
    // Database Name
    public static final String DATABASE_NAME = "bg";

	
	private final ContactHandler contactHandler;
	private final PhoneCallHandler phoneCallHandler;
	
	public DataBaseHandlerBg(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.phoneCallHandler = new PhoneCallHandler(this);
		this.contactHandler= new ContactHandler(this);
		}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.phoneCallHandler.onCreate(db);
		this.contactHandler.onCreate(db);
		}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.phoneCallHandler.onUpgrade(db,oldVersion,newVersion);
		this.contactHandler.onUpgrade(db,oldVersion,newVersion);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.onUpgrade(db, oldVersion, newVersion);
	}

	public ContactHandler getContactHandler() {
		return contactHandler;
	}

	public PhoneCallHandler getPhoneCallHandler() {
		return phoneCallHandler;
	}

	public void checkMaxSize() {
		this.phoneCallHandler.checkMax();
	}

	
	
}
