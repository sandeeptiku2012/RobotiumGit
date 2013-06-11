package com.comcast.ui.libv1.widget;

import java.util.ArrayList;

import com.comcast.ui.libv1.R;
import com.comcast.ui.libv1.widget.model.ContentModel;
import com.comcast.ui.libv1.widget.model.VideoInfo;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.view.View.*;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

@RemoteView
public class VideoStripLayout extends ViewGroup implements OnFocusChangeListener, OnKeyListener {

	View				mIconView;
	TextView			mTitleView;
	LayoutInflater		inflater;

	int					mIconWidthOnFocus					= 270;
	int					mIconHeightOnFocus					= 270;
	int					mIconWidthOnNonFocus				= 188;
	int					mIconHeightOnNonFocus				= 188;

	int					mVideoFrameWidthOnFocus = 480;
	int					mVideoFrameHeightOnFocus = 270;
	int					mVideoFrameLayoutMargin				= 15;
	int					mIconLayoutMargin;
	int					mVideoFrameMoveUpDown;
	boolean				mIsIconVisible						= true;
	int					mCurrentChildIndex					= 0;
	int					mScrollDistance						= 0;

	int					mVideoFrameWidthOnNonFocus;
	int					mVideoFrameHeightOnNonFocus;

	ArrayList<View>		mVideoFrames						= new ArrayList<View>();
	ContentModel		model;

	static final int	ANIMATION_DURATION_SLIDE_LEFT_RIGHT	= 350;						// milliseconds
	static final int	ANIMATION_DURATION_SLIDE_UPDOWN		= 200;						// milliseconds
	static final int	ANIMATION_DURATION_FOCUS_CHANGE		= 200;						// milliseconds

	public static final String DEFAULT_FONT_NAME = "fonts/Flama-Basic.otf";
	
	public static Typeface DefaultFont;

	String mChannelTitleFontName;
	String mVideoTitleFontName;
	String mVideoDescriptionFontName;
	String mVideoMaturityFontName;
	
	Typeface mChannelTitleFont;
	Typeface mVideoTitleFont;
	Typeface mVideoDescriptionFont;
	Typeface mVideoMaturityFont;

	IFocusHopperListener listener;

	public interface OnSelectionChangeListener {
		void onSelectionChanged(ContentModel model, VideoInfo vi, boolean selected);
	}

	public interface OnVideoSelectionListener {
		void onSelectVideo(ContentModel model, VideoInfo vi, int index);
	}

	OnSelectionChangeListener	changeListener;
	OnVideoSelectionListener	selectionListener;

	public VideoStripLayout(Context context) {
		super(context);
		inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		init();
	}

