package phone.crm2.uploadHistoric;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import phone.crm2.MainActivity;
import phone.crm2.model.AppAccount;

@Deprecated
public class RequestHistoric extends AsyncTask<String, Integer, String> {

	public static final String TAG = "REQUEST_HISTORIC";

	// connection timeout, in milliseconds (waiting to connect)
	private static final int CONN_TIMEOUT = 20000;

	// socket timeout, in milliseconds (waiting for data)
	private static final int SOCKET_TIMEOUT = 5000;
	
	private String url = MainActivity.SERVICE_URL_CAFE_CRM+"/historic";
	private int fromIndex=0;
	private int nbmax =1000;
	private AppAccount appAccount;
	private ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

	

	public RequestHistoric(AppAccount appAccount, int start, int nb) {
		this.fromIndex = start;
		this.nbmax = nb;
		this.appAccount = appAccount;
	}

	public RequestHistoric(AppAccount appAccount) {
		this(appAccount,0,100);
	}

	@Override
	protected void onPreExecute() {
	}

	protected String doInBackground(String... args) {
		Log.i(TAG, "doInBackGround fromIndex");
		try {
			Log.i(TAG, "doInBackGround historic fromIndex url :" + url + "<");
			String urlFull = url+"?";
			 urlFull += "fromIndex="+fromIndex;
			 urlFull += "&nbmax="+nbmax;
			 urlFull += "&emailUser="+URLEncoder.encode(appAccount.getMail());
			 urlFull += "&passwordUser="+URLEncoder.encode(appAccount.getPassword());
			 urlFull += "&cryptPhrase="+URLEncoder.encode(appAccount.getcryptPhrase());
			HttpResponse response = doResponse(url);

			if (response == null) {
				Log.i(TAG, " from " + url + " reponse is null " );
				// return hResult;
			} else if (response.getStatusLine()== null){
				Log.i(TAG, " from " + url + " reponse status null" );
			} else if (response.getStatusLine().getStatusCode()!= 200){
				Log.i(TAG, " from " + url + " reponse status pb " +response.getStatusLine().getStatusCode());
			} else {
					// hResult =
				parseResult(response.getEntity().getContent());
				Log.i(TAG, " from " + url + " hResult " + response);
			}
		} catch (Exception e) {
			Log.e(TAG, "exception " + e, e);
		}

		return "DoInBackGround Done";
	}

	private void parseResult(InputStream in) {
		StringBuffer sb = new StringBuffer();
		try {
			int c ;
			
			while((c=in.read()) != -1){
				sb.append((char) c);
			}
			String s = sb.toString();
			Log.i(TAG, "parseResult "+s);
			JSONArray jsonArray = new JSONArray(s);
			 for (int i = 0; i < jsonArray.length(); i++) {
			        JSONObject jsonObject = jsonArray.getJSONObject(i);
			        Log.i(TAG, jsonObject.getString("text"));
			      }
		} catch (Exception e) {
			Log.e(TAG, "parseResult "+sb.toString(),e);
		}
	}

	@Override
	protected void onPostExecute(String response) {
		Log.i(TAG, "onPostExecute response " + response);
	}

	// Establish connection and socket (data retrieval) timeouts
	private HttpParams getHttpParams() {

		HttpParams htpp = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
		HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);

		return htpp;
	}

	private HttpResponse doResponse(String url) {
		HttpClient httpclient = new DefaultHttpClient(getHttpParams());
		HttpResponse response = null;
		try {
			HttpGet httpget = new HttpGet(url);
			response = httpclient.execute(httpget);

		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage(), e);
		}

		return response;
	}

}
