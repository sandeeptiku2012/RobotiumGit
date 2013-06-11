package no.sumo.api.vo;

import java.util.List;

import org.simpleframework.xml.ElementList;

public class StringList  {

	@ElementList(entry = "value", inline= true, required = false)
	private List<String> list;


	public StringList() {

	}
	public List<String> getValues() {
		return list;
	}
	public boolean contains(String string) {
		return list.contains( string );
	}

}
