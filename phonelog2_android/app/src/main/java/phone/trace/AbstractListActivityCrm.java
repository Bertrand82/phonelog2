package phone.trace;

import phone.trace.R;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AbstractListActivityCrm extends ListActivity{



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

    protected ApplicationBg getApplicationBg() {
        return (ApplicationBg) getApplication();
    }

}
