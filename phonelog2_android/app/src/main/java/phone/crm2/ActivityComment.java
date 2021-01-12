package phone.crm2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import java.io.Serializable;

import phone.crm2.legacy.UtilContact;
import phone.crm2.model.Contact;
import phone.crm2.model.PhoneCall;

public class ActivityComment extends AbstractActivityCrm {

	private String TAG = getClass().getSimpleName();

	private BootstrapEditText editText;
	private BootstrapButton buttonAddRemoveToPrivateList;
	private ApplicationBg applicationBg;
	private TextView textViewContact;
	private TextView textViewNumber;
	private TextView textViewTime;
	private AwesomeTextView imagePhoneOuMessage;
	private int colorBackground = -1;
	private PhoneCall phoneCall;
	private boolean mailSent = false;
	private BgCalendar storage;

	private static PhoneCall phoneCall_Z_1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Log.i("bg2","ActivityComment onCreate");
		this.applicationBg = this.getApplicationBg();
		Bundle bundle = getIntent().getExtras();
		Log.i("bg2","ActivityComment onCreate bundle : "+bundle);
		if (bundle == null) {
			this.storage = null;
		} else {
			this.storage = (BgCalendar) bundle.getSerializable("storage");
			this.mailSent=bundle.getBoolean(KEY_SENT_MAIL, false);
		}
		PhoneCall phoneCallFromExtra =  (PhoneCall)getIntent().getSerializableExtra(PhoneCall.KEY_PHONE_CALL_EXTRA);


		if (phoneCallFromExtra != null) {
			Log.i("bg2","ActivityComment phoneCall In extra");
			this.phoneCall = phoneCallFromExtra;
		} else {
			// TODO on ne doit plus gerer le phoneCAll dans applicationBg.
			Log.i("bg2","ActivityComment phoneCall In applicationBg");
			this.phoneCall = applicationBg.getPhoneCall();
		}

		// if no phonecall, we redirect the user to the logs
		if (applicationBg.getPhoneCall() == null) {
			Log.i("bg2", "ActivityComment No last PhoneCall redirect on logs");
			Intent intent = new Intent(this, ActivityLogs2.class);
			startActivity(intent);
			return;
		}

		Log.d("bg2","ActivityComment show "+this.applicationBg.isForeground());

		setContentView(R.layout.activity_comment);

		ImageView imageViewPhoto = (ImageView) findViewById(R.id.logoPhoto);
		TextView textViewPhoto = (TextView) findViewById(R.id.logoPhotoText);

		UtilLogoPhoto.init(this, textViewPhoto, imageViewPhoto, phoneCall.getContact());

		// imageViewPhoto.setImageURI(phoneCall.getContact().getExtra(this.applicationBg).getPhotoUri());
		OnClickListener listenerEditContact = new OnClickListener() {

			@Override
			public void onClick(View v) {
				editContact();
			}
		};
		if (imageViewPhoto.getVisibility() == View.VISIBLE) {
			imageViewPhoto.setOnClickListener(listenerEditContact);
		}
		if (textViewPhoto.getVisibility() == View.VISIBLE) {
			textViewPhoto.setOnClickListener(listenerEditContact);
		}
		textViewContact = (TextView) findViewById(R.id.labelContact);
		textViewContact.setText(phoneCall.getContact().getExtra(this.applicationBg).getDisplayName());

		textViewNumber = (TextView) findViewById(R.id.labelNumber);
		textViewNumber.setText(phoneCall.getContact().getNumber());

		imagePhoneOuMessage = (AwesomeTextView) findViewById(R.id.logoPhoneOuMessage);
		UtilActivitiesCommon.setImagePhoneOuMessage(phoneCall.getType(), imagePhoneOuMessage);

		AwesomeTextView imageViewType = (AwesomeTextView) findViewById(R.id.logoType);
		UtilActivitiesCommon.setImage(phoneCall.getType(), imageViewType);

		textViewTime = (TextView) findViewById(R.id.labelTime);
		textViewTime.setText(phoneCall.getDateAsHour());

