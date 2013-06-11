package com.comcast.ui.libv1.widget;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

public interface IFocusHopperListener {
	public void onFocusChanged(boolean hasFocus, Point[] point, Drawable bitmapDrawable, ArrayList<Animator> set);
	public void onChildFocusChanged(Point[] points, Drawable drawable, ArrayList<Animator> set);
}
