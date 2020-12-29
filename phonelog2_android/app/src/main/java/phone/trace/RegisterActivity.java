package phone.trace;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;


/**
 * In charge of the Sign up process. Since it's not an AuthenticatorActivity decendent,
 * it returns the hResult back to the calling activity, which is an AuthenticatorActivity,
 * and it return the hResult back to the Authenticator
 *
 */
public class RegisterActivity extends Activity {

    private String TAG = getClass().getSimpleName();
    private String mAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mAccountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        setContentView(R.layout.activity_register);

        findViewById(R.id.alreadyMember).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {

//        // Validation!
//
//        new AsyncTask<String, Void, Intent>() {
//
//            String accountName = ((TextView) findViewById(R.id.accountName)).getText().toString().trim();
//            String accountPassword = ((TextView) findViewById(R.id.accountPassword)).getText().toString().trim();
//
//            @Override
//            protected Intent doInBackground(String... params) {
//
//                Log.d("udinic", TAG + "> Started authenticating");
//
//                String authtoken = null;
//                Bundle data = new Bundle();
//                try {
//                    User user = sServerAuthenticate.userSignUp(accountName, accountPassword, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
//                    if (user != null)
//                        authtoken = user.getSessionToken();
//
//                    data.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
//                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
//                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
//
//                    // We keep the user's object id as an extra data on the account.
//                    // It's used later for determine ACL for the data we send to the Parse.com service
//                    Bundle userData = new Bundle();
//                    userData.putString(USERDATA_USER_OBJ_ID, user.getObjectId());
//                    data.putBundle(AccountManager.KEY_USERDATA, userData);
//
//                    data.putString(PARAM_USER_PASS, accountPassword);
//                } catch (Exception e) {
//                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
//                }
//
//                final Intent res = new Intent();
//                res.putExtras(data);
//                return res;
//            }
//
//            @Override
//            protected void onPostExecute(Intent intent) {
//                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
//                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
//                } else {
//                    setResult(RESULT_OK, intent);
//                    finish();
//                }
//            }
//        }.execute();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}