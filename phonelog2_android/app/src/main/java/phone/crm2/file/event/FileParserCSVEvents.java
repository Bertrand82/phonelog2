package phone.crm2.file.event;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileParserCSVEvents {

	public Map<Integer, List<Integer>> mapColumns = new HashMap<Integer, List<Integer>>();
	private Context context;
	private File file;
	IParserLigneEvent parseLigne;

	public FileParserCSVEvents(Context context, File file, IParserLigneEvent parseLigne) {
		super();
		this.context = context;
		this.file = file;
		this.parseLigne = parseLigne;
	}

	public void processFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			int nbLignesPArsed =0;
			int nbLignesTotal =0;
			String line;
			while ((line = br.readLine()) != null) {
				nbLignesTotal++;
				if (processLine(line)) {
					nbLignesPArsed++;
				}
			}
			if(parseLigne==null){
				
			}else{
				parseLigne.parseFileTerminated(file,nbLignesPArsed,nbLignesTotal);
			}
		} catch (Exception e) {
			log("E1 ProcessFile", e);
			Log.w("bg2", " Exception process file", e);
		}
	}

	private  boolean processLine(String line) {
		if (line == null) {
			return false;
		}
		line = line.trim();
		if (line.length() < 5) {
			return false;
		}
		

		//System.out.println("start : " + start + "  ]]]]]]]]]]  " + date);
		if(parseLigne==null){
		}else{
			parseLigne.parseLigne( line);
		}
		return true;
	}

	

	private void log(String s, Exception e) {
		if (context == null) {
			System.err.println(s);
			e.printStackTrace();
		} else {
			AlertDialog alertDialog = new AlertDialog.Builder(context).create();
			alertDialog.setTitle("Erreur");
			alertDialog.setMessage(s + " \n" + e.getMessage());
			alertDialog.show();
		}

	}

}
