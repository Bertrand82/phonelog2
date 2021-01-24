package phone.crm2.receivers;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.Serializable;

import phone.crm2.ApplicationBg;
import phone.crm2.model.Contact;

public class CallManager {

	private final ApplicationBg applicationBg;
	private String number;
	private String number_Z_1;
	private String stateTelephonyManager_Z_1 = null;
	//private Contact contact;
   private final static String TAG = "bg2";

	public CallManager(ApplicationBg applicationBg_) {
		this.applicationBg = applicationBg_;
			
	}

	@Deprecated
	public void processTelephone(Context context, Intent intent) {
		String stateTelephonyManager = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		if (stateTelephonyManager == null) {
			// we do nothing
		} else if (stateTelephonyManager.equals(TelephonyManager.EXTRA_STATE_RINGING)) { // sonnerie

			// Lors de la sonnerie , on envoie une alerte au sol pour un suivi
			// de l'appel sur un ordi
			number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

		} else if (stateTelephonyManager.equals("OFFHOOK")) { // Decrochage On affiche un ecran du contact
			if (number != null) {
				//if (!stateTelephonyManager.equals(stateTelephonyManager_Z_1)) {
				if (TelephonyManager.EXTRA_STATE_RINGING.equals(stateTelephonyManager_Z_1)) {
					Contact contact = applicationBg.getDb().getContact().getByNumberOrCreate(number);
					//delayProcessCall(context,this.contact);
					processCall(context,contact);
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
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					Log.d("TAG","InterruptedException"); // Useless!
				}
				processCall(context, contact);
			}
		};
		Thread t = new Thread(runnable);
		t.start();
		
	}
	
	private void processCall( Context context,  Contact contact){
		Serializable storage = applicationBg.getStorageCalendar();
		// Bug Majeur : Masque l'ecran permettant de terminer l'appel
		
		//displayActivityLogDetatil_WithoutHistory(context, contact, storage);
	}


		

	
	private static void listTask(Context context) {
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		int sizeStack =  am.getRunningTasks(2).size();
		for(int i = 0;i < sizeStack;i++){
			//com.android.phone.InCallScreen
		    ComponentName cn = am.getRunningTasks(2).get(i).topActivity;
		}
	}
	
	public static int getPID_PHONE(Context context){
		int pid = getPID(context, "com.android.phone");
		return pid;
	}
	
	public static int getPID(Context context,String processName){
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		for(RunningAppProcessInfo rInfo : am.getRunningAppProcesses()){
			if (rInfo.processName.equals(processName)){
				return rInfo.pid;
			}
		}
		return 0;
	}
	
	public static void moveTaskPhoneToFront(Context context) {
		try {
			ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
			int pidPhone = getPID_PHONE(context);
			am.moveTaskToFront(pidPhone, ActivityManager.MOVE_TASK_WITH_HOME);
		} catch (Exception e) {
			Log.w("bg2","CallManager.moveTaskPhoneToFront Exception",e);
		}
	}
}
