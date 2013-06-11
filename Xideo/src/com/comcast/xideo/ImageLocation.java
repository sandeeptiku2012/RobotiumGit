package com.comcast.xideo;

public enum ImageLocation {
	VIDEO_THUMBNAIL("thumb-iptv"),
	CHANNEL_LOGO("channel-logo-iptv"),
	BANNER_SINGLE("banner-single-iptv"),
	BANNER_DOUBLE("banner-double-iptv"),
	MAIN("main");

	String name;

	private ImageLocation(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
