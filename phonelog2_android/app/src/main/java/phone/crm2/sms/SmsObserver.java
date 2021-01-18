package phone.crm2.sms;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import phone.crm2.ApplicationBg;
import phone.crm2.UtilCalendar;
import phone.crm2.model.AppAccount;
import phone.crm2.model.Contact;
import phone.crm2.model.SMS;

public class SmsObserver extends ContentObserver {
	
	private static final String TAG = "SmsObserver";
	
	private static final int TYPE_SMS_RECEIVED=1;
	private static final int TYPE_SMS_SENT=2;
	private final ApplicationBg context;

	public SmsObserver(Context context_) {
		super(new Handler());
		this.context = ((ApplicationBg) context_.getApplicationContext());
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Log.i("bg2", "onchange selfChange :"+selfChange);
		querySMS(this.context);
	}

	@Override
	public boolean deliverSelfNotifications() {
		return true;
	}

	protected static void querySMS(ApplicationBg applicationBg_) {
		Log.w("bg40", "querySMS ");
		Uri uriSMS = Uri.parse("content://sms/");
		Cursor cur = applicationBg_.getContentResolver().query(uriSMS, null, null, null, null);
		cur.moveToNext(); // this will make it point to the first record, which
							// is the last SMS sent
		String message = cur.getString(cur.getColumnIndex("body")); // content of sms
		String number = cur.getString(cur.getColumnIndex("address")); // phone num
		long date = cur.getLong(cur.getColumnIndex("date")); // date
		String protocol = cur.getString(cur.getColumnIndex("protocol")); // protocol
		//todo is it a string or a long?
		long id = cur.getLong(cur.getColumnIndex("_id"));
		int type = cur.getInt(cur.getColumnIndex("type"));

		int person = cur.getInt(cur.getColumnIndex("person"));
	
		
		Contact contact = applicationBg_.getDb().getContact().getByNumber(number);
		if(contact == null){
			contact = new Contact();
			contact.setNumber(number);
			((ApplicationBg) applicationBg_.getApplicationContext()).getDb().getContact().insert(contact);
		}
		if(!contact.isPrivate(applicationBg_)){
		
			if (type== TYPE_SMS_RECEIVED){
				// do nothing, already doine inside phone.crm2.receivers.IncomingCallReceiver
			}else if (type == TYPE_SMS_SENT){
				Log.i(TAG,"SMS sent");
				AppAccount account = applicationBg_.getAppAccount();
				
				SMS sms = new SMS(SMS.TYPE_OUTGOING_SMS, date, contact, account);
				sms.setMessage(message);
				ApplicationBg applicationBg = (ApplicationBg) applicationBg_.getApplicationContext();
				// Insert SMS inside BDD
				Log.i(TAG,"the sms : " + sms.toString());
				
				// Write Sms in Calendar
				UtilCalendar.insertEventInSelectedCalendars(applicationBg, sms);

			}
		}
	}
}