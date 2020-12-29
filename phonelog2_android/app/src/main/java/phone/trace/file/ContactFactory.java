package phone.trace.file;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

import java.util.ArrayList;

import phone.trace.file.FileParserCSVContacts.ContactMap;

public class ContactFactory {

	public ContactFactory() {
		super();
	}
	
	public int nbInserted = 0;
	public int nbUpdated = 0;

	public void insertOrUpdate(Context context, ContactMap contactMap) throws Exception {
		String firstname = contactMap.getColumn(FileParserCSVContacts.KEY_FIRST_NAME, 0);
		String name = contactMap.getColumn(FileParserCSVContacts.KEY_NAME, 0);
		String phoneNumber = contactMap.getColumn(FileParserCSVContacts.KEY_PHONE_NUMBER, 0);
		String societe = contactMap.getColumn(FileParserCSVContacts.KEY_SOCIETE, 0);
		String adresse = contactMap.getColumn(FileParserCSVContacts.KEY_ADRESSE, 0);
		String mail = contactMap.getColumn(FileParserCSVContacts.KEY_MAIL, 0);
		String society = contactMap.getColumn(FileParserCSVContacts.KEY_SOCIETE, 0);
		String clientId = contactMap.getColumn(FileParserCSVContacts.KEY_CLIENT_ID, 0);
		int raw_contact_id = getRawContactIdFromNumber(context, phoneNumber);
		if (raw_contact_id == 0) {
			nbInserted +=insert2Contacts(context, name, phoneNumber,clientId,mail,societe);
		} else {
			nbUpdated += update(context, raw_contact_id, name, phoneNumber, clientId, mail, societe)	;	
			Log.i("bg2","insert : contact already exists !!"+raw_contact_id+" "+name);
		}
	}

	public int getRawContactIdFromNumber(Context context, String phoneNumber) {

		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		String[] params = new String[] { Data.RAW_CONTACT_ID };
		Cursor cursor = context.getContentResolver().query(uri, params, null, null, null);
		int raw_contact_id = 0;
		if (cursor.moveToFirst()) {
			raw_contact_id = cursor.getInt(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
		}
		cursor.close();
		return raw_contact_id;
	}

	
	public  int insert2Contacts(Context ctx, String nameSurname, String telephone, String clientId, String email,String company) throws Exception{
		Log.d("bg2","insert2Contacts name :"+nameSurname+"  telephone :"+telephone);
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = ops.size();

		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI).withValue(RawContacts.ACCOUNT_TYPE, null).withValue(RawContacts.ACCOUNT_NAME, null).build());
		ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex).withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE).withValue(Phone.NUMBER, telephone).build());
		ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.DISPLAY_NAME, nameSurname)
				.withValue(Data.DATA2, clientId)
				.withValue(Email.DATA,email)
				.withValue(Organization.COMPANY,company)
				.build());
			ContentProviderResult[] res = ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			return 1;
		
	}
	
	public int update(Context ctx,int raw_contact_id,String name,String  phoneNumber,String clientId,String mail,String societe) throws Exception{
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		String[] values =  {""+raw_contact_id};
		 ops.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI)
				 
		          .withSelection(Data.RAW_CONTACT_ID + "=?",values)
		          
		          .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
		          .withValue(StructuredName.DISPLAY_NAME, name)
		          .withValue(Data.DATA2, clientId)
		          .withValue(Email.DATA,mail)
		          .withValue(Organization.COMPANY,societe)
		          .build());
		 ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		 return 1;
	}

}
