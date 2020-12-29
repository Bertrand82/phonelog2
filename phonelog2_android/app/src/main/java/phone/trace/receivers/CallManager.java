package phone.trace.receivers;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.Serializable;

import phone.trace.ActivityLogDetail;
import phone.trace.ApplicationBg;
import phone.trace.model.Contact;

public class CallManager {

	private ApplicationBg applicationBg;
	private String number;
	private String number_Z_1;
	private String stateTelephonyManager_Z_1 = null;
	private Contact contact;
   private final static String TAG = "bg2";

	public CallManager(ApplicationBg applicationBg_) {
		this.applicationBg = applicationBg_;
			
	}

	public void processTelephone(Context context, Intent intent) {
		String stateTelephonyManager = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		//Log.i(TAG, "CallManager.stateTelephonyManager  :  " + stateTelephonyManager + "   Z_1 : " + stateTelephonyManager_Z_1);
		if (stateTelephonyManager == null) {
			// we do nothing
		} else if (stateTelephonyManager.equals(TelephonyManager.EXTRA_STATE_RINGING)) { // sonnerie

			// Lors de la sonnerie , on envoie une alerte au sol pour un suivi
			// de l'appel sur un ordi
			number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			Log.i(TAG, "CallManager RINGiNG  caller number : " + number+"   ");
			
		} else if (stateTelephonyManager.equals("OFFHOOK")) { // Decrochage On affiche un ecran du contact
			Log.i(TAG, "CallManager OFFHOOK!!  caller number : " + number+"   stat_Z_1 "+stateTelephonyManager_Z_1);
			if (number != null) {
				//if (!stateTelephonyManager.equals(stateTelephonyManager_Z_1)) {
				if (TelephonyManager.EXTRA_STATE_RINGING.equals(stateTelephonyManager_Z_1)) {
					Log.i(TAG,"CallManager Decrovhage Bingo !!! ");
					this.contact = applicationBg.getDb().getContact().getByNumberOrCreate(number);
					//delayProcessCall(context,this.contact);
					processCall(context,this.contact);
				}
			}
		} else if (stateTelephonyManager.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
			// On est dans l'etat Inoccupé ou attente . Est ce une transition ?
			number = null;
		}
		stateTelephonyManager_Z_1 = stateTelephonyManager;
	}
	
	private void delayProcessCall(final Context context, final Contact contact) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					long delay = 2000;
					Log.i("bg2","CallManager.delayProcess wait "+delay);
					Thread.sleep(delay);
				} catch (InterruptedException e) {
				}
				processCall(context, contact);
				Log.i("bg2","CallManager.delayProcess CCCC ");
			}
		};
		Thread t = new Thread(runnable);
		t.start();
		
	}
	
	private void processCall( Context context,  Contact contact){
		Log.i("bg2","CallManager.ProcessCall BBBB ");
		Serializable storage = applicationBg.getStorage();
		// Bug Majeur : Masque l'ecran permettant de terminer l'appel
		
		//displayActivityLogDetatil_WithoutHistory(context, contact, storage);
	}


		
	public static void displayActivityLogDetatil_WithoutHistory____DEPRECATED(Context context, Contact contact, Serializable storage) {
		Log.i("bg2", "displayActivityLogDetailWthoutHistory " + contact + "  Storage:" + storage );
		Intent intent = new Intent(context,  ActivityLogDetail.class);
		Bundle b = new Bundle();
		b.putSerializable("contact", contact);
		b.putSerializable("storage", storage);
		b.putBoolean(ActivityLogDetail.KEY_MASKABLE, true);
		intent.putExtras(b);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	private static void listTask(Context context) {
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		int sizeStack =  am.getRunningTasks(2).size();
		for(int i = 0;i < sizeStack;i++){
			//com.android.phone.InCallScreen
		    ComponentName cn = am.getRunningTasks(2).get(i).topActivity;
		    Log.d("bg2", "CallManager.ComponentName "+cn.getClassName()+" "+cn.flattenToString()+" "+cn.describeContents()+"  ");
		}
		Log.i("bg2","CallManager.listTask getRunningAppProcesses().size "+am.getRunningAppProcesses().size());
		Log.i("bg2","CallManager.listTask end ");
	}
	
	public static int getPID_PHONE(Context context){
		int pid = getPID(context, "com.android.phone");
		Log.i("bg2","CallManager.getPID_PHONE "+pid);
		return pid;
	}
	
	public static int getPID(Context context,String processName){
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		for(RunningAppProcessInfo rInfo : am.getRunningAppProcesses()){
			Log.d("bg2", "CallManager.RunningAppProcessInfo PID:"+rInfo.pid+" "+rInfo.processName);
			if (rInfo.processName.equals(processName)){
				return rInfo.pid;
			}
		}
		return 0;
	}
	
	public static void moveTaskPhoneToFront(Context context) {
		Log.i("bg2","CallManager.moveTaskPhoneToFront start");
		try {
			ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
			int pidPhone = getPID_PHONE(context);
			am.moveTaskToFront(pidPhone, ActivityManager.MOVE_TASK_WITH_HOME);
			Log.i("bg2","CallManager.moveTaskPhoneToFront done");
		} catch (Exception e) {
			Log.w("bg2","CallManager.moveTaskPhoneToFront Exception",e);
		}
	}
}
