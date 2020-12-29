package phone.trace;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import phone.trace.model.Event;
import phone.trace.model.PhoneCall;
import phone.trace.model.SMS;

public class UtilEmail {

	public static boolean sendMail(ApplicationBg context, PhoneCall phoneCall, Serializable storage) {
		if   (phoneCall== null){
			return false;
		}
		if   (phoneCall.getContact()== null){
			return false;
		}
		if   (phoneCall.getContact().getNumber()== null){
			return false;
		}
	
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean isEmaillablePreRequis =  ( ((""+sharedPrefs.getString("mailPassword","")).trim().length()>1) && phoneCall.isConsistent());
		boolean isEMaillableAllCall = (sharedPrefs.getBoolean("mode_send_email_for_all_call", false) && isEmaillablePreRequis);
		boolean isEMaillableMissCall = (sharedPrefs.getBoolean("mode_send_email_for_miss_call", false) && isEmaillablePreRequis);
		boolean isEMaillable  =  isEMaillableAllCall || isEMaillableMissCall; 
		if (isEMaillable) {
			String sujet = "Appel :"+phoneCall.toStringDigest(context);
			String body =  "Cafe-crm : "+phoneCall.toStringDigest(context)+"\n";
			 List<Event> events = new ArrayList<Event>();
			 BgCalendar bgCalendar = (BgCalendar) storage;
			List<Event> list = UtilCalendar.getListEventByContact(context, bgCalendar, phoneCall.getContact(), 0);
			events.addAll(list);
			for(Event event : events){
				body += event.getDateAsHour()+" "+event.getDateAsDay()+"  "+event.getTypeStr()+"  "+event.getMessageText()+"\n";
			}
			body+="\n http://cafe-crm.appspot.com/ \n";
			SenderMail senderMail = new SenderMail(context, sujet, body);
			senderMail.execute("");
		}else {
			Log.i("bg2", "Is Not Maillable");
		}
		return true;
	}

	public static void sendMail(ApplicationBg applicationBg, SMS sms) {
		if (sms == null)  {
			return;
		}
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationBg);
		boolean isEmaillable =  ( ((""+sharedPrefs.getString("mailPassword","")).trim().length()>1) && sms.isConsistent());
		if (isEmaillable){
			String sujet = "Cafe crm sms:"+sms.toStringDigest(applicationBg);
			String body ="CRM : \n";
			 List<Event> events = new ArrayList<Event>();
			 BgCalendar bgCalendar = (BgCalendar) applicationBg.getStorage();
			List<Event> list = UtilCalendar.getListEventByContact(applicationBg, bgCalendar, sms.getContact(), 0);
			events.addAll(list);
			for(Event event : events){
				body += event.getDateAsHour()+" "+event.getDateAsDay()+"  "+event.getTypeStr()+"  "+event.getMessageText()+"\n";
			}
			SenderMail senderMail = new SenderMail(applicationBg, sujet, body);
			senderMail.execute("");
		}
		
	}

	

}
