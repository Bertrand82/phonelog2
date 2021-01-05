package phone.trace.receivers2;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import phone.trace.ApplicationBg;
import phone.trace.model.Contact;
// Useless gerer par PhoneCallService
@Deprecated
public class PhoneStateListener2 extends android.telephony.PhoneStateListener {

    ApplicationBg applicationBg;
    public PhoneStateListener2(ApplicationBg appBg) {
        this.applicationBg = appBg;
        Log.e("bg2 PhoneStateListener2","constructeur");

    }

    @Override
    public void onCallStateChanged(int state, String number) {
        boolean isRinging = TelephonyManager.CALL_STATE_RINGING==state;
        boolean isOffHook = TelephonyManager.CALL_STATE_OFFHOOK==state;
        boolean isIdle = TelephonyManager.CALL_STATE_IDLE==state;
        Log.v("bg2 PhoneStateListener2","state :"+state+"::::::::::::::::::::   Number :>"+number+"<  state " +state+"| isRinging : "+isRinging+" | isOffHook: "+isOffHook+"| isIdle : "+isIdle+"   hashCode : "+hashCode()+" ");
        if(isRinging){
            // Lors de la sonnerie , on envoie une alerte au sol pour un suivi
            // de l'appel sur un ordi
        }else if (isOffHook){
            // Decrochage : On ne fait rien .
            // C'est gérer par le service PhoneCallService qui observe la basse de données des appels .
            Log.i("bg2 PhoneStateListener2", "CallManager OFFHOOK!!  caller number : " + number);

        }
    }
}

