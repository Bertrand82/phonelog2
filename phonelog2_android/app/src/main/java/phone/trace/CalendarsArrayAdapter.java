package phone.trace;

import java.util.List;

import phone.trace.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


public class CalendarsArrayAdapter extends ArrayAdapter<BgCalendar>{

	private String TAG = "bg2 "+getClass().getSimpleName();
	
	private final Context context;
	private   List<BgCalendar> listCalendars;
 
	public CalendarsArrayAdapter(Context context, List<BgCalendar> calendars) {
		super(context,R.layout.item_list_calendar, calendars);
		this.context = context;
		this.listCalendars = calendars;
	}
	
	String dateDay ="";
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView =  inflater.inflate(R.layout.item_list_calendar, parent, false);
		try {
			final BgCalendar calendar = listCalendars.get(position);

			final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.calendarSelected);
			checkBox.setChecked(calendar.isSelected());
			checkBox.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean isSelected  = checkBox.isChecked();
					calendar.setSelected(isSelected);
					Log.i("bg2"," checkBox.isChecked "+checkBox.isChecked());
					updateDb(calendar);
				}

			});
			TextView textViewName = (TextView) rowView.findViewById(R.id.displayName);
			textViewName.setText(calendar.getDisplayName());
			
			TextView textViewNumber = (TextView) rowView.findViewById(R.id.accountName);
			textViewNumber.setText(calendar.getAccountName());
			// On ne met pas 2 fois le même nom
			String ownerName = calendar.getOwnerName();
			if ((""+ownerName.trim()).equals((""+calendar.getDisplayName()).trim())){
				ownerName="";
			}
			TextView textViewDate = (TextView) rowView.findViewById(R.id.ownerName);
			textViewDate.setText(ownerName);
		} catch (Exception e) {
			Log.w("bg2"," getView Exception ",e);
		}
		return rowView;
	}

	private void updateDb(BgCalendar calendar) {
		ApplicationBg applicationBg = (ApplicationBg) context.getApplicationContext();
		applicationBg.getDb().getCalendarsSelected().update(calendar);
		if(calendar.isSelected()){
			applicationBg.setStorage(calendar);
		}
	}
	
}
