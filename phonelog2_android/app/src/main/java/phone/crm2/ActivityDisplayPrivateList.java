package phone.crm2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import phone.crm2.model.Contact;


public class ActivityDisplayPrivateList extends AbstractActivityCrm {
	
	
	private ListView listView;
	private TextView textViewNbPrivate;
	List<Contact> listPrivateContacts;
	ApplicationBg applicationBg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_display_private_list);

		applicationBg = (ApplicationBg) getApplication();
		textViewNbPrivate = (TextView) findViewById(R.id.textViewNbNumeros);
		listView = (ListView) findViewById(R.id.listview2);
		initList();
		Button buttonClearList = (Button) findViewById(R.id.buttonClearBlackList);
		OnClickListener clicListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				clear();
				
			}
		};
		buttonClearList.setOnClickListener(clicListener);
		// UtilActivitiesCommon.initButtonNavigation(this,buttonLogs,buttonPrivateList,buttonOptions);
		
		
		UtilActivitiesCommon.initButtonNavigation_(this, 2);
	}
	
	
	private void initList() {
		listPrivateContacts = applicationBg.getDb().getContact().getAllPrivate();
		//listNumero.addAll(applicationBg.gethPrivateList().values());
		String label = " " + listPrivateContacts .size() + " Blacklisted";
		textViewNbPrivate.setText(label);
		
		List<String> listNumber = new ArrayList<String>();
		for(Contact c : listPrivateContacts){
			listNumber.add(c.getNumber());
		}
		ArrayAdapter<String> baseAdapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, listNumber);
		listView.setAdapter(baseAdapter);
	}
	
	protected void updateNb() {
		
	}


	public ApplicationBg getApplicationBg() {
		return (ApplicationBg) getApplication();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
	
	
	private class StableArrayAdapter extends ArrayAdapter<String> {

		private final Context contextBg;
		public StableArrayAdapter(Context context, int textViewResourceId, List<String> list) {
			super(context, textViewResourceId, list);
			this.contextBg = context;
			
		}

		
		
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) contextBg.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.item_list_private, parent, false);
			TextView textView =(TextView) rowView.findViewById(R.id.labelPrivateItem);
			Contact contact = listPrivateContacts.get(position);
			textView.setText(contact.getContactNumbberAndNAme());
			Button button = (Button)  rowView.findViewById(R.id.buttonPrivateItemRemove);
			OnClickListener onClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					remove(position);
				}
			};
			button.setOnClickListener(onClickListener);
			//TODO
//			textView.setText(getApplicationBg().gethPrivateList().get(number));
			
			return rowView;
		}

		private void remove(int position){
			Log.i("bg2","Remove position"+position+"  ");
			Contact contact = listPrivateContacts.get(position);
			contact.setPrivate(false);
			Contact c2 = applicationBg.getDb().getContact().update(contact);
			Log.i("bg2","Remove"+c2);
			
			initList();
			Log.i("bg2","Remove listPrivateContacts.size "+listPrivateContacts.size());
			updateNb();
			super.notifyDataSetChanged();
		}
	}

	public void clear() {
		Log.i("bg2","clear  size: "+listPrivateContacts.size());
		
		for(Contact c : this.listPrivateContacts){
			c.setPrivate(false);
			Contact c2 = applicationBg.getDb().getContact().update(c);
			Log.i("bg2","ActivityDisplayPrivateList   clear "+c2);
		}
		
		this.initList();
	}
	

}
