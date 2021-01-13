package phone.crm2.server;

import android.os.AsyncTask;
import android.os.Looper;

/**
 * 
 * @author Bertrand Suite a Exception: : Caused by: java.lang.RuntimeException:
 *         Can't create handler inside thread that has not called
 *         Looper.prepare(), Je crée un thread
 */
@Deprecated
public class Sender implements Runnable {

	private AsyncTask<?, ?, ?> request;

	public Sender() {
	}

	
	@Override
	public void run() {
		Looper.prepare();
		request.execute();
	}

	public void execute(AsyncTask request) {
		this.request = request;
		Thread t = new Thread(this);
		t.start();
	}

}
