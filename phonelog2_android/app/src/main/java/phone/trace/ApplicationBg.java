package phone.trace;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import phone.trace.db.AppAccountTable;
import phone.trace.db.DbHelper;
import phone.trace.model.AppAccount;
import phone.trace.model.Contact;
import phone.trace.model.PhoneCall;
import phone.trace.receivers.CallManager;
import phone.trace.receivers2.PhoneStateListener2;
import phone.trace.services.call.PhoneCallService;
import phone.trace.sms.SmsObserver;
import phone.trace.sms.SmsSendService;


public class ApplicationBg extends Application  implements LifecycleObserver {


	private String TAG = "bg2";

	public static final String BG_UNKNOWN_="Unknown";
	public static final String ACCOUNT_TYPE = "com.cafe_crm.account";
	private Contact contactCurrent;
	private String phoneLastCall;
	private PhoneCall phoneCallDetail;
	private PhoneCall phoneCall;
	private AppAccount appAccount;
	private DbHelper db;
	private List<BgCalendar> listCalendars = new ArrayList<BgCalendar>();
	private long time_HOOK =0;
	private SmsObserver smsObserverBg__;
	private TelephonyManager  telephonyManager =null;
	private PhoneStateListener2 phoneListener=null;
	private boolean isForeground= false;

	private CallManager callManager ;

	private BgCalendar storage ;


