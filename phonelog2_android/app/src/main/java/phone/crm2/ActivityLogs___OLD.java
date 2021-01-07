package phone.crm2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.List;

import phone.crm2.model.Contact;
import phone.crm2.model.Event;

//import android.widget.ListView;
//@deprecated Use  ActivityLogs2
@Deprecated
public class ActivityLogs___OLD extends AbstractListActivityCrm {

	private static final String TAG = "bg2 ActivityLogs___OLD";
	private PhoneCallLArrayAdapter adapter;
	private Spinner spinner;
	public static String TAG_BDD_LOCALE_ = "Data Base Android";
	private int page = 0;
	private ApplicationBg applicationBg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.i("bg2", "ActivityLogs___OLD.onCreate hashcode: " + this.hashCode());
		setContentView(R.layout.activity_logs);
		applicationBg = (ApplicationBg) getApplication();
		this.initSpinner();
		this.logsSelected();
		ListView listView = getListView();
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int n = firstVisibleItem + visibleItemCount;
				if (n >= totalItemCount){
					Log.i("bg2","ActivityLogs___OLD Fin de page firstVisibleItem "+firstVisibleItem+" visibleItemCount "+visibleItemCount+"  "+(firstVisibleItem+visibleItemCount)+" totalItemCount :"+totalItemCount);
					appendNexPage();
				}
			}

		});
	}

	private void initSpinner() {
		spinner = (Spinner) findViewById(R.id.bdd_spinner);
		@SuppressWarnings("rawtypes")
		List lisBgCalendarsSelected = applicationBg.getListCalendarsSelected();
		Object storageDefault = applicationBg.getStorage();
		int positionSelected = 0;
		int i = 0;
		for (Object o : lisBgCalendarsSelected) {
			if (o.equals(storageDefault)) {
				positionSelected = i;
			}
			i++;
		}

		ArrayAdapter<Object> adapterComboBox = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_item, lisBgCalendarsSelected.toArray());
		spinner.setAdapter(adapterComboBox);
		spinner.setSelection(positionSelected);
		AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				logsSelected();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		};
		spinner.setOnItemSelectedListener(onItemSelectedListener);
		// Specify the layout to use when the list of choices appears
		adapterComboBox.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	}

	

	private void initListDisplayed(List<Event> listPhoneCAll) {
		if (this.adapter == null) {
			this.adapter = new PhoneCallLArrayAdapter(this, listPhoneCAll);
			setListAdapter(this.adapter);
			
		} else {
			this.adapter.getListEvents().clear();
			this.adapter.getListEvents().addAll(listPhoneCAll);

		}
		this.adapter.notifyDataSetChanged();

	}

	@Override
	protected void onListItemClick(ListView listView, View v, int position, long id) {

		super.onListItemClick(listView, v, position, id);

		// ListView Clicked item index
		int itemPosition = position;

		// ListView Clicked item value
		Event itemValue = (Event) listView.getItemAtPosition(position);

		Log.i(TAG, "ActivityLogs___OLD.onListItemClick :   Position :" + itemPosition + "   ListItem : " + itemValue);
		displayActivityLogDetail((Contact) itemValue.getContact());

	}

	private void displayActivityLogDetail(Contact contact) {
		Serializable storage = (Serializable) this.spinner.getSelectedItem();
		UtilActivitiesCommon.displayActivityLogDetatil(this, contact, storage, false);
	}

	



	

	private void logsSelected() {

		Object o = spinner.getSelectedItem();
		BgCalendar bgCalendar = (BgCalendar) o;
		List<Event> communicationList = UtilCalendar.getListEvent(this, bgCalendar, page);
		initListDisplayed(communicationList);
		applicationBg.setStorage(bgCalendar);
	}

	

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("bg2", "ActivityLogs___OLD.ondestroy" + this.hashCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("bg2", "ActivityLogs___OLD.onRestart  " + this.hashCode());
	}

	@Override
	protected void onResume() {
		super.onResume();
		logsSelected();
		adapter.notifyDataSetChanged();
	}
	

	private void appendNexPage() {
		Log.i("bg2","ActivityLogs___OLD.appendNexPage  page : "+page);
		Object o = spinner.getSelectedItem();
		BgCalendar bgCalendar = (BgCalendar) o;
		page ++;
		List<Event> communicationList = UtilCalendar.getListEvent(this, bgCalendar, page);
		if(communicationList.size()==0){
			page--;
		}
		this.adapter.getListEvents().addAll(communicationList);
		this.adapter.notifyDataSetChanged();
	}

}