		editText = (BootstrapEditText) findViewById(R.id.editText1);
		String comment = phoneCall.getComment();
		if (comment != null) {
			editText.setText(comment);
		}
		Button buttonMask = (Button) findViewById(R.id.buttonMask);
		OnClickListener listenerButtonMask = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				UtilEmail.sendMessage(ActivityComment.this,phoneCall);
				Log.i("bg2", "MAsk  finish");
				//ActivityComment.this.finish();// Empeche la navigation arriere
			}
		};
		buttonMask.setOnClickListener(listenerButtonMask);

		BootstrapButton buttoncallAgain = (BootstrapButton) findViewById(R.id.button_call_again);
		OnClickListener listenerButtoncallAgain = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.i("bg2", "CallAgain Contact start");

				if (phoneCall != null) {
					Log.i("bg2", "Edit Contact : " + phoneCall.getContact());
					UtilActivitiesCommon.callNumber(ActivityComment.this, phoneCall.getContact().getNumber());
				}
			}
		};
		buttoncallAgain.setOnClickListener(listenerButtoncallAgain);
		BootstrapButton buttonEnvoi = (BootstrapButton) findViewById(R.id.button_envoi);

		OnClickListener buttonListenerEnvoi = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String comment = "" + editText.getText();
				PhoneCall phoneCall = applicationBg.getPhoneCall();

				if (phoneCall != null) {
					phoneCall.setComment(comment);
					Log.i(TAG, "comment :" + comment);
					// Send to ground
					UpdateResult result = UtilCalendar.update(applicationBg, phoneCall);

					showConfirmSend(result);
					finish();
				}

			}
		};
		buttonEnvoi.setOnClickListener(buttonListenerEnvoi);

		buttonAddRemoveToPrivateList = (BootstrapButton) findViewById(R.id.button_add_remove_from_private_list);
		setButtonLabel();
		OnClickListener buttonListenerRemoveFromPrivateList = new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if (phoneCall == null) {
					return;
				}

				Contact contact = phoneCall.getContact();
				Log.i(TAG, "Change contact privacy :" + contact);
				contact.setPrivate(!contact.isPrivate(applicationBg));
				applicationBg.getDb().getContact().update(contact);
				setButtonLabel();
			}
		};

		buttonAddRemoveToPrivateList.setOnClickListener(buttonListenerRemoveFromPrivateList);

	}

	private void setButtonLabel() {
		if (phoneCall == null) {
		} else if (phoneCall.getContact().isPrivate(applicationBg)) {
			buttonAddRemoveToPrivateList.setText(getString(R.string.activity_comment_remove_from_private_list));
			//buttonAddRemoveToPrivateList.setBootstrapType("success");
		} else {
			buttonAddRemoveToPrivateList.setText(getString(R.string.activity_comment_add_to_private_list));
			//buttonAddRemoveToPrivateList.setBootstrapType("danger");
		}
	}

	public void editContact() {
		Log.i(TAG, "editContact from log detail Contact_LEGACY_DEPRECATED :" + phoneCall.getContact());
		UtilContact.updateContact(this, phoneCall.getContact());
	}

	public static String KEY_MESSAGE = "bg.message";
	public static String KEY_CONTACT = "bg.contact";
	public static String KEY_NUMBER = "bg.Number";
	public static String KEY_TIME = "bg.Time";
	public static String KEY_TYPE = "bg.Type";
	public static String KEY_COLOR_BACKGROUND = "bg.Color";
	public static String KEY_RESULT_CALENDAR = "bg.ResultCalendar";
	public static String KEY_RESULT_BDD_________________old = "bg.ResultBDD_";

	public static String KEY_SENT_MAIL ="sendMAil";

	private void showConfirmSend(UpdateResult result_) {
		Intent intent = new Intent(ActivityComment.this, ActivityConfirmSend.class);
		Bundle b = new Bundle();
		b.putString(KEY_MESSAGE, "" + this.editText.getText());
		b.putString(KEY_NUMBER, "" + this.textViewNumber.getText());
		b.putString(KEY_CONTACT, "" + this.textViewContact.getText());
		b.putString(KEY_TIME, "" + this.textViewTime.getText());
		b.putInt(KEY_COLOR_BACKGROUND, colorBackground);
		if (result_ == null) {
			result_ = new UpdateResult();
		}
		b.putSerializable(KEY_RESULT_CALENDAR, (Serializable) result_);
		intent.putExtras(b); // Put your id to your next Intent
		startActivity(intent);
		finish();
	}

	public ApplicationBg getApplicationBg() {
		return (ApplicationBg) getApplication();
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			UtilActivitiesCommon.displayActivityLogDetatil(this, phoneCall.getContact(), storage, false);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	private void mailSend_DEPRECATED() {
		if (phoneCall == null) {// inutile d'envoyer un mail!
			Log.w("bg2", "mailSend phoneCall is Null!! ");
			return;
		}
		if (mailSent == true) {// Le mail a été deja envoyé
			return;
		}
		if (phoneCall.equals2(phoneCall_Z_1)) {// Le mail a été deja envoyé
			return;
		}
		phoneCall_Z_1 = phoneCall;
		mailSent = UtilEmail.sendMessage(this, phoneCall);
	}

}
