package phone.crm2.services.call;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class PhoneCallService extends Service{
    private PhoneCallObserver observer;
    @Override  
    public void onCreate() {
        observer = new PhoneCallObserver(this);
         // REGISTER ContetObserver 
         Log.i("bg2","PhoneCallService registerContentObserver");
         Uri uri = android.provider.CallLog.Calls.CONTENT_URI;
         this.getContentResolver().registerContentObserver(uri, true, observer);
        Log.i("bg2","PhoneCallService registerContentObserver done");
    }

    @Override
    public void onDestroy() {
        if (observer != null) {
            try {
                this.getContentResolver().unregisterContentObserver(observer);
            } catch(Exception e){
                Log.i("bg2","onDestroy Unregister exception ",e);
            }
        }
    }
    @Override
    public IBinder onBind(Intent arg0) {
         return null;
    }
}
