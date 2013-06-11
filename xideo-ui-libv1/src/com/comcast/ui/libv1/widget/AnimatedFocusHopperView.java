package com.comcast.ui.libv1.widget;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.R;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

public class AnimatedFocusHopperView extends FrameLayout {

	public static final int ANIMATION_DURATION = 150;
	public static final Interpolator DEFAULT_INTERPOLATER = new DecelerateInterpolator(2.2f);
	
	ConcurrentLinkedQueue<Point> pendingList = new ConcurrentLinkedQueue<Point>();

	int mViewWidth = 50;
	Interpolator mInterpolator = DEFAULT_INTERPOLATER;
	boolean isAnimating = false;
	int animationDuration;
	
	public AnimatedFocusHopperView(Context context) {
		this(context, null);
	}

	public AnimatedFocusHopperView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AnimatedFocusHopperView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setDefaultAnimationDuration();
	}

	View contentView ;
	public void setContentLayout(int layoutId) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(layoutId, this, true);
//		addView(contentView);
		inflater = null;
	}

	public void setAnimationDuration(int duration) {
		animationDuration = duration;
	}

	public void setDefaultAnimationDuration() {
		animationDuration = ANIMATION_DURATION;
	}

	public void positionTo(Point point) {
		this.setTranslationX( point.x );
		this.setTranslationY( point.y );
	}

	public void positionCenterInScreen() {
		int[] location = new int[2];
		this.getLocationOnScreen( location );
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) getContext().getSystemService( Context.WINDOW_SERVICE );
		wm.getDefaultDisplay().getMetrics( dm );
		
		int tx = (dm.widthPixels - getWidth())/2;
		int ty = (dm.heightPixels - getHeight())/2;
		this.setTranslationX( tx );
		this.setTranslationY( ty );
	}

	public void show() {
		ValueAnimator animAlpha;
		animAlpha = ObjectAnimator.ofFloat(this, "alpha", 1.0f);
		animAlpha.setDuration(animationDuration);
		animAlpha.start();
	}
	
	public void hide() {
		ValueAnimator animAlpha;
		animAlpha = ObjectAnimator.ofFloat(this, "alpha", 0f);
		animAlpha.setDuration(animationDuration);
		animAlpha.start();
	}

	public void showImmediate() {
		this.setVisibility( View.VISIBLE );
	}

	public void hideImmediate() {
		this.setVisibility( View.GONE );
	}

	RotateAnimation rotate;
	public void startRotateAnimation() {
		rotate = new RotateAnimation( 0, 360, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f );
		rotate.setDuration( 2000 );
		rotate.setInterpolator( new LinearInterpolator() );
		rotate.setRepeatCount( Animation.INFINITE );
		View childView = ((ViewGroup) contentView).getChildAt( 0 );
		childView.startAnimation( rotate );
	}

	public void stopRotateAnimation() {
		if(rotate != null) {
			rotate.cancel();
		}
	}

	public void animateTo(View view) {
		int[] location = new int[2];
		view.getLocationOnScreen( location );
		Point point = new Point();
		point.x = location[0] + view.getMeasuredWidth()/2;
		point.y = location[1] + view.getMeasuredHeight()/2;
		animateBlob( new Point[] {point} );
	}

	public void animateBlob(final Point[] points) {
		mViewWidth = getMeasuredWidth();
		ArrayList<Point> arrPoints = new ArrayList<Point>();
		for (Point point : points) {
			arrPoints.add(point);
		}

		pendingList.clear();
		pendingList.addAll(arrPoints);
		if(!isAnimating) animateBlob(false);
	}

	int x1 = 0, y1 = 0;
	int x2 = 0, y2 = 0;
	
	private void animateBlob(final boolean hideOnAnimationEnd){
		Point p;
		p = pendingList.poll();
		if(p != null){
			if (x1 <= 0 && y1 <= 0) {
				x1 = p.x;
				y1 = p.y;
			}else{
				x2 = p.x;
				y2 = p.y;
			}

			if(x2 == 0 && y2 == 0){
				animateBlob(hideOnAnimationEnd);
			}else{
				animateBlob(x1, y1, x2, y2, mInterpolator, hideOnAnimationEnd);
			}
		}
	}

	public void setInterpolatorForDestination(Interpolator interpolator) {
		mInterpolator = interpolator;
	}

	private void animateBlob(final int tx1, final int ty1, final int tx2, final int ty2,
			Interpolator interpolator, final boolean hideOnAnimationEnd) {
		
		ValueAnimator animX = ObjectAnimator.ofFloat(this, "translationX",
				tx2 - mViewWidth/2);
		ValueAnimator animY = ObjectAnimator.ofFloat(this, "translationY",
				ty2 - mViewWidth/2);
		animX.setDuration(animationDuration);
		animY.setDuration(animationDuration);

		AnimatorSet aset = new AnimatorSet();
		ArrayList<Animator> items = new ArrayList<Animator>();

		if(pendingList.size() == 0) {
			animX.setInterpolator(interpolator);
			animY.setInterpolator(interpolator);
			if(hideOnAnimationEnd) {
				hide();
			}
		}
		
		aset.addListener(new AnimatorListener(){

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator vanimation) {
				x1 = tx2;
				y1 = ty2;
				if(pendingList.size() == 0){
					isAnimating = false;
					setDefaultAnimationDuration();
				}else{
					isAnimating = true;
					animateBlob(false);
				}
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
				contentView.setVisibility(View.VISIBLE);
				contentView.setAlpha(1.0f);
			}
		});
		
		items.add(animX);
		items.add(animY);

//		aset.setDuration(ANIMATION_DURATION);
		aset.playTogether(items);
		aset.start();
	}
}
