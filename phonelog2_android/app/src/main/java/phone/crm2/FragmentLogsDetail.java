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

import java.util.ArrayList;
import java.util.List;

import phone.crm2.legacy.UtilContact;
import phone.crm2.model.Contact;
import phone.crm2.model.ContactExtra;
import phone.crm2.model.Event;
import phone.crm2.model.EventCRM;
import phone.crm2.model.PhoneCall;


public class FragmentLogsDetail extends Fragment implements AdapterView.OnItemClickListener {



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
		View view = inflater.inflate(R.layout.fragment_logs_detail, container, false);
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
		this.contact = (Contact) b.getSerializable("contact");//contact
		this.contact.getExtra(this.getActivity());
		applicationBg.setContactCurrent(contact);
		this.storage = (BgCalendar) b.getSerializable("storage");

		TextView textViewClientId = view.findViewById(R.id.textViewClientId);
		textViewClientId.setText("");


		Button buttonPhoneCall = view.findViewById(R.id.buttonPhoneCall);
		buttonPhoneCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UtilActivitiesCommon.callNumber(FragmentLogsDetail.this.getActivity(), contact.getNumber());
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
				UtilEmail.sendMessage(FragmentLogsDetail.this.getActivity(), FragmentLogsDetail.this.contact);
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

		ImageView imageViewPhoto_ = view.findViewById(R.id.logoPhoto1);
		TextView  textViewPhoto = view.findViewById(R.id.logoPhotoText1);
		OnClickListener listenerPhoto = new OnClickListener() {

			@Override
			public void onClick(View v) {
				UtilContact.updateContact(FragmentLogsDetail.this.getActivity(), contact);
			}
		};
		imageViewPhoto_.setOnClickListener(listenerPhoto);
		UtilLogoPhoto.init(this.getActivity(),textViewPhoto,imageViewPhoto_,contact);
		
		textViewPhoto.setOnClickListener(listenerPhoto);
		setListEvents(displayed);

		adapter = new PhoneCallLDetailArrayAdapter(this.getContext(),  events);
		// Assign adapter to List
		//setListAdapter(adapter);
		ListView listView =  this.getActivity().findViewById(R.id.listDetail0);
		listView.setAdapter(adapter);

		AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int n = firstVisibleItem + visibleItemCount;
				if (n >= totalItemCount){
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
		super.onResume();
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		onListItemClickBg(view,position);
	}

	public void onListItemClickBg( View v, int position) {

		try {
			//super.onListItemClick(l, v, position, id);

			// ListView Clicked item index
			int itemPosition = position;

			// ListView Clicked item value
			Event itemValue = events.get(position);

			if (itemValue == null) {
			} else if (itemValue instanceof EventCRM) {
				// Select an eventCRM . What do ?
			} else {
				displayCommentPhoneCall((PhoneCall) itemValue);
			}
		} catch (Throwable e) {
			Log.w(TAG, "FragmentLogsDetail.onListItemClick :   Position :" + position , e);
		}

	}

	private void displayCommentPhoneCall(PhoneCall phoneCall) {

		((ApplicationBg) this.getActivity().getApplication()).setPhoneCall(phoneCall);
		UtilActivitiesCommon.openComment(this.getActivity(),phoneCall,storage);

	}


	private void callNumber() {
		UtilActivitiesCommon.callNumber(this.getActivity(), contact.getNumber());
	}

	public void editContact() {
		UtilContact.updateContact(this.getActivity(), contact);
	}

	private void setListEvents(DISPLAY_F displayed) {
		this.events.clear();
		if (displayed == DISPLAY_F.PHONE_LIST) {

			BgCalendar bgCalendar = storage;
			List<Event> list = UtilCalendar.getListEventByContact(this.getActivity(), bgCalendar, contact, page);
			events.addAll(list);

		} else if (displayed == DISPLAY_F.DISPLAY_CRM) {

			BgCalendar bgCalendar = storage;
			String clientId = contact.getClientId(this.getActivity());
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
		Button buttonDisplayPhoneCAllCommentedOnly = view. findViewById(R.id.buttonFiltreCommentedOnly);
		if (displayed== FragmentLogsDetail.DISPLAY_F.PHONE_LIST_COMMENTED_ONLY){
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
		String email = null;
		ContactExtra contactExtra_ = this.contact.getExtra(this.getActivity());
		if (contactExtra_ == null) {
		} else {
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
		List<Event> list = UtilCalendar.getListEventByNumeroClient(this.getActivity(), this.storage, contact, page);
		this.adapter.getEvents().addAll(list);
		this.adapter.notifyDataSetChanged();
	}

}
