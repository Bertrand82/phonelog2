package phone.crm2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import phone.crm2.file.event.FileParserCSVEvents;
import phone.crm2.file.event.IParserLigneEvent;
import phone.crm2.server.Sender;


public class ActivityImportEventsCRM extends AbstractActivityCrm {

	private File dirCurrent_ = new File("/");
	FileParserCSVEvents parser;
	TextView textView ;
	BgCalendar calendar ;
	EditText editTextUrl;
	Button buttonChooseURL;
	Button buttonHelpDemo;
	ApplicationBg applicationBg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applicationBg = (ApplicationBg) getApplication();
		setContentView(R.layout.activity_import_events_crm);
		calendar = applicationBg.getDefaultCalendar();
		buttonHelpDemo = (Button) findViewById(R.id.buttonHelpDemo);
		buttonHelpDemo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				helpDemo();
			}
		});
		
		 buttonChooseURL = (Button) findViewById(R.id.buttonFetchImportUrl);
		buttonChooseURL.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonChooseURL.setEnabled(false);
				chooseURL();
			}
		});
		
		Button buttonChooseFile = (Button) findViewById(R.id.buttonChooseFile2);
		buttonChooseFile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				chooseFile();
			}
		});
		
		textView = (TextView) findViewById(R.id.textResultInsertEvent);
		editTextUrl = (EditText) findViewById(R.id.editTextUrl);
	}
	
	private void chooseFile() {
		Log.i("bg2", "ChooseFile");
		chooseFile(this.dirCurrent_);
	}
	
	
	private void chooseURL() {
		try {
			String urlStr =""+ this.editTextUrl.getText();
			URLEventCSVParser urlEventCSVParser = new URLEventCSVParser(urlStr);
			Sender sender = new Sender();
			sender.execute(urlEventCSVParser);
		} catch (Exception e) {
			Log.w("bg2","bgxxxxx",e);
		}
	}
	

	

	protected  Dialog chooseFile( final File dirCurrent) {
		
		calendar = applicationBg.getDefaultCalendar();
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
					chooseFile(dirCurrent.getParentFile());
				}else if (fileChosen.isDirectory()){
					chooseFile( fileChosen);
				}else {
					processFile(fileChosen);
				}
			}		
		});
		
		dialog = builder.show();
		return dialog;
	}
	

	private void processFile(File fileChosen) {
		Log.i("bg2","No Implemented yeeeet");
		IParserLigneEvent parserLigne = new IParserLigneEvent() {			
			@Override
			public void parseLigne( String line) {
				parseLigne2(line);
			}

			@Override
			public void parseFileTerminated(File file, int nbLignesParsed, int nbLignesTotal) {
				terminated("File Processed : "+file.getName(),"Lines : "+nbLignesParsed+" / "+nbLignesTotal);
			}
		};
		this.parser = new FileParserCSVEvents(this, fileChosen,parserLigne);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				parser.processFile();
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	static private SimpleDateFormat[] sdf = {new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"), new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"), new SimpleDateFormat("dd/MM/yyyy"),new SimpleDateFormat("dd-MM-yyyy") };
	
	private static Date toDate(String start) {
		for (SimpleDateFormat s : sdf) {
			try {
				Date date = s.parse(start);
				return date;
			} catch (ParseException e) {

			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	int i=0;
	private void parseLigne2(String line) {
		StringTokenizer st = new StringTokenizer(line, ",;");
		if (!st.hasMoreTokens()){
			return; 
		}
		String start = st.nextToken();

		Date date = toDate(start);
		if (date == null){
			return ;
		}
		long startMills = date.getTime();
		long endMillis = startMills+1000;
		String tittle = "CRM "+line;
		String description = line;
		
		UtilCalendar.insertEvent(this.getContentResolver(),startMills,endMillis,calendar,tittle,description);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				i++;
				textView.setText("Insert event "+i+" in : "+calendar.getDisplayName());
			}
		};
		this.runOnUiThread(runnable);
	}
	
	private void terminated(final String title,final String message) {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				UtilActivitiesCommon.popUp(ActivityImportEventsCRM.this, title, message);
			}
		};
		this.runOnUiThread(runnable);
		
	}
	
	private void helpDemo() {
		Log.i("bg2","viewUrl "+editTextUrl.getText());
		UtilActivitiesCommon.popUp(this, "Demo", "1 - Import events from URL \n2 - Display one of yours contact\n3 - view CRM tab\n 4 - set client Id: 007");
	}
	
	public class URLEventCSVParser extends AsyncTask<Object, Integer, String> {

		public static final String TAG = "bg2";
		
		private String urlStr ;
		

		public URLEventCSVParser(String urlStr) {
			this.urlStr= urlStr;
		}


		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(Object... argss) {
			Log.i(TAG, "doInBackGround URLEventCSVParser fromIndex");
			try {
				Log.i(TAG, "doInBackGround URLEventCSVParser fromIndex urlStr :" + urlStr+ "<");
				
				Log.i("bg2", "URLStr___ "+urlStr);
				URL url2 = new URL(urlStr);
				BufferedReader in = new BufferedReader(new InputStreamReader(url2.openStream()));	
				if (calendar==null){
					return "DoInBackGround Error!! ";
				}
				String inputLine;
				int i= 0;
				while ((inputLine = in.readLine()) != null){
					Log.i("bg2", i+++" "+inputLine);
					parseLigne2(inputLine);
				}
				in.close();
			} catch (Exception e) {
				Log.e(TAG, "exception " + e, e);
			}

			return "DoInBackGround Done";
		}

		
		@Override
		protected void onPostExecute(String response) {
			Log.i(TAG, "onPostExecute response " + response);
			buttonChooseURL.setEnabled(true);
			if (calendar==null){
				UtilActivitiesCommon.popUp(ActivityImportEventsCRM.this, "No Calendar Selected", "Please Select A Default Calendar (setting");
				return;
			}
		}


		
	}


}
