package phone.trace;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class AbstractFragmentActivity extends AppCompatActivity {
	
	public AbstractFragmentActivity() {
		super();
	}

	
	@Override
	public final boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater(); 
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public  final boolean onOptionsItemSelected(MenuItem item) {
		if (UtilActivitiesCommon.onOptionsItemSelected(item, this)) {
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
}
