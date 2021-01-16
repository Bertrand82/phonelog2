package phone.crm2;

import android.content.ContentResolver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;
import java.util.Date;

import phone.crm2.calendar.date.DateTimeManager;
import phone.crm2.calendar.date.IDateTimeListener;
import phone.crm2.calendar.date.UtilReminder;


public class FragmentConfirmSend extends Fragment {

	private String number;
	private String contact;
	private String message;

	Button buttonSetReminder;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_confirm_send, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	protected void initFragmentConfirmSend(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UtilActivitiesCommon.initButtonNavigation_(this.getActivity(), 0);
		Bundle b = getArguments();
		message = b.getString(FragmentComment.KEY_MESSAGE);
		number = b.getString(FragmentComment.KEY_NUMBER);
		contact = b.getString(FragmentComment.KEY_CONTACT);
		String time = b.getString(FragmentComment.KEY_TIME);
		String type = b.getString(FragmentComment.KEY_TYPE);
		UpdateResult updateResult = (UpdateResult) b.getSerializable(FragmentComment.KEY_RESULT_CALENDAR);
		TextView textViewMessage = this.getActivity().findViewById(R.id.labelMessage);
		TextView textViewNumber = this.getActivity().findViewById(R.id.labelNumber);
		TextView textViewContact = this.getActivity().findViewById(R.id.labelContact);
		TextView textViewTime = this.getActivity().findViewById(R.id.labelTime);
		TextView textViewType = this.getActivity().findViewById(R.id.labelType);
		textViewMessage.setText(message);
		textViewNumber.setText(number);
		textViewContact.setText(contact);
		textViewType.setText(type);
		textViewTime.setText(time);
		TextView[] textViewsResult = new TextView[4];

		textViewsResult[0] = this.getActivity().findViewById(R.id.labelResult_R_1);
		textViewsResult[1] = this.getActivity().findViewById(R.id.labelResult_R_2);
		textViewsResult[2] = this.getActivity().findViewById(R.id.labelResult_R_3);
		textViewsResult[3] = this.getActivity().findViewById(R.id.labelResult_R_4);
		// view.setBackgroundColor(color);
		buttonSetReminder = this.getActivity().findViewById(R.id.buttonAddReminder);
		buttonSetReminder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setReminderRequest();
			}
		});

		Button buttonLogs = this.getActivity().findViewById(R.id.buttonCallAgain);
		buttonLogs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UtilActivitiesCommon.callNumber(FragmentConfirmSend.this.getActivity(), number);

			}
		});

		int i = 0;
		for (BgCalendar calendar : updateResult.gethResult().keySet()) {
			i++;
			Boolean rs = updateResult.gethResult().get(calendar);
			if (i < textViewsResult.length) {
				if (rs) {
					textViewsResult[i].setText("Updated in " + calendar.getDisplayName());
				} else {
					textViewsResult[i].setText("Failure in " + calendar.getDisplayName());
				}
			}
		}

	}

	private void setReminderRequest() {
		Log.i("bg2", "FragmentConfirmSend buttonSetReminder on click");
		IDateTimeListener listener = new IDateTimeListener() {

			@Override
			public void onDateSet(Calendar c) {
				Log.i("bg2", "SetDAte  " + c.getTime());
				setReminder(c.getTime());
			}
		};
		DateTimeManager dateTimeManager = new DateTimeManager(buttonSetReminder, listener);
		FragmentManager sf = this.getActivity().getSupportFragmentManager();
		Log.i("bg2", "FragmentConfirmSend dateTimeManager.show");
		dateTimeManager.show(sf);
		Log.i("bg2", "FragmentConfirmSend buttonSetReminder BBBBBBBBBBBBB");
		BgCalendar bgCalendar =((ApplicationBg)this.getActivity().getApplication()).getStorageCalendar();
		//UtilReminder.setReminder(bgCalendar,number,message,contact);
	}

	private void setReminder(Date date) {
		ApplicationBg applicationBg = (ApplicationBg) this.getActivity().getApplication();

		BgCalendar bgCalendar = applicationBg.getDefaultCalendar();
		String eventStr = this.number + " " + this.contact + " " + this.message;
		ContentResolver cr = this.getActivity().getContentResolver();
		UtilReminder.setReminder(cr, bgCalendar, date, eventStr);
		Log.i("bg2", "SetReminder done  ");
	}
}
