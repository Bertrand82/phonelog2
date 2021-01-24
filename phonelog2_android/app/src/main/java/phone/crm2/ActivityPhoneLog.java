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

    /**
     * Appelé lors d'un click sur un element de la liste
     * @param view
     */
    public void perform_action_click_select_contact(View view){
         try {
            Contact contact = (Contact) view.getTag();
            Serializable storage = ((ApplicationBg) getApplication()).getStorageCalendar();
            UtilActivitiesCommon.displayActivityLogDetail(this,contact,storage);
        }catch(Exception e){
            Log.e("bg2","Exception "+e);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
       Bundle bundle = intent.getExtras();
       if (bundle != null){
           String gotoFragment = bundle.getString(ActivityPhoneLog.KEY_DISPLAY);
           PhoneCall phoneCall = (PhoneCall) bundle.getSerializable(PhoneCall.KEY_PHONE_CALL_EXTRA);
      }
    }

}
