package phone.crm2;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

public class ActivityListCalendars2 extends AbstractActivityCrm {

    private ApplicationBg applicationBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calendars2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        applicationBg = (ApplicationBg) getApplication();

    }


    private void alertNoCalendarSelected() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Choose one account");
        alertDialog.setMessage("Error "+  " \n");
        alertDialog.show();
    }
}
