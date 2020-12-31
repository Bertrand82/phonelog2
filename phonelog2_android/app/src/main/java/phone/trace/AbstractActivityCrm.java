package phone.trace;


import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class AbstractActivityCrm extends AppCompatActivity {

    private static String  TAG="bg2 AbstractActivity";

	public AbstractActivityCrm() {
		super();
	}
	
	@Override
	public final boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		Log.v(TAG,"onCreateOptionsMenu 1");
		MenuInflater inflater = getMenuInflater(); 
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public  boolean onOptionsItemSelected(MenuItem item) {
		Log.v(TAG,"onOptionsItemSelected 1");
		if (UtilActivitiesCommon.onOptionsItemSelected(item, this)) {
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	

}
