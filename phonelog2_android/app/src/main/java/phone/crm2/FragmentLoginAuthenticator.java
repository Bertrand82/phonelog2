package phone.crm2;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tagmanager.TagManager;

import phone.crm2.model.AppAccount;

//import com.google.tagmanager.ContainerOpener;
//import com.google.tagmanager.ContainerOpener.OpenType;

/**
 * The Authenticator activity.
 * <p/>
 * Called by the Authenticator and in charge of identifing the user.
 * <p/>
 * It sends back to the Authenticator the hResult.
 */
public class FragmentLoginAuthenticator extends Fragment {

	private final String TAG = "bg2 FragmentLoginAuthenticator";
	private EditText editTextAccountName;
	private ApplicationBg context;
	private static final String CONTAINER_ID = "UA-48106381-1";// "GTM-XXXX";//
																// UA-48106381-

	private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
	private Bundle mResultBundle = null;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_login, container, false);

	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	/** Called when the activity is first created. */

	public void init() {
		Log.i(TAG, "FragmentLogin 1 init ");

		context = (ApplicationBg) this.getActivity().getApplication();
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccountsByType(ApplicationBg.ACCOUNT_TYPE);
		TagManager tagManager = TagManager.getInstance(this.getActivity());
		tagManager.setVerboseLoggingEnabled(true);
		//ContainerOpener.openContainer(tagManager, CONTAINER_ID, OpenType.PREFER_NON_DEFAULT, null);
		Log.i(TAG, "FragmentLoginAuth 2 accounts.length  "+accounts.length);
		if (accounts.length == 0) {

			editTextAccountName = getActivity().findViewById(R.id.accountName);
			editTextAccountName.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					if (!arg1 && !isValidLogin(editTextAccountName.getText())) {
						editTextAccountName.setError("Invalid email!!");
					}
				}
            });
			String accountName = editTextAccountName.getText().toString();
			if (accountName.trim().length() == 0) {
				editTextAccountName.setText(UtilActivitiesCommon.getPossiblePrimaryEmailAdress(context));
			}
			Button mLoginButton = getActivity().findViewById(R.id.submitSignIn);
			mLoginButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					boolean isValidLogin = isValidLogin(("" + editTextAccountName.getText()).trim());
					Log.i(TAG, "FragmentLoginAuth On Click  isValidEmail >" + editTextAccountName.getText());
					Log.i(TAG, "FragmentLoginAuth  On Click  isValidEmail " + isValidLogin);
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
						Toast toast = Toast.makeText(FragmentLoginAuthenticator.this.getActivity(), text, duration);
						toast.setGravity(Gravity.TOP, 0, 200);
						toast.show();
					}
				}
			});
			Log.i(TAG, "FragmentLoginAuth 3 ");
		} else {// accounts.length != 0
			CharSequence text = "You already have an account on this phone!";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(FragmentLoginAuthenticator.this.getActivity(), text, duration);
			toast.show();
			finishAndClean();
		}
	}

	private Boolean createAccountOnDevice(AppAccount appAccount) {
		Log.i(TAG, "FragmentLoginAuth createAccountOnDevice appAccount :" + appAccount);
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
			UtilActivitiesCommon.openListCalendars(this.getActivity());

			isAccountAdded = true;
			this.finishAndClean();
		} else {
			isAccountAdded = false;
		}
		Log.i(TAG, "FragmentLoginAuth isAccountAdded " + isAccountAdded);
		return isAccountAdded;
	}

	public static boolean isValidLogin(CharSequence s) {
		if (s == null) {
			return false;
		}
        return s.toString().trim().length() >= 3;
	}

	/**
	 * Sends the result or a Constants.ERROR_CODE_CANCELED error if a result isn't present.
	 */
	public void finishAndClean() {
		if (mAccountAuthenticatorResponse != null) {
			// send the result bundle back if set, otherwise send an error.
			if (mResultBundle != null) {
				mAccountAuthenticatorResponse.onResult(mResultBundle);
			} else {
				mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED,
						"canceled");
			}
			mAccountAuthenticatorResponse = null;
		}

	}


	/**
	 * Set the result that is to be sent as the result of the request that caused this
	 * Activity to be launched. If result is null or this method is never called then
	 * the request will be canceled.
	 * @param result this is returned as the result of the AbstractAccountAuthenticator request
	 */
	public final void setAccountAuthenticatorResult(Bundle result) {
		mResultBundle = result;
	}

}
