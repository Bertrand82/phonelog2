package phone.crm2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.material.snackbar.Snackbar;

import phone.crm2.legacy.UtilContact;
import phone.crm2.model.Contact;
import phone.crm2.model.PhoneCall;

public class FragmentComment extends Fragment {

	private final String TAG = "bg2";

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

		this.applicationBg = this.getApplicationBg();
		Bundle bundle = getArguments();
		if (bundle == null) {
			this.storage = null;
		} else {
			this.storage = (BgCalendar) bundle.getSerializable("storage");
			this.mailSent=bundle.getBoolean(KEY_SENT_MAIL, false);
		}
		PhoneCall phoneCallFromExtra =(PhoneCall)  bundle.get(PhoneCall.KEY_PHONE_CALL_EXTRA);


		if (phoneCallFromExtra != null) {
			this.phoneCall = phoneCallFromExtra;
		} else {
			// TODO on ne doit plus gerer le phoneCAll dans applicationBg.
			this.phoneCall = applicationBg.getPhoneCall();
		}

		if (this.phoneCall == null){
			this.phoneCall =applicationBg.getPhoneCall();
		}
		if (phoneCall == null) {
			UtilActivitiesCommon.openLogs(this.getActivity());
			return;
		}
		ImageView imageViewPhoto = this.getActivity().findViewById(R.id.logoPhoto2);
		TextView textViewPhoto = this.getActivity().findViewById(R.id.logoPhotoText2);

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
		textViewContact = this.getActivity().findViewById(R.id.labelContact3);
		String nameContact = phoneCall.getContact().getExtra(this.applicationBg).getDisplayName();

		textViewContact.setText(nameContact);

		textViewNumber = this.getActivity().findViewById(R.id.labelNumber2);
		String number = phoneCall.getContact().getNumber();
		textViewNumber.setText(number);
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
		Button buttonDeleteEnregistrement = this.getActivity().findViewById(R.id.buttonDeleteEnregistrement);
		OnClickListener listenerDelete = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//UtilEmail.sendMessage(FragmentComment.this.getActivity(),phoneCall);

				//FragmentComment.this.finish();// Empeche la navigation arriere
			}
		};
		buttonDeleteEnregistrement.setOnClickListener(listenerDelete);

		BootstrapButton buttonBackToDetail = this.getActivity().findViewById(R.id.button_back_to_detail);
		OnClickListener listenerButtoncallAgain = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (phoneCall != null) {
					UtilActivitiesCommon.displayActivityLogDetail(FragmentComment.this.getActivity(), phoneCall.getContact(),storage);
				}
			}
		};
		buttonBackToDetail.setOnClickListener(listenerButtoncallAgain);
		BootstrapButton buttonAddComment = this.getActivity().findViewById(R.id.button_add_comment);

		OnClickListener buttonListenerAddComment = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String comment = "" + editText.getText();
				PhoneCall phoneCall = applicationBg.getPhoneCall();

				if (phoneCall != null) {
					phoneCall.setComment(comment);
					// Send to ground
					UpdateResult result = UtilCalendar.update(applicationBg, phoneCall);

					showConfirmSend();
				}

			}
		};
		buttonAddComment.setOnClickListener(buttonListenerAddComment);

		buttonAddRemoveToPrivateList = this.getActivity().findViewById(R.id.button_add_remove_from_private_list);
		setButtonLabel();
		OnClickListener buttonListenerRemoveFromPrivateList = new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if (phoneCall == null) {
					return;
				}

				Contact contact = phoneCall.getContact();
				contact.setPrivate(!contact.isPrivate(applicationBg));
				applicationBg.getDb().getContact().update(contact);
				setButtonLabel();
			}
		};

		buttonAddRemoveToPrivateList.setOnClickListener(buttonListenerRemoveFromPrivateList);
	}

	private void setButtonLabel() {
		if (phoneCall == null) {
		}else if (phoneCall.getContact()==null){
		} else if (phoneCall.getContact().isPrivate(applicationBg)) {
			buttonAddRemoveToPrivateList.setText(getString(R.string.activity_comment_remove_from_private_list));
			//buttonAddRemoveToPrivateList.setBootstrapType("success");
		} else {
			buttonAddRemoveToPrivateList.setText(getString(R.string.activity_comment_add_to_private_list));
			//buttonAddRemoveToPrivateList.setBootstrapType("danger");
		}
	}

	public void editContact() {
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

	private void showConfirmSend() {

		String  text  =  ""+this.editText.getText();
		String number =  ""+this.textViewNumber.getText();
		String contact = ""+this.textViewContact.getText();
		String time =   ""+this.textViewTime.getText();



		try {
			hideKeyBoard();
			showMessage(text);
		} catch(Throwable e){
			Log.e("bg2","Exception ",e);
		}
		//UtilActivitiesCommon.showConfirmSend(this.getActivity(),text,number,contact,time,colorBackground,result_);

	}

    private void showMessage(String text){
		RelativeLayout layout  = this.getActivity().findViewById(R.id.layoutComment);
		Snackbar.make(layout, "Enregistré :" + text,
				Snackbar.LENGTH_SHORT)
				.show();
		UtilActivitiesCommon.displayActivityLogDetail(FragmentComment.this.getActivity(), phoneCall.getContact(),storage);
	}
    private void hideKeyBoard() {
		View view = this.getActivity().getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	public ApplicationBg getApplicationBg() {
		return (ApplicationBg) this.getActivity().getApplication();
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			UtilActivitiesCommon.displayActivityLogDetail(this.getActivity(), phoneCall.getContact(), storage);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}



}
