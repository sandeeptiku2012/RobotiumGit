package com.vimond.rest;

import java.util.List;

import org.simpleframework.xml.ElementList;

public class RestStringList {
	@ElementList(entry = "value", inline= true)
	private List<String> list;
	
	public boolean contains(String string) {
		return list.contains(string);
	}
	public List<String> getStrings() {
		return list;
	}
	

}
