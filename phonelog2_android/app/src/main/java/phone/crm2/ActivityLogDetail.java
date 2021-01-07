package phone.crm2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import phone.crm2.legacy.UtilContact;
import phone.crm2.model.Contact;
import phone.crm2.model.ContactExtra;
import phone.crm2.model.Event;
import phone.crm2.model.EventCRM;
import phone.crm2.model.PhoneCall;
import phone.crm2.receivers.CallManager;


public class ActivityLogDetail extends AbstractListActivityCrm {

	private static final int DISPLAY_PHONE_LIST = 0;
	private static final int DISPLAY_CRM = 1; 
	private static final int DISPLAY_PHONE_LIST_COMMENTED_ONLY__ =2;
	public static final String KEY_MASKABLE = "KEY_MASKABLE";
	
	private int displayed = DISPLAY_PHONE_LIST;
	private String TAG = "bg2";
	private Button buttonDisplayCRM_;
	private ApplicationBg applicationBg;
	private PhoneCallLDetailArrayAdapter adapter;
	private Contact contact;
	private BgCalendar storage;
	private int page = 0;
	private List<Event> events = new ArrayList<Event>();
	private TextView textViewClientId;
	private Button buttonEditClientId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.applicationBg = (ApplicationBg) this.getApplicationContext();
		Bundle b = getIntent().getExtras();
		// long contactId = b.getLong("contactId");
		this.contact = (Contact) b.getSerializable("contact");
		this.contact.getExtra(this);
		this.applicationBg.setContactCurrent(contact);
		this.storage = (BgCalendar) b.getSerializable("storage");
		Boolean isMaskable = b.getBoolean(KEY_MASKABLE, false);
		Log.i("bg2", "ActivityLogDetail    contact : " + contact + "  storage " + storage);
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		/*if (currentapiVersion >= 11) {
			ActionBar actionBar = super.getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}*/

