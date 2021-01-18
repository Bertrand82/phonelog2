package phone.crm2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import java.io.Serializable;

import phone.crm2.model.Contact;
import phone.crm2.model.PhoneCall;

/*
PAr defaut, le FragmentMain est displayed
 */
public class ActivityPhoneLog extends AbstractActivityCrm{

    public static String KEY_DISPLAY="displayBg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_log);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void perform_action_click_select_contact(View view){
         Log.e("bg2","ActivityPhoneLog click   AAA TAG: "+view.getTag());
        try {
            Contact contact = (Contact) view.getTag();
            Serializable storage = ((ApplicationBg) getApplication()).getStorage();
            UtilActivitiesCommon.displayActivityLogDetail(this,contact,storage,true);
        }catch(Exception e){
            Log.e("bg2","Exception "+e);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
       Bundle bundle = intent.getExtras();
       Log.i("bg2","ActivityPhoneLog onNewIntent "+bundle);
       if (bundle != null){
           String gotoFragment = bundle.getString(ActivityPhoneLog.KEY_DISPLAY);
           Log.i("bg2", "FragmentMain  gotoFragment : " + gotoFragment);
           PhoneCall phoneCall = (PhoneCall) bundle.getSerializable(PhoneCall.KEY_PHONE_CALL_EXTRA);
      }
    }

}
