package phone.crm2.server;




//////////////////////////////////////////////


import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.InputStream;
import java.util.ArrayList;

import phone.crm2.model.AppAccount;

public class Request extends AsyncTask<Object, Integer, String> {
	 
	public static final String TAG = "REQUEST";
	
    public static final int POST = 1;
    public static final int GET = 2;
     
   
    // connection timeout, in milliseconds (waiting to connect)
    private static final int CONN_TIMEOUT = 20000;
     
    // socket timeout, in milliseconds (waiting for data)
    private static final int SOCKET_TIMEOUT = 5000;
     
    private int taskType = GET;
    private final String url;
    private ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

 
   

    public Request(int taskType,String url, ArrayList<NameValuePair> data){
    	
    	this.taskType 	= taskType;
    	this.url 		= url;
    	this.data 		= data;
    }
    

    
   

    @Override
    protected void onPreExecute() {
    }

    protected String doInBackground(Object... args) {
    	Log.i(TAG,"doInBackGround start");
        String result = null;
        try {
			Log.i(TAG,"doInBackGround start url :"+url+"<");
			HttpResponse response = doResponse(url);
            Log.i(TAG," from "+url+" hResult " +result);
			if (response != null) {
				result = parseResult(response.getEntity().getContent());
			}
		} catch (Exception e) {
			Log.e(TAG,"exception args:"+args, e);
		}
        
        return result;
    }

    @Override
    protected void onPostExecute(String response) {
        Log.i(TAG,"onPostExecute response "+response) ;
    }
     
    // Establish connection and socket (data retrieval) timeouts
    private HttpParams getHttpParams() {
         
        HttpParams http = new BasicHttpParams();
         
        HttpConnectionParams.setConnectionTimeout(http, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(http, SOCKET_TIMEOUT);
         
        return http;
    }
     
    private HttpResponse doResponse(String url) {
         
        // Use our connection and data timeouts as parameters for our
        // DefaultHttpClient
        HttpClient httpClient = new DefaultHttpClient(getHttpParams());

        HttpResponse response = null;

        try {
            switch (taskType) {

	            case POST:
	                HttpPost httpPost = new HttpPost(url);
	                // Add parameters
	                if (data != null){
	                	checkParams(data);
	                	httpPost.setEntity(new UrlEncodedFormEntity(data));
	                }
	                //add auth headers here
//	                if(appAccount != null){
//	                	httpPost.setHeader("Authorization", getCredentials(appAccount));
//	                }
	                
	                response = httpClient.execute(httpPost);
	                break;
	            case GET:
	                HttpGet httpGet = new HttpGet(url);
	                response = httpClient.execute(httpGet);
	                break;
            }
        } catch (Exception e) {

            Log.e(TAG, e.getLocalizedMessage(), e);

        }

        return response;
    }
    
    private String parseResult(InputStream in) {
		StringBuffer sb = new StringBuffer();

		
		try {
			int c ;
			
			while((c=in.read()) != -1){
				sb.append((char) c);
			}
			
		} catch (Exception e) {
			Log.e(TAG, "parseResult "+sb.toString(),e);
		}
		return sb.toString();
	}
    
    private String getCredentials(AppAccount appAccount){
    	String base64EncodedCredentials = "Basic " + Base64.encodeToString((appAccount.getMail() + ":" + appAccount.getPassword()).getBytes(), Base64.NO_WRAP);


        return base64EncodedCredentials;
    }


    //TODO remove this logging method
	private void checkParams(ArrayList<NameValuePair> data2) {
		for(NameValuePair nvp : data2){
			Log.e(TAG, "checkParams "+nvp.getName()+" : "+nvp.getValue());
		}
	}
     
   

}
