package phone.crm2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;

import java.util.List;

import phone.crm2.model.AppAccount;
import phone.crm2.model.Contact;
import phone.crm2.model.Event;
import phone.crm2.model.EventCRM;
import phone.crm2.model.PhoneCall;
import phone.crm2.model.SMS;

public class PhoneCallLDetailArrayAdapter extends ArrayAdapter<Event>{

	private final String TAG = getClass().getSimpleName();
	
	private final Context context;
	private   List<Event> events;
	private String dateDay ="";
	
	
	public PhoneCallLDetailArrayAdapter(Context context, List<Event> events) {
		super(context, R.layout.item_list_phonecall, events);
		this.context = context;
		this.events = events;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.item_list_phonecall_detail, parent, false);
		
		Event event = events.get(position);
		
		TextView textViewDate = rowView.findViewById(R.id.labelDDate);
		textViewDate.setText(event.getDateAsHour());
		
		TextView textViewDay= rowView.findViewById(R.id.labelDay);
		String dateDay = event.getDateAsDay();
		if (dateDay.equals(this.dateDay)){
			textViewDay.setText("");
			textViewDay.setHeight(0);
		}else {
			textViewDay.setText(dateDay);
			this.dateDay = dateDay;
		}
		
		TextView textViewComment= rowView.findViewById(R.id.labelComment);
		if(event instanceof SMS){
			SMS sms = (SMS) event;
			textViewComment.setText(sms.getMessage());
		}else if(event instanceof PhoneCall){
			PhoneCall phoneCall = (PhoneCall) event;
			textViewComment.setText(phoneCall.getComment());
		}else if(event instanceof EventCRM){
			EventCRM eventCRM =(EventCRM) event;
			textViewComment.setText(event.getMessageText());
		}

		AwesomeTextView imageViewType = rowView.findViewById(R.id.logoType);
		UtilActivitiesCommon.setImage(event.getType(), imageViewType);

		AwesomeTextView imagePhoneOuMessage = rowView.findViewById(R.id.logoPhoneOuMessage);
		UtilActivitiesCommon.setImagePhoneOuMessage(event.getType(), imagePhoneOuMessage);
		
		return rowView;
	}



	@Override
	public void notifyDataSetChanged() {
		Log.i("bg2", "PhoneCallDetailArrayAdapter.notifyDataSetChanged  BBB events "+toStringList(events));
		super.notifyDataSetChanged();
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	private String toStringList(List<?> list){
		if (list == null){
			return null;
		}
		return " size: "+list.size();
	}

}
