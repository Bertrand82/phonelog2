package phone.trace;


import android.app.Application;



public class ApplicationBg extends Application {

	
	private final String TAG = "bg2";
    
	public static final String BG_UNKNOWN_="Unknown";
	public static final String ACCOUNT_TYPE = "com.cafe_crm.account";
	private String phoneLastCall;
	private final long time_HOOK =0;


	public ApplicationBg() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	

	public Boolean getNotificationActivated_____() {
		//SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		//boolean notificationActivated =  sharedPrefs.getBoolean("notification_activated", true);
		//return notificationActivated;
		return true;
	}



}
