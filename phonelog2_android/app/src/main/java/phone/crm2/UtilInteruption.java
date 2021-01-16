package phone.crm2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import phone.crm2.model.PhoneCall;

public class UtilInteruption {

    private static final String TAG = "bg2 UtilInteruption" ;


    public static void showPhoneCallDialog(ApplicationBg applicationBg, PhoneCall phoneCall) {
        Log.i(TAG,"processPhoneCall showPhoneCallDialog debut isForeground :"+applicationBg.isForeground());

        Log.v(TAG, "processPhoneCall showAlertDialog PhoneCall :" + phoneCall);
        applicationBg.setPhoneCall(phoneCall);
        Log.v(TAG, "processPhoneCall startAlertDialog Notification ");


        Log.i(TAG,"processPhoneCall showPhoneCallDialog  isForeground :"+applicationBg.isForeground());
        if (applicationBg.isForeground()) {// Afficher l'ecran
            Log.i(TAG,"processPhoneCall showPhoneCallDialog start ACtivity");
            Intent intent =getIntent(applicationBg,phoneCall);
            applicationBg.startActivity(intent);
        }else {// Afficher une annotation + enregistrer l'appel dans le calendar
            Log.i(TAG,"processPhoneCall showPhoneCallDialog show Notification");
            recordPhoneCall(applicationBg,phoneCall);
            showCallNotification(applicationBg,phoneCall);
        }

        //SenderMail senderMAil = new SenderMail(applicationBg_,  "Phone Call "+phoneCall, "Historic ");
        //senderMAil.execute("");
    }

    private static Intent getIntent(ApplicationBg applicationBg,PhoneCall phoneCall) {
        Log.e(TAG,"getIntent : bad intent devrait etre vers Comment Fragment TODO");
        Intent intent = new Intent(applicationBg, ActivityPhoneLog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(PhoneCall.KEY_PHONE_CALL_EXTRA,phoneCall);
        return intent;
    }

    private static void recordPhoneCall(ApplicationBg applicationBg, PhoneCall phoneCall) {
        UpdateResult result = UtilCalendar.update(applicationBg, phoneCall);
        UtilEmail.sendMessage(applicationBg, phoneCall);

    }

    public static String CHANNEL_ID_0 ="ptit-crm-notification-0";

    public static void showCallNotification(ApplicationBg applicationBg,PhoneCall phoneCall) {
        NotificationManager mNotificationManager =	(NotificationManager) applicationBg.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Log.i(TAG,"processPhoneCall show Notification showPhoneCallDialog >(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)");
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_0,	"Phone Calls", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Call Notification");
            mNotificationManager.createNotificationChannel(channel);
        }else {
            Log.i(TAG,"processPhone show Notification NO Notification Manager");
        }

        Intent intent = getIntent(applicationBg,phoneCall);
        PendingIntent pendingIntent = PendingIntent.getActivity(applicationBg.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(applicationBg.getApplicationContext(), CHANNEL_ID_0)
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("Call : "+phoneCall.getNameOrNumber()) // title for notification
                .setContentText(phoneCall.getNameOrNumber())// message for notification
                .setAutoCancel(true) // clear notification after click
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_mr_button_grey,"Comment",pendingIntent);
        mNotificationManager.notify(0, mBuilder.build());
        Log.i(TAG,"processPhoneCall showPhoneCallDialog show Notification done");
    }
}
