package com.comcast.xideo;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class ImageEffects {
	private static native void blur(Bitmap bitmap, Bitmap out, int radius);

	private static native void soften( Bitmap bitmap, Bitmap out, int threshold );

	static {
		System.loadLibrary("imageeffects");
	}

	public static Bitmap applyBlur(Bitmap bitmapOrig, int radius) {
		Bitmap alternateBitmap = null;
		alternateBitmap = Bitmap.createBitmap(bitmapOrig.getWidth(), bitmapOrig.getHeight(), Config.ARGB_8888);
		blur(bitmapOrig, alternateBitmap, radius);
		return alternateBitmap;
	}

	public static Bitmap soften( Bitmap bitmapOrig, int threshold ) {
		Bitmap alternateBitmap = null;
		alternateBitmap = Bitmap.createBitmap( bitmapOrig.getWidth(), bitmapOrig.getHeight(), Config.ARGB_8888 );
		soften( bitmapOrig, alternateBitmap, threshold );
		return alternateBitmap;
	}
}
