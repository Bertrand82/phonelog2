package phone.trace;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import java.util.List;
@Deprecated
public class ActivityListCalendars___OLD extends AbstractListActivityCrm {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calendars);
        Log.i("bg2","start list calendars2" );
        List<BgCalendar> listCalendar =((ApplicationBg) this.getApplication()).getListCalendars();
        Log.i("bg2","start list calendars "+listCalendar );
        TextView textViewNbCalendars = (TextView)findViewById(R.id.textViewCalendarNb);
        TextView textViewCommentCalendars = (TextView)findViewById(R.id.textViewCalendarComment);
        Log.i("bg2",listCalendar.toString());
        textViewNbCalendars.setText("Size : "+listCalendar.size());
        if (listCalendar.size() == 0){
            textViewCommentCalendars.setText("Please, import an account");
        }
        CalendarsArrayAdapter  adapter =  new CalendarsArrayAdapter(this, listCalendar);;

        setListAdapter(adapter);
        Button button = (Button) findViewById(R.id.buttonDisplayLogs);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ActivityListCalendars___OLD.this.getApplicationBg().getDefaultCalendar()==null) {
                    alertNoCalendarSelected();
                }else {
                    UtilActivitiesCommon.openSettingsEmail_(ActivityListCalendars___OLD.this);
                }
            }
        });
    }

    private void alertNoCalendarSelected() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Choose one account");
        alertDialog.setMessage("Error "+  " \n");
        alertDialog.show();
    }

}
