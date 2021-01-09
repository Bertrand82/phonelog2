package phone.crm2;

import phone.crm2.db.AppAccountTable;
import phone.crm2.model.AppAccount;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends  AbstractActivityCrm {

    private String TAG = "bg "+getClass().getSimpleName();
    private static final int REQUEST_RUNTIME_PERMISSION = 123;
    public static final String SERVICE_URL_CAFE_CRM = "http://phone-log.appspot.com/r/phonecall";
    ApplicationBg applicationBg;

    private static String[] permissions = {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.READ_CALENDAR,
            android.Manifest.permission.WRITE_CALENDAR,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_CALL_LOG,
            android.Manifest.permission.WRITE_CALL_LOG,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.GET_ACCOUNTS,
            android.Manifest.permission.ACCOUNT_MANAGER,
            android.Manifest.permission.READ_SYNC_SETTINGS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_PHONE_NUMBERS,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.RECEIVE_SMS,
            android.Manifest.permission.WAKE_LOCK,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.REORDER_TASKS
          //  Manifest.permission.CALL_PRIVILEGED

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG,"OnCreate 1");
        String[] permissionsToRequest  = getListPermissionToRequest( permissions);
        ActivityCompat.requestPermissions(this, permissionsToRequest, REQUEST_RUNTIME_PERMISSION);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Starting");
        Log.w(TAG,"OnCreate 2");
        this.applicationBg = (ApplicationBg) this.getApplication();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.w(TAG,"OnCreate 3");

        AccountManager accountManager = AccountManager.get(applicationBg);
        Account[] accounts = accountManager.getAccountsByType(applicationBg.ACCOUNT_TYPE);
        Log.w(TAG,"OnCreate 4");
        Log.i(TAG,"accounts: "+toString(accounts));

        if (accounts.length == 0) { // Il n'y a pas de compte de type cafe-crm2
            Log.w(TAG,"OnCreate 5 Start Login");
            startLoginIntent();

        } else {
            Log.w(TAG,"MainActivity OnCreate 6 Start ");
            try {
                AppAccount appAccount = applicationBg.getDb().getAppAccount().getBy(AppAccountTable.KEY_MAIL, accounts[0].name);
                startLogsIntent(appAccount);
            } catch (Exception e) {
                Log.e("bg2","ExcepXX",e);
                startLoginIntent();
            }
        }

    }
    private void startLogsIntent(AppAccount appAccount) {
        try {
            applicationBg.setAccount(appAccount);
            Log.i(TAG,"appAccount : "+appAccount);
            Intent intent = new Intent(this, ActivityLogs2.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.w("bg2","ExcepXX",e);
            startLoginIntent();
        }
    }
    private void startLoginIntent() {
        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
        finish();
    }

    private String[] getListPermissionToRequest( String[] permissions) {
        List<String> lisPermissionRefused = new ArrayList<String>();
        for (String permission : permissions){
            if (!isGranted( permission)){
                lisPermissionRefused.add(permission);
            }
        }
        return toStringArray(lisPermissionRefused);
    }
    private boolean isGranted( String permission) {
        return ContextCompat.checkSelfPermission(this,
                permission) == PackageManager.PERMISSION_GRANTED;
    }

    // Function to convert ArrayList<String> to String[]
    private static String[] toStringArray(List<String> list)    {
        String strArray[] = new String[list.size()];
        for (int j = 0; j < list.size(); j++) {
            strArray[j] = list.get(j);
        }
        return strArray;
    }

    private String  toString(Object[] oo){
        if (oo == null){
            return null;
        }
        String s ="";
        for(Object o : oo){
            s += " "+o+";";
        }
        return s;
    }
}
