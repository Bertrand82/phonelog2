package phone.crm2.receivers2;

import android.app.Notification;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.HashMap;

import phone.crm2.ApplicationBg;
import phone.crm2.UtilNotifications;

// Useless gerer par PhoneCallService
// TODO utiliser cette info pour montrer l'historique ou la dernière annotation ?
public class PhoneStateListener2 extends android.telephony.PhoneStateListener {
    private ApplicationBg applicationBg;

    private HashMap<String,Appel> hIdNotification=new HashMap<>();
    private final UtilNotifications utilNotifications;

    public PhoneStateListener2(ApplicationBg appBg) {
        this.applicationBg =appBg;
        this.utilNotifications = new UtilNotifications(appBg);

    }

    Notification notification;

    @Override
    public void onCallStateChanged(int state, String number) {
        boolean isRinging = TelephonyManager.CALL_STATE_RINGING==state;
        boolean isOffHook = TelephonyManager.CALL_STATE_OFFHOOK==state;
        boolean isIdle = TelephonyManager.CALL_STATE_IDLE==state;
        if(isRinging){
            if (isNotified(number)){
            }else {

                int idNotification = this.utilNotifications.notificationSonneries( number);
                setIdNotification(number, idNotification);
            }
        }else {
            int idNotification = getIdNotification(number);
            removeIdNotification(number);
            if (idNotification!= 0) {
                utilNotifications.cancelNotification( idNotification);
            }
            // Decrochage : On supprime la notification
            // C'est gérer par le service PhoneCallService qui observe la basse de données des appels .
        }
    }

    private boolean isNotified(String number) {
        Appel appel  =  hIdNotification.get(number);
        if (appel ==null){
            return false;
        }
        // Il faudrait regarder la date!

        return appel.age() < 60000L;
    }

    private void removeIdNotification(String number) {
        hIdNotification.remove(number);
    }

    private int getIdNotification(String number) {
        Appel appel  =  hIdNotification.get(number);
        if (appel ==null){
            return 0;
        }
        return appel.idNotification;
    }

    private void setIdNotification(String number, int idNotification) {
        if (hIdNotification.size()>5){
            hIdNotification.clear();
        }
        Appel appel = new Appel(number,idNotification);
        this.hIdNotification.put(number,appel);
    }

    private static class Appel{

        Appel(String number, int idNotification){
            this.number=number;
            this.idNotification=idNotification;
            this.timeNotification= System.currentTimeMillis();
        }
        String number;
        int idNotification;
        long timeNotification;


        long age(){
            return  System.currentTimeMillis() -timeNotification;
        }
    }
}

