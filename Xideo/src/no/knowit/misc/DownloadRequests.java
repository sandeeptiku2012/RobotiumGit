package no.knowit.misc;

import android.net.Uri;

public class DownloadRequests {
	private Uri uri;

	public DownloadRequests( Uri uri ) {
		this.uri = uri;
	}

	public Uri getUri() {
		return this.uri;
	}
}
