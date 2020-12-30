package phone.trace;

import phone.trace.db.AppAccountTable;
import phone.trace.model.AppAccount;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.instantapps.PackageManagerCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "bg "+getClass().getSimpleName();
    private static final int REQUEST_RUNTIME_PERMISSION = 123;
    public static final String SERVICE_URL_CAFE_CRM = "http://phone-log.appspot.com/r/phonecall";
    ApplicationBg applicationBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissions = {
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
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.READ_SYNC_SETTINGS,
                android.Manifest.permission.WRITE_CONTACTS,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.READ_PHONE_STATE
        };

        String[] permissionsToRequest  = getListPermissionToRequest( permissions);
        ActivityCompat.requestPermissions(this, permissionsToRequest, REQUEST_RUNTIME_PERMISSION);


        this.applicationBg = (ApplicationBg) this.getApplication();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        AccountManager accountManager = AccountManager.get(applicationBg);
        Account[] accounts = accountManager.getAccountsByType(applicationBg.ACCOUNT_TYPE);

        Log.i(TAG,accounts.toString());

        if (accounts.length == 0) { // Il n'y a pas de compte de type cafe-crm
            startLoginIntent();

        } else {
            try {
                AppAccount appAccount = applicationBg.getDb().getAppAccount().getBy(AppAccountTable.KEY_MAIL, accounts[0].name);

                applicationBg.setAccount(appAccount);
                Log.i(TAG,accounts[0].toString());
                Log.i(TAG,appAccount.toString());
                Intent intent = new Intent(this, ActivityLogs.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.w("bg2","ExcepXX",e);
                startLoginIntent();
            }
        }

    }

    private void startLoginIntent() {
        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
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

}
