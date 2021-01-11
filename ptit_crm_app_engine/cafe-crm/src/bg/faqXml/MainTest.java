package bg.faqXml;

import java.io.File;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class MainTest {

	public MainTest() throws Exception{
		
	}

	public static void main(String[] args) throws Exception {
		test(Locale.getDefault());
		test(Locale.GERMAN);
		test(Locale.US);
		test(null);
	}
	
	public static void test(Locale locale) throws Exception {
			FAQ faq = UtilFAQ.getFAQ(locale);
			System.out.println("faq ---- "+ "  exists " +(faq!=null)+" locale :"+locale);
	}

}
