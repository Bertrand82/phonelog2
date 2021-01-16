package phone.crm2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import java.io.Serializable;

import phone.crm2.model.Contact;

public class ActivityPhoneLog extends AbstractActivityCrm{


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

}
