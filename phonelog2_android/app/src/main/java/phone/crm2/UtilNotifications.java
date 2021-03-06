package phone.crm2;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import java.util.List;

import phone.crm2.model.Event;
import phone.crm2.model.PhoneCall;

public class UtilNotifications {

    private static final String TAG = "bg2 UtilNotifications" ;
    private static final String tagNotification="ptitcrm";
    public static String CHANNEL_ID_0 ="ptit-crm-notification-bg-0";
    public static String CHANNEL_ID_1____________ ="ptit-crm-notification-bg-1";

    private  NotificationManager notificationManager;
    private final ApplicationBg applicationBg;
    public UtilNotifications(ApplicationBg applicationBg){
        this.applicationBg = applicationBg;
        createNotificationChannel(applicationBg);

    }
    private  void createNotificationChannel(ApplicationBg applicationBg) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String  name = CHANNEL_ID_0;
            String description = "Notification sonnerie";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_0, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = applicationBg.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }else {
            Log.w("bg2","UtilNotification No NotificationMAnager :"+Build.VERSION.SDK_INT );
        }
    }

    public  void cancelNotification(int notificationId){
        notificationManager.cancel(tagNotification,notificationId);
    }
    /*
    Ne marche pas : ne s'affiche pas lors de la sonnerie
    J'ai essyé AlertDialog , Dialog et là Notification. MAis la notification est ecrasée par celle du telephone
    TODO
     */

    @Deprecated
    public  int notificationSonneries( String number) {
         List<Event> listEvents = UtilCalendar.getListEventByNumberAndCommentNotNull(applicationBg,applicationBg.getDefaultCalendar(),number,0);
        String displayed ;
        if(isNullOrEmpty(listEvents)){
            displayed = "No historic";
        }else {
            Event event = listEvents.get(0);
            displayed = event.getMessageText();
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(applicationBg.getApplicationContext(), CHANNEL_ID_0)
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("Ring : "+number) // title for notification
                .setContentText(displayed)// message for notification
                .setAutoCancel(true) // clear notification after click
                .setTimeoutAfter(30000)
                 .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                 ;

        Notification notification = mBuilder.build();
        int id = (int) (System.currentTimeMillis() &  0xFFFFFFFF);
        notificationManager.notify(tagNotification,id, notification);
        return id;
    }

    public  Notification notificationFinDappel( PhoneCall phoneCall) {
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
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.ic_mr_button_grey,"Comment",pendingIntent);

        Notification notification = mBuilder.build();

        int id = (int) (System.currentTimeMillis() &  0xFFFFFF);
        notificationManager.notify(tagNotification, id, notification);
        return notification;
    }

    private static boolean isNullOrEmpty(List<?> l){
        if (l== null){
            return true;
        }
        return l.isEmpty();
    }




}
