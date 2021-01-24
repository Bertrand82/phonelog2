package phone.crm2.server;

import android.util.Log;



import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import phone.crm2.model.AppAccount;
import phone.crm2.model.Contact;
import phone.crm2.model.Event;
import phone.crm2.model.PhoneCall;
import phone.crm2.model.SMS;

public class Adapter {

	public static ArrayList<NameValuePair> toNameValuePair(Contact o) {
		
		
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new ValuePair("id",""+o.getId()));
		data.add(new ValuePair("number",""+o.getNumber()));
		data.add( new ValuePair("isPrivate", ""+o.isPrivate2()));
		
		return data;
	}
	
	public static ArrayList<NameValuePair> toNameValuePair(Event event) {
		
		ArrayList<NameValuePair> data = toNameValuePair(event.getAccount());
		data.add(new ValuePair("contact_id",""+event.getContact().getId()));
		//TODO there is no name value for contact, it's legacy code not used anymore. Need to discuss about that
		data.add(new ValuePair("contactName",""+event.getContact().getName()));
		data.add(new ValuePair("number",""+event.getContact().getNumber()));
		data.add(new ValuePair("date",""+event.getDate()));
		data.add( new ValuePair("type", ""+event.getType()));
		return data;
	}
	
	public static ArrayList<NameValuePair> toNameValuePair(PhoneCall o) {
		
		ArrayList<NameValuePair> data = toNameValuePair((Event) o);
		data.add( new ValuePair("duration", ""+o.getDuration_ms()));
		data.add( new ValuePair("id", ""+o.getId()));
		data.add( new ValuePair("comment", ""+o.getComment()));
		
		return data;
	}
	
	
	public static ArrayList<NameValuePair> toNameValuePair(SMS o) {
		
		ArrayList<NameValuePair> data = toNameValuePair((Event) o );
		data.add( new ValuePair("id", ""+o.getId()));
		data.add( new ValuePair("message", ""+o.getMessage()));
		return data;
	}

	public static ArrayList<NameValuePair> toNameValuePair(AppAccount o) {
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new ValuePair("idUser",""+o.getId()));
		data.add(new ValuePair("emailUser",""+o.getMail()));
		data.add( new ValuePair("passwordUser", ""+o.getPassword()));
		data.add( new ValuePair("cryptPhrase", ""+o.getcryptPhrase()));
		
		return data;
	}
	
	public static JSONObject toJson(AppAccount appAccount) {
		JSONObject params = new JSONObject();
		try {
			params.accumulate("idUser", "" + appAccount.getId());
			params.accumulate("emailUser", appAccount.getMail());
			params.accumulate("passwordUser", appAccount.getPassword());
		}catch(Exception e){
			Log.w("bg","Exception17 ",e);
		}
		return params;
	}
	
	
}
