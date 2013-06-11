package com.comcast.xideo;

public class Constants {
	public static final long MAIN_CATEGORY = 100;
	public static final String VIMOND_IMAGE_SERVICE_BASE_URI = "http://image.stage.xidio.com/api/v2/img";
	public static final String VIMOND_API_BASEURI = "http://api.stage2.xidio.com/api";
	public static final String ERROR_REPORT_RECEIVER_URL = "http://eservice.projecthelen.com/reportError/stb";

	public static final String VIMOND_API_PLATFORM = "iptv";
	public static final String DATE_FORMAT = "MM/dd/yy";

	public static final long IMAGE_CACHE_LIFETIME_MS = 3600000;
	public static final String DEFAULT_VIDEO_IMAGE = "http://vcc.projecthelen.com/static_assets/question-mark.jpg";
	public static final String VIDEO_THUMBNAIL_URL = VIMOND_IMAGE_SERVICE_BASE_URI + "/assetimage/%d/thumb-stb";

	public static final String ERROR_REPORT_RECEIVER_SHARED_SECRET = "U6rAeH9Ae3!GwUvx";
	public static final String PROPERTIES_FILENAME = "projecthelen.properties";

	public static class IntentIdentifiers {
		public static final String SHOW_MY_CHANNELS = "SHOW_MY_CHANNELS";
		public static final int SHOW_MY_CHANNELS_CODE = 100;
	}

}
