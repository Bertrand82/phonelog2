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
import phone.crm2.model.PhoneCall;

public class FragmentMain extends Fragment {

    private final String TAG = "bg "+getClass().getSimpleName();
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
        String gotoFragment=null;
        PhoneCall phoneCall= null;
       // Bundle b = getArguments(); Ne marche pas
        Bundle bArgs = savedInstanceState;
        Bundle bIntent =getActivity().getIntent().getExtras();
        if(bIntent!= null) {
            gotoFragment = bIntent.getString(ActivityPhoneLog.KEY_DISPLAY);
            phoneCall = (PhoneCall) bIntent.getSerializable(PhoneCall.KEY_PHONE_CALL_EXTRA);
        }
        initFragmentMain(gotoFragment,phoneCall);
    }



    protected void initFragmentMain(String gotoFragment,PhoneCall phoneCall) {
        this.applicationBg = (ApplicationBg) this.getActivity().getApplication();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());


        AccountManager accountManager = AccountManager.get(applicationBg);
        Account[] accounts = accountManager.getAccountsByType(ApplicationBg.ACCOUNT_TYPE);
        if (accounts.length == 0) { // Il n'y a pas de compte de type cafe-crm2
            startLoginIntent();// On ne va pas plus loin.Identification required
        } else {
            if (gotoFragment== null) {
                try {
                    AppAccount appAccount = applicationBg.getDb().getAppAccount().getBy(AppAccountTable.KEY_MAIL, accounts[0].name);
                    startLogsIntent(appAccount);
                } catch (Exception e) {
                    Log.e("bg2", "ExcepXX", e);
                    startLoginIntent();
                }
            }else if (gotoFragment.equals(FragmentComment.class.getName())){
                UtilActivitiesCommon.openComment(this.getActivity(),phoneCall,applicationBg.getStorageCalendar());
            }
        }

    }
    private void startLogsIntent(AppAccount appAccount) {
        try {
            applicationBg.setAccount(appAccount);
            UtilActivitiesCommon.openLogs(this.getActivity());

        } catch (Exception e) {
            Log.w("bg2","FragmentMAin ExcepX",e);
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
