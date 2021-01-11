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
public class QuestionResponse {

	
	@XmlAttribute(name="header")
	boolean isChapitre = false;
	
	@XmlElement
	String question;
	
	@XmlElement (name = "reponse")
	List<String> responses = new ArrayList<String>();
	
	public QuestionResponse() {
	}

	public String getQuestion() {
		return question;
	}

	public QuestionResponse(String question, String response) {
		super();
		this.question = question;
		responses.add(response);
	}

	public void setQuestion(String question) {
		this.question = question;
	}




	public List<String> getResponses() {
		return responses;
	}

	public void setResponses(List<String> responses) {
		this.responses = responses;
	}

	@Override
	public String toString() {
		return "QuestionResponse [question=" + question + ", responses=" + responses + "]";
	}

	public boolean isChapitre() {
		return isChapitre;
	}

	public void setChapitre(boolean isChapitre) {
		this.isChapitre = isChapitre;
	}

}
