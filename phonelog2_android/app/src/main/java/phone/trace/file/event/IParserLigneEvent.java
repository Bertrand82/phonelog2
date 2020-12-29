package phone.trace.file.event;

import java.io.File;


public interface IParserLigneEvent {

	public void parseLigne(String line);
	public void parseFileTerminated(File file,int nbLignesParsed ,int nbLignesTotal);
}
