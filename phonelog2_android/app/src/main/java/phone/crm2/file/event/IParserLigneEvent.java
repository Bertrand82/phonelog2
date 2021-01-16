package phone.crm2.file.event;

import java.io.File;


public interface IParserLigneEvent {

	void parseLigne(String line);
	void parseFileTerminated(File file, int nbLignesParsed, int nbLignesTotal);
}
