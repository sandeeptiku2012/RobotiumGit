/*************************************************************************
*
* ADOBE CONFIDENTIAL
* ___________________
*
*  Copyright 2012 Adobe Systems Incorporated
*  All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains
* the property of Adobe Systems Incorporated and its suppliers,
* if any.  The intellectual and technical concepts contained
* herein are proprietary to Adobe Systems Incorporated and its
* suppliers and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material
* is strictly forbidden unless prior written permission is obtained
* from Adobe Systems Incorporated.
**************************************************************************/

package com.comcast.media.utils;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.comcast.xideo.R;

public class MarkableSeekBar extends ProgressBar {
	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Bitmap thumbImage, thumbPressedImage, thumbDisabledImage;
	private float thumbHalfWidth, thumbHalfHeight, padding;
	private ArrayList<Marker> markers = new ArrayList<Marker>();
	private Drawable markerBackground;
	private Range availableProgress = new Range();

	static class Range {
		float start, end;
	}

	public MarkableSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MarkableSeekBar(Context context) {
		super(context);
		init();
	}

	public MarkableSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// Load the background drawables
		markerBackground = getResources().getDrawable(R.drawable.msb_mark_bg);
		// Load the thumb bitmaps
		// thumbImage = BitmapFactory.decodeResource(getResources(),
		// R.drawable.msb_thumb_normal);
		// thumbPressedImage = BitmapFactory.decodeResource(getResources(),
		// R.drawable.msb_thumb_pressed);
		// thumbDisabledImage = BitmapFactory.decodeResource(getResources(),
		// R.drawable.msb_thumb_disabled);

		// thumbHalfWidth = 0.5f * thumbImage.getWidth();
		// thumbHalfHeight = 0.5f * thumbImage.getHeight();
		// padding = thumbHalfWidth;
	}

	public static class Marker {
		private int start, end;

		public Marker(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return end;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Marker other = (Marker) obj;
			return this.start == other.start && this.end == other.end;
		}
	}

	/**
	 * Adds a marker to the seek bar.
	 * 
	 * @param marker
	 */
	public void addMarker(Marker marker) {
		if (marker == null || marker.getStart() > marker.getEnd() || marker.getStart() > getMax() || marker.getEnd() > getMax())
			return;
		markers.add(marker);
	}

	/**
	 * Removes a marker from the seek bar.
	 * 
	 * @param marker
	 */
	public void removeMarker(Marker marker) {
		markers.remove(marker);
	}

	/**
	 * Removes all markers
	 */
	public void clearMarkers() {
		markers.clear();
	}

	/**
	 * Converts a normalized value into screen space.
	 * 
	 */
	private float normalizedToScreen(double normalizedCoord) {
		return (float) (padding + normalizedCoord * (getWidth() - 2 * padding));
	}

	/**
	 * Draws the thumb image on specified X coordinate.
	 * 
	 */
	private void drawThumb(float screenCoord, Canvas canvas) {
		Bitmap thumb = thumbImage;
		if (isEnabled() == false)
			thumb = thumbDisabledImage;
		else if (isPressed())
			thumb = thumbPressedImage;
		canvas.drawBitmap(thumb, screenCoord - thumbHalfWidth, (float) ((0.5f * getHeight()) - thumbHalfHeight), paint);
	}

	/**
	 * Returns the drawable intrinsec height.
	 * If it has no intrinsic height (such as with a solid color), returns a third of the view height.
	 * @param drawable
	 * @return
	 */
	private int getDrawableHeight(Drawable drawable) {
		int height = drawable.getIntrinsicHeight();
		if (height == -1) {
			// Has no intrinsic height, make it a third the view height.
			return getHeight() / 3;
		}
		return height;
	}

	/**
	 * Gets top coordinate based on the drawable height and the view height.
	 * 
	 * @param drawable
	 * @return
	 */
	private int getTop(Drawable drawable) {
		int height = getDrawableHeight(drawable);
		if (height >= getHeight())
			return 0;
		// Center in view.
		return (getHeight() - height) / 2;
	}

	/**
	 * Gets bottom coordinate based on the drawable height and the view height.
	 * 
	 * @param drawable
	 * @return
	 */
	private int getBottom(Drawable drawable) {
		int height = getDrawableHeight(drawable);
		if (height >= getHeight())
			return getHeight();
		// Center in view.
		return height + (getHeight() - height) / 2;
	}

	/**
	 * Sets the available progress to the specified value.
	 * 
	 * @param progress
	 */
	public void setAvailable(int start, int end) {
		if (start > getMax() || start < 0 || end > getMax() || end < 0)
			throw new IllegalArgumentException("Please choose values between 0 and " + getMax());
		boolean invalidate = availableProgress.start != start || availableProgress.end != end;
		this.availableProgress.start = start;
		this.availableProgress.end = end;
		if (invalidate)
			invalidate();
	}

	/**
	 * Returns the available progress.
	 * 
	 * @return
	 */
	private Range getAvailable() {
		return availableProgress;
	}

	@Override
	public void onDraw( Canvas canvas ) {
		super.onDraw( canvas );

		int left = (int) padding;
		int right = (int) (getWidth() - padding);

		// Draw markers
		if (markerBackground != null) {
			for (Marker marker : markers) {
				left = (int) normalizedToScreen((float) marker.getStart() / getMax());
				right = (int) normalizedToScreen((float) marker.getEnd() / getMax());
				markerBackground.setBounds( left, 0, right, getHeight() );
				markerBackground.draw(canvas);
			}
		}
	}
}
