package phone.trace.server;

import android.util.Log;

import phone.trace.MainActivity;
import phone.trace.model.AppAccount;
import phone.trace.model.PhoneCall;
import phone.trace.model.PhoneCallAlert;
import phone.trace.model.SMS;

public class ServerUtil {

	public static String TAG = "ServerUtil";

	public static void send_DEPRECATED(PhoneCall phoneCall) {

		Log.i(TAG, "sendPhoneCall : \n" + phoneCall);

		try {
			String url = MainActivity.SERVICE_URL_CAFE_CRM + "/log";

			Request request = new Request(Request.POST, url, Adapter.toNameValuePair(phoneCall),phoneCall.getAccount());

			send(request);
			
		} catch (Exception e) {
			Log.e(TAG, "echec ", e);
		}
	}
	
	public static void send( PhoneCallAlert phoneCallAlert) {

		Log.i(TAG, "sendPhoneCallAlert : \n" + phoneCallAlert);

		try {
			String url = MainActivity.SERVICE_URL_CAFE_CRM + "/alert";

			Request request = new Request(Request.POST, url, Adapter.toNameValuePair(phoneCallAlert), phoneCallAlert.getAccount());

			send(request);

		} catch (Exception e) {
			Log.e(TAG, "echec ", e);
		}
	}
	
	public static void send_DEPRECATED( SMS sms) {

		Log.i(TAG, "sendSMS : \n" + sms);

		try {
			String url = MainActivity.SERVICE_URL_CAFE_CRM + "/log";

			Request request = new Request(Request.POST, url, Adapter.toNameValuePair(sms), sms.getAccount());

			send(request);

		} catch (Exception e) {
			Log.e(TAG, "echec ", e);
		}
	}

	public static void send(AppAccount appAccount) {
		Log.i(TAG, "sendAppAccount : \n" + appAccount);

		try {
			String url = MainActivity.SERVICE_URL_CAFE_CRM + "/account";

			Request request = new Request(Request.POST, url, Adapter.toNameValuePair(appAccount));

			send(request);

		} catch (Exception e) {
			Log.e(TAG, "echec ", e);
		}
		
	}
	
	
	
	public static void send(Request request){
		Log.i(TAG, "send the request : ");

		try {
			
			Sender sender = new Sender();
			sender.execute(request);

		} catch (Exception e) {
			Log.e(TAG, "echec ", e);
		}
	}

}
