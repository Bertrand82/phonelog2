package bg.faqXml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class FAQ {

	@XmlElement
	private String title;
	@XmlAttribute
	private String id;
	@XmlElement (name="QuestionReponse")
	private List<QuestionResponse> listQuestionReponse = new ArrayList<QuestionResponse>();
	
	public FAQ() {
	}
	
	public List<QuestionResponse> getListQuestionReponse() {
		return listQuestionReponse;
	}
	public void setListQuestionReponse(List<QuestionResponse> listQuestionReponse) {
		this.listQuestionReponse = listQuestionReponse;
	}
	@Override
	public String toString() {
		String s =  "FAQ ";
		for(QuestionResponse qr : listQuestionReponse){
			s += qr+" \n";
		}
		return s;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

}
