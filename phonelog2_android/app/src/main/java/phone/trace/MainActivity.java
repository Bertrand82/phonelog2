package phone.trace;

import phone.trace.db.AppAccountTable;
import phone.trace.model.AppAccount;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class MainActivity extends AbstractActivityCrm {

    private String TAG = getClass().getSimpleName();

    public static final String SERVICE_URL_CAFE_CRM = "http://phone-log.appspot.com/r/phonecall";
    ApplicationBg applicationBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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






}
