
package phone.crm2.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

import java.util.Date;

import phone.crm2.ApplicationBg;
import phone.crm2.UtilCalendar;
import phone.crm2.model.AppAccount;
import phone.crm2.model.Contact;
import phone.crm2.model.SMS;

public class IncomingCallReciever extends BroadcastReceiver {

	private static final String TAG = "bg2 IncomingCallReciever ";

	public final static String KEY_MESSAGE_ALERT_CALL = "bg.bgfirst.AlertCall";
	public final static String KEY_NUMERO_CALLER_ = "bg.bgfirst.NumeroCaller";
	public final static String KEY_PHONE_CALL_ = "bg.PhoneCall";
	private ApplicationBg applicationBg;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("bg2","IncomingCallReciever.onReceive ----------- Start");
		this.applicationBg = (ApplicationBg) context.getApplicationContext();
		if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
			processSmsReceived(context, intent);
		}
		if ("android.intent.action.PHONE_STATE".equals(intent.getAction())) {
			this.applicationBg.getCallManager().processTelephone(context,intent);
		}
		Log.v("bg2","IncomingCallReciever.onReceive ----------- End");
	}

	

	private void processSmsReceived(Context context, Intent intent) {
		Log.i(TAG, "SMS_RECEIVED");
		Bundle bundle = intent.getExtras(); // ---get the SMS message passed
											// in---
		SmsMessage[] msgs = null;
		String number;
		if (bundle != null) {
			// ---retrieve the SMS message received---
			try {
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[pdus.length];
				for (int i = 0; i < msgs.length; i++) {
					msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					number = msgs[i].getOriginatingAddress();
					String message = msgs[i].getMessageBody();
					Log.i(TAG, "msg : " + message + " from : " + number);
					Contact contact = applicationBg.getDb().getContact().getByNumber(number);
					if(contact == null){
						contact = new Contact();
						contact.setNumber(number);
						applicationBg.getDb().getContact().insert(contact);
					}
					if (!contact.isPrivate(applicationBg)) {
						AppAccount account = applicationBg.getAppAccount();
						
						SMS sms = new SMS(SMS.TYPE_INCOMING_SMS, new Date().getTime(), contact, account);
						sms.setMessage(message);
						
						UtilCalendar.insertEventInSelectedCalendars(applicationBg, sms);

					}
				}
			} catch (Exception e) {
				Log.i(TAG, ""+e.getMessage());
			}
		}
	}

	
	
	

}