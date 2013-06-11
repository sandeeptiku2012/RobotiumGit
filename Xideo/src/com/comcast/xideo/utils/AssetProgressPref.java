package com.comcast.xideo.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.inject.Inject;
import com.vimond.entity.AssetProgress;
import com.vimond.entity.Video;
import com.vimond.entity.VideoReference;

public class AssetProgressPref implements AssetProgress {
	private static final String PROGRESS_KEY = "_p";

	@Inject
	private SharedPreferences preferences; // NO_UCD (use final)

	@Override
	public void setProgress( VideoReference video, long position ) {
		Long assetId = video.getId();
		Editor edit = preferences.edit();
		edit.putLong( assetId + PROGRESS_KEY, position );
		edit.commit();
	}

	@Override
	public Long getProgress( VideoReference video ) {
		Long assetId = video.getId();
		return preferences.getLong( assetId.toString() + PROGRESS_KEY, 0 );
	}

	@Override
	public void removeProgress( VideoReference video ) {
		Long assetId = video.getId();
		Editor edit = preferences.edit();
		edit.remove( assetId + PROGRESS_KEY );
		edit.commit();
	}

	@Override
	public boolean hasSeen( Video video ) {
		return getProgress( video ) != null;
	}
}
