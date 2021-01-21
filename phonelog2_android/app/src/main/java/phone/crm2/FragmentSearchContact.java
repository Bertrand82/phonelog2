package phone.crm2;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import phone.crm2.model.Contact;


public class FragmentSearchContact extends Fragment {

	private EditText editText;
	private final List<ContactLabelNumber> listContacts = new ArrayList<ContactLabelNumber>();
	private ApplicationBg applicationBg;
	private ListView listView;

	@Override
	public View onCreateView(  LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_search_contact, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initFragmentSeatchContact();
	}

	protected void initFragmentSeatchContact() {

		applicationBg = (ApplicationBg) this.getActivity().getApplication();
		listView = this.getActivity().findViewById(R.id.listview3);
		editText = this.getActivity().findViewById(R.id.editTextSearchContact);
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					Log.i("bg2", "FragmentSearchContact.searchChar");
					initListContact();
				
				} catch (Exception e) {
					Log.e("bg2", "FragmentSearchContact.searchChar",e);
					
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}
	
	
	


	
	private void initListContact() {
		String mSearchString = (""+editText.getText()).trim();
		 String[] params =
		        {
		            ContactsContract.Contacts._ID,
		            ContactsContract.Contacts.LOOKUP_KEY,
		            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
		            ContactsContract.Contacts.DISPLAY_NAME,
		            ContactsContract.Contacts.PHOTO_URI,
		            ContactsContract.Contacts.LOOKUP_KEY,
		        };
		Uri contentUri = Uri.withAppendedPath( ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(mSearchString));
		Cursor cursor = this.getActivity().getContentResolver().query(contentUri, params, null, null, null);
		traceAndClose(cursor);
		ArrayAdapter<ContactLabelNumber> baseAdapter = new StableArrayAdapter(this.getActivity(), R.id.listview3, listContacts);
		listView.setAdapter(baseAdapter);
	}
	
	private String  processNumber(ContactLabelNumber cln){
		Cursor phones = this.getActivity().getContentResolver().query(Phone.CONTENT_URI, null,  Phone.CONTACT_ID + " = " + cln._id, null, null);
		 String number="";
		if(phones.moveToFirst()) {
			
	        cln.number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
	        int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
	        
	    }
	    phones.close();
		return number;	
	}

	private void traceAndClose(Cursor cursor) {
		int i = 0;
		listContacts.clear();
		cursor.moveToFirst();
		while (!cursor.isAfterLast() && (i < 12)) {
			ContactLabelNumber c = new ContactLabelNumber(cursor);
			listContacts.add(c);
			Log.w("bg2", "Contact " + c);
			i++;
			cursor.moveToNext();
		}
		Log.w("bg2", "Contact size : " + listContacts.size() + " phoneNumber: "	);
		cursor.close();
	}

	
	public void search(View v) {
		Log.i("bg2", "FragmentSearchContact.search : >" + editText.getText()+"<");
		try {
			initListContact();
		} catch (Exception e) {
			Log.w("bg2", "ActivitySeaarch Exception",e);
			e.printStackTrace();
		}
	}

	private class StableArrayAdapter extends ArrayAdapter<ContactLabelNumber> {

		private final Context contextBg;

		public StableArrayAdapter(Context context, int textViewResourceId,List<ContactLabelNumber> list) {
			super(context, textViewResourceId, list);
			this.contextBg = context;

		}

		@Override
		public View getView(final int position, View convertView,	ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) contextBg.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.item_list_search_contact,parent, false);
			TextView textView = rowView.findViewById(R.id.displayNameSearchContact);
			rowView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					select_(position);
				}
			});
			ContactLabelNumber contactLabelNumber = listContacts.get(position);
			processNumber(contactLabelNumber);
			textView.setText(contactLabelNumber.getDisplayName());
			return rowView;
		}
		
	
		private void select_(int position) {
			Log.d("bg2", "FragmentSearchContact select position :" + position + "  ");
			ContactLabelNumber contactLabelNumber = listContacts.get(position);
			Log.i("bg2", "FragmentSearchContact select contact :" + contactLabelNumber);
			Contact contact = new Contact(contactLabelNumber.displayName, contactLabelNumber.number);
			if (contactLabelNumber.number == null){
				// Quoi faire ?
			}else {
				UtilActivitiesCommon.displayActivityLogDetail(FragmentSearchContact.this.getActivity(), contact,applicationBg.getStorageCalendar());
			}
		
		}
	}

	static class ContactLabelNumber {
		String displayName;
		Uri photoUri;
		String number;
		long _id;

		public ContactLabelNumber(Cursor cursor) {
			
            
			displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            _id=cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String uriString =null;
            //String uriString = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts.PHOTO_URI));

			if (uriString == null) {
				photoUri = null;
			} else {
				photoUri = Uri.parse(uriString);
			}
			//normalizedNumber = cursor.getString(cursor.getColumnIndex(PhoneLookup.NORMALIZED_NUMBER));

			
		}

		public CharSequence getDisplayName() {

			return  displayName;
		}

		@Override
		public String toString() {
			return "ContactLabelNumber [displayName=" + displayName+ " ]";
		}

	}

}
