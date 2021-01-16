package phone.crm2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.Provider;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SenderMail extends AsyncTask<String, String, String> {

	static {
		Security.addProvider(new JSSEProvider());
	}


	String subject;
	String body;
	ApplicationBg context;
	
	public SenderMail(ApplicationBg context, String subject, String body) {
		super();
		this.context = context;
		this.body = body;
		this.subject = subject;
	}

	@Override
	protected String doInBackground(String... params) {
		sendMail3();
		return "OK";
	}

	private void sendMail3() {
		Log.i("bg2", "sendMail3");
		GMailSender2 sender = null;
		try {
			sender = new GMailSender2(context);
			sender.sendMail(subject, body);
		} catch(AuthenticationFailedException ee){
			Log.e("bg2", "sendMail3 AuthenticationFailedException " + ee.getMessage(), ee);
			context.addEmailTrace("eMail  Authentication Failed \nhost:"+sender.mailhost+":"+sender.mailPort+"\n  user: "+sender.user);
		} catch (Exception e) {
			Log.e("bg2", "sendMail3 Exception " + e.getMessage(), e);
			context.addEmailTrace("eMail Exception "+e.getMessage());
			
		}
	
	}
	
	private void alert_(Exception e) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle("Error sending eMail");
		alertDialog.setMessage("Error "+  " \n"+ e.getMessage()+"\n"+e.getClass());
		alertDialog.show();
	}
}

class GMailSender2 extends javax.mail.Authenticator {

	protected String mailhost = "smtp.gmail.com";
	protected String user;
	private final String password_;
	protected String mailPort= "465";
	private final Session session;
	private final String mailRecipient;
	private final String mailSender;
	public GMailSender2(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		this.mailhost = sharedPrefs.getString("mailHost", "smtp.gmail.com");
		this.password_ = sharedPrefs.getString("mailPassword", "secret");
		this.mailSender = sharedPrefs.getString("mailUser", "aaa@aaa.com");
		this.user = mailSender;
		this.mailPort = sharedPrefs.getString("mailPort", "465");
		this.mailRecipient = sharedPrefs.getString("mailTo", user);
		Log.w("bg2", ""+this);
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", mailhost);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", mailPort);
		props.put("mail.smtp.socketFactory.port", mailPort);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.quitwait", "false");

		session = Session.getDefaultInstance(props, this);
		Log.i("bg2", "testMail_3 constructeur done");
	}

	

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password_);
	}

	public synchronized void sendMail(String subject, String body) throws Exception {
		Log.w("bg2", "testMail_3 sendMail start mailRecipient: "+mailRecipient+" mailhost "+mailhost+"   "+password_);
		MimeMessage message = new MimeMessage(session);
		DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
		message.setSender(new InternetAddress(mailSender));
		message.setSubject(subject);
		message.setDataHandler(handler);
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailRecipient));
		Transport.send(message);
		Log.i("bg2", "sendMail  done subject :"+subject);
	}

	public static class ByteArrayDataSource implements DataSource {
		private final byte[] data;
		private String type;

		public ByteArrayDataSource(byte[] data, String type) {
			super();
			this.data = data;
			this.type = type;
		}

		public ByteArrayDataSource(byte[] data) {
			super();
			this.data = data;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getContentType() {
			if (type == null)
				return "application/octet-stream";
			else
				return type;
		}

		public InputStream getInputStream() throws IOException {
			return new ByteArrayInputStream(data);
		}

		public String getName() {
			return "ByteArrayDataSource";
		}

		public OutputStream getOutputStream() throws IOException {
			throw new IOException("Not Supported");
		}
	}


	public String toString() {
		return "GMailSender2 [mailhost=" + mailhost + ", user=" + user + ", mailPort=" + mailPort + ", session=" + session + ", mailRecipient=" + mailRecipient + ", mailSender=" + mailSender + "]";
	}
	
	
}

final class JSSEProvider extends Provider {

	private static final long serialVersionUID = 1L;

	public JSSEProvider() {
		super("HarmonyJSSE", 1.0, "Harmony JSSE Provider");
		AccessController.doPrivileged(new java.security.PrivilegedAction<Void>() {
			public Void run() {
				put("SSLContext.TLS", "org.apache.harmony.xnet.provider.jsse.SSLContextImpl");
				put("Alg.Alias.SSLContext.TLSv1", "TLS");
				put("KeyManagerFactory.X509", "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl");
				put("TrustManagerFactory.X509", "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl");
				return null;
			}
		});
	}
}
