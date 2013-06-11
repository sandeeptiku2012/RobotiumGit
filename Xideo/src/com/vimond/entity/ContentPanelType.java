package com.vimond.entity;

public enum ContentPanelType {
	FEATURED("featured"),
	RECOMMENDED("recommended"),
	POPULAR("popular"),
	UPNEXT("upnext"),
	OTHERS("others");

	String name;

	private ContentPanelType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
