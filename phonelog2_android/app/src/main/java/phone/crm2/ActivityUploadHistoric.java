package phone.crm2;

import android.os.Bundle;

import phone.crm2.model.AppAccount;
import phone.crm2.server.Sender;
import phone.crm2.uploadHistoric.RequestHistoric;


public class ActivityUploadHistoric extends AbstractActivityCrm {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_historic);
		ApplicationBg applicationBg = (ApplicationBg) this.getApplication();
		uploadHistoric(applicationBg.getAppAccount());
	}

	
	
	
	private void uploadHistoric(AppAccount appAccount) {
		RequestHistoric requestHistoric = new RequestHistoric( appAccount);
		Sender sender = new Sender();
		sender.execute(requestHistoric);
	}

}