		setContentView(R.layout.activity_log_detail);
		
		
		textViewClientId = (TextView) findViewById(R.id.textViewClientId);
		textViewClientId.setText("");
		Button buttonMask = (Button) findViewById(R.id.buttonMask);
		if (isMaskable){
			buttonMask.setVisibility(View.VISIBLE);
			buttonMask.setOnClickListener(new OnClickListener() {
				@Override 
				public void onClick(View v) {
					Log.i("bg2","buttonMask click");
					CallManager.moveTaskPhoneToFront(ActivityLogDetail.this);
				}
			});
		}
		buttonEditClientId = (Button) findViewById(R.id.buttonEditClientId);
		buttonEditClientId.setVisibility(View.INVISIBLE);
		buttonEditClientId.setEnabled(false);;
		buttonEditClientId.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				displayActivityFormClientId();
			}
		});

		Button buttonPhoneCall = (Button) findViewById(R.id.buttonPhoneCall);
		buttonPhoneCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UtilActivitiesCommon.callNumber(ActivityLogDetail.this, contact.getNumber());
			}
		});
		
		Button buttonDisplayPhoneCAllCommentedOnly = (Button) findViewById(R.id.buttonFiltreCommentedOnly);
		buttonDisplayPhoneCAllCommentedOnly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMessageCommentedOnly();
			}

		});
		
		Button buttonDisplayMail = (Button) findViewById(R.id.buttonEmail);
		String email = getEmailFromContact();
		if (email==null){
			buttonDisplayMail.setVisibility(Button.GONE);
		}else {
			buttonDisplayMail.setVisibility(Button.VISIBLE);
		}
		buttonDisplayMail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMAils();
			}
		});

		buttonDisplayCRM_ = (Button) findViewById(R.id.buttonCRM);
		String displayName = contact.getExtra(applicationBg).getDisplayName();
		if (displayName == null) {
			displayName = contact.getContactNameOrNumber();
		}
		setTitle(displayName);

		TextView textViewContact = (TextView) findViewById(R.id.labelContact);
		textViewContact.setText(displayName);

		String normalisedNumber = contact.getExtra(applicationBg).getNormalizedNumber();
		if (normalisedNumber == null) {
			normalisedNumber = contact.getNumber();
		}
		TextView textViewDetailNumber = (TextView) findViewById(R.id.labelNumber);
		textViewDetailNumber.setText(normalisedNumber);

		ImageView imageViewPhoto_ = (ImageView) findViewById(R.id.logoPhoto);
		TextView  textViewPhoto = (TextView)findViewById(R.id.logoPhotoText);
		OnClickListener listenerPhoto = new OnClickListener() {

			@Override
			public void onClick(View v) {
				UtilContact.updateContact(ActivityLogDetail.this, contact);
			}
		};
		imageViewPhoto_.setOnClickListener(listenerPhoto);
		UtilLogoPhoto.init(this,textViewPhoto,imageViewPhoto_,contact);
		
		textViewPhoto.setOnClickListener(listenerPhoto);
		setListEvents();

		Log.i("bg2", " events size" + events.size());
		adapter = new PhoneCallLDetailArrayAdapter(this, contact, events);
		// Assign adapter to List
		setListAdapter(adapter);

		buttonDisplayCRM_.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				displayCRM();
			}
		});
		ListView listView = getListView();
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int n = firstVisibleItem + visibleItemCount;
				if (n >= totalItemCount){
					Log.i("bg2","ActivityLogDetail.onScroll firstItem "+firstVisibleItem+" visibleItemCount "+visibleItemCount+"  "+(firstVisibleItem+visibleItemCount)+" totalItemCount :"+totalItemCount);
					appendNexPage();
				}
			}

		});
	}

	@Override
	protected void onResume() {
		Log.i(TAG, " ActivityLog  onResume");
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		try {
			super.onListItemClick(l, v, position, id);

			// ListView Clicked item index
			int itemPosition = position;

			// ListView Clicked item value
			Object itemValue = l.getItemAtPosition(position);

			Log.i(TAG, "ActivityLogDetail.onListItemClick    Position :" + itemPosition + "   ListItem : " + itemValue);
			if (itemValue == null) {
			} else if (itemValue instanceof EventCRM) {
				// Select an eventCRM . What do ?
			} else {
				displayCommentPhoneCall((PhoneCall) itemValue);
			}
		} catch (Exception e) {
			Log.i(TAG, "ActivityLogDetail.onListItemClick :   Position :" + position + "   id : " + id, e);
		}

	}

	private void displayCommentPhoneCall(PhoneCall phoneCall) {

		Log.i(TAG, "displayCommentPhoneCall " + phoneCall + "   " + this.getApplication());
		((ApplicationBg) this.getApplication()).setPhoneCall(phoneCall);
		Intent intent = new Intent(this, ActivityComment.class);
		Bundle b = new Bundle();
		b.putSerializable("storage", storage);
		b.putBoolean(ActivityComment.KEY_SENT_MAIL, true);
		intent.putExtras(b);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

	}

	private void callNumber() {
		Log.i(TAG, "Call Number");
		UtilActivitiesCommon.callNumber(this, contact.getNumber());

	}

	public void editContact() {
		Log.i(TAG, "editContact from log detail Contact_LEGACY_DEPRECATED :" + contact);
		UtilContact.updateContact(this, contact);
	}

	private void setListEvents() {
		this.events.clear();
		if (displayed == DISPLAY_PHONE_LIST) {

			BgCalendar bgCalendar = (BgCalendar) storage;
			List<Event> list = UtilCalendar.getListEventByContact(this, bgCalendar, contact, page);
			events.addAll(list);

		} else if (displayed == DISPLAY_CRM) {

			Log.i("bg2", "Bdd Calendar!");
			BgCalendar bgCalendar = (BgCalendar) storage;
			String clientId = contact.getClientId(this);
			Log.i("bg2", "setListEvents clientId " + clientId);
			if (clientId == null) {

			} else {
				List<Event> list = UtilCalendar.getListEventByNumeroClient(this, bgCalendar, contact, page);
				events.addAll(list);
			}

		}else if (displayed == DISPLAY_PHONE_LIST_COMMENTED_ONLY__) {

			BgCalendar bgCalendar = (BgCalendar) storage;
			List<Event> list = UtilCalendar.getListEventByContactAndCommentNotNull(this, bgCalendar, contact, page);
			events.addAll(list);

		}
		if (this.adapter != null) {
			this.adapter.notifyDataSetChanged();
		}
	}

	private void displayCRM() {
		Log.i("bg2", "displayCRM contact: " + contact);
		Log.i("bg2", "displayCRM storage: storage : " + this.storage);

		
		if (displayed == DISPLAY_CRM) {
			displayed = DISPLAY_PHONE_LIST;
		}else if(displayed== DISPLAY_PHONE_LIST){
			displayed = DISPLAY_CRM;
		}
		if (displayed == DISPLAY_CRM) {
			buttonEditClientId.setVisibility(View.VISIBLE);
			buttonEditClientId.setEnabled(true);
			
			textViewClientId.setText(contact.getClientId());
		}else {
			//Display CAlls
			buttonEditClientId.setVisibility(View.INVISIBLE);
			buttonEditClientId.setEnabled(false);
		}
		displayCRM(displayed);
		page = 0;
	}

	private void displayCRM(int display) {

		Log.i("bg2", "displayCRM " + display);
		if (display == DISPLAY_CRM) {
			this.buttonDisplayCRM_.setText("Calls");
		} else if (display == DISPLAY_PHONE_LIST) {
			this.buttonDisplayCRM_.setText("CRM");
		}
		// ActivityLogs___OLD.displayActivity(this, contact, storage, false,
		// ActivityDisplayCRM________________DEPRECATED.class);
		setListEvents();
		adapter.notifyDataSetChanged();
	}

	private void showMessageCommentedOnly() {
		Log.i("bg2", "showMessageCommentedOnly " + contact);
		displayed= ActivityLogDetail.DISPLAY_PHONE_LIST_COMMENTED_ONLY__;
		setListEvents();
	}
	
	private String getEmailFromContact(){
		String email = null;
		if (contact==null){
			return null;
		}
		ContactExtra contactExtra_ = this.contact.getExtra(this);
		if (contactExtra_ == null) {
		} else {
			Long raw_contact_id = contactExtra_.getRaw_contact_id(this);
			if (raw_contact_id != null) {
				email = UtilContact.getEmail(raw_contact_id, this);
			}
		}
		return email;
	}
	private void showMAils() {
		Log.i("bg2", "showMAils " + contact);
		String email = null;
		ContactExtra contactExtra_ = this.contact.getExtra(this);
		if (contactExtra_ == null) {
		} else {
			Log.i("bg2", " Contact Extra " + contactExtra_);
			Long raw_contact_id = contactExtra_.getRaw_contact_id(this);
			if (raw_contact_id != null) {
				email = UtilContact.getEmail(raw_contact_id, this);
			}
		}
		if (contactExtra_  ==null){
			UtilActivitiesCommon.popUp(this, "No Contact for " + contact.getContactNameOrNumber(), "" + contact.getContactNumbberAndNAme());
		} else  if (email == null) {
			UtilActivitiesCommon.popUp(this, "No Email for " + contact.getContactNameOrNumber(), "" + contact.getContactNumbberAndNAme());
		} else {
			Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.email");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
			startActivity(intent);
		}
	}
	
	private void displayActivityFormClientId() {
		Log.i("bg2", "displayActivityFormClientId_1 " + contact + "  Storage:" + storage );
		Class clazz = ActivityFormClientId.class;
		Intent intent = new Intent(this, clazz);
		Bundle b = new Bundle();
		b.putSerializable("contact", contact);
		b.putSerializable("storage", storage);
		Log.i(TAG, "Start ----  " + b.getSerializable("contact"));
		intent.putExtras(b);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		
		this.startActivity(intent);
	}
	
	private void appendNexPage() {
		page++;
		Log.i("bg2","ActivityLogDetail.appendNexPage "+page);
		List<Event> list = UtilCalendar.getListEventByNumeroClient(this, this.storage, contact, page);
		this.adapter.getEvents().addAll(list);
		this.adapter.notifyDataSetChanged();
	}

}
