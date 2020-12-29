package phone.trace;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;

import java.util.List;

import phone.trace.model.AppAccount;
import phone.trace.model.Contact;
import phone.trace.model.Event;
import phone.trace.model.EventCRM;
import phone.trace.model.PhoneCall;
import phone.trace.model.SMS;

public class PhoneCallLDetailArrayAdapter extends ArrayAdapter<Event>{

	private String TAG = getClass().getSimpleName();
	
	private final Context context;
	private   List<Event> events;
	private String dateDay ="";
	
	
	public PhoneCallLDetailArrayAdapter(Context context, Contact contact, List<Event> events) {
		super(context, R.layout.item_list_phonecall, events);
		this.context = context;
		this.events = events;
	}
	

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.item_list_phonecall_detail, parent, false);
		
		Event event = events.get(position);
		
		TextView textViewDate = (TextView) rowView.findViewById(R.id.labelDDate);
		textViewDate.setText(event.getDateAsHour());
		
		TextView textViewDay= (TextView) rowView.findViewById(R.id.labelDay);
		String dateDay = event.getDateAsDay();
		if (dateDay.equals(this.dateDay)){
			textViewDay.setText("");
			textViewDay.setHeight(0);
		}else {
			textViewDay.setText(dateDay);
			this.dateDay = dateDay;
		}
		
		TextView textViewComment= (TextView) rowView.findViewById(R.id.labelComment);
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

		AwesomeTextView imageViewType = (AwesomeTextView) rowView.findViewById(R.id.logoType);
		UtilActivitiesCommon.setImage(event.getType(), imageViewType);

		AwesomeTextView imagePhoneOuMessage = (AwesomeTextView) rowView.findViewById(R.id.logoPhoneOuMessage);
		UtilActivitiesCommon.setImagePhoneOuMessage(event.getType(), imagePhoneOuMessage);
		
		return rowView;
	}

	public static String getEmailFromEventIfDifferent(ApplicationBg applicationBg, Event event) {
		if(event == null){
			return "";
		}
		AppAccount account = event.getAccount();
		if (account == null){
			return "";
		}
		String email = account.getMail();
		if (email == null){
			return "";
		}
		email = email.trim();
		if(email.equalsIgnoreCase(applicationBg.getAppAccount().getMail())) {
			return "";
		}
		return email;
	}




	@Override
	public void notifyDataSetChanged() {
		Log.i("bg2", "PhoneCallDetailArrayAdapter.notifyDataSetChanged  BBB");
		super.notifyDataSetChanged();
	}
	
	




	public List<Event> getEvents() {
		return events;
	}




	public void setEvents(List<Event> events) {
		this.events = events;
	}

}
