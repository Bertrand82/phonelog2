package cafe.crm.engine.server.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class UtilPersistence {

	 private static final EntityManagerFactory emfInstance =   Persistence.createEntityManagerFactory("transactions-optional");

	 public static EntityManagerFactory getEntityManagerFactory() {
		  return emfInstance;
	 }	   

	 public static EntityManager createEntityManager() {
		 return getEntityManagerFactory().createEntityManager();
	 }
		   
}
