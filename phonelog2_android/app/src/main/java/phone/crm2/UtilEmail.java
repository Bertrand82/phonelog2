package phone.crm2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import phone.crm2.model.Contact;
import phone.crm2.model.Event;
import phone.crm2.model.PhoneCall;
import phone.crm2.model.SMS;

public class UtilEmail {

	public static boolean sendMessage(Context activity, PhoneCall phoneCall){
		return  sendMessage(activity, phoneCall.getContact());
	}

	public static boolean sendMessage(Context activity, Contact contact) {
		return sendMessage(activity,contact.getEmailFromContact(activity),"","");
	}

	public static boolean sendMessage(Context activity, String destinataire, String subject, String text){

			Log.i("bg2","UtilMail sendMessage mail simple 0000000");
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL  , new String[]{destinataire});
			i.putExtra(Intent.EXTRA_SUBJECT, subject);
			i.putExtra(Intent.EXTRA_TEXT   , text);
			try {
				activity.startActivity(Intent.createChooser(i, "Send mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
				Log.i("bg2","UtilMail sendMail mail simple 0000000",ex);
				Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
	}

	public static boolean sendEmail(Context activity, String destinataire, String subject, String text){

		Log.i("bg2","UtilMail sendMail mail simple v2");
		Intent i = new Intent(Intent.ACTION_SENDTO);
		i.setData(Uri.parse("mailto:")); // only email apps should handle this
		//i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{destinataire});
		i.putExtra(Intent.EXTRA_SUBJECT, subject);
		i.putExtra(Intent.EXTRA_TEXT   , text);
		try {
			activity.startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Log.i("bg2","UtilMail sendMail mail simple 0000000",ex);
			Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}



}
