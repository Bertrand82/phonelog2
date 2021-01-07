package phone.crm2;

import java.io.Serializable;
import java.util.HashMap;

public class UpdateResult implements Serializable{

	
	private static final long serialVersionUID = 1L;
	HashMap<BgCalendar, Boolean> hResult = new HashMap<BgCalendar, Boolean>();

	public UpdateResult() {
		super();
	}

	public HashMap<BgCalendar, Boolean> gethResult() {
		return hResult;
	}

	public void sethResult(HashMap<BgCalendar, Boolean> hResult) {
		this.hResult = hResult;
	}
	
	
}
