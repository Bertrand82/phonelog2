package phone.crm2;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class Navigation {
    private static String TAG="Navigation";

    public static boolean onOptionsItemSelected(View item, Activity activity) {
        Log.i(TAG,"activity "+activity);
       /*if (item.getId() == R.id.buttonSettings) {
            Navigation.openSettings(activity);
            return true;
        }*/
        return true;
    }

    private static void openSettings(Activity activity){
        Intent intent = new Intent(activity, ActivitySettingsBg.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);
    }
}
