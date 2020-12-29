package phone.trace.server;

import android.os.AsyncTask;
import android.os.Looper;

/**
 * 
 * @author Bertrand Suite a Exception: : Caused by: java.lang.RuntimeException:
 *         Can't create handler inside thread that has not called
 *         Looper.prepare(), Je cr?e un thread
 */
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
