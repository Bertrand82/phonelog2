package phone.crm2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import java.util.List;

public class FirstFragmentCalendar extends ListFragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendars, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListCalendars();
        initButton();
    }

    private void initButton() {
        Button button = (Button) this.getActivity().findViewById(R.id.buttonDisplayEmailSettings);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("bg2","Calendar selected : "+getApplication().getDefaultCalendar());
                if (getApplication().getDefaultCalendar()==null) {
                    alertNoCalendarSelected();
                }else {
                    UtilActivitiesCommon.openSettingsEmail_(FirstFragmentCalendar.this.getActivity());
                }
            }
        });
    }

    private ApplicationBg getApplication() {
        return (ApplicationBg) this.getActivity().getApplication();
    }

    private void initListCalendars(){
        Log.i("bg2","start list calendars2" );
        List<BgCalendar> listCalendar =this.getApplication().getListCalendars();
        Log.i("bg2","start list calendars2 "+listCalendar );
        TextView textViewNbCalendars = (TextView)this.getActivity().findViewById(R.id.comment_list_calendar_second);
         TextView textViewComment = (TextView)this.getActivity().findViewById(R.id.comment_list_calendar_second);
        Log.i("bg2",listCalendar.toString());


        CalendarsArrayAdapter  adapter =  new CalendarsArrayAdapter(this.getActivity(), listCalendar);;
        setListAdapter(adapter);
        textViewNbCalendars.setText("Size :"+listCalendar.size());
        if (listCalendar.size() == 0){
            textViewComment.setText("Please, import an account");
        }else {
            textViewComment.setText("List Calendars OK ");
        }
    }

    private void alertNoCalendarSelected() {
        AlertDialog alertDialog = new AlertDialog.Builder(this.getActivity()).create();
        alertDialog.setTitle("Choose one account");
        alertDialog.setMessage("Error "+  " \n");
        alertDialog.show();
    }
}
