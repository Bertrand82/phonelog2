package cafe.crm.engine.server.message;

import java.util.Date;

import javax.persistence.EntityManager;

import cafe.crm.engine.server.util.UtilPersistence;



public class BeanMessageFactory {

	public static BeanMessageFactory instance = new BeanMessageFactory();
	
	public void persist(String message, String metadata, String name, String accounts) {
		EntityManager em = UtilPersistence.createEntityManager();
		BeanMessage bm = new BeanMessage(message,metadata,name,accounts);
		em.persist(bm);
		em.close();
	}
}
