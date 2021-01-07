package phone.crm2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapEditText;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;



public class ActivityAbout extends AbstractActivityCrm {
	
	private static final String VERSION = "2.02";//3/2015
	private static final String BUILD = "6/2/2015";
	
	private BootstrapEditText editTextMessage;
	private String adress = "http://cafe-crm.appspot.com/r/crm/message";
	private String urlStr = "http://cafe-crm.appspot.com/";
	private Button buttonSendMessage;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		editTextMessage = (BootstrapEditText) findViewById(R.id.editTextMessage);
		Button button = (Button) findViewById(R.id.site_url);
		button.setText(urlStr);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(urlStr));
				startActivity(intent);
			}
		});
		TextView textView = (TextView) findViewById(R.id.textViewVersion);
		textView.setText("" + VERSION+" "+BUILD);
		buttonSendMessage = (Button) findViewById(R.id.aboutButtonSendMessage);
		buttonSendMessage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setEnable(false);
				sendMessage();
			}
		});
	}
	
	protected void setEnable(boolean b){
		buttonSendMessage.setEnabled(b);
		editTextMessage.setEnabled(b);
	}

	
	private void sendMessage() {
		String message = "" + editTextMessage.getText();
		Log.i("bg2", "SendMessage " + message);
		try {
			SendMessageTask sendMessageTask = new SendMessageTask();
			sendMessageTask.execute("" + message);
		} catch (Exception e) {
			Log.i("bg2", "Exception", e);
			UtilActivitiesCommon.popUp(this, "Houps ...Exception ", "" + e.getMessage(),"OK");
		}
		UtilActivitiesCommon.openLogs(this);

	}

	@SuppressWarnings("deprecation")
	private HttpResponse postMessge(String message) throws Exception {
		Log.i("bg2", "postMessge start");
		String meta="";
		ApplicationBg applicationBg = (ApplicationBg)getApplication();
		String name=""+applicationBg.getAppAccount();
		meta += "versionCafeCRM="+VERSION+"  "+BUILD;
		meta += "| android.os.Build.VERSION.SDK="+android.os.Build.VERSION.SDK;
		meta += "| android.os.Build.VERSION.RELEASE="+android.os.Build.VERSION.RELEASE;
		meta += "| android.os.Build.MODEL="+android.os.Build.MODEL;
		meta += "| android.os.Build.VERSION.CODENAME="+android.os.Build.VERSION.CODENAME;
		String accounts =""+applicationBg.getAppAccount();
		for(BgCalendar calendar: applicationBg.getListCalendars()){
			accounts+=" | "+calendar;
		}
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(adress);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("message", message));
		pairs.add(new BasicNameValuePair("meta", meta));
		pairs.add(new BasicNameValuePair("name", name));
		pairs.add(new BasicNameValuePair("accounts", accounts));
		
		post.setEntity(new UrlEncodedFormEntity(pairs));
		HttpResponse response = client.execute(post);
		Log.i("bg2", "postMessge done code " + response.getStatusLine().getStatusCode());
		Log.i("bg2", "postMessge done " + response);
		for (Header header : response.getAllHeaders()) {
			Log.i("bg2", " response header : " + header.getName() + " " + header.getValue());
		}
		return response;
	}

	class SendMessageTask extends AsyncTask<String, Integer, Long> {
		HttpResponse response;
		Exception exception;
		String messageSend ;
		protected Long doInBackground(String... messages) {
			int i = 0;
			try {
				messageSend = messages[i];
				response = postMessge(messageSend);
			} catch (Exception e) {
				exception = e;
				Log.i("bg2", "Exception", e);
			}
			return 0l;
		}

		protected void onPostExecute(Long result) {
			if (response == null){
				if (exception == null){
					UtilActivitiesCommon.popUp(ActivityAbout.this, "Message send "+messageSend, "Big Problem");
				}else {
					UtilActivitiesCommon.popUp(ActivityAbout.this, "Message Exception ", ""+exception.getMessage());
				}
			}
			setEnable(true);
			UtilActivitiesCommon.popUp(ActivityAbout.this, "Message send : code " + response.getStatusLine().getStatusCode(), messageSend,"OK", ActivityLogs2.class);
		}
	}

}
