package phone.trace;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tagmanager.TagManager;
//import com.google.tagmanager.ContainerOpener;
//import com.google.tagmanager.ContainerOpener.OpenType;


import phone.trace.model.AppAccount;

/**
 * The Authenticator activity.
 * <p/>
 * Called by the Authenticator and in charge of identifing the user.
 * <p/>
 * It sends back to the Authenticator the hResult.
 */
public class ActivityLogin extends AccountAuthenticatorActivityBg {

	private String TAG = "bg2";
	EditText editTextAccountName;
	Button mLoginButton;
	ApplicationBg context;
	private static final String CONTAINER_ID = "UA-48106381-1";// "GTM-XXXX";//
																// UA-48106381-1

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "ActivityLogin 1 onCreate ");
		super.onCreate(savedInstanceState);
		context = (ApplicationBg) this.getApplication();
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccountsByType(ApplicationBg.ACCOUNT_TYPE);
		TagManager tagManager = TagManager.getInstance(this);
		tagManager.setVerboseLoggingEnabled(true);
		//ContainerOpener.openContainer(tagManager, CONTAINER_ID, OpenType.PREFER_NON_DEFAULT, null);
		Log.i(TAG, "ActivityLogin 2 accounts.length  "+accounts.length);
		if (accounts.length == 0) {
			setContentView(R.layout.activity_login);
			editTextAccountName = (EditText) findViewById(R.id.accountName);
			editTextAccountName.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					if (!arg1 && !isValidLogin(editTextAccountName.getText())) {
						editTextAccountName.setError("Invalid email!!");
					}
				};
			});
			String accountName = editTextAccountName.getText().toString();
			if (accountName.trim().length() == 0) {
				editTextAccountName.setText(UtilActivitiesCommon.getPossiblePrimaryEmailAdress(context));
			}
			mLoginButton = (Button) findViewById(R.id.submitSignIn);
			mLoginButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					boolean isValidLogin = isValidLogin(("" + editTextAccountName.getText()).trim());
					Log.i(TAG, "ActivityLogin On Click  isValidEmail >" + editTextAccountName.getText());
					Log.i(TAG, "ActivityLogin  On Click  isValidEmail " + isValidLogin);
					if (isValidLogin) {
						String mail = editTextAccountName.getText().toString().trim();
						String password = "xxx";
						if (mail.length() > 0 && password.length() > 0) {
							AppAccount appAccount = new AppAccount();
							appAccount.setMail(mail);
							appAccount.setPassword(password);
							createAccountOnDevice(appAccount);
						}
					} else {
						CharSequence text = "Invalid email! \n " + editTextAccountName.getText();
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast.makeText(ActivityLogin.this, text, duration);
						toast.setGravity(Gravity.TOP, 0, 200);
						toast.show();
					}
				}
			});
			Log.i(TAG, "ActivityLogin 3 ");
		} else {// accounts.length != 0
			CharSequence text = "You already have an account on this phone!";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(ActivityLogin.this, text, duration);
			toast.show();
			finish();
		}
	}

	private Boolean createAccountOnDevice(AppAccount appAccount) {
		Log.i(TAG, "ActivityLogin createAccountOnDevice appAccount :" + appAccount);
		appAccount = context.getDb().getAppAccount().insert(appAccount);
		Bundle result = null;
		Account account = new Account(appAccount.getMail(), context.getString(R.string.ACCOUNT_TYPE));
		AccountManager am = AccountManager.get(context);
		boolean isAccountAdded;
		if (am.addAccountExplicitly(account, appAccount.getPassword(), null)) {
			result = new Bundle();
			result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
			result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
			setAccountAuthenticatorResult(result);

			Intent intent = new Intent(context, ActivityListCalendars2.class);
			startActivity(intent);

			isAccountAdded = true;
			this.finish();
		} else {
			isAccountAdded = false;
		}
		Log.i(TAG, "ActivityLogin isAccountAdded " + isAccountAdded);
		return isAccountAdded;
	}

	public static boolean isValidLogin(CharSequence s) {
		if (s == null) {
			return false;
		}
		if (s.toString().trim().length() < 3) {
			return false;
		} else {
			return true;
		}
	}

}
