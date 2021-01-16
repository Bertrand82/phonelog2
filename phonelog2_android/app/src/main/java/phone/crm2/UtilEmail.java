package phone.crm2;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import phone.crm2.model.Contact;
import phone.crm2.model.PhoneCall;

public class UtilEmail {

	public static boolean sendMessage(Context activity, PhoneCall phoneCall){
		return  sendMessage(activity, phoneCall.getContact());
	}



	public static boolean sendMessage(Context activity,Contact contact) {
		Log.i("bg2","UtilMail sendMessage message simple 9999999999999999");

		//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		String destinataireMail = contact.getEmailFromContact(activity);
		if (destinataireMail==null){
			destinataireMail ="aaaa@bbb.com";
		}
		sendMessage(activity,contact.getNumber(),destinataireMail,"oooo","aaaaa");

		return true;

	}

	public static boolean sendMessage(Context activity, String phoneNumber, String destinataireEmail, String subject, String message){
		Log.i("bg2","UtilMail sendMessage phoneNumber: "+phoneNumber);
		Intent intent = new Intent(Intent.ACTION_SEND);//intent will do work of sending something
		intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{destinataireEmail});
		intent.putExtra(Intent.EXTRA_TEXT, message);//send given message
		intent.putExtra(Intent.EXTRA_SUBJECT,subject);//give the subject for your message
		intent.putExtra(Intent.EXTRA_PHONE_NUMBER,phoneNumber);//give the subject for your message
		intent.putExtra("address", phoneNumber);// Pour avoir le destinataire dans le sms
		//Intent.Extra_Text is actually a globol key
		//intent.setType("plane/text");//type of intent Manque sms
		//intent.setType("*/*");// trop de choix
		intent.setType("text/plain"); // Une dizaine de choix dont sms et whatsapp .
		//intent.setType("message/rfc822");// Plus de sms

		activity.startActivity(Intent.createChooser(intent,"Send with: "));//createChooser is a dialogBox which shows app available to send data
		return true;
	}
	public static boolean sendMessage___OLD(Context activity, String destinataire, String subject, String text){
		String phoneNumber = "+15555555555";
		Uri uri = Uri.parse("smsto:" + phoneNumber);
		Log.i("bg2","UtilMail sendMessage mail simple v2 aaaaaabbbbbbbbbbbbb");
		Intent i = new Intent(Intent.ACTION_SEND,uri);
		//i.setData(Uri.parse("mailto:")); // only email apps should handle this

		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{destinataire});
		i.putExtra(Intent.EXTRA_SUBJECT, subject);
		i.putExtra(Intent.EXTRA_TEXT   , text);
		try {
			activity.startActivity(Intent.createChooser(i, "Send ..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Log.e("bg2","UtilMail sendMail mail simple 0000000"+ex);
			Log.e("bg2","UtilMail sendMail mail simple 0000000",ex);
			Toast.makeText(activity, "There are no sender clients installed.", Toast.LENGTH_SHORT).show();
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
			Log.e("bg2","UtilMail sendMail mail simple 0000000"+ex);
			Log.e("bg2","UtilMail sendMail mail simple 0000000",ex);
			Toast.makeText(activity, "There are no sender clients installed.", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}



}
