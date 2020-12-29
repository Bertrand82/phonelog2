package phone.trace.server;

import org.apache.http.NameValuePair;

public class ValuePair implements NameValuePair{
	String name ;
	String value;
	ValuePair(String k, String v){
		name =k;
		value=v;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getValue() {
		return value;
	}
}