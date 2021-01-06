package phone.trace;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;

import java.io.Serializable;

import phone.trace.model.Contact;

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
    public void perform_action_click(View view){
         Log.e("bg2","ActivityLogs2 BINGOOOOOOOE  AAA TAG: "+view.getTag());
        try {
            Contact contact = (Contact) view.getTag();
            Serializable storage = ((ApplicationBg) getApplication()).getStorage();
            UtilActivitiesCommon.displayActivityLogDetatil(this,contact,storage,true);
            View parentRow = (View) view.getParent();
            ListView listView = (ListView) parentRow.getParent();
            final int position = listView.getPositionForView(parentRow);

        }catch(Exception e){
            Log.e("bg2","Exception "+e);
        }


    }

}
