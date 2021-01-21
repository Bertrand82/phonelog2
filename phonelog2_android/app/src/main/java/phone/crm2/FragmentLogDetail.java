package phone.crm2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

import phone.crm2.legacy.UtilContact;
import phone.crm2.model.Contact;
import phone.crm2.model.ContactExtra;
import phone.crm2.model.Event;
import phone.crm2.model.EventCRM;
import phone.crm2.model.PhoneCall;
import phone.crm2.receivers.CallManager;


public class FragmentLogDetail extends Fragment implements AdapterView.OnItemClickListener {



	enum DISPLAY_F  {PHONE_LIST, DISPLAY_CRM, PHONE_LIST_COMMENTED_ONLY}

	public static final String KEY_MASKABLE = "KEY_MASKABLE";
	
	private DISPLAY_F displayed = DISPLAY_F.PHONE_LIST;
	private final String TAG = "bg2";
	private PhoneCallLDetailArrayAdapter adapter;
	private Contact contact;
	private BgCalendar storage;
	private int page = 0;
	private final List<Event> events = new ArrayList<Event>();
	private Button buttonEditClientId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_log_detail, container, false);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		onCreateBg(view,savedInstanceState);
	}

	protected void onCreateBg(View view,Bundle savedInstanceState) {
		ApplicationBg applicationBg = (ApplicationBg) this.getActivity().getApplicationContext();
		Bundle b = getArguments();

		// long contactId = b.getLong("contactId");
		Log.i("bg2","Bundle  : " +b);
		this.contact = (Contact) b.getSerializable("contact");//contact
		this.contact.getExtra(this.getActivity());
		applicationBg.setContactCurrent(contact);
		this.storage = (BgCalendar) b.getSerializable("storage");
		Log.i("bg2", "FragmentLogDetail    contact : " + contact + "  storage " + storage);

		TextView textViewClientId = view.findViewById(R.id.textViewClientId);
		textViewClientId.setText("");


		Button buttonPhoneCall = view.findViewById(R.id.buttonPhoneCall);
		buttonPhoneCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UtilActivitiesCommon.callNumber(FragmentLogDetail.this.getActivity(), contact.getNumber());
			}
		});
		
		Button buttonDisplayPhoneCAllCommentedOnly = view.findViewById(R.id.buttonFiltreCommentedOnly);
		buttonDisplayPhoneCAllCommentedOnly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMessageCommentedOnly(view);
			}

		});
		Button buttonSendMessage = view.findViewById(R.id.buttonSendMessage);
		buttonSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("bg2", "sendMessage2");
				UtilEmail.sendMessage(FragmentLogDetail.this.getActivity(), FragmentLogDetail.this.contact);
			}
		});


		String displayName = contact.getExtra(applicationBg).getDisplayName();
		if (displayName == null) {
			displayName = contact.getContactNameOrNumber();
		}
		this.getActivity().setTitle(displayName);

		TextView textViewContact = view.findViewById(R.id.labelContact);
		textViewContact.setText(displayName);

		String normalisedNumber = contact.getExtra(applicationBg).getNormalizedNumber();
		if (normalisedNumber == null) {
			normalisedNumber = contact.getNumber();
		}
		TextView textViewDetailNumber = view.findViewById(R.id.labelNumber);
		textViewDetailNumber.setText(normalisedNumber);

		ImageView imageViewPhoto_ = view.findViewById(R.id.logoPhoto);
		TextView  textViewPhoto = view.findViewById(R.id.logoPhotoText);
		OnClickListener listenerPhoto = new OnClickListener() {

			@Override
			public void onClick(View v) {
				UtilContact.updateContact(FragmentLogDetail.this.getActivity(), contact);
			}
		};
		imageViewPhoto_.setOnClickListener(listenerPhoto);
		UtilLogoPhoto.init(this.getActivity(),textViewPhoto,imageViewPhoto_,contact);
		
		textViewPhoto.setOnClickListener(listenerPhoto);
		setListEvents(displayed);

		Log.i("bg2", "FragmenLogDetail events size" + events.size());
		adapter = new PhoneCallLDetailArrayAdapter(this.getContext(),  events);
		// Assign adapter to List
		//setListAdapter(adapter);
		ListView listView = (ListView)  this.getActivity().findViewById(R.id.listDetail0);
		listView.setAdapter(adapter);

		AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int n = firstVisibleItem + visibleItemCount;
				if (n >= totalItemCount){
					Log.i("bg2","FragmentLogDetail.onScroll firstItem "+firstVisibleItem+" visibleItemCount "+visibleItemCount+"  "+(firstVisibleItem+visibleItemCount)+" totalItemCount :"+totalItemCount);
					appendNexPage();
				}
			}

		};
		listView.setOnItemClickListener(this);
		if (events.size()>=UtilCalendar.LIMIT_P_PAGE) {
			listView.setOnScrollListener(onScrollListener);
		}

	}

	@Override
	public void onResume() {
		Log.i(TAG, " FragmentLogDetail  onResume");
		super.onResume();
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		onListItemClickBg(view,position);
	}

	public void onListItemClickBg( View v, int position) {

		try {
			Log.i(TAG, "FragmentLogDetail.onListItemClick AAA  start");
			//super.onListItemClick(l, v, position, id);

			// ListView Clicked item index
			int itemPosition = position;

			// ListView Clicked item value
			Event itemValue = events.get(position);

			Log.i(TAG, "FragmentLogDetail.onListItemClick    Position :" + itemPosition + "   ListItem : " + itemValue);
			if (itemValue == null) {
			} else if (itemValue instanceof EventCRM) {
				// Select an eventCRM . What do ?
			} else {
				displayCommentPhoneCall((PhoneCall) itemValue);
			}
		} catch (Throwable e) {
			Log.i(TAG, "FragmentLogDetail.onListItemClick :   Position :" + position , e);
		}

	}

	private void displayCommentPhoneCall(PhoneCall phoneCall) {

		Log.i(TAG, "displayCommentPhoneCall " + phoneCall + "   " + this.getActivity().getApplication());
		((ApplicationBg) this.getActivity().getApplication()).setPhoneCall(phoneCall);
		UtilActivitiesCommon.openComment(this.getActivity(),phoneCall,storage);

	}


	private void callNumber() {
		Log.i(TAG, "Call Number");
		UtilActivitiesCommon.callNumber(this.getActivity(), contact.getNumber());

	}

	public void editContact() {
		Log.i(TAG, "editContact from log detail Contact_LEGACY_DEPRECATED :" + contact);
		UtilContact.updateContact(this.getActivity(), contact);
	}

	private void setListEvents(DISPLAY_F displayed) {
		Log.i("bg2","setListEvents displayed :"+displayed);
		this.events.clear();
		if (displayed == DISPLAY_F.PHONE_LIST) {

			BgCalendar bgCalendar = storage;
			List<Event> list = UtilCalendar.getListEventByContact(this.getActivity(), bgCalendar, contact, page);
			events.addAll(list);

		} else if (displayed == DISPLAY_F.DISPLAY_CRM) {

			Log.i("bg2", "setListEvents AAA  Bdd Calendar!");
			BgCalendar bgCalendar = storage;
			Log.i("bg2", "setListEvents BBB  bgCalendar "+bgCalendar);
			String clientId = contact.getClientId(this.getActivity());
			Log.i("bg2", "setListEvents CCC  clientId "+clientId);

			if (clientId == null) {

			} else {
				List<Event> list = UtilCalendar.getListEventByNumeroClient(this.getActivity(), bgCalendar, contact, page);
				events.addAll(list);
			}

		}else if (displayed == DISPLAY_F.PHONE_LIST_COMMENTED_ONLY) {

			BgCalendar bgCalendar = storage;
			List<Event> list = UtilCalendar.getListEventByContactAndCommentNotNull(this.getActivity(), bgCalendar, contact, page);
			events.addAll(list);

		}
		if (this.adapter != null) {
			this.adapter.notifyDataSetChanged();
		}
	}




	private void showMessageCommentedOnly(View view) {
		Log.i("bg2", "FragmentLogDetail showMessageCommentedOnly A" + contact);
		Log.i("bg2", "FragmentLogDetail showMessageCommentedOnly B" + displayed);
		Button buttonDisplayPhoneCAllCommentedOnly = view. findViewById(R.id.buttonFiltreCommentedOnly);
		if (displayed== FragmentLogDetail.DISPLAY_F.PHONE_LIST_COMMENTED_ONLY){
			displayed = DISPLAY_F.PHONE_LIST;
			buttonDisplayPhoneCAllCommentedOnly.setText(R.string.labeFiltreCommmnentedOnly);
		}else {
			displayed = DISPLAY_F.PHONE_LIST_COMMENTED_ONLY;
			buttonDisplayPhoneCAllCommentedOnly.setText(R.string.labeFiltreCommmnentedOnlyAll);
		}
		setListEvents(displayed);
	}
	
	private String getEmailFromContact(){
		String email = null;
		if (contact==null){
			return null;
		}
		ContactExtra contactExtra_ = this.contact.getExtra(this.getActivity());
		if (contactExtra_ == null) {
		} else {
			Long raw_contact_id = contactExtra_.getRaw_contact_id(this.getActivity());
			if (raw_contact_id != null) {
				email = contactExtra_.getMail( this.getActivity());
			}
		}
		return email;
	}
	private void showMAils() {
		Log.i("bg2", "ActivityLogDetails showMAils contact:" + contact);
		String email = null;
		ContactExtra contactExtra_ = this.contact.getExtra(this.getActivity());
		if (contactExtra_ == null) {
		} else {
			Log.i("bg2", "ActivityLogDetails Contact Extra " + contactExtra_);
			Long raw_contact_id = contactExtra_.getRaw_contact_id(this.getActivity());
			if (raw_contact_id != null) {
				email = contactExtra_.getMail( this.getActivity());
			}
		}
		if (contactExtra_  ==null){
			UtilActivitiesCommon.popUp(this.getActivity(), "No Contact for " + contact.getContactNameOrNumber(), "" + contact.getContactNumbberAndNAme());
		} else  if (email == null) {
			UtilActivitiesCommon.popUp(this.getActivity(), "No Email for " + contact.getContactNameOrNumber(), "" + contact.getContactNumbberAndNAme());
		} else {
			Intent intent = this.getActivity().getPackageManager().getLaunchIntentForPackage("com.android.email");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
			startActivity(intent);
		}
	}
	

	
	private void appendNexPage() {
		page++;
		Log.i("bg2","Frqagment.logDetail.appendNexPage "+page);
		List<Event> list = UtilCalendar.getListEventByNumeroClient(this.getActivity(), this.storage, contact, page);
		this.adapter.getEvents().addAll(list);
		this.adapter.notifyDataSetChanged();
	}

}
