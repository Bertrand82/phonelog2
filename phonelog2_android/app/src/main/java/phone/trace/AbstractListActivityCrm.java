package phone.trace;

import phone.trace.R;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AbstractListActivityCrm extends AbstractListActivity{

    private String TAG ="bg2 AbstractListActivityCrm";

    @Override
    public final boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG,"AbstractListActivityCrm.onCreateOptionsMenu ");
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG,"AbstractListActivityCrm.onOptionsItemSelected ");
        if (UtilActivitiesCommon.onOptionsItemSelected(item, this)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    protected ApplicationBg getApplicationBg() {
        return (ApplicationBg) getApplication();
    }

}
