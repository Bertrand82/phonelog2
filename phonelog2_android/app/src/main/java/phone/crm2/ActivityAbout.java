package phone.crm2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class ActivityAbout extends AbstractActivityCrm {
	
	private static final String VERSION = "2.02";//3/2015
	private static final String BUILD = "6/2/2015";
	

	private String adress = "http://cafe-crm.appspot.com/r/crm/message";
	private String urlStr = "http://cafe-crm.appspot.com/";
	private Button buttonSendMessage;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

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
				sendMail();
			}
		});
	}
	


	private void sendMail() {
		UtilEmail.sendEmail(this,"bertrand.guiral@gmail.com","crm :  Retour from android ","");
	}




}
