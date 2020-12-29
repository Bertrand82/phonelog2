
package phone.trace.services;

import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Entity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import phone.trace.DataBaseHandlerBg;
import phone.trace.R;
import phone.trace.model.Contact;
import phone.trace.model.PhoneCall;


public class ContactsSyncAdapterService extends Service {
	private static final String TAG = "ContactsSyncAdapterService";
	private static SyncAdapterImpl sSyncAdapter = null;
	private static ContentResolver mContentResolver = null;
	private static String PhoneNumberColumn = RawContacts.SYNC1;

	public ContactsSyncAdapterService() {
		super();
	}

	private static class SyncAdapterImpl extends AbstractThreadedSyncAdapter {
		private Context mContext;

		public SyncAdapterImpl(Context context) {
			super(context, true);
			mContext = context;
		}

		@Override
		public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
			try {
				ContactsSyncAdapterService.performSync(mContext, account, extras, authority, provider, syncResult);
			} catch (OperationCanceledException e) {
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		IBinder ret = null;
		ret = getSyncAdapter().getSyncAdapterBinder();
		return ret;
	}

	private SyncAdapterImpl getSyncAdapter() {
		if (sSyncAdapter == null)
			sSyncAdapter = new SyncAdapterImpl(this);
		return sSyncAdapter;
	}

	private static void addContact(Account account,Contact contact) {
		Log.i(TAG, "Adding contact: " + contact.getNumber());
		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();

		int rawContactInsertIndex = operationList.size();
		ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
		builder.withValue(RawContacts.ACCOUNT_NAME, account.name);
		builder.withValue(RawContacts.ACCOUNT_TYPE, account.type);
		builder.withValue(RawContacts.SYNC1, contact.getNumber());
		operationList.add(builder.build());

		//Create a Data record of common type 'StructuredName' for our RawContact
		 builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
		 builder.withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, rawContactInsertIndex);
		 builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
		 builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName());
		 operationList.add(builder.build());


		//Create a Data record of common type 'StructuredName' for our RawContact
		 builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
		 builder.withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, rawContactInsertIndex);
		 builder.withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
		 builder.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber());
		 operationList.add(builder.build());
		 
		builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
		builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex);
		builder.withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.bg.android_phone_log.activity_log_detail");
		builder.withValue(ContactsContract.Data.DATA1, contact.getNumber());
		builder.withValue(ContactsContract.Data.DATA2, "SyncProviderDemo Profile");
		builder.withValue(ContactsContract.Data.DATA3, "View profile");
