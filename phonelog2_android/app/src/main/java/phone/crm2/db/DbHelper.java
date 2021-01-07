package phone.crm2.db;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;
public class DbHelper extends SQLiteOpenHelper {

    private static String TAG = "DbHelper";

    private static final String DATABASE_NAME = "cafe_crm.db";
    private static final int DATABASE_VERSION = 9;//12/02/2014

    private ContactTable contact;
    private AppAccountTable appAccount;
    private CalendarSelectedTable calendarsSelected;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.contact = new ContactTable(this);
        this.appAccount = new AppAccountTable(this);
        this.calendarsSelected = new CalendarSelectedTable(this);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        this.contact.onCreate(database);
        this.appAccount.onCreate(database);
        this.calendarsSelected.onCreate(database);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        this.contact.onUpgrade(db,oldVersion,newVersion);
        this.calendarsSelected.onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * @return the contact
     */
    public ContactTable getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(ContactTable contact) {
        this.contact = contact;
    }


    /**
     * @return the account
     */
    public AppAccountTable getAppAccount() {
        return appAccount;
    }

    /**
     * @param account the account to set
     */
    public void setAppAccount(AppAccountTable appAccount) {
        this.appAccount = appAccount;
    }



    public CalendarSelectedTable getCalendarsSelected() {
        return calendarsSelected;
    }


}
