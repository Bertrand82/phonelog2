package phone.crm2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import phone.crm2.legacy.UtilContact;
import phone.crm2.model.Contact;
import phone.crm2.model.PhoneCall;

public class FragmentComment extends Fragment {

	private final String TAG = getClass().getSimpleName();

	private BootstrapEditText editText;
	private BootstrapButton buttonAddRemoveToPrivateList;
	private ApplicationBg applicationBg;
	private TextView textViewContact;
	private TextView textViewNumber;
	private TextView textViewTime;

	private PhoneCall phoneCall;
	private boolean mailSent = false;
	private BgCalendar storage;

	private static PhoneCall phoneCall_Z_1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_comment, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initFragmentComment();
	}

	private void initFragmentComment() {



		Log.i("bg2","FragmentComment onCreate");
		this.applicationBg = this.getApplicationBg();
		Bundle bundle = getArguments();
		Log.i("bg2","FragmentComment onCreate bundle : "+bundle);
		if (bundle == null) {
			this.storage = null;
		} else {
			this.storage = (BgCalendar) bundle.getSerializable("storage");
			this.mailSent=bundle.getBoolean(KEY_SENT_MAIL, false);
		}
		PhoneCall phoneCallFromExtra =(PhoneCall)  bundle.get(PhoneCall.KEY_PHONE_CALL_EXTRA);


		if (phoneCallFromExtra != null) {
			Log.i("bg2","FragmentComment phoneCall In extra arguments");
			this.phoneCall = phoneCallFromExtra;
		} else {
			// TODO on ne doit plus gerer le phoneCAll dans applicationBg.
			Log.i("bg2","FragmentComment phoneCall In applicationBg");
			this.phoneCall = applicationBg.getPhoneCall();
		}

		if (this.phoneCall == null){
			Log.e("bg2", "FragmentComment no phoneCall in extra, i get celui de applicationBg . PAs Normal");
			this.phoneCall =applicationBg.getPhoneCall();
		}
		if (phoneCall == null) {
			Log.i("bg2", "FragmentComment No last PhoneCall redirect on logs fragment");
			UtilActivitiesCommon.openLogs(this.getActivity());
			return;
		}

		Log.d("bg2","FragmentComment isForeground "+this.applicationBg.isForeground());



		ImageView imageViewPhoto = this.getActivity().findViewById(R.id.logoPhoto);
		TextView textViewPhoto = this.getActivity().findViewById(R.id.logoPhotoText);

		UtilLogoPhoto.init(this.getActivity(), textViewPhoto, imageViewPhoto, phoneCall.getContact());

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
		textViewContact = this.getActivity().findViewById(R.id.labelContact);
		String nameContact = phoneCall.getContact().getExtra(this.applicationBg).getDisplayName();
		Log.v(TAG,"FragmentComment nameContact :"+nameContact);
		textViewContact.setText(nameContact);

		textViewNumber = this.getActivity().findViewById(R.id.labelNumber);
		textViewNumber.setText(phoneCall.getContact().getNumber());
		AwesomeTextView imagePhoneOuMessage = this.getActivity().findViewById(R.id.logoPhoneOuMessage);
		UtilActivitiesCommon.setImagePhoneOuMessage(phoneCall.getType(), imagePhoneOuMessage);

		AwesomeTextView imageViewType = this.getActivity().findViewById(R.id.logoType);
		UtilActivitiesCommon.setImage(phoneCall.getType(), imageViewType);

		textViewTime = this.getActivity().findViewById(R.id.labelTime);
		textViewTime.setText(phoneCall.getDateAsHour());

		editText = this.getActivity().findViewById(R.id.editText1);
		String comment = phoneCall.getComment();
		if (comment != null) {
			editText.setText(comment);
		}
		Button buttonMask = this.getActivity().findViewById(R.id.buttonMask);
		OnClickListener listenerButtonMask = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				UtilEmail.sendMessage(FragmentComment.this.getActivity(),phoneCall);
				Log.i("bg2", "MAsk  finish");
				//FragmentComment.this.finish();// Empeche la navigation arriere
			}
		};
		buttonMask.setOnClickListener(listenerButtonMask);

		BootstrapButton buttoncallAgain = this.getActivity().findViewById(R.id.button_call_again);
		OnClickListener listenerButtoncallAgain = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.i("bg2", "CallAgain Contact start");

				if (phoneCall != null) {
					Log.i("bg2", "Edit Contact : " + phoneCall.getContact());
					UtilActivitiesCommon.callNumber(FragmentComment.this.getActivity(), phoneCall.getContact().getNumber());
				}
			}
		};
		buttoncallAgain.setOnClickListener(listenerButtoncallAgain);
		BootstrapButton buttonEnvoi = this.getActivity().findViewById(R.id.button_envoi);

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
				}

			}
		};
		buttonEnvoi.setOnClickListener(buttonListenerEnvoi);

		buttonAddRemoveToPrivateList = this.getActivity().findViewById(R.id.button_add_remove_from_private_list);
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
		Log.v(TAG,"FragmentComment init done!!");
	}

	private void setButtonLabel() {
		if (phoneCall == null) {
		}else if (phoneCall.getContact()==null){
			Log.i(TAG,"fragmentComment No Contact for this number!! ");
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
		UtilContact.updateContact(this.getActivity(), phoneCall.getContact());
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
	public static String KEY_Storage ="storage";

	private void showConfirmSend(UpdateResult result_) {

		String  text  =  ""+this.editText.getText();
		String number =  ""+this.textViewNumber.getText();
		String contact = ""+this.textViewContact.getText();
		String time =   ""+this.textViewTime.getText();

		if (result_ == null) {
			result_ = new UpdateResult();
		}
		 int colorBackground = -1;
		UtilActivitiesCommon.showConfirmSend(this.getActivity(),text,number,contact,time,colorBackground,result_);

	}



	public ApplicationBg getApplicationBg() {
		return (ApplicationBg) this.getActivity().getApplication();
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			UtilActivitiesCommon.displayActivityLogDetail(this.getActivity(), phoneCall.getContact(), storage, false);
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
		if (mailSent ) {// Le mail a été deja envoyé
			return;
		}
		if (phoneCall.equals2(phoneCall_Z_1)) {// Le mail a été deja envoyé
			return;
		}
		phoneCall_Z_1 = phoneCall;
		mailSent = UtilEmail.sendMessage(this.getActivity(), phoneCall);
	}

}
