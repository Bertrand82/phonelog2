package phone.crm2;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

/** 
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */

public class ActivitySettingEmailSMTP extends PreferenceActivity {
	/**
	 * Determines whether to always show the simplified settings UI, where
	 * settings are presented in a single list. When false, settings are shown
	 * as a master/detail two-pane view on tablets. When true, a single pane is
	 * shown on tablets.
	 */
	private static final boolean ALWAYS_SIMPLE_PREFS = false;

	private static String TAG = "bg2";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		setupSimplePreferencesScreen();
		Button button =new Button(this);
		button.setText("OK");
		AbsListView.LayoutParams  lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
	        
		button.setLayoutParams(lp);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 
				UtilActivitiesCommon.openLogs(ActivitySettingEmailSMTP.this);
			}
		});
		ListView v = getListView();
		v.addFooterView(button);
	}
	
	@Override
	public final boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater(); 
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public final boolean onOptionsItemSelected(MenuItem item) {

		if (UtilActivitiesCommon.onOptionsItemSelected(item, this)) {
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Shows the simplified settings UI if the device configuration if the
	 * device configuration dictates that a simplified, single-pane UI should be
	 * shown.
	 */
	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() {
		if (!isSimplePreferences(this)) {
			return;
		}

		try {
			// In the simplified UI, fragments are not used at all and we
			// instead
			// use the older PreferenceActivity APIs.
			setDefaultValueFirstInit_();
			// Add 'general' preferences.
			addPreferencesFromResource(R.xml.pref_general_email_smtp);
			
			// Add 'notifications' preferences, and a corresponding header.
			PreferenceCategory fakeHeader = new PreferenceCategory(this);
			// fakeHeader.setTitle(R.string.pref_header_notifications);
			getPreferenceScreen().addPreference(fakeHeader);
			
			// Add 'data and sync' preferences, and a corresponding header.
			fakeHeader = new PreferenceCategory(this);
			// fakeHeader.setTitle(R.string.pref_header_data_sync);
			getPreferenceScreen().addPreference(fakeHeader);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to
			// their values. When their values change, their summaries are
			// updated
			// to reflect the new value, per the Android Design guidelines.
		
			bindPreferenceSummaryToValue(findPreference("mode_send_email_for_all_call"));
			bindPreferenceSummaryToValue(findPreference("mode_send_email_for_miss_call"));
			bindPreferenceSummaryToValue(findPreference("mailHost"));
			bindPreferenceSummaryToValue(findPreference("mailUser"));
			bindPreferenceSummaryToValue(findPreference("mailPassword"));
			bindPreferenceSummaryToValue(findPreference("mailTo"));
			bindPreferenceSummaryToValue(findPreference("mailPort"));
		} catch (Exception e) {
			Log.w(TAG, "ExceptionAAAA", e);
		}
	}

	private void setDefaultValueFirstInit_() {
		setDefaultValueFirstInit_("mailUser");
		setDefaultValueFirstInit_("mailTo");
	}

	private void setDefaultValueFirstInit_(String key) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		String mailUser = sharedPrefs.getString(key, "");
		String possiblePrimaryEmailAdress = UtilActivitiesCommon.getPossiblePrimaryEmailAdress(this.getApplicationContext());
		
		if (mailUser.trim().length()==0){
			Editor editor = sharedPrefs.edit();
			editor.putString(key, possiblePrimaryEmailAdress);
			editor.commit();
		}
		
	}

	/** {@inheritDoc} */
	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && !isSimplePreferences(this);
	}

	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	/**
	 * Determines whether the simplified settings UI should be shown. This is
	 * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
	 * doesn't have newer APIs like {@link PreferenceFragment}, or the device
	 * doesn't have an extra-large screen. In these cases, a single-pane
	 * "simplified" settings UI should be shown.
	 */
	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB || !isXLargeTablet(context);
	}

	/** {@inheritDoc} */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
			loadHeadersFromResource(R.xml.pref_headers, target);
		}
	}

	
	
	

	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener__ = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();
			Log.i(TAG,"onPreferenceChange  Change Setting "+preference+"  "+value);
			if (preference instanceof ListPreference) {
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);
				
				Log.i(TAG, "listPreference.OnPreferenceChangeListener index :" + index);
				// Set the summary to reflect the new value.
				preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
				// Notify 
				ApplicationBg applicationBg = (ApplicationBg) preference.getContext().getApplicationContext();
				applicationBg.onChangeStoragePreference();
			} else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		Log.i(TAG, "bindPreferenceSummaryToValue " + sBindPreferenceSummaryToValueListener__);
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener__);

		// Trigger the listener immediately with the preference's
		// current value.
	}

	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class GeneralPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_general_email_smtp);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			
			bindPreferenceSummaryToValue(findPreference("mode_send_email_for_all_call"));
			bindPreferenceSummaryToValue(findPreference("mode_send_email_for_miss_call"));
			bindPreferenceSummaryToValue(findPreference("mailHost"));
			bindPreferenceSummaryToValue(findPreference("mailUser"));
			bindPreferenceSummaryToValue(findPreference("mailPassword"));
			bindPreferenceSummaryToValue(findPreference("mailTo"));
			//
			
		}
	}

	public ApplicationBg getApplicationBg() {
		return (ApplicationBg) getApplication();
	}

}
