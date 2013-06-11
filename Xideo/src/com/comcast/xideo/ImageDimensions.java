package com.comcast.xideo;

import android.content.res.Resources;

import com.vimond.math.Size;

public class ImageDimensions {

	public static Size SUBS_VIDEO_THUMB_ON_FOCUS;
	public static Size SUBS_VIDEO_THUMB_ON_NON_FOCUS;
	public static Size SUBS_CHANNEL_THUMB_ON_FOCUS;
	public static Size SUBS_CHANNEL_THUMB_ON_NON_FOCUS;

	public static Size HOME_VIDEO_THUMB_ON_FOCUS;
	public static Size HOME_VIDEO_THUMB_ON_NON_FOCUS;
	public static Size HOME_CHANNEL_THUMB_ON_FOCUS;
	public static Size HOME_CHANNEL_THUMB_ON_NON_FOCUS;

	static {
		loadImageSizes();
	}

	private static void loadImageSizes() {
		Resources res = XideoApplication.getContext().getResources();
		ImageDimensions.HOME_VIDEO_THUMB_ON_FOCUS = getSize(res, R.dimen.subs_video_thumb_width, R.dimen.subs_video_thumb_height);
		ImageDimensions.HOME_VIDEO_THUMB_ON_NON_FOCUS = getSize(res, R.dimen.subs_video_thumb_width_no_focus, R.dimen.subs_video_thumb_height_no_focus);
		ImageDimensions.HOME_CHANNEL_THUMB_ON_FOCUS = getSize(res, R.dimen.subs_icon_thumb_width_height, R.dimen.subs_icon_thumb_width_height);
		ImageDimensions.HOME_CHANNEL_THUMB_ON_FOCUS = getSize(res, R.dimen.subs_icon_thumb_width_height_no_focus, R.dimen.subs_icon_thumb_width_height_no_focus);

		ImageDimensions.SUBS_VIDEO_THUMB_ON_FOCUS = getSize(res, R.dimen.subs_video_thumb_width, R.dimen.subs_video_thumb_height);
		ImageDimensions.SUBS_VIDEO_THUMB_ON_NON_FOCUS = getSize(res, R.dimen.subs_video_thumb_width_no_focus, R.dimen.subs_video_thumb_height_no_focus);
		ImageDimensions.SUBS_CHANNEL_THUMB_ON_FOCUS = getSize(res, R.dimen.subs_icon_thumb_width_height, R.dimen.subs_icon_thumb_width_height);
		ImageDimensions.SUBS_CHANNEL_THUMB_ON_FOCUS = getSize(res, R.dimen.subs_icon_thumb_width_height_no_focus, R.dimen.subs_icon_thumb_width_height_no_focus);
	}

	public static Size getSize(Resources r, int id1, int id2) {
		Size size = new Size((int) r.getDimension(id1), (int) r.getDimension(id2));
		return size;
	}
}
