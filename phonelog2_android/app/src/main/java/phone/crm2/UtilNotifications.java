package phone.crm2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import phone.crm2.model.PhoneCall;

public class UtilNotifications {

    private static final String TAG = "bg2 UtilNotifications" ;
    public static String CHANNEL_ID_0 ="ptit-crm-notification-bg-0";



    public static void showPhoneCallDialog(ApplicationBg applicationBg,PhoneCall phoneCall) {
        Log.i(TAG,"processPhoneCall showPhoneCallDialog debut isForeground :"+applicationBg.isForeground());
        Log.v(TAG, "processPhoneCall showAlertDialog PhoneCall :" + phoneCall);
        NotificationManager mNotificationManager =	(NotificationManager) applicationBg.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i(TAG,"processPhoneCall show Notification showPhoneCallDialog >(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)");
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_0,	"Phone Calls", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Call Notification");
        mNotificationManager.createNotificationChannel(channel);


        Intent intent = new Intent(applicationBg, ActivityPhoneLog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(PhoneCall.KEY_PHONE_CALL_EXTRA,phoneCall);
        intent.putExtra(ActivityPhoneLog.KEY_DISPLAY,FragmentComment.class.getName());

        PendingIntent pendingIntent = PendingIntent.getActivity(applicationBg.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(applicationBg.getApplicationContext(), CHANNEL_ID_0)
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("Call : "+phoneCall.getNameOrNumber()) // title for notification
                .setContentText(phoneCall.getNameOrNumber())// message for notification
                .setAutoCancel(false) // clear notification after click
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_mr_button_grey,"Comment",pendingIntent);

        Notification notification = mBuilder.build();
        Log.i(TAG,"UtilNotifications notification notification.getTimeoutAfter :"+notification.getTimeoutAfter());
        mNotificationManager.notify(0, notification);
        Log.i(TAG,"UtilNotifications showPhoneCallDialog show Notification done");
    }
}
