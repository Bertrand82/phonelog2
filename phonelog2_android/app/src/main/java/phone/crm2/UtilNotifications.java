package phone.crm2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.List;

import phone.crm2.model.Event;
import phone.crm2.model.PhoneCall;

public class UtilNotifications {

    private static final String TAG = "bg2 UtilNotifications" ;
    private static final String tagNotification="ptitcrm";
    public static String CHANNEL_ID_0 ="ptit-crm-notification-bg-0";
    public static String CHANNEL_ID_1 ="ptit-crm-notification-bg-1";
    public static void cancelNotification(ApplicationBg applicationBg,int notificationId){
        NotificationManager mNotificationManager =	(NotificationManager) applicationBg.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(tagNotification,notificationId);
    }

    public static int notificationSonneries(ApplicationBg applicationBg, String number) {
        List<Event> listEvents = UtilCalendar.getListEventByNumberAndCommentNotNull(applicationBg,applicationBg.getDefaultCalendar(),number,0);
        String displayed ;
        if(isNullOrEmpty(listEvents)){
            displayed = "No comments";
        }else {
            Event event = listEvents.get(0);
            displayed = event.getMessageText();
        }
        Log.i(TAG,"UtilNotifications notificationSonnerie debut isForeground :"+applicationBg.isForeground());
        Log.v(TAG, "UtilNotifications notificationSonnerie number :" + number);
        NotificationManager mNotificationManager =	(NotificationManager) applicationBg.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i(TAG,"UtilNotifications notificationSonnerie show Notification showPhoneCallDialog >(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)");
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_0,	"Phone Calls", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Ring Notification: display previous comment");
        mNotificationManager.createNotificationChannel(channel);


        Intent intent = new Intent(applicationBg, ActivityPhoneLog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(PhoneCall.KEY_NUMBER,number);
        intent.putExtra(ActivityPhoneLog.KEY_DISPLAY, FragmentLogsDetail.class.getName());

        PendingIntent pendingIntent = PendingIntent.getActivity(applicationBg.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(applicationBg.getApplicationContext(), CHANNEL_ID_1)
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("Call : "+number) // title for notification
                .setContentText(displayed)// message for notification
                .setAutoCancel(true) // clear notification after click
                .setTimeoutAfter(30000)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_mr_button_grey,"Comment",pendingIntent);

        Notification notification = mBuilder.build();
        Log.i(TAG,"UtilNotifications notificationSonnerie notification.getTimeoutAfter :"+notification.getTimeoutAfter());
        int id = (int) (System.currentTimeMillis() &  0xFFFFFF);
        mNotificationManager.notify(tagNotification,id, notification);
        Log.i(TAG,"UtilNotifications notificationSonnerie show Notification done");
        return id;
    }

    public static Notification notificationFinDappel(ApplicationBg applicationBg, PhoneCall phoneCall) {
        Log.i(TAG,"UtilNotifications notificationFinDappel debut isForeground :"+applicationBg.isForeground());
        Log.v(TAG, "UtilNotifications notificationFinDappel PhoneCall :" + phoneCall);
        NotificationManager mNotificationManager =	(NotificationManager) applicationBg.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i(TAG,"UtilNotifications notificationFinDappelshowPhoneCallDialog >(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)");
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
                .setTimeoutAfter(30000)
                .addAction(R.drawable.ic_mr_button_grey,"Comment",pendingIntent);

        Notification notification = mBuilder.build();
        Log.i(TAG,"UtilNotifications notificationFinDappel notification.getTimeoutAfter :"+notification.getTimeoutAfter());
        int id = (int) (System.currentTimeMillis() &  0xFFFFFF);
        mNotificationManager.notify(tagNotification, id, notification);
        Log.i(TAG,"UtilNotifications notificationFinDappel show Notification done");
        return notification;
    }

    private static boolean isNullOrEmpty(List<?> l){
        if (l== null){
            return true;
        }
        return l.isEmpty();
    }
}
