package gueei.binding.observables;

import android.graphics.Bitmap;

import gueei.binding.Observable;

public class BitmapObservable extends Observable<Bitmap> {
	public BitmapObservable() {
		super( Bitmap.class );
	}
}
