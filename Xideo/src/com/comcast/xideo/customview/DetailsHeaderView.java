package com.comcast.xideo.customview;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.IBindableView;
import gueei.binding.ViewAttribute;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comcast.xideo.ItemSelectedEvent;
import com.comcast.xideo.R;
import com.comcast.xideo.XideoApplication;
import com.comcast.xideo.viewmodel.DetailsHeaderViewModel;

public class DetailsHeaderView extends LinearLayout implements
		IBindableView<DetailsHeaderView>, OnClickListener {

	public enum ActionName {
		BACK,
		ICON,
		EXTRA_1,
		EXTRA_2,
	}

	public interface OnActionListener {
		public void onActionSelected( ActionName action );
	}

	public static final int ANIMATION_DURATION = 300;
	public static final int ANIMATION_DURATION_BUTTON = 400;

	int imageWidthOnFocus, imageHeightOnFocus;
	int imageWidthOnNonFocus, imageHeightOnNonFocus;

	View detailsContainer, imageThumbContainer;
	TextView text1, text2, text3;
	View button1Layout, button2Layout, button3Layout;
	ViewGroup labelsContainer;

	ImageButton button1, button2, button3;
	TextView buttonExtra2Text_1, buttonExtra3Text_1, buttonExtra3Text_2;
	View lastFocusedView = null;
	Binder.InflateResult inflateResult;
	Object source;
	OnActionListener listener;

	public DetailsHeaderView(Context context) {
		this( context, null );
	}

	public DetailsHeaderView(Context context, AttributeSet attrs) {
		this( context, attrs, 0 );
	}

	public DetailsHeaderView(Context context, AttributeSet attrs, int defStyle) {
		super( context, attrs, defStyle );
		init();
	}

	private void init() {
		setLayoutParams( new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT ) );
		if (!isInEditMode()) {
			inflateResult = Binder.inflateView( getContext(), R.layout.details_header, this, true );
			ViewGroup view = (ViewGroup) inflateResult.rootView;
			for (View pview : inflateResult.processedViews) {
				AttributeBinder.getInstance()
						.bindView( getContext(), pview, null );
			}

			detailsContainer = view.findViewById( R.id.details_container );
			imageThumbContainer = view.findViewById( R.id.image_thumb_container );
			labelsContainer = (ViewGroup) view.findViewById( R.id.label_container );
			text1 = (TextView) findViewById( R.id.text1 );
			text2 = (TextView) findViewById( R.id.text2 );
			text3 = (TextView) findViewById( R.id.text3 );

			button1Layout = findViewById( R.id.button_layout1 );
			button2Layout = findViewById( R.id.button_layout2 );
			button3Layout = findViewById( R.id.button_layout3 );

			button1 = (ImageButton) button1Layout.findViewById( R.id.image_back );
			button2 = (ImageButton) button2Layout.findViewById( R.id.image_extra1 );
			button3 = (ImageButton) button3Layout.findViewById( R.id.image_extra2 );

			buttonExtra2Text_1 = (TextView) button2Layout.findViewById( R.id.button_layout2_text1 );
			buttonExtra3Text_1 = (TextView) button2Layout.findViewById( R.id.button_layout3_text1 );
			buttonExtra3Text_2 = (TextView) button2Layout.findViewById( R.id.button_layout3_text2 );

			ArrayList<View> focusableViews = getFocusables( View.FOCUS_RIGHT );
			if (focusableViews != null) {
				for (View childView : focusableViews) {
					childView.setOnFocusChangeListener( new OnFocusChangeListener() {

						@Override
						public void onFocusChange( View v, boolean hasFocus ) {
							setIconState( v, hasFocus );
							if (hasFocus) {
								lastFocusedView = v;
								handleFocusChange( hasFocus );
							}
						}
					} );
				}
			}

			button1.setOnClickListener( this );
			button2.setOnClickListener( this );
			button3.setOnClickListener( this );
			imageThumbContainer.setOnClickListener( this );
		}
	}

	private void setIconState( View v, boolean hasFocus ) {
		int resId = 0;
		if (hasFocus) {
			switch (v.getId()) {
				case R.id.image_back:
					resId = R.drawable.ic_details_back_k;
					break;

				case R.id.image_thumb_container:
					resId = R.drawable.ic_details_down_k;
					break;

				case R.id.image_extra1:
					resId = R.drawable.ic_details_preview_k;
					break;

				case R.id.image_extra2:
					resId = R.drawable.ic_details_subs_k;
					break;
			}
		} else {
			switch (v.getId()) {
				case R.id.image_back:
					resId = R.drawable.ic_details_back;
					break;

				case R.id.image_thumb_container:
					resId = R.drawable.ic_details_down;
					break;

				case R.id.image_extra1:
					resId = R.drawable.ic_details_preview;
					break;

				case R.id.image_extra2:
					resId = R.drawable.ic_details_subs;
					break;
			}
		}

		if (v.getId() == R.id.image_thumb_container) {
			ImageView imgdown = (ImageView) findViewById( R.id.image_down );
			imgdown.setImageResource( resId );
		}else {
			if (v instanceof ImageButton) {
				ImageButton iv = (ImageButton) v;
				iv.setImageResource( resId );
			}
		}
	}

	public void setActionListener( OnActionListener listener ) {
		this.listener = listener;
	}

	private ViewAttribute<DetailsHeaderView, Object> DataSourceAttribute = new ViewAttribute<DetailsHeaderView, Object>(
			Object.class, DetailsHeaderView.this, "DataSource" ) {
		@Override
		protected void doSetAttributeValue( Object newValue ) {
			if(newValue == null)
				return;
			if (!(newValue instanceof DetailsHeaderViewModel)) {
				return;
			}
			source = newValue;
			Binder.bindView( DetailsHeaderView.this.getContext(), inflateResult, newValue );
		}

		@Override
		public Object get() {
			return source;
		}
	};

	public void setImageWidthHeight( int w, int h, boolean focusedState ) {
		if (focusedState == true) {
			imageWidthOnFocus = w;
			imageHeightOnFocus = h;
		} else {
			imageWidthOnNonFocus = w;
			imageHeightOnNonFocus = h;
		}
	}

	public void setButtonExtra2Visibility( int visibility ) {
		button2Layout.setVisibility( visibility );
	}

	public void setButtonExtra3Visibility( int visibility ) {
		button3Layout.setVisibility( visibility );
	}

	public View getButtonback() {
		return button1;
	}

	public View getButtonExtra2() {
		return button2;
	}

	public View getButtonExtra3() {
		return button3;
	}

	public void handleFocusChange( boolean hasFocus ) {
		animateLayout( hasFocus );
		ImageView imgdown = (ImageView) findViewById( R.id.image_down );
		if (hasFocus) {
			text1.setVisibility( View.VISIBLE );
			text3.setVisibility( View.VISIBLE );
			imgdown.setVisibility( View.VISIBLE );
			labelsContainer.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
			animateLayoutIn( button2Layout, ANIMATION_DURATION_BUTTON );
			animateLayoutIn( button3Layout, ANIMATION_DURATION_BUTTON );
		} else {
			text1.setVisibility( View.GONE );
			text3.setVisibility( View.GONE );
			imgdown.setVisibility( View.GONE );
			labelsContainer.getLayoutParams().height = LayoutParams.MATCH_PARENT;
			animateLayoutOut( button2Layout, ANIMATION_DURATION_BUTTON );
			animateLayoutOut( button3Layout, ANIMATION_DURATION_BUTTON );
		}
		labelsContainer.requestLayout();
	}

	private void animateLayoutOut( View target, int animationDuration ) {
		ObjectAnimator oa1 = ObjectAnimator.ofFloat( target, "scaleX", 3.0f );
		ObjectAnimator oa2 = ObjectAnimator.ofFloat( target, "scaleY", 3.0f );
		ObjectAnimator oa3 = ObjectAnimator.ofFloat( target, "alpha", 0.0f );

		AnimatorSet set = new AnimatorSet();

		set.playTogether( oa1, oa2, oa3 );
		set.setDuration( animationDuration );
		set.setInterpolator( new DecelerateInterpolator( 1.2f ) );
		set.start();
	}

	private void animateLayoutIn( View target, int animationDuration ) {
		ObjectAnimator oa1 = ObjectAnimator.ofFloat( target, "scaleX", 1.0f );
		ObjectAnimator oa2 = ObjectAnimator.ofFloat( target, "scaleY", 1.0f );
		ObjectAnimator oa3 = ObjectAnimator.ofFloat( target, "alpha", 1.0f );

		AnimatorSet set = new AnimatorSet();

		set.playTogether( oa1, oa2, oa3 );
		set.setDuration( animationDuration );
		set.setInterpolator( new DecelerateInterpolator( 1.2f ) );
		set.start();
	}

	private void animateLayout( final boolean hasFocus ) {
		final View imageThumb = detailsContainer.findViewById( R.id.image_thumb );
		int startWidth = imageThumb.getWidth();
		int startHeight = imageThumb.getHeight();
		int endWidth;
		int endHeight;
		if (hasFocus) {
			endWidth = imageWidthOnFocus;
			endHeight = imageHeightOnFocus;
		} else {
			endWidth = imageWidthOnNonFocus;
			endHeight = imageHeightOnNonFocus;
		}

		if (startWidth == endWidth || startHeight == endHeight) {
			return;
		}

		ValueAnimator anim = new ValueAnimator();
		PropertyValuesHolder pvhX, pvhY;
		pvhX = PropertyValuesHolder.ofInt( "x", startWidth, endWidth );
		pvhY = PropertyValuesHolder.ofInt( "y", startHeight, endHeight );
		anim.setInterpolator( new DecelerateInterpolator( 1.2f ) );
		anim.setValues( pvhX, pvhY );

		anim.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate( ValueAnimator valueAnimator ) {
				int x = (Integer) valueAnimator.getAnimatedValue( "x" );
				int y = (Integer) valueAnimator.getAnimatedValue( "y" );
				// detailsContainer.getLayoutParams().height = y;

				imageThumb.getLayoutParams().width = x;
				imageThumb.getLayoutParams().height = y;
				detailsContainer.requestLayout();
			}
		} );

		anim.addListener( new AnimatorListener() {

			@Override
			public void onAnimationCancel( Animator animation ) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd( Animator animation ) {
				if (hasFocus) {
					View currentFocused = findFocus();
					if (currentFocused == imageThumbContainer) {
						XideoApplication.getBus().post( new ItemSelectedEvent( DetailsHeaderView.this, imageThumb ) );
					}
				}
			}

			@Override
			public void onAnimationRepeat( Animator animation ) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart( Animator animation ) {
				// TODO Auto-generated method stub

			}
		} );

		anim.setDuration( ANIMATION_DURATION );
		anim.start();

	}

	public void setHeaderText( String text ) {
		text1.setText( text );
	}

	public void setSubHeaderText( String text ) {
		text2.setText( text );
	}

	public void setHeaderDescriptionText( String text ) {
		text3.setText( text );
	}

	public void setButtonExtra2Text_1( String text ) {
		buttonExtra2Text_1.setText( text );
	}

	public void setButtonExtra3Text_1( String text ) {
		buttonExtra3Text_1.setText( text );
	}

	public void setButtonExtra3Text_2( String text ) {
		buttonExtra3Text_2.setText( text );
	}

	@Override
	public boolean dispatchKeyEvent( KeyEvent event ) {
		return super.dispatchKeyEvent( event ) || executeKeyEvent( event );
	}

	private boolean executeKeyEvent(KeyEvent event) {
		boolean handled = false;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch(event.getKeyCode()) {
				case KeyEvent.KEYCODE_DPAD_DOWN:
					if (!hasFocusableChildView( View.FOCUS_DOWN )) {
						setIconState( lastFocusedView, false );
						handleFocusChange( false );
					}
					break;
				case KeyEvent.KEYCODE_DPAD_UP:
					if (!hasFocusableChildView( View.FOCUS_UP )) {
						setIconState( lastFocusedView, false );
						handleFocusChange( false );
					}
					break;
				case KeyEvent.KEYCODE_DPAD_LEFT:
					if (!hasFocusableChildView( View.FOCUS_LEFT ))
						handleFocusChange( false );
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					if (!hasFocusableChildView( View.FOCUS_RIGHT ))
						handleFocusChange( false );
					break;
			}
		}
		return handled;
	}

	public boolean hasFocusableChildView( int direction ) {
		View currentFocused = findFocus();
		View nextFocused = FocusFinder.getInstance().findNextFocus( this, currentFocused, direction );
		return nextFocused != null;
	}

	@Override
	public ViewAttribute<? extends View, ?> createViewAttribute( String attributeId ) {
		if (attributeId.equals( "dataSource" ))
			return DataSourceAttribute;
		return null;
	}

	@Override
	public void onClick( View v ) {
		ActionName actionName = null;
		if(listener != null) {
			switch (v.getId()) {
				case R.id.image_back:
					actionName = ActionName.BACK;
					break;

				case R.id.image_extra1:
					actionName = ActionName.EXTRA_1;
					break;

				case R.id.image_extra2:
					actionName = ActionName.EXTRA_2;
					break;
				case R.id.image_thumb_container:
					actionName = ActionName.ICON;
					break;
			}

			if (actionName != null) {
				listener.onActionSelected( actionName );
			}
		}
	}
}
