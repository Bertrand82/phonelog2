package phone.crm2.calendar.date;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.fragment.app.FragmentManager;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTimeManager  implements DatePickerDialog.OnDateSetListener, OnTimeSetListener{

	TimePickerFragment fragmentTime;
	DatePickerFragment fragmentDate;
	View view;
	IDateTimeListener dateTimeListener;
	Calendar c = GregorianCalendar.getInstance();
	
	public DateTimeManager(View v,IDateTimeListener listener) {
		this.view = v;
		this.dateTimeListener = listener;
		fragmentTime = new TimePickerFragment();
		fragmentDate = new DatePickerFragment();
		fragmentDate.setListener(this);
		fragmentTime.setListener(this);
		
	}
	
	public void show(FragmentManager sf) {
		fragmentTime.show(sf, "timePicker");
		fragmentDate.show(sf, "datePicker");
		}

	
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
	  	c.set(Calendar.YEAR, year);
	    c.set(Calendar.MONTH, month);
	    c.set(Calendar.DAY_OF_MONTH, day);
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		alert();
	}

	private void alert() {
		if (this.dateTimeListener!= null){
			this.dateTimeListener.onDateSet(c);
		}
	}

	public void setDateTimeListener(IDateTimeListener dateTimeListener) {
		this.dateTimeListener = dateTimeListener;
	}

}
