package cafe.crm.engine.server.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class UtilMail {

	public static void sendMessage(String subject, String msgBody, String... emails) {
		sendMessage(subject, msgBody,false, emails);
	}
	public static void sendMessage(String subject, String msgBody,  boolean isHtml,String... emails) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("bertrand.guiral@gmail.com", "CAFE-CRM Mssage Uttilisateur"));
			for (String email : emails) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
			}
			if (isHtml){
				msg.setContent(msgBody,"text/html");
			}else {
				msg.setText(msgBody);
			}
			msg.setSubject(subject);
			
			Transport.send(msg);
		
		} catch (Exception e) {
			System.out.println("Erreur send message sujet: "+subject+" body: "+msgBody+" emails:"+emails);
			e.printStackTrace();
		}
	}
	public static boolean isEmailOk(String email) {
		if (email== null){
			return false;
		}
		if (email.trim().length()== 0){
			return false;
		}
		if(email.indexOf("@")<1){
			return false;
		}
		return true;
	}

}
