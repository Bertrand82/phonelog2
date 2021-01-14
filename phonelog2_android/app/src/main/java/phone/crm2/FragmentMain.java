package phone.crm2;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import phone.crm2.db.AppAccountTable;
import phone.crm2.model.AppAccount;

public class FragmentMain extends Fragment {

    private String TAG = "bg "+getClass().getSimpleName();
    private static final int REQUEST_RUNTIME_PERMISSION = 123;
    public static final String SERVICE_URL_CAFE_CRM = "http://phone-log.appspot.com/r/phonecall";
    ApplicationBg applicationBg;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState   ) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }



    protected void init() {

        Log.w(TAG,"FragmentMain OnCreate 1");


        this.applicationBg = (ApplicationBg) this.getActivity().getApplication();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());


        AccountManager accountManager = AccountManager.get(applicationBg);
        Account[] accounts = accountManager.getAccountsByType(applicationBg.ACCOUNT_TYPE);

        Log.i(TAG,"FragmentMain accounts: "+toString(accounts));

        if (accounts.length == 0) { // Il n'y a pas de compte de type cafe-crm2
            Log.w(TAG,"FragmentMain OnCreate Start Login");
            startLoginIntent();
        } else {
            Log.w(TAG,"FragmentMain OnCreate  Start display logs ");
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
            Log.i(TAG,"FragmentMain appAccount displayLogsRequest  appAccount : "+appAccount);
            UtilActivitiesCommon.openLogs(this.getActivity());

        } catch (Exception e) {
            Log.w("bg2","FragmentMAin ExcepXX",e);
            startLoginIntent();
        }
    }
    private void startLoginIntent() {
        UtilActivitiesCommon.openLogin(this.getActivity());
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
