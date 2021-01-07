package phone.crm2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import phone.crm2.file.ContactFactory;
import phone.crm2.file.FileParserCSVContacts;
import phone.crm2.file.FileParserCSVContacts.ContactMap;
import phone.crm2.file.IProcessItem;


public class ActivityImportContacts extends AbstractActivityCrm {

	private File dirCurrent_ = new File("/");
	
	private FileParserCSVContacts fileParserCSV;
	private TextView textViewResultInserted;
	private TextView textViewResultUpdated;
	private TextView textViewResultException;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import);
		// Show the Up button in the action bar.
		// setupActionBar();
		Button buttonChooseFile = (Button) findViewById(R.id.buttonChooseFile);
		buttonChooseFile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				chooseFile();
			}
		});
		textViewResultInserted =(TextView) findViewById(R.id.textResultImportContactInserted);
		textViewResultUpdated =(TextView) findViewById(R.id.textResultImportContactUpdated);
		textViewResultException =(TextView) findViewById(R.id.textResultImportContactException);
	}

	private void chooseFile() {
		Log.i("bg2", "ChooseFile");
		onCreateDialog(this.dirCurrent_);
	}
	
	
	

	protected  Dialog onCreateDialog( final File dirCurrent) {
		Log.i("bg2","onCreateDialog dir: "+dirCurrent.getAbsolutePath());
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Choose your file "+dirCurrent.getName());
		String[] fileListStr_= dirCurrent.list();
		if (fileListStr_== null){
			fileListStr_ = new String[0];
		}
		final String[] items = new String[fileListStr_.length+1];
		items[0]="..";
		System.arraycopy(fileListStr_, 0, items, 1, fileListStr_.length);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String mChosenFileStr = items[which];
				File fileChosen = new File(dirCurrent,mChosenFileStr);
				if (fileChosen.equals("..")){
					onCreateDialog(dirCurrent.getParentFile());
				}else if (fileChosen.isDirectory()){
					onCreateDialog( fileChosen);
				}else {
					processFile(fileChosen);
				}
			}

		});
		
		dialog = builder.show();
		return dialog;
	}
	

	private void processFile(File fileChosen) {
		IProcessItem iProcessItem = new IProcessItem() {			
			@Override
			public void processItem(ContactMap contactMap) {
				processItemContact( contactMap);
			}
		};
		this.fileParserCSV = new FileParserCSVContacts(this, fileChosen,iProcessItem);
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				fileParserCSV.processFile();
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
		
	}
	ContactFactory contactsFactory = new ContactFactory();
	String message="";
	public void processItemContact(ContactMap contactMap) {
		try {
			contactsFactory.insertOrUpdate(this,contactMap);
			
		} catch (Exception e) {
			Log.w("bg2","Exception insering contact",e);
			message="Exception  "+e.getMessage();
		}
		Runnable runnableUI = new Runnable() {
			@Override
			public void run() {
				updateUI();
			}
		};
		runOnUiThread(runnableUI);
	}
	
	private void updateUI() {
		textViewResultInserted.setText("Inserted : "+contactsFactory.nbInserted);
		textViewResultUpdated.setText("Updated : "+contactsFactory.nbUpdated);
		textViewResultException.setText(""+message);
	}
	
	
}


