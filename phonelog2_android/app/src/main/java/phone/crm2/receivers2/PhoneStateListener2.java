package phone.crm2.receivers2;

import android.telephony.TelephonyManager;
import android.util.Log;

import phone.crm2.ApplicationBg;

// Useless gerer par PhoneCallService
// TODO utiliser cette info pour montrer l'historique ou la dernière annotation ?
public class PhoneStateListener2 extends android.telephony.PhoneStateListener {


    public PhoneStateListener2() {
        Log.v("bg2 PhoneStateListener2 ","constructeur");

    }

    @Override
    public void onCallStateChanged(int state, String number) {
        boolean isRinging = TelephonyManager.CALL_STATE_RINGING==state;
        boolean isOffHook = TelephonyManager.CALL_STATE_OFFHOOK==state;
        boolean isIdle = TelephonyManager.CALL_STATE_IDLE==state;
        Log.v("bg2 PhoneStateListener2 ","state :"+state+"::::::::::::::::::::   Number :>"+number+"<  state " +state+"| isRinging : "+isRinging+" | isOffHook: "+isOffHook+"| isIdle : "+isIdle+"   hashCode : "+hashCode()+" ");
        if(isRinging){
            Log.i("bg2 PhoneStateListener2 ", "isRinging!  caller number : " + number);
        }else if (isOffHook){
            // Decrochage : On ne fait rien .
            // C'est gérer par le service PhoneCallService qui observe la basse de données des appels .
            Log.i("bg2 PhoneStateListener2 ", "CallManager OFFHOOK!!  caller number : " + number);

        }
    }
}

