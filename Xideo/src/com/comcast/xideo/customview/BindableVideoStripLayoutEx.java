package com.comcast.xideo.customview;

import gueei.binding.collections.ObservableCollection;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RemoteViews.RemoteView;
import android.widget.ViewSwitcher;

import com.comcast.ui.libv1.widget.BindableVideoStripLayout;
import com.comcast.xideo.ItemSelectedEvent;
import com.comcast.xideo.R;
import com.comcast.xideo.XideoApplication;
import com.comcast.xideo.events.ChannelItemEvent;
import com.comcast.xideo.events.IItemEvent;
import com.comcast.xideo.events.VideoItemEvent;
import com.comcast.xideo.viewmodel.ChannelInfoViewModel;
import com.comcast.xideo.viewmodel.VideoInfoViewModel;

@RemoteView
public class BindableVideoStripLayoutEx extends BindableVideoStripLayout
		implements BindableVideoStripLayout.OnSelectionChangeListener {

	public static final int ANIMATION_DURATION = 350;// ms

	int thumbWidthSmall, thumbHeightSmall, thumbWidthLarge, thumbHeightLarge;

	public BindableVideoStripLayoutEx(Context context) {
		this(context, null);
	}

	public BindableVideoStripLayoutEx(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("NewApi")
	public BindableVideoStripLayoutEx(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
		setOnSelectionChangeListener(this);
//		LayoutTransition lt = new LayoutTransition();
//		if (Build.VERSION.SDK_INT >= 16) {
//			lt.enableTransitionType(LayoutTransition.CHANGING);
//			Animator animator = AnimatorInflater.loadAnimator(context, R.anim.slide_bottom_to_top);
//			lt.setAnimator(LayoutTransition.CHANGING, animator);
//        }
//		setLayoutTransition(lt);
	}

	private void init() {
		setImageThumbnailDimensions();
	}

	private void setImageThumbnailDimensions() {
		this.thumbWidthSmall = (int) getContext().getResources().getDimension( R.dimen.subs_video_thumb_width_no_focus );
		this.thumbHeightSmall = (int) getContext().getResources().getDimension( R.dimen.subs_video_thumb_height_no_focus );
		this.thumbWidthLarge = (int) getContext().getResources().getDimension( R.dimen.subs_video_thumb_width );
		this.thumbHeightLarge = (int) getContext().getResources().getDimension( R.dimen.subs_video_thumb_height );
	}

	@Override
	public void onSelectionChanged(ObservableCollection<Object> itemList,
			int index, final View view, final boolean selected) {
		if (view == null)
			return;
		View v1, v2, v3, v4, v5;
		v1 = view.findViewById( R.id.text2 );
		v2 = view.findViewById( R.id.text3 );
		v3 = view.findViewById( R.id.text4 );
		v4 = view.findViewById( R.id.text5 );
		v5 = view.findViewById( R.id.video_status_image );

		ViewSwitcher vs = (ViewSwitcher) view.findViewById( R.id.viewSwitcher1 );
		View vic = view.findViewById( R.id.videoinfoContainer );

		if (selected) {
			int top = view.getTop();
			v1.setVisibility( View.VISIBLE );
			v2.setVisibility( View.VISIBLE );
			v3.setVisibility( View.VISIBLE );
			v4.setVisibility( View.VISIBLE );
			v5.setVisibility( View.VISIBLE );
			if (vs.getCurrentView().getId() == R.id.image2)
				vs.showNext();

			ObjectAnimator anim = ObjectAnimator.ofFloat( view,
					"translationY", top, 0 );
			anim.setDuration( ANIMATION_DURATION );
			anim.setInterpolator( new DecelerateInterpolator( 1.2f ) );
			anim.start();
		} else {
			v1.setVisibility( View.GONE );
			v2.setVisibility( View.GONE );
			v3.setVisibility( View.GONE );
			v4.setVisibility( View.GONE );
			v5.setVisibility( View.GONE );

			if (vs.getCurrentView().getId() == R.id.image3)
				vs.showPrevious();
		}

		vic.setVisibility( View.VISIBLE );
		view.requestLayout();
		if (selected) {
			View iv = view.findViewById( R.id.image1 );
			XideoApplication.getBus().post( new ItemSelectedEvent( this, iv ) );
		}

	}

	@Override
	public void onItemSelected(Object obj, int index, View child) {
		IItemEvent evt = null;
		if (obj instanceof ChannelInfoViewModel) {
			evt = new ChannelItemEvent(this, (ChannelInfoViewModel) obj);
		}
		if (obj instanceof VideoInfoViewModel) {
			evt = new VideoItemEvent( this, (VideoInfoViewModel) obj );
		}
		if (evt != null)
			XideoApplication.getBus().post(evt);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	public void onFocusChanged( final View view, final boolean hasFocus ) {
		int startWidth = view.getWidth();
		int startHeight = view.getHeight();
		int endWidth;
		int endHeight;
		View vic = view.findViewById( R.id.videoinfoContainer );
		if (hasFocus) {
			endWidth = thumbWidthLarge;
			endHeight = thumbHeightLarge;
			vic.setVisibility( View.VISIBLE );
		} else {
			endWidth = thumbWidthSmall;
			endHeight = thumbHeightSmall;
			vic.setVisibility( View.GONE );
		}

		ValueAnimator anim = new ValueAnimator();
		PropertyValuesHolder pvhX, pvhY;
		pvhX = PropertyValuesHolder.ofInt( "x", startWidth, endWidth );
		pvhY = PropertyValuesHolder.ofInt( "y", startHeight, endHeight );
		anim.setInterpolator( new AccelerateInterpolator() );
		anim.setValues( pvhX, pvhY );

		anim.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate( ValueAnimator valueAnimator ) {
				int x = (Integer) valueAnimator.getAnimatedValue( "x" );
				int y = (Integer) valueAnimator.getAnimatedValue( "y" );
				View mview = view.findViewById( R.id.image_frame1 );
				mview.getLayoutParams().width = x;
				mview.getLayoutParams().height = y;
				view.requestLayout();
			}
		} );

		anim.setDuration( ANIMATION_DURATION );
		anim.start();
	}

	AnimatorSet setGrow, setShrink;

	@Override
	public void onRowFocusChanged( boolean hasFocus ) {
		View view = getChildAt( mSelectedIndex );
		int startWidth = view.getWidth();
		int startHeight = view.getHeight();
		int endWidth;
		int endHeight;
		if (hasFocus) {
			endWidth = thumbWidthLarge;
			endHeight = thumbHeightLarge;
		} else {
			endWidth = thumbWidthSmall;
			endHeight = thumbHeightSmall;
		}

		for (int i = 0; i < getChildCount(); i++) {
			final View cview = getChildAt( i );
			View vic = cview.findViewById( R.id.videoinfoContainer );
			if (hasFocus) {
				vic.setVisibility( View.VISIBLE );
			} else {
				vic.setVisibility( View.GONE );
			}
		}

		AnimatorSet set = new AnimatorSet();
		ArrayList<Animator> anims = new ArrayList<Animator>();

		for (int i = 0; i < getChildCount(); i++) {
			final View cview = getChildAt( i );

			ValueAnimator anim = new ValueAnimator();
			PropertyValuesHolder pvhX, pvhY;
			pvhX = PropertyValuesHolder.ofInt( "x", startWidth, endWidth );
			pvhY = PropertyValuesHolder.ofInt( "y", startHeight, endHeight );
			anim.setInterpolator( new DecelerateInterpolator( 1.2f ) );
			anim.setValues( pvhX, pvhY );

			anim.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
				View childView = cview;
				@Override
				public void onAnimationUpdate( ValueAnimator valueAnimator ) {
					int x = (Integer) valueAnimator.getAnimatedValue( "x" );
					int y = (Integer) valueAnimator.getAnimatedValue( "y" );
					View mview = childView.findViewById( R.id.image_frame1 );
					mview.getLayoutParams().width = x;
					mview.getLayoutParams().height = y;
					childView.requestLayout();
				}
			} );
			anims.add( anim );
		}

		set.setInterpolator( new DecelerateInterpolator( 1.2f ) );
		set.setDuration( ANIMATION_DURATION );
		set.playTogether( anims );

		set.start();

		int distance = (startWidth - endWidth) * mSelectedIndex;

		ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
		distance -= (mlp.leftMargin + mlp.rightMargin) * mSelectedIndex;

		ValueAnimator va = ValueAnimator.ofInt( 0, distance );
		va.setDuration( ANIMATION_DURATION );
		va.setInterpolator( new DecelerateInterpolator( 1.2f ) );
		va.addUpdateListener( new AnimatorUpdateListener() {
			int lastValue = 0;

			@Override
			public void onAnimationUpdate( ValueAnimator animation ) {
				int amount = lastValue - (Integer) animation.getAnimatedValue();
				scrollBy( amount, 0 );
				lastValue = (Integer) animation.getAnimatedValue();
			}
		} );

		va.start();
	}
}