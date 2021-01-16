package phone.crm2;

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

import phone.crm2.model.Event;
import phone.crm2.model.PhoneCall;
import phone.crm2.model.SMS;

public class PhoneCallLArrayAdapter extends ArrayAdapter<Event> {

	private final String TAG = "bg2";

	private final Context context;
	private final List<Event> listEvents;

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

		TextView textViewName = rowView.findViewById(R.id.labelName);
		textViewName.setText(event.getContact().getExtra(context).getDisplayName());
		textViewName.setTag(event.getContact());


		TextView textViewNumber = rowView.findViewById(R.id.labelNumber);
		textViewNumber.setText(event.getContact().getNumber());
		textViewNumber.setTag(event.getContact());

		TextView textViewDate = rowView.findViewById(R.id.labelDDate);
		String dateAsHour  = ""+event.getDateAsHour();
		textViewDate.setText(dateAsHour);

		TextView textViewDay = rowView.findViewById(R.id.labelDay);
		String dateDay = event.getDateAsDay();
		
		if (dateDay.equals(this.dateDay__) && (position > 0)) {
			textViewDay.setText("");
			textViewDay.setHeight(0);
		} else {
			textViewDay.setText(dateDay);
			this.dateDay__ = dateDay;
		}
		

		TextView textViewComment = rowView.findViewById(R.id.labelComment);
		if (event instanceof SMS) {
			SMS sms = (SMS) event;
			textViewComment.setText(sms.getMessage());
		} else if (event instanceof PhoneCall) {
			PhoneCall phoneCall = (PhoneCall) event;
			textViewComment.setText(phoneCall.getComment());
		}

		TextView textViewPhoto = rowView.findViewById(R.id.logoPhotoText);
		ImageView imageViewPhoto = rowView.findViewById(R.id.logoPhoto);
		textViewPhoto.setTag(event.getContact());
		UtilLogoPhoto.init(context, textViewPhoto,imageViewPhoto,event.getContact());

		AwesomeTextView imageViewType = rowView.findViewById(R.id.logoType);
		UtilActivitiesCommon.setImage(event.getType(), imageViewType);

		AwesomeTextView imagePhoneOuMessage = rowView.findViewById(R.id.logoPhoneOuMessage);
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
