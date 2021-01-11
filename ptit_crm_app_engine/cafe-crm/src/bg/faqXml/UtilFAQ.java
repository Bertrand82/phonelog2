package bg.faqXml;

import java.net.URL;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;


public class UtilFAQ {

	public UtilFAQ() {
	}
	private static FAQ faq;
	private static FAQ faq_fr;
	
	public static FAQ getFAQ() {
		return getFAQ(null);
	}
	
	public static FAQ getFAQ(Locale locale) {
		String langage="";
		if (locale!= null){
			langage = ""+locale.getLanguage();
		}
	
		FAQ faq_return = null;
		if ("fr".equalsIgnoreCase(langage)){
			if (faq_fr==null){
				faq_fr = createFAQ("/faq_fr.xml");
			}
			faq_return = faq_fr;
		}else {
			if (faq==null){
				faq = createFAQ("/faq.xml");
			}
			faq_return=faq;
		}
		return faq_return;
	}
	
	
	private static FAQ createFAQ(String fileNAme) {
		try {
			JAXBContext jaxbContext2  = JAXBContext.newInstance(FAQ.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext2.createUnmarshaller();
			URL url = MainTest.class.getResource(fileNAme);
			if (url == null){
				return null;
			}
			FAQ faq2 = (FAQ) jaxbUnmarshaller.unmarshal(url);
			return faq2;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
