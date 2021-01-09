package phone.crm2;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import phone.crm2.legacy.UtilContact;
import phone.crm2.model.Contact;

/**
 *
 */
public class ActivityFormClientId extends AbstractActivityCrm {

	private ApplicationBg applicationBg;
	private Contact contact;
	private EditText editTextClientId;
	private BgCalendar storage;
	private Button buttonSetClientId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_client_id);

		this.applicationBg = (ApplicationBg) this.getApplicationContext();

		Bundle b = getIntent().getExtras();
		// long contactId = b.getLong("contactId");
		this.contact = (Contact) b.getSerializable("contact");
		this.storage = (BgCalendar) b.getSerializable("storage");
		this.contact.getExtra(this);
		Log.i("bg2", "ActivityFormClientId    contact : " + contact);

		buttonSetClientId = (Button) findViewById(R.id.buttonSetClientId);
		buttonSetClientId.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				actionSetClientId();
			}
		});

		String displayName = contact.getExtra(applicationBg).getDisplayName();
		if (displayName == null) {
			displayName = contact.getContactNameOrNumber();
		}
		setTitle(displayName);

		TextView textViewContact = (TextView) findViewById(R.id.labelContact);
		textViewContact.setText(displayName);

		String normalisedNumber = contact.getExtra(applicationBg).getNormalizedNumber();
		if (normalisedNumber == null) {
			normalisedNumber = contact.getNumber();
		}
		TextView textViewDetailNumber = (TextView) findViewById(R.id.labelNumber);
		textViewDetailNumber.setText(normalisedNumber);

		ImageView imageViewPhoto = (ImageView) findViewById(R.id.logoPhoto);
		imageViewPhoto.setImageURI(contact.getExtra(applicationBg).getPhotoUri());
		imageViewPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				UtilContact.updateContact(ActivityFormClientId.this, contact);
			}
		});

		this.editTextClientId = (EditText) findViewById(R.id.editTextClientId);
		String clientId = this.contact.getClientId(this);
		if (clientId != null) {
			this.editTextClientId.setText(clientId);
		}
	}

	private void actionSetClientId() {
		Log.i("bg2", "actionSetClientId contact :" + contact);
		Log.i("bg2", "actionSetClientId contact : getRaw_contact_id " + contact.getExtra(this).getRaw_contact_id(this));
		String clientId = this.editTextClientId.getText().toString();
		Log.i("bg2", "actionSetClientId clientId :" + clientId);

		// Insert
		Long raw_contact_id = contact.getExtra(this).getRaw_contact_id(this);
		if (contact.getClientId(this) == null) {
			try { // Insert
				Log.i("bg2","CRM ClientId insert");				
				insert(clientId, raw_contact_id);
				UtilActivitiesCommon.displayActivityLogDetatil(this, contact, storage, false);
			} catch (Exception e) {
				Log.w("bg2", "actionSetClientId 1 exception", e);
				UtilActivitiesCommon.popUp(this, "Exception Inserting ClientId", "Exception " + e.getMessage());
			}
		} else {
			try {
				Log.i("bg2","CRM ClientId Update start");
				update( raw_contact_id,clientId);
				contact.setClientId(clientId);
				Log.i("bg2","CRM ClientId Update done");
				UtilActivitiesCommon.displayActivityLogDetatil(this, contact, storage, false);
			} catch (Exception e) {
				Log.w("bg2", "actionSetClientId 2 exception", e);
				UtilActivitiesCommon.popUp(this, "Exception updating ClientId", "Exception " + e.getMessage());
			}
		}
	}

	/**
	 * 
	 * @param clientId
	 * @param raw_contact_id
	 * @return
	 * @throws Exception
	 */

	public int insert_WORKING(String clientId, Long raw_contact_id) throws Exception {
		ArrayList<ContentProviderOperation> listOperations = new ArrayList<ContentProviderOperation>();
		String custom = "vnd.com.google.cursor.item/contact_user_defined_field";
		// ops.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI) //
		// Doesn't work Suppress Contact !!
		// ContentProviderOperation cpo =
		// ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI).withSelection(selection,selectionParams)
		// .withValue(RawContacts.Data.DATA2, clientId).build();
		ContentProviderOperation cpo = ContentProviderOperation.newInsert(Data.CONTENT_URI).withValue(Data.RAW_CONTACT_ID, raw_contact_id).withValue(Data.MIMETYPE, custom).withValue(RawContacts.Data.DATA2, clientId).withValue(RawContacts.Data.DATA1, "clientId").build();
		listOperations.add(cpo);
		ContentProviderResult[] results = this.getContentResolver().applyBatch(ContactsContract.AUTHORITY, listOperations);
		int i = 0;
		Log.i("bg2", i + "nb update results.length :" + results.length);
		for (ContentProviderResult cpr : results) {
			Log.i("bg2", i + " update ContentProviderResult :" + cpr.uri + "   describeContents :" + cpr.describeContents());
		}
		return results.length;
	}

	public void insert(String clientId, Long raw_contact_id) throws Exception {
		String custom = "vnd.com.google.cursor.item/contact_user_defined_field";
		Uri uri = Data.CONTENT_URI;
		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, raw_contact_id);
		values.put(Data.MIMETYPE, custom);
		values.put(RawContacts.Data.DATA2, clientId);
		values.put(RawContacts.Data.DATA1, "clientId");

		ContentResolver cr = this.getContentResolver();
		Uri uriResult = cr.insert(uri, values);
		Log.i("bg2", "uriResult " + uriResult);
	}
	
	private static final String custom=  "vnd.com.google.cursor.item/contact_user_defined_field";
	
	
	
	public int update(Long raw_contact_id,String clientId) throws Exception{
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		String[] values =  {""+raw_contact_id};
		 ops.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI)
				 
		          .withSelection(Data.RAW_CONTACT_ID + "=?",values)
		          .withValue(Data.MIMETYPE,custom )
		          .withValue(RawContacts.Data.DATA2, clientId)
		          .withValue(Data.DATA2, clientId)
		          .withValue(RawContacts.Data.DATA1, "clientId")
		         .build());
		 ContentProviderResult[] cpResult = this.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		 Log.i("bg2","CRM ClientId Update cpResult.length: "+cpResult.length);
			
		 for(ContentProviderResult c :cpResult){
			 Log.i("bg2","CRM ClientId Update ContentProviderResult "+c.describeContents());
		 }
		 return cpResult.length;
	}
}
