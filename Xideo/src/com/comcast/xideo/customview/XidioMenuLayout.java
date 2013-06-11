package com.comcast.xideo.customview;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.comcast.ui.libv1.widget.MenuLayoutEx;
import com.comcast.ui.libv1.widget.MenuLayoutEx.OnMenuExListener;
import com.comcast.xideo.R;
import com.comcast.xideo.SearchTextEvent;
import com.comcast.xideo.XideoApplication;

public class XidioMenuLayout extends MenuLayoutEx implements OnMenuExListener {
	public final static int MENU_ID_SEARCH = 100;
	public final static int MENU_ID_HOME = 101;
	public final static int MENU_ID_SUBSCRIPTIONS = 102;
	public final static int MENU_ID_SETTINGS = 103;

	private String searchText;
	private int searchTextWidth;

	OnMenuExListener listener;

	public XidioMenuLayout(Context context) {
		this( context, null );
	}

	public XidioMenuLayout(Context context, AttributeSet attrs) {
		this( context, attrs, 0 );
	}

	public XidioMenuLayout(Context context, AttributeSet attrs, int defStyle) {
		super( context, attrs, defStyle );
		init();
	}

	public void init() {
		super.setMenuListener( this );
		createMenuItems();
	}

	public void setListener( OnMenuExListener listener ) {
		this.listener = listener;
	}

	@Override
	public void setMenuListener( OnMenuExListener listener ) {
		super.setMenuListener( this );
	}

	void createMenuItems() {
		MenuItemEx m1 = new MenuItemEx();
		m1.id = MENU_ID_SEARCH;
		m1.viewId = R.layout.menu_item_search;

		MenuItemEx m2 = new MenuItemEx();
		m2.id = MENU_ID_HOME;
		m2.viewId = R.layout.menu_item_home;

		MenuItemEx m3 = new MenuItemEx();
		m3.id = MENU_ID_SUBSCRIPTIONS;
		m3.viewId = R.layout.menu_item_subscriptions;

		MenuItemEx m4 = new MenuItemEx();
		m4.id = MENU_ID_SETTINGS;
		m4.viewId = R.layout.menu_item_settings;

		setBackgroundFocusIndicatorLayoutId( R.layout.menu_focus_background_indicator, R.id.image1 );
		setBackgroundFocusIndicatorResId( R.drawable.menu_background_onfocus, true );
		setBackgroundFocusIndicatorResId( R.drawable.menu_background_onselection, false );
		addMenuItem( m1 );
		addMenuItem( m2 );
		addMenuItem( m3 );
		addMenuItem( m4 );
	}

	@Override
	public void onMenuItemSelected( MenuItemEx menuItem, final View mView, int index ) {
		if (listener != null) {
			listener.onMenuItemSelected( menuItem, mView, index );
		}
	}

	@Override
	public void onMenuItemAdded( MenuItemEx menuItem, View mView, int index ) {
		View view = null;
		view = mView.findViewById( R.id.text1 );
		if (view != null) {
			Typeface mFace = Typeface.createFromAsset( getContext().getAssets(), "fonts/Flama-Thin.otf" );
			TextView tv = (TextView) view;
			tv.setTypeface( mFace );
		}
	}

	@Override
	public void onMenuItemChanged( MenuItemEx menuItem, View mView, int index, final boolean hasFocus ) {
		int menuImageResId = 0;

		if (!hasFocus) {
			switch (menuItem.id) {
				case MENU_ID_SEARCH:
					menuImageResId = R.drawable.ic_search_menu;
					break;
				case MENU_ID_HOME:
					menuImageResId = R.drawable.ic_home_menu;
					break;
				case MENU_ID_SUBSCRIPTIONS:
					menuImageResId = R.drawable.ic_subscritions_menu;
					break;
				case MENU_ID_SETTINGS:
					menuImageResId = R.drawable.ic_settings_menu;
					break;
			}
		} else {
			switch (menuItem.id) {
				case MENU_ID_SEARCH:
					menuImageResId = R.drawable.ic_search_menu_k;
					break;
				case MENU_ID_HOME:
					menuImageResId = R.drawable.ic_home_menu_k;
					break;
				case MENU_ID_SUBSCRIPTIONS:
					menuImageResId = R.drawable.ic_subscritions_menu_k;
					break;
				case MENU_ID_SETTINGS:
					menuImageResId = R.drawable.ic_settings_menu_k;
					break;
			}
		}

		ImageView imview1 = (ImageView) mView.findViewById( R.id.image2 );
		if (imview1 != null) {
			imview1.setImageResource( menuImageResId );
		}


		if (listener != null) {
			listener.onMenuItemChanged( menuItem, mView, index, hasFocus );
		}

		final View view = mView.findViewById( R.id.text1 );
		int startWidth, endWidth;
		float startAlpha, endAlpha;

		int widthMeasureSpec = MeasureSpec.makeMeasureSpec( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
		view.measure( widthMeasureSpec, widthMeasureSpec );
		startAlpha = 0f;
		endAlpha = 1.0f;
		if (hasFocus) {
			view.setAlpha( 0f );
			startWidth = 0;
			endWidth = view.getMeasuredWidth();
			view.getLayoutParams().width = 0;

		} else {
			startWidth = view.getMeasuredWidth();
			endWidth = 0;
			startAlpha = 1.0f;
			endAlpha = 0f;
		}

		ValueAnimator anim = new ValueAnimator();
		PropertyValuesHolder pvhX, pvhY;
		pvhX = PropertyValuesHolder.ofInt( "x", startWidth, endWidth );
		pvhY = PropertyValuesHolder.ofFloat( "y", startAlpha, endAlpha );
		anim.setInterpolator( new AccelerateInterpolator() );
		anim.setValues( pvhX, pvhY );

		view.setVisibility( View.VISIBLE );
		anim.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate( ValueAnimator valueAnimator ) {
				int x = (Integer) valueAnimator.getAnimatedValue( "x" );
				float y = (Float) valueAnimator.getAnimatedValue( "y" );
				view.getLayoutParams().width = x;
				view.setAlpha( y );
				view.requestLayout();
			}

		} );