//		builder.withValue(ContactsContract.Data.DATA2, R.string.app_name);
//		builder.withValue(ContactsContract.Data.DATA3, R.string.view_diary);
		builder.withYieldAllowed(true);
		operationList.add(builder.build());

		try {
			mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
		} catch (Exception e) {
			// Display a warning
//            Context ctx = getApplicationContext();
//
//            CharSequence txt = getString(R.string.contactCreationFailure);
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(ctx, txt, duration);
//            toast.show();

            // Log exception
            Log.e(TAG, "Exception encountered while inserting contact: " + e);
		}
	}

	private static void updateContactStatus(ArrayList<ContentProviderOperation> operationList, long rawContactId, String status) {
		Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId);
		Uri entityUri = Uri.withAppendedPath(rawContactUri, Entity.CONTENT_DIRECTORY);
		Cursor c = mContentResolver.query(entityUri, new String[] { RawContacts.SOURCE_ID, Entity.DATA_ID, Entity.MIMETYPE, Entity.DATA1 }, null, null, null);
		try {
			while (c.moveToNext()) {
				if (!c.isNull(1)) {
					String mimeType = c.getString(2);

					if (mimeType.equals( R.string.MIMETYPE_PHONELOG)) {
						ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.StatusUpdates.CONTENT_URI);
						builder.withValue(ContactsContract.StatusUpdates.DATA_ID, c.getLong(1));
						builder.withValue(ContactsContract.StatusUpdates.STATUS, status);
						builder.withValue(ContactsContract.StatusUpdates.STATUS_RES_PACKAGE, "org.c99.SyncProviderDemo");
						builder.withValue(ContactsContract.StatusUpdates.STATUS_LABEL, R.string.app_name);
						builder.withValue(ContactsContract.StatusUpdates.STATUS_ICON, R.drawable.ic_launcher);
						builder.withValue(ContactsContract.StatusUpdates.STATUS_TIMESTAMP, System.currentTimeMillis());
						operationList.add(builder.build());

						//Only change the text of our custom entry to the status message pre-Honeycomb, as the newer contacts app shows
						//statuses elsewhere
						if(Integer.decode(Build.VERSION.SDK) < 11) {
							builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
							builder.withSelection(BaseColumns._ID + " = '" + c.getLong(1) + "'", null);
							builder.withValue(ContactsContract.Data.DATA3, status);
							operationList.add(builder.build());
						}
					}
				}
			}
		} finally {
			c.close();
		}
	}
	
	private static void updateContactPhoto(ArrayList<ContentProviderOperation> operationList, long rawContactId, byte[] photo) {
		ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
		builder.withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = '" + rawContactId 
				+ "' AND " + ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null);
		operationList.add(builder.build());

		try {
			if(photo != null) {
				builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
				builder.withValue(ContactsContract.CommonDataKinds.Photo.RAW_CONTACT_ID, rawContactId);
				builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
				builder.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photo);
				operationList.add(builder.build());

				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static class SyncEntry {
		public Long raw_id = 0L;
		public Long phoneNumber = null;
	}

	private static void performSync(Context context, Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
			throws OperationCanceledException {
		HashMap<String, SyncEntry> localContacts = new HashMap<String, SyncEntry>();
		mContentResolver = context.getContentResolver();
		Log.i(TAG, "performSync: " + account.toString());

		// Load the local contacts
		Uri rawContactUri = RawContacts.CONTENT_URI.buildUpon().appendQueryParameter(RawContacts.ACCOUNT_NAME, account.name).appendQueryParameter(
				RawContacts.ACCOUNT_TYPE, account.type).build();
		Cursor c1 = mContentResolver.query(rawContactUri, new String[] { BaseColumns._ID, PhoneNumberColumn }, null, null, null);
		while (c1.moveToNext()) {
			SyncEntry entry = new SyncEntry();
			entry.raw_id = c1.getLong(c1.getColumnIndex(BaseColumns._ID));
			entry.phoneNumber = c1.getLong(c1.getColumnIndex(PhoneNumberColumn));
			localContacts.put(c1.getString(1), entry);
		}

		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();

		DataBaseHandlerBg db = new DataBaseHandlerBg(context);
		PhoneCall[] phoneCalls =db.getPhoneCallHandler(). getPhoneCallsArray();
		try {
			for(PhoneCall phoneCall : phoneCalls){
				if(localContacts.containsKey(phoneCall.getContact().getNumber())){
					//TODO update somthing?
//					addContact(account, phoneCall.getNumber());
				}else{
					localContacts.put(phoneCall.getContact().getNumber(),new SyncEntry());
					addContact(account,phoneCall.getContact());
				}
			}
			
			
//			if (localContacts.get("efudd") == null) {
//				addContact(account, phoneNumber);
//			} else {
//				if (localContacts.get("efudd").photo_timestamp == null || System.currentTimeMillis() > (localContacts.get("efudd").photo_timestamp + 604800000L)) {
//					//You would probably download an image file and just pass the bytes, but this sample doesn't use network so we'll decode and re-compress the icon resource to get the bytes
//					ByteArrayOutputStream stream = new ByteArrayOutputStream();
//					Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
//					icon.compress(CompressFormat.PNG, 0, stream);
//					updateContactPhoto(operationList, localContacts.get("efudd").raw_id, stream.toByteArray());
//				}
//				updateContactStatus(operationList, localContacts.get("efudd").raw_id, "hunting wabbits");
//			}
//			if (operationList.size() > 0){
//				mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
//			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
