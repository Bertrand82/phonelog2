package phone.crm2.services.call;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog.Calls;
import android.util.Log;

import java.util.HashMap;

import phone.crm2.ActivityComment;
import phone.crm2.ApplicationBg;
import phone.crm2.BgCalendar;
import phone.crm2.UtilCalendar;
import phone.crm2.model.AppAccount;
import phone.crm2.model.Contact;
import phone.crm2.model.PhoneCall;

/**
 * Observe la base de données ou sont stockés les appels.
 */
public class PhoneCallObserver extends ContentObserver {
	
	private static String TAG = "bg2";
	private static long id_Z_1=0;
	private static long timeStart_Z_1=0;
	private static String number_Z_1;
	private static int type_Z_1;
	private ApplicationBg applicationBg_;
	private static PhoneCall phoneCall_Z_1;
	
	public PhoneCallObserver(Context context_) {
		super(new Handler());
		this.applicationBg_ = ((ApplicationBg) context_.getApplicationContext());
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		
		Log.w(TAG, "PhoneCallObserver onchange selfChange: "+selfChange);
		Log.w(TAG, "PhoneCallObserver queryPhoneCall A number_Z_1 : "+number_Z_1);
		if (selfChange){
			Log.w(TAG, "PhoneCallObserver onchange selfChange NO Processing !!  ");
			return;
		}
		// Uri uriSMS = Uri.parse("content://sms/");
		PhoneCall phoneCall = lastOutGoingPhoneCall();
		Log.w(TAG, "PhoneCallObserver queryPhoneCall B  lastOutGoingPhoneCall : " + phoneCall);
		if (phoneCall == null) {
			return;
		} 
		String number = phoneCall.getContact().getNumber();
		if (number == null){
			return;
		}
        if ((phoneCall.getId() == -1 )) {
			Log.w(TAG, "PhoneCallObserver queryPhoneCall phoneCall No valid id==-1 " + phoneCall);
        }else if ((phoneCall.getId()==id_Z_1 )) {
        	Log.w(TAG, "PhoneCallObserver same id "+phoneCall);
        }else if ((number.equals("-1") )) {
    			Log.w(TAG, "PhoneCallObserver queryPhoneCall phoneCall numbrvNo -1 " + phoneCall);
		} else if (number.equals(number_Z_1) && (phoneCall.getDate() == timeStart_Z_1) && (phoneCall.getType()==type_Z_1)) {
			Log.w(TAG, "PhoneCallObserver queryPhoneCall same number, time,  type,  processed " + phoneCall);
		} else if (number.equals(number_Z_1) && (phoneCall.getDate() == timeStart_Z_1) ) {
			Log.w(TAG, "PhoneCallObserver queryPhoneCall already processed " + phoneCall);
		} else {
			Log.w(TAG, "PhoneCallObserver queryPhoneCall ok process ");
			phoneCall.setId(0);// Si Id !0, il ne sera pas inseré. // TODO
								// Garder l'id de la table CALLS
			processPhoneCall(phoneCall);
			id_Z_1 = phoneCall.getId();
			timeStart_Z_1 = phoneCall.getDate();
			type_Z_1 = phoneCall.getType();
			number_Z_1 = number;
		}
	}

	@Override
	public boolean deliverSelfNotifications() {
		return true;
	}
	
	private void processPhoneCall(PhoneCall phoneCall){

		String number = phoneCall.getContact().getNumber();
		boolean  isPrivate = this.applicationBg_.getDb().getContact().isPrivateByNumber(number);
		Log.i(TAG,"PhoneCallObserver processPhoneCall number :"+number+" isPrivate :"+isPrivate);
		phoneCall.getContact().setPrivate(isPrivate);
		long age = System.currentTimeMillis()  -(phoneCall.getDate()+phoneCall.getDuration_ms());
		if (age >60L*1000L){
			Log.i(TAG,"PhoneCallObserver processPhoneCall Fausse Alerte");
			// C'est un bug. Il ya des fausses  alertes parfois
		} else if (phoneCall.getContact().isPrivate(this.applicationBg_)) {
			Log.i(TAG,"PhoneCallObserver processPhoneCall isPrivate");
		} else if (phoneCall.equals2(phoneCall_Z_1)) {
			Log.i(TAG,"PhoneCallObserver processPhoneCall equals Z_1");
		}else{
			//applicationBg_.getDb().getPhoneCall().insert(phoneCall);
			HashMap<BgCalendar,  Long> hIds = UtilCalendar.insertEventInSelectedCalendars(applicationBg_, phoneCall);
			long id = hIds.get(applicationBg_.getStorageCalendar());
			phoneCall.setId(id);
			phoneCall.sethIds(hIds);
			
			showPhoneCallDialog_( phoneCall);
		}
	}
	
	

	private  PhoneCall lastOutGoingPhoneCall() {
		Cursor cursor = applicationBg_.getContentResolver().query(
				Calls.CONTENT_URI, null, null, null,
				Calls.DATE + " DESC");
		
		Calls.getLastOutgoingCall(applicationBg_); 
		int calls_id = cursor.getColumnIndex(Calls._ID);
		
		int numberColumn = cursor.getColumnIndex(Calls.NUMBER);
		int dateColumn = cursor.getColumnIndex(Calls.DATE);
		// type can be: Incoming, Outgoing or Missed
		int typeColumn = cursor.getColumnIndex(Calls.TYPE);
		int durationColumn = cursor.getColumnIndex(Calls.DURATION);
		// Will hold the calls, available to the cursor
		cursor.moveToFirst();
		
		String number = cursor.getString(numberColumn);
		long date = cursor.getLong(dateColumn);
		int type = cursor.getInt(typeColumn);
		int duration = cursor.getInt(durationColumn);
		
		Contact contact = applicationBg_.getDb().getContact().getByNumber(number);
		if(contact == null){
			contact = new Contact();
			contact.setNumber(number);
			applicationBg_.getDb().getContact().insert(contact);
		}
		
		AppAccount account = applicationBg_.getAppAccount();
		
		PhoneCall phoneCall = new PhoneCall(type, date, contact, account);
		phoneCall.setDuration_ms(duration*1000);
		phoneCall.setId(calls_id);
		cursor.close();
		return phoneCall;
	}
	
	private void showPhoneCallDialog_( PhoneCall phoneCall) {
		Log.i(TAG,"processPhoneCall showPhoneCallDialog debut isForeground :"+applicationBg_.isForeground());
		phoneCall_Z_1=phoneCall;
		Log.v(TAG, "processPhoneCall showAlertDialog PhoneCall :" + phoneCall);
		this.applicationBg_.setPhoneCall(phoneCall);
		Log.v(TAG, "processPhoneCall startAlertDialog NotificationActivated : "+applicationBg_.getNotificationActivated());
		if (applicationBg_.getNotificationActivated()){

			Log.i(TAG,"processPhoneCall showPhoneCallDialog startActivity isForeground :"+applicationBg_.isForeground());
			Intent intent = new Intent(applicationBg_, ActivityComment.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			applicationBg_.startActivity(intent);
		}else {
			Log.v(TAG, "processPhoneCall No Phone AlertDialog ActivityComment No Activated");
		}
		//SenderMail senderMAil = new SenderMail(applicationBg_,  "Phone Call "+phoneCall, "Historic ");
		//senderMAil.execute("");
	}
	
	
}