		anim.setDuration( ANIMATION_DURATION_SLIDE_LEFT_RIGHT );
		anim.start();
	}

	@Override
	public void onMenuItemFocusChanged( MenuItemEx menuItem, View mView, int index, boolean hasFocus ) {
		int menuImageResId = 0;
		if (!hasFocus) {
			switch (menuItem.id) {
				case MENU_ID_SEARCH:
					menuImageResId = R.drawable.ic_search_menu;
					break;
				case MENU_ID_HOME:
					menuImageResId = R.drawable.ic_home_menu;
					break;
				case MENU_ID_SUBSCRIPTIONS:
					menuImageResId = R.drawable.ic_subscritions_menu;
					break;
				case MENU_ID_SETTINGS:
					menuImageResId = R.drawable.ic_settings_menu;
					break;
			}
		} else {
			switch (menuItem.id) {
				case MENU_ID_SEARCH:
					menuImageResId = R.drawable.ic_search_menu_k;
					break;
				case MENU_ID_HOME:
					menuImageResId = R.drawable.ic_home_menu_k;
					break;
				case MENU_ID_SUBSCRIPTIONS:
					menuImageResId = R.drawable.ic_subscritions_menu_k;
					break;
				case MENU_ID_SETTINGS:
					menuImageResId = R.drawable.ic_settings_menu_k;
					break;
			}
		}

		ImageView imview1 = (ImageView) mView.findViewById( R.id.image2 );
		if (imview1 != null) {
			imview1.setImageResource( menuImageResId );
		}

		if (listener != null) {
			listener.onMenuItemFocusChanged( menuItem, mView, index, hasFocus );
		}

	}

	@Override
	protected int getScrollDistanceLeft() {
		int distance = super.getScrollDistanceLeft();
		View view = getChildAt( mPreviousSelectedIndex + menuItemStartPos );
		ViewGroup vg = (ViewGroup) view;
		View cv = vg.getChildAt( 1 );
		distance -= cv.getMeasuredWidth();
		return distance;
	}

	public boolean onUserKeyPressedEvent( KeyEvent event ) {
		boolean handled = false;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			int keyCode = event.getKeyCode();
			if (keyCode == KeyEvent.KEYCODE_DPAD_UP
					||
					keyCode == KeyEvent.KEYCODE_DPAD_DOWN
					||
					keyCode == KeyEvent.KEYCODE_DPAD_LEFT
					||
					keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				return false;
			}

			View view = this.findViewById( MENU_ID_SEARCH );
			if (view != null) {
				TextView tv = (TextView) view.findViewById( R.id.text1 );
				if (tv != null) {
					String text = "";
					if (searchText == null) {
						searchText = tv.getText().toString();
						searchTextWidth = tv.getMeasuredWidth();
						tv.getLayoutParams().width = searchTextWidth;
					} else {
						text = tv.getText().toString();
					}

					if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
						if (text.length() > 0) {
							text = text.substring( 0, text.length() - 1 );
						}

						if (text.length() == 0) {
							text = searchText;
							int widthMeasureSpec = MeasureSpec.makeMeasureSpec( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
							view.measure( widthMeasureSpec, widthMeasureSpec );
							view.getLayoutParams().width = view.getMeasuredWidth();
							view.requestLayout();
							searchText = null;
							XideoApplication.getBus().post( new SearchTextEvent( this, "" ) );
						} else {
							XideoApplication.getBus().post( new SearchTextEvent( this, text ) );
						}
						tv.setText( text );
						handled = true;
					} else {
						if ((keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)
								||
								(keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z)
								||
								(keyCode == KeyEvent.KEYCODE_SPACE)
							){
							char pressedKey = (char) event.getUnicodeChar();
							text += pressedKey;
							handled = true;
							tv.setText( text );
							XideoApplication.getBus().post( new SearchTextEvent( this, text ) );
						}
					}

				}
			}
		}
		return handled;
	}

	@Override
	public boolean dispatchKeyEvent( KeyEvent event ) {
		if (onUserKeyPressedEvent( event ) == false)
			return super.dispatchKeyEvent( event );

		return true;
	}

	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event ) {
		if (onUserKeyPressedEvent( event ) == false)
			return super.onKeyDown( keyCode, event );
		return true;
	}
}
