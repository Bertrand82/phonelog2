package phone.trace;

import android.content.ContentResolver;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import java.util.Calendar;
import java.util.Date;

import phone.trace.calendar.date.DateTimeManager;
import phone.trace.calendar.date.IDateTimeListener;
import phone.trace.calendar.date.UtilReminder;

public class ActivityConfirmSend extends AbstractFragmentActivity {

	private String number;
	private String contact;
	private String message;

	Button buttonSetReminder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_send);
		UtilActivitiesCommon.initButtonNavigation_(this, 0);
		Bundle b = getIntent().getExtras();
		message = b.getString(ActivityComment.KEY_MESSAGE);
		number = b.getString(ActivityComment.KEY_NUMBER);
		contact = b.getString(ActivityComment.KEY_CONTACT);
		String time = b.getString(ActivityComment.KEY_TIME);
		String type = b.getString(ActivityComment.KEY_TYPE);
		UpdateResult updateResult = (UpdateResult) b.getSerializable(ActivityComment.KEY_RESULT_CALENDAR);
		TextView textViewMessage = (TextView) findViewById(R.id.labelMessage);
		TextView textViewNumber = (TextView) findViewById(R.id.labelNumber);
		TextView textViewContact = (TextView) findViewById(R.id.labelContact);
		TextView textViewTime = (TextView) findViewById(R.id.labelTime);
		TextView textViewType = (TextView) findViewById(R.id.labelType);
		textViewMessage.setText(message);
		textViewNumber.setText(number);
		textViewContact.setText(contact);
		textViewType.setText(type);
		textViewTime.setText(time);
		TextView[] textViewsResult = new TextView[4];

		textViewsResult[0] = (TextView) findViewById(R.id.labelResult_R_1);
		textViewsResult[1] = (TextView) findViewById(R.id.labelResult_R_2);
		textViewsResult[2] = (TextView) findViewById(R.id.labelResult_R_3);
		textViewsResult[3] = (TextView) findViewById(R.id.labelResult_R_4);
		// view.setBackgroundColor(color);
		buttonSetReminder = (Button) findViewById(R.id.buttonAddReminder);
		buttonSetReminder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setReminderRequest();
			}
		});

		Button buttonLogs = (Button) findViewById(R.id.buttonCallAgain);
		buttonLogs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UtilActivitiesCommon.callNumber(ActivityConfirmSend.this, number);

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
		Log.i("bg2", "ActivityConfirmSend buttonSetReminder on click");
		IDateTimeListener listener = new IDateTimeListener() {

			@Override
			public void onDateSet(Calendar c) {
				Log.i("bg2", "SetDAte  " + c.getTime());
				setReminder(c.getTime());
			}
		};
		DateTimeManager dateTimeManager = new DateTimeManager(buttonSetReminder, listener);
		FragmentManager sf = getSupportFragmentManager();
		Log.i("bg2", "ActivityConfirmSend dateTimeManager.show");
		dateTimeManager.show(sf);
		Log.i("bg2", "ActivityConfirmSend buttonSetReminder BBBBBBBBBBBBB");
		BgCalendar bgCalendar =((ApplicationBg)this.getApplication()).getStorageCalendar();
		//UtilReminder.setReminder(bgCalendar,number,message,contact);
	}

	private void setReminder(Date date) {
		ApplicationBg applicationBg = (ApplicationBg) getApplication();

		BgCalendar bgCalendar = applicationBg.getDefaultCalendar();
		String eventStr = this.number + " " + this.contact + " " + this.message;
		ContentResolver cr = this.getContentResolver();
		UtilReminder.setReminder(cr, bgCalendar, date, eventStr);
		Log.i("bg2", "SetReminder done  ");
	}
}