	public VideoStripLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public VideoStripLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VideoStripLayout, defStyle, 0);

		mVideoFrameWidthOnFocus = (int) a.getDimension(R.styleable.VideoStripLayout_videoFrame_widthOnFocus, 480);
		mVideoFrameHeightOnFocus = (int) a.getDimension(R.styleable.VideoStripLayout_videoFrame_heightOnFocus, 270);

		mVideoFrameWidthOnNonFocus = (int) a.getDimension(R.styleable.VideoStripLayout_videoFrame_widthOnNonFocus, 270);
		mVideoFrameHeightOnNonFocus = (int) a.getDimension(R.styleable.VideoStripLayout_videoFrame_heightOnNonFocus,
				188);

		mIsIconVisible = a.getBoolean(R.styleable.VideoStripLayout_isIconVisible, true);

		mIconWidthOnFocus = (int) a.getDimension(R.styleable.VideoStripLayout_icon_widthOnFocus, 270);
		mIconHeightOnFocus = mVideoFrameHeightOnFocus;
		mIconWidthOnFocus = mVideoFrameHeightOnFocus;

		mIconWidthOnNonFocus = (int) a.getDimension(R.styleable.VideoStripLayout_icon_widthOnNonFocus, 188);
		mIconHeightOnNonFocus = mVideoFrameHeightOnNonFocus;

		mVideoFrameLayoutMargin = (int) a.getDimension(R.styleable.VideoStripLayout_videoFrame_layoutMargin, 15);
		mIconLayoutMargin = (int) a.getDimension(R.styleable.VideoStripLayout_icon_layoutMargin, 10);
		mVideoFrameMoveUpDown = (int) a.getDimension(R.styleable.VideoStripLayout_videoFrame_moveUpDown, 40);

		mChannelTitleFontName = a.getString(R.styleable.VideoStripLayout_textFont_channelTitle);
		mVideoTitleFontName = a.getString(R.styleable.VideoStripLayout_textFont_videoTitle);
		mVideoDescriptionFontName = a.getString(R.styleable.VideoStripLayout_textFont_videoDesc);
		mVideoMaturityFontName = a.getString(R.styleable.VideoStripLayout_textFont_videoMaturity);
		a.recycle();
		init();
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	public void setIconWidth(int width) {
		mIconWidthOnFocus = width;
	}

	public void setIconDrawable(Drawable drawable) {
		ImageView iv = (ImageView) findViewById(R.id.imageview1);
		iv.setImageDrawable(drawable);
	}

	public void setIconBitmapDrawable(Bitmap bitmap) {
		ImageView iv = (ImageView) findViewById(R.id.imageview1);
		iv.setImageBitmap(bitmap);
	}

	public void setBlobListener(IFocusHopperListener listener) {
		this.listener = listener;
	}

	private View getIconView() {
		View view = inflater.inflate(R.layout.content_icon, null);
		return view;
	}

	private TextView getTitleView() {
		mTitleView = new TextView(getContext());
		mTitleView.setTextColor(Color.BLACK);
		return mTitleView;
	}

	public void setIconVisible(boolean state) {
		mIsIconVisible = state;
	}

	public void setContentModel(ContentModel model) {
		this.model = model;
		initData();
		invalidate();
	}

	public void setOnSelectionChangeListener(OnSelectionChangeListener listener) {
		this.changeListener = listener;
	}

	public void setOnVideoSelectionListner(OnVideoSelectionListener listener) {
		this.selectionListener = listener;
	}

	private void init() {
		if(!isInEditMode()){
			AssetManager assetManager = getContext().getAssets();
			DefaultFont = Typeface.createFromAsset(assetManager, DEFAULT_FONT_NAME);
			
			if(mChannelTitleFontName != null){
				mChannelTitleFont = Typeface.createFromAsset(assetManager, mChannelTitleFontName);
			}else{
				mChannelTitleFont = DefaultFont;
			}
			
			if(mVideoTitleFontName != null){
				mVideoTitleFont = Typeface.createFromAsset(assetManager, mVideoTitleFontName);
			}else{
				mVideoTitleFont = DefaultFont;
			}
			
			if(mVideoDescriptionFontName != null){
				mVideoDescriptionFont = Typeface.createFromAsset(assetManager, mVideoDescriptionFontName);
			}else{
				mVideoDescriptionFont = DefaultFont;
			}
			
			if(mVideoMaturityFontName != null){
				mVideoMaturityFont = Typeface.createFromAsset(assetManager, mVideoMaturityFontName);
			}else{
				mVideoMaturityFont = DefaultFont;
			}
		}

		setFocusable(true);
		setClickable(true);
		setOnKeyListener(this);
		setOnFocusChangeListener(this);
	}

	private void initData() {
		removeAllViews();
		if (mIconView == null)
			mIconView = getIconView();
		if (mTitleView == null) {
			mTitleView = getTitleView();
			if (model != null) {
				mTitleView.setText(model.title);
			}
		}

		addVideoFrames();
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		addView(mIconView, lp);
		lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		addView(mTitleView, lp);

		TextView tv = (TextView) mIconView.findViewById(R.id.text1);
		//TODO: need to set the icon text from model -- should it be channel name or title ?
		tv.setText(model.title);
		tv.setTypeface(mChannelTitleFont);
	}

	private void addVideoFrames() {
		if (model == null) {
			return;
		}

		if (model.videoList == null) {
			return;
		}

		for (int i = 0; i < model.videoList.size(); i++) {
			View view = inflater.inflate(R.layout.content_video_button, null);
			view.setLayoutParams(new MarginLayoutParams(mVideoFrameLayoutMargin, mVideoFrameLayoutMargin));

			VideoInfo vi = model.videoList.get(i);

			TextView tv;
			tv = (TextView) view.findViewById(R.id.text1);
			tv.setText(vi.title);
			tv.setTypeface(mVideoTitleFont);
			
			tv = (TextView) view.findViewById(R.id.text2);
			tv.setText(vi.description);
			tv.setTypeface(mVideoDescriptionFont);

			tv = (TextView) view.findViewById(R.id.text3);
			tv.setText(vi.rating);
			tv.setTypeface(mVideoMaturityFont);

			int watchStatusDrawableId = R.drawable.unwatched;
			switch(vi.watchstatus){
				case watched:
					watchStatusDrawableId = R.drawable.watched;
					break;
				case partial:
					watchStatusDrawableId = R.drawable.partial;
					break;
			}
			
			Drawable watchStatusDrawable = getResources().getDrawable(watchStatusDrawableId);
			ImageView iv = (ImageView) view.findViewById(R.id.image2);
			iv.setImageDrawable(watchStatusDrawable);
			
			addView(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			//TODO: remove this assignment and hide the video num text view from video button layout
			tv = (TextView) view.findViewById(R.id.videonum);
			tv.setText(i + "");
			
			mVideoFrames.add(view);
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mVideoFrames.size() > 0) {
			if (hasFocus()) {
				showInfoForAllVideoFrames();
			} else {
				hideInfoFromAllVideoFrames();
			}
		}

		int maxHeight = 0;
		int maxWidth = 0;
		int totalWidth = 0;

		int specModeW = MeasureSpec.getMode(widthMeasureSpec);
		int specSizeW = MeasureSpec.getSize(widthMeasureSpec);

		int specModeH = MeasureSpec.getMode(heightMeasureSpec);
		int specSizeH = MeasureSpec.getSize(heightMeasureSpec);
		maxWidth = specSizeW;
		maxHeight = specSizeH;

		// Spec mode for Width has not been considered for calculating view dimensions

		totalWidth = specSizeW + mVideoFrameLayoutMargin * mVideoFrames.size();

		if (!mIsIconVisible) {
			totalWidth -= mIconWidthOnFocus;
		}

		if (mIconView != null) {
			ImageView iv = (ImageView) mIconView.findViewById(R.id.imageview1);
			ViewGroup.LayoutParams lp1 = (ViewGroup.LayoutParams) iv.getLayoutParams();
			if (hasFocus()) {
				lp1.width = mIconWidthOnFocus;
				lp1.height = mIconHeightOnFocus;
			} else {
				lp1.width = mIconWidthOnNonFocus;
				lp1.height = mIconHeightOnNonFocus;
			}

			iv.setLayoutParams(lp1);
		}

		for (int i = 0; i < mVideoFrames.size(); i++) {
			View videoFrame = mVideoFrames.get(i).findViewById(R.id.image1);
			ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) videoFrame.getLayoutParams();
			if (hasFocus() || i < mCurrentChildIndex) {
				lp.width = mVideoFrameWidthOnFocus;
				lp.height = mVideoFrameHeightOnFocus;
			} else {
				lp.width = mVideoFrameWidthOnNonFocus;
				lp.height = mVideoFrameHeightOnNonFocus;
			}
			videoFrame.setLayoutParams(lp);
		}

		measureChildren(widthMeasureSpec, heightMeasureSpec);

		int maxChildHeight = 0;
		for (int i = 0; i < mVideoFrames.size(); i++) {
			View videoFrame = mVideoFrames.get(i);
			int vHeight = videoFrame.getMeasuredHeight() + mVideoFrameLayoutMargin * 2;
			maxChildHeight = Math.max(maxChildHeight, vHeight);
		}

		if (specModeH == MeasureSpec.UNSPECIFIED) {
			maxHeight = maxChildHeight;
		} else if (specModeH == MeasureSpec.AT_MOST) {
			maxHeight = Math.min(maxHeight, maxChildHeight);
		}

		maxWidth = Math.max(maxWidth, totalWidth > specSizeW ? specSizeW : totalWidth);
		setMeasuredDimension(maxWidth, maxHeight);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		int totalHeight = getMeasuredHeight();

		int count = mVideoFrames.size();
		int distanceFromLeft = hasFocus() ? 20 : 80;

		int childTop = mVideoFrameMoveUpDown;
		int iconHeight = 0;
		int iconWidth = 0;
		if (mIsIconVisible && mIconView != null) {
			iconWidth = mIconView.getMeasuredWidth();
			iconHeight = mIconView.getMeasuredHeight();
			int iconTop = (totalHeight - iconHeight);
			mIconView.layout(distanceFromLeft, iconTop, iconWidth
					+ distanceFromLeft, iconTop + iconHeight);
		} else {
			if (mTitleView != null) {
				mTitleView.layout(left, top, mTitleView.getMeasuredWidth(), mTitleView.getMeasuredHeight());
			}
		}

		int childLeft = distanceFromLeft + iconWidth + mVideoFrameLayoutMargin/2;

		for (int i = 0; i < count; i++) {
			final View child = mVideoFrames.get(i);
			if (child.getVisibility() != GONE) {
				childTop = totalHeight - child.getMeasuredHeight() - mVideoFrameLayoutMargin/2;
				final int width = child.getMeasuredWidth() + mVideoFrameLayoutMargin/2;
				final int height = child.getMeasuredHeight() + mVideoFrameLayoutMargin/2;

				child.layout(childLeft, childTop, childLeft + width, childTop + height);
				childLeft += width;
			}
		}
	}

	public void toggleSelectionView(int index, boolean hasFocus) {
		toggleSelectionView(mVideoFrames.get(index), hasFocus);
	}

	public void toggleSelectionView(View v, boolean hasFocus) {
		View v1, v2, v3;
		v1 = v.findViewById(R.id.text1);
		v2 = v.findViewById(R.id.text2);
		v2 = v.findViewById(R.id.text2);
		v3 = v.findViewById(R.id.text3);

		ViewSwitcher vs = (ViewSwitcher) v.findViewById(R.id.viewSwitcher1);
		
		if (hasFocus) {
			v1.setVisibility(View.VISIBLE);
			v2.setVisibility(View.VISIBLE);
			v3.setVisibility(View.VISIBLE);
			if(vs.getCurrentView().getId() == R.id.image2)
				vs.showNext();
		} else {
			v2.setVisibility(View.GONE);
			v3.setVisibility(View.GONE);
			if(vs.getCurrentView().getId() == R.id.image3)
				vs.showPrevious();
		}
	}

	public void toggleOnFocusView(int index, boolean hasFocus) {
		View v = mVideoFrames.get(index);
		View v1, v2, v3, iv;
		View iconTextView;
		v1 = v.findViewById(R.id.text1);
		v2 = v.findViewById(R.id.text2);
		v2 = v.findViewById(R.id.text2);
		v3 = v.findViewById(R.id.text3);
		iv = v.findViewById(R.id.viewSwitcher1);

		iconTextView = mIconView.findViewById(R.id.text1);
		if (hasFocus) {
			v1.setVisibility(View.VISIBLE);
			iconTextView.setVisibility(View.VISIBLE);
			iv.setVisibility(View.VISIBLE);
		} else {
			v1.setVisibility(View.GONE);
			v2.setVisibility(View.GONE);
			v3.setVisibility(View.GONE);
			iv.setVisibility(View.GONE);
			iconTextView.setVisibility(View.GONE);
		}
	}

	Object	objLock;

	@SuppressLint("NewApi")
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		boolean consumed = false;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_LEFT:
					if (mCurrentChildIndex > 0) {
						if (changeListener != null) {
							VideoInfo vi = model.videoList.get(mCurrentChildIndex);
							changeListener.onSelectionChanged(model, vi, false);
						}

						notifyChildFocusChangeForBlob(true);
						mCurrentChildIndex--;
						scrollToRight();
						if (changeListener != null) {
							VideoInfo vi = model.videoList.get(mCurrentChildIndex);
							changeListener.onSelectionChanged(model, vi, true);
						}
					}
					consumed = true;
					break;

				case KeyEvent.KEYCODE_DPAD_RIGHT:
					if (mCurrentChildIndex < mVideoFrames.size() - 1) {
						if (changeListener != null) {
							VideoInfo vi = model.videoList.get(mCurrentChildIndex);
							changeListener.onSelectionChanged(model, vi, false);
						}

						notifyChildFocusChangeForBlob(false);
						mCurrentChildIndex++;
						scrollToLeft();
						if (changeListener != null) {
							VideoInfo vi = model.videoList.get(mCurrentChildIndex);
							changeListener.onSelectionChanged(model, vi, true);
						}
					}
					consumed = true;
					break;

			}
		}
		return consumed;
	}

	@SuppressLint("NewApi")
	private boolean scrollToLeft() {
		final int currentChildIndex = mCurrentChildIndex;
		if (currentChildIndex < 0)
			return false;

		final View leftChild = mVideoFrames.get(currentChildIndex - 1);

		mScrollDistance -= (leftChild.getMeasuredWidth() + mVideoFrameLayoutMargin/2);

		ObjectAnimator anim2 = ObjectAnimator.ofFloat(leftChild, "alpha", 0.0f);
		anim2.setDuration(ANIMATION_DURATION_SLIDE_LEFT_RIGHT);
		anim2.setInterpolator(new AccelerateInterpolator());
		anim2.start();

		toggleSelectionView(mVideoFrames.get(mCurrentChildIndex - 1), false);

		int count = mVideoFrames.size();
		for (int i = currentChildIndex - 1; i < count; i++) {
			final View child = mVideoFrames.get(i);
			ObjectAnimator anim = ObjectAnimator.ofFloat(child, "translationX", mScrollDistance);
			anim.setDuration(ANIMATION_DURATION_SLIDE_LEFT_RIGHT);
			anim.setInterpolator(new DecelerateInterpolator());
			anim.start();
		}

		toggleSelectionView(currentChildIndex, true);
		return true;
	}

	@SuppressLint("NewApi")
	private boolean scrollToRight() {
		final int currentChildIndex = mCurrentChildIndex;
		if (currentChildIndex > mVideoFrames.size())
			return false;

		final View leftChild = mVideoFrames.get(currentChildIndex);
		mScrollDistance += (leftChild.getMeasuredWidth() + mVideoFrameLayoutMargin/2);

		ObjectAnimator anim2 = ObjectAnimator.ofFloat(leftChild, "alpha", 1.0f);
		anim2.setDuration(ANIMATION_DURATION_SLIDE_LEFT_RIGHT);
		anim2.setInterpolator(new AccelerateInterpolator());
		anim2.start();

		toggleSelectionView(mVideoFrames.get(currentChildIndex + 1), false);

		for (int i = currentChildIndex; i < mVideoFrames.size(); i++) {
			final View child = mVideoFrames.get(i);
			ObjectAnimator anim = ObjectAnimator.ofFloat(child, "translationX", mScrollDistance);
			anim.setDuration(ANIMATION_DURATION_SLIDE_LEFT_RIGHT);
			anim.setInterpolator(new DecelerateInterpolator());
			anim.start();
		}

		toggleSelectionView(currentChildIndex, true);
		return true;
	}

	int[] location = new int[2];
	View focussedChildView;
	int moveDistance;

	private void setupBlobValues(){
		Thread action = new Thread(new Runnable(){
			@Override
			public void run() {
				focussedChildView = getChildAt(mCurrentChildIndex);
				moveDistance = focussedChildView.getMeasuredWidth()/2;
				View view = focussedChildView.findViewById(R.id.viewSwitcher1);
				view.getLocationOnScreen(location);
				location[0] += view.getMeasuredWidth()/2;
				location[1] += view.getMeasuredHeight()/2;
			}
		});

		// let the layout changes get completed otherwise would not be
		// able to get the correct coordinates
		postDelayed(action, 250);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		if (mVideoFrames != null && mVideoFrames.size() > 0) {
			if (hasFocus) {
				showInfoForAllVideoFrames();
				toggleSelectionView(mCurrentChildIndex, true);
			} else {
				hideInfoFromAllVideoFrames();
			}

			setupBlobValues();
			Thread action1 = new Thread(new Runnable(){
				@Override
				public void run() {
					notifyFocusChangeForBlob(true);
				}
			});

			if(hasFocus){
				// let the layout completes otherwise would not be
				// able to know the coordinates
				postDelayed(action1, 350);
			}else{
				notifyFocusChangeForBlob(false);
			}
		}
	}

	private void showInfoForAllVideoFrames() {
		for (int i = 0; i < mVideoFrames.size(); i++) {
			toggleOnFocusView(i, true);
		}
	}

	private void hideInfoFromAllVideoFrames() {
		for (int i = 0; i < mVideoFrames.size(); i++) {
			toggleOnFocusView(i, false);
		}
	}

	private void notifyFocusChangeForBlob(boolean focussed){
		if (this.listener != null) {
			int x = location[0];
			int y = location[1];

			Point p1 = new Point(x, y);
			listener.onFocusChanged(focussed, new Point[] { p1, p1}, null, null);
		}
	}

	private void notifyChildFocusChangeForBlob(boolean toRight) {
		if (this.listener != null) {
			int x = location[0];
			int y = location[1];

			Point p1 = new Point(x, y);
			if (toRight) {
				Point p2 = new Point(x - moveDistance, y);
				listener.onChildFocusChanged(new Point[] { p1, p2, p1 }, null, null);
			} else {
				Point p2 = new Point(x + moveDistance, y);
				listener.onChildFocusChanged(new Point[] { p1, p2, p1 }, null, null);
			}
		}
	}

	public static class LayoutParams extends ViewGroup.MarginLayoutParams {

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		public LayoutParams(int w, int h) {
			super(w, h);
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

		@Override
		public void setMargins(int left, int top, int right, int bottom) {
			super.setMargins(left, top, right, bottom);
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable("instanceState", super.onSaveInstanceState());
		bundle.putParcelable("model", model);
		bundle.putInt("mCurrentChildIndex", mCurrentChildIndex);
		bundle.putInt("mScrollDistance", mScrollDistance);
		bundle.putInt("moveDistance", moveDistance);
		bundle.putInt("mVideoFrameMoveUpDown",mVideoFrameMoveUpDown);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			this.model = bundle.getParcelable("model");
			this.mCurrentChildIndex = bundle.getInt("mCurrentChildIndex");
			this.mScrollDistance = bundle.getInt("mScrollDistance");
			this.moveDistance = bundle.getInt("moveDistance");
			this.mVideoFrameMoveUpDown = bundle.getInt("mVideoFrameMoveUpDown");
			super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
			return;
		}

		super.onRestoreInstanceState(state);
	}

}