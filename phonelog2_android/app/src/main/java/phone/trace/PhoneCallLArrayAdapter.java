package phone.trace;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;

import java.util.List;

import phone.trace.model.Event;
import phone.trace.model.PhoneCall;
import phone.trace.model.SMS;

public class PhoneCallLArrayAdapter extends ArrayAdapter<Event> {

	private String TAG = "bg2";

	private final Context context;
	private List<Event> listEvents;

	public PhoneCallLArrayAdapter(Context context, List<Event> listEv) {
		super(context, R.layout.item_list_phonecall, listEv);
		this.context = context;
		this.listEvents = listEv;
	}

	String dateDay__ = "00";

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position >= listEvents.size()) {
			Log.w(TAG, "PhoneCallArrayAdapter getView position>= listEvents.size !!! position: " + position + "  listEvents.size :" + listEvents.size() + " " + listEvents);
			TextView textView = new TextView(context);
			textView.setText("Error !! " + position);
			return textView;
		}
		Event event = listEvents.get(position);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.item_list_phonecall, parent, false);

		TextView textViewName = (TextView) rowView.findViewById(R.id.labelName);
		textViewName.setText(event.getContact().getExtra(context).getDisplayName());

		TextView textViewNumber = (TextView) rowView.findViewById(R.id.labelNumber);
		textViewNumber.setText(event.getContact().getNumber());

		TextView textViewDate = (TextView) rowView.findViewById(R.id.labelDDate);
		String dateAsHour  = ""+event.getDateAsHour();
		textViewDate.setText(dateAsHour);

		TextView textViewDay = (TextView) rowView.findViewById(R.id.labelDay);
		String dateDay = event.getDateAsDay();
		
		if (dateDay.equals(this.dateDay__) && (position > 0)) {
			textViewDay.setText("");
			textViewDay.setHeight(0);
		} else {
			textViewDay.setText(dateDay);
			this.dateDay__ = dateDay;
		}
		

		TextView textViewComment = (TextView) rowView.findViewById(R.id.labelComment);
		if (event instanceof SMS) {
			SMS sms = (SMS) event;
			textViewComment.setText(sms.getMessage());
		} else if (event instanceof PhoneCall) {
			PhoneCall phoneCall = (PhoneCall) event;
			textViewComment.setText(phoneCall.getComment());
		}

		TextView textViewPhoto = (TextView) rowView.findViewById(R.id.logoPhotoText);
		ImageView imageViewPhoto = (ImageView) rowView.findViewById(R.id.logoPhoto);
		UtilLogoPhoto.init(context, textViewPhoto,imageViewPhoto,event.getContact());

		AwesomeTextView imageViewType = (AwesomeTextView) rowView.findViewById(R.id.logoType);
		UtilActivitiesCommon.setImage(event.getType(), imageViewType);

		AwesomeTextView imagePhoneOuMessage = (AwesomeTextView) rowView.findViewById(R.id.logoPhoneOuMessage);
		UtilActivitiesCommon.setImagePhoneOuMessage(event.getType(), imagePhoneOuMessage);

		
		return rowView;
	}

	public List<Event> getListEvents() {
		return listEvents;
	}

	/**
	 * Notifies the attached observers that the underlying data has been changed
	 * and any View reflecting the data set should refresh itself.
	 */
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public void setDropDownViewResource(int resource) {
		super.setDropDownViewResource(resource);
	}

	
}
