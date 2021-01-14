package phone.crm2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapEditText;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import phone.crm2.legacy.UtilContact;
import phone.crm2.model.Contact;
import phone.crm2.model.EventCRM;


public class ActivityAddEvent extends AbstractActivityCrm {

	private Contact contact_;
	private ApplicationBg applicationBg;
	private Serializable storage;
	private TextView textViewClientId_;
	private TextView textViewContact ;
	TextView textViewDetailNumber ;
	ImageView imageViewPhoto;
	TextView textViewPhoto_;
	DatePicker datePicker;
	BootstrapEditText editText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applicationBg = (ApplicationBg) getApplication();
		contact_ = applicationBg.getContactCurrent();
		Log.i("bg2", "ActivityAddEvent " + contact_);
		setContentView(R.layout.activity_add_event);
		textViewClientId_ = (TextView) findViewById(R.id.textViewClientId22);
		textViewContact = (TextView) findViewById(R.id.labelContact2);
		textViewDetailNumber = (TextView) findViewById(R.id.labelNumber2);
		imageViewPhoto = (ImageView) findViewById(R.id.logoPhoto2);
		textViewPhoto_ = (TextView) findViewById(R.id.logoPhotoText2);
		datePicker = (DatePicker) findViewById(R.id.datepicker1);
		editText = (BootstrapEditText) findViewById(R.id.editText2);
		Log.w("bg2","textViewClientId "+textViewClientId_);
		Log.w("bg2","textViewPhoto"+textViewPhoto_);
		Log.w("bg2","textViewDetailNumber"+textViewDetailNumber);
		Log.w("bg2","textViewContact"+textViewContact);
		
		initDatePicker();
		init();
	}

	protected void init() {
		this.storage = applicationBg.getStorage();
		Log.i("bg2", "ActivityAddEvent    contact : " + contact_ + "  storage " + storage);
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;


		
		textViewClientId_.setText("");
		String displayName;
		String normalisedNumber;
        if (contact_==null){
        	displayName="No Name";
			normalisedNumber="";
		}else {
			 displayName = contact_.getExtra(applicationBg).getDisplayName();
			if (displayName == null) {
				displayName = contact_.getContactNameOrNumber();
			}
			normalisedNumber = contact_.getExtra(applicationBg).getNormalizedNumber();
			if (normalisedNumber == null) {
				normalisedNumber = contact_.getNumber();
			}
			UtilLogoPhoto.init(this, textViewPhoto_, imageViewPhoto, contact_);

		}
		setTitle(displayName);
		textViewContact.setText(displayName);
		textViewDetailNumber.setText(normalisedNumber);

		OnClickListener listenerPhoto = new OnClickListener() {

			@Override
			public void onClick(View v) {
				UtilContact.updateContact(ActivityAddEvent.this, contact_);
			}
		};
		imageViewPhoto.setOnClickListener(listenerPhoto);


		textViewPhoto_.setOnClickListener(listenerPhoto);
		
	}

	public void ok(View view) {
		Date date = getDateFromDatePicker(datePicker);
		String comment = this.editText.getText().toString();
		Log.i("bg2", "AddEvent  date : "+date+"  comment :"+comment); 
		insert(date, comment);
		UtilActivitiesCommon.displayActivityLogDetail(this, contact_, this.storage, false);
	}
	
	private void insert(Date date, String comment) {
		long startMills = date.getTime();
		long endMillis = startMills+1000;
		EventCRM event = new EventCRM(date.getTime(), comment,contact_);
		String description = comment;
		BgCalendar calendar = applicationBg.getStorageCalendar();
		String tittle = contact_.getNameRemember()+" CRM "+contact_.getClientId();
		UtilCalendar.insertEvent(this.getContentResolver(),startMills,endMillis,calendar,tittle,description);
	}

	public void searchContact(View view) {
		Log.i("bg2", "AddEvent searchContact No implemented yet");
		UtilActivitiesCommon.openSearchContact(this);
	}

	// display current date
	public void initDatePicker() {

		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		datePicker.init(year, month, day, null);
	}
	
	
    /**
 * 
 * @param datePicker
 * @return a java.util.Date
 */
public static Date getDateFromDatePicker(DatePicker datePicker){
    int day = datePicker.getDayOfMonth();
    int month = datePicker.getMonth();
    int year =  datePicker.getYear();

    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day);

    return calendar.getTime();
}

}
