package phone.crm2.file;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class FileParserCSVContacts {

	public Map<Integer, List<Integer>> mapColumns = new HashMap<Integer, List<Integer>>();
	private Context context;
	private File file;
	private IProcessItem iProcessItem;
	
	public FileParserCSVContacts(Context context, File file,IProcessItem iProcessItem) {
		super();
		this.context = context;
		this.file = file;
		this.iProcessItem = iProcessItem;
		
	}
	
	
	public void processFile(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String lineTitle = br.readLine();
			
			initTitleMap(lineTitle);
			showColumnsDebug();
			System.out.println("mapColumn "+lineTitle);
			System.out.println("mapColumn "+mapColumns);
			String line;
			while(( line = br.readLine()) != null){
				ContactMap contactMap = new ContactMap(line);
				processContactMap(contactMap);
			}
		} catch (Exception e) {
			log("E1 ProcessFile",e);
			Log.w("bg2"," Exception process file",e);
		}
	}
	
	private void processContactMap(ContactMap contactMap) {
		if (iProcessItem == null){
			
		}else {
			this.iProcessItem.processItem(contactMap);
		}
	}


	private void log(String s, Exception e) {
		if (context == null){
			System.err.println(s);
			e.printStackTrace();
		}else {
			 AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		        alertDialog.setTitle("Erreur");
		        alertDialog.setMessage(s+" \n"+e.getMessage());
		        
		        alertDialog.show();
		}
		   
	}
	public static String[] COLUMNS= {"PHONE_NUMBER","FIRST_NAME","NAME","MAIL","ADRESSE","VILLE","SOCIETE"};
	public static int KEY_PHONE_NUMBER = 0;
	public static int KEY_FIRST_NAME=1;
	public static int KEY_NAME = 2;
	public static int KEY_MAIL=3;
	public static int KEY_ADRESSE=4;
	public static int KEY_VILLE=5;
	public static int KEY_SOCIETE=6;
	public static int KEY_CLIENT_ID=7;
	 
	private void  initTitleMap(String lineTitle) {
		lineTitle = lineTitle.toLowerCase();
		StringTokenizer st = new StringTokenizer(lineTitle,",;:");
		Integer i =0;
		while(st.hasMoreTokens()){	
			String v = st.nextToken();
			if (isNumber(v)){
				getListColumns(KEY_PHONE_NUMBER).add(i);
			}else if(isFirstName(v)){
				getListColumns(KEY_FIRST_NAME).add(i);
			}else if(isName(v)){
				getListColumns(KEY_NAME).add(i);
			}else if(isMail(v)){
				getListColumns(KEY_MAIL).add(i);
			}else if(isAdresse(v)){
				getListColumns(KEY_ADRESSE).add(i);
			}else if(isVille(v)){
				getListColumns(KEY_VILLE).add(i);
			}else if(isSociete(v)){
				getListColumns(KEY_SOCIETE).add(i);
			}else if(isClientId(v)){
				getListColumns(KEY_CLIENT_ID).add(i);
			}
		    i++;
		}
		
	}
	

	List<Integer> getListColumns(int i){
		List<Integer> l  = this.mapColumns.get(i);
		if (l == null){
			l = new ArrayList<Integer>();
			this.mapColumns.put(i, l);
		}
		System.out.println("getListColumns   "+i+"   ");
		return l;
	}

	private boolean isNumber(String v) {
		return v.matches((".*(phone|telephone|number).*"));
	}
	private boolean isFirstName(String v) {
		return v.matches((".*(first name|prenom).*"));
	}
	private boolean isName(String v) {
		return v.matches((".*(name|nom).*"));
	}
	private boolean isMail(String v) {
		return v.matches((".*(mail).*"));
	}
	private boolean isAdresse(String v) {
		return v.matches((".*(adresse|addresse).*"));
	}
	private boolean isVille(String v) {
		return v.matches((".*(ville|city|town).*"));
	}
	private boolean isSociete(String v) {
		return v.matches((".*(societe|company).*"));
	}
	private boolean isClientId(String v) {
		return v.matches((".*(id|client).*"));
	}
	
	public class ContactMap {
		List<String> list = new ArrayList<String>();
		public ContactMap(String line) {
			StringTokenizer st = new StringTokenizer(line,",;:");
			int i =0;
			while(st.hasMoreTokens()){
				String c = st.nextToken();
				list.add(c);
			}
			
		}
		public List<String> getList(int column){
			List<String> l = new ArrayList<String>();
			List<Integer> lc = mapColumns.get(column);
			if(lc == null){
				return l;
			}
			for(Integer i : lc){
				l.add(list.get(i));
			}
			return l;
		}
		public String getColumn(int column, int i) {
			List<String> list = getList(column);
			if (list == null){
				return "";
			}else if (list.size()<=i){
				return "";
			}else {
				return list.get(i);
			}
			
		}
	};
	

	private void showColumnsDebug(){
		Integer i=0;
		for(String s : COLUMNS){
			System.out.println(i+"  "+s+"   "+mapColumns.get(i));
			i++;
		}
	}
}
