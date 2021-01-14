package phone.crm2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import java.io.Serializable;

import phone.crm2.model.Contact;

public class ActivityLogs2 extends AbstractActivityCrm{

    private ApplicationBg applicationBg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        applicationBg = (ApplicationBg) getApplication();
    }

    public void perform_action_click_select_contact(View view){
         Log.e("bg2","ActivityLogs2 click   AAA TAG: "+view.getTag());
        try {
            Contact contact = (Contact) view.getTag();
            Serializable storage = ((ApplicationBg) getApplication()).getStorage();
            UtilActivitiesCommon.displayActivityLogDetail(this,contact,storage,true);


        }catch(Exception e){
            Log.e("bg2","Exception "+e);
        }
    }

}