	public ApplicationBg() {

		super();
		this.callManager=new CallManager(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
		this.db = new DbHelper(this);
		this.telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		phoneListener = new PhoneStateListener2(this);
		// Register listener for LISTEN_CALL_STATE
		telephonyManager.listen(phoneListener, PhoneStateListener2.LISTEN_CALL_STATE);
		ComponentName componentNameSms = startService(new Intent(this, SmsSendService.class));
		ComponentName componentNamePhoneCall =startService(new Intent(this, PhoneCallService.class));
		Log.i(TAG,"PhoneCallService started   componentNamePhoneCall :"+componentNamePhoneCall);
		// Recupere la liste des Calendar du Telephone
		UtilCalendar.runQueryListCalendar(listCalendars, getContentResolver());
		Log.i(TAG,"listCalendars size : "+listCalendars.size());
		// Regarde si dans cette liste des calendars sont selected
		this.db.getCalendarsSelected().setSelectedParam(this.listCalendars);
		this.initStoragePreference();

	}
	private void initStoragePreference() {
		try {
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
			String calendarSelectedStr =   sharedPrefs.getString("list_calendars", "000");
			Boolean modeDebug =   sharedPrefs.getBoolean("mode_debug", false);

			Log.i(TAG,"sharedPrefs CalendarSelected  :"+calendarSelectedStr);
			if (calendarSelectedStr == null){
				calendarSelectedStr="";
			}
			for(BgCalendar bgCalendar : this.getListCalendars()){
				if (bgCalendar.isSelected()){
					storage = bgCalendar;
				}
				if (calendarSelectedStr.equals(bgCalendar.toString())){
					bgCalendar.setSelected(true);
					return;
				}
			}

		} catch (Exception e) {
			Log.e(TAG," initStoragePreference Exception "+e.getMessage());
		}finally{
			Log.i(TAG," initStoragePreference storage "+storage);
			if (storage==null){
				Log.e(TAG,"ApplicationBg initStoragePreference Storage is Null!!!!!!!");
			}
		}

	}

	@Override
	public void onTerminate() {
		if (telephonyManager!= null) {
			this.telephonyManager.listen(this.phoneListener, PhoneStateListener.LISTEN_NONE);
		}
		super.onTerminate();
	}



	/**
	 * @return the account
	 */
	public AppAccount getAppAccount() {
		if(appAccount == null){
			AccountManager accountManager = AccountManager.get(this);
			Account[] accounts = accountManager.getAccountsByType(this.ACCOUNT_TYPE);
			AppAccount appAccount = this.getDb().getAppAccount().getBy(AppAccountTable.KEY_MAIL, accounts[0].name);
			this.appAccount = appAccount;
		}
		return appAccount;
	}

	/**
	 * @param appAccount the account to set
	 */
	public void setAccount(AppAccount appAccount) {
		this.appAccount = appAccount;
	}



	/**
	 * @return the db
	 */
	public DbHelper getDb() {
		return db;
	}

	/**
	 * @param db the db to set
	 */
	public void setDb(DbHelper db) {
		this.db = db;
	}




	public String getMyPhoneNumber(){
		TelephonyManager mTelephonyMgr = (TelephonyManager)   this.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getLine1Number();
	}

	public PhoneCall getPhoneCall() {
		return phoneCall;
	}

	public void setPhoneCall(PhoneCall phoneCall) {
		this.phoneCall = phoneCall;
	}


	public String getPhoneLastCall() {
		return phoneLastCall;
	}

	public void setPhoneLastCall(String phoneLastCall) {
		this.phoneLastCall = phoneLastCall;
	}


	public PhoneCall getPhoneCallDetail() {
		return phoneCallDetail;
	}

	public void setPhoneCallDetail(PhoneCall phoneCallDetail) {
		this.phoneCallDetail = phoneCallDetail;
	}

	public List<BgCalendar> getListCalendars() {
		return listCalendars;
	}

	public List<BgCalendar> getListCalendarsSelected() {
		List<BgCalendar> list = new ArrayList<BgCalendar>();
		for(BgCalendar bgc : this.listCalendars){
			if (bgc.isSelected()){
				list.add(bgc);
			}
		}
		return list;
	}

	public void setListCalendars(List<BgCalendar> listCalendars) {
		this.listCalendars = listCalendars;
	}

	public CallManager getCallManager() {
		return callManager;
	}

	public Serializable getStorage() {
		return storage;
	}

	public BgCalendar getStorageCalendar() {
		return storage;
	}

	public void setStorage(BgCalendar storage) {
		this.storage = storage;
	}

	public void onChangeStoragePreference() {
		this.initStoragePreference();
	}

	public  BgCalendar getDefaultCalendar() {
		if (storage != null){
			return storage;
		}
		try {
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
			String calendarSelectedStr =   sharedPrefs.getString("list_calendars", "000");
			Log.i(TAG,"sharedPrefs calendarSelectedStr  :"+calendarSelectedStr);

			for(BgCalendar bgCalendar : this.getListCalendars()){
				if (calendarSelectedStr.equals(bgCalendar.toString())){
					return bgCalendar;
				}
			}
			List<BgCalendar> lSelected  = this.getListCalendarsSelected();
			if (lSelected.size()>0){
				return lSelected.get(0);
			}
			List<BgCalendar> listFulll  = this.getListCalendars();
			if (listFulll.size() > 0){
				return listFulll.get(0);
			}
			return null;
		} catch (Exception e) {
			Log.w(TAG," getDefaultCalendar "+e.getMessage(),e);
			return null;
		}finally{
			Log.w(TAG," initStoragePreference storage "+storage);
		}

	}

	public Boolean getNotificationActivated() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean notificationActivated =  sharedPrefs.getBoolean("notification_activated", true);
		return notificationActivated;
	}

	List<TraceDebug> listTraceDebug = new ArrayList<TraceDebug>();
	public void addEmailTrace(String message) {
		listTraceDebug.add(new TraceDebug(message));
		if (listTraceDebug.size()>2){
			listTraceDebug.remove(0);
		}
	}

	public String getTracesDebug() {
		if (listTraceDebug.size()==0){
			return "No Traces !!";
		}
		String s = "";
		for(int i=0;i<Math.min(1, listTraceDebug.size());i++){
			s+=" - "+listTraceDebug.get(i)+"\n";
		}
		return s;
	}



	public Contact getContactCurrent() {
		return contactCurrent;
	}

	public void setContactCurrent(Contact contactCurrent) {
		this.contactCurrent = contactCurrent;
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
	public void onAppBackgrounded() {
		//App in background
		isForeground=false;
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_START)
	public void onAppForegrounded() {
		// App in foreground
		isForeground=true;
	}

	public boolean isForeground() {
		return isForeground;
	}

	class TraceDebug{
		String message="";
		Date date = new Date();

		public TraceDebug(String message) {
			super();
			this.message = message;
		}

		@Override
		public String toString() {
			return "" + message + "\n date=" + date ;
		}


	}


}
