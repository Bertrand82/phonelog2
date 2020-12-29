package phone.trace.services.call;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

public class PhoneCallService extends Service{

    @Override  
    public void onCreate() {
    	 PhoneCallObserver observer = new PhoneCallObserver(this);  
         // REGISTER ContetObserver 
        
         Uri uri = android.provider.CallLog.Calls.CONTENT_URI;
         this.getContentResolver().registerContentObserver(uri, true, observer);  
    } 

    @Override
    public IBinder onBind(Intent arg0) {
         // TODO Auto-generated method stub

         return null;
    }
}
