package com.comcast.ui.libv1.widget;

import java.util.ArrayList;
import java.util.List;

import com.comcast.ui.libv1.R;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.View.*;
import android.view.ViewGroup;
import android.view.animation.*;
import android.widget.ImageView;

public class MenuLayoutEx extends ViewGroup implements OnKeyListener,
		OnFocusChangeListener{

	public static final int ANIMATION_DURATION_SLIDE_LEFT_RIGHT = 450;
	public static final int ANIMATION_DURATION_LAYOUT_TRANSITION = 750;
	public static final int DEFAULT_MENU_PINNED_X_POSITION = 200; // in px

	public static class MenuItemEx {
		public int id;
		public Class<?> activityClass;
		public int viewId;
	}

	public interface OnMenuExListener {
		void onMenuItemAdded(MenuItemEx menuItem, View view, int index);
		void onMenuItemSelected(MenuItemEx menuItem, View view, int index);
		void onMenuItemChanged(MenuItemEx menuItem, View view, int index, boolean hasFocus);
		void onMenuItemFocusChanged(MenuItemEx menuItem, View view, int index, boolean hasFocus);
	}

	ArrayList<MenuItemEx> menuItems = new ArrayList<MenuItemEx>();
	View bkgFocusIndicator;
	int bkgImageItemId;
	
	int bkgFocusIndicatorLayoutId;

	int bkgFocusIndicatorResIdOnFocus;
	int bkgFocusIndicatorResIdOnNonFocus;
	
	protected int mPreviousSelectedIndex = 0;
	protected int mSelectedIndex = 0;
	protected int mPinnedXPosition = DEFAULT_MENU_PINNED_X_POSITION;

	OnMenuExListener menuListener;

	public MenuLayoutEx(Context context) {
		this(context, null);
	}

	public MenuLayoutEx(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MenuLayoutEx(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuLayoutEx, defStyle, 0);

		mPinnedXPosition = (int) a.getDimension(R.styleable.MenuLayoutEx_pinnedXPosition, DEFAULT_MENU_PINNED_X_POSITION);
		a.recycle();
		init();
	}

	public void setMenuListener(OnMenuExListener listener) {
		this.menuListener = listener;
	}

	public void setBackgroundFocusIndicatorLayoutId(int id, int itemId) {
		bkgFocusIndicatorLayoutId = id;
		bkgImageItemId = itemId;
	}

	public void setBackgroundFocusIndicatorResId(int id, boolean onFocus) {
		if(onFocus)
			bkgFocusIndicatorResIdOnFocus = id;
		else
			bkgFocusIndicatorResIdOnNonFocus = id;
	}
	
	private void init() {
//		ViewTreeObserver vto = getViewTreeObserver();
//		vto.addOnGlobalLayoutListener(this);
		setFocusable(true);
		setOnKeyListener(this);
		setOnFocusChangeListener(this);
		setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		int maxHeight = 0;
		int maxWidth = 0;
		int totalWidth = 0;

		int specModeW = MeasureSpec.getMode(widthMeasureSpec);
		int specSizeW = MeasureSpec.getSize(widthMeasureSpec);

		int specModeH = MeasureSpec.getMode(heightMeasureSpec);
		int specSizeH = MeasureSpec.getSize(heightMeasureSpec);

		maxWidth = specSizeW;
		maxHeight = specSizeH;

		int count = getChildCount();

		MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();

		int maxChildHeight = 0;
		
		for (int i = 0; i < count; i++) {
			View vw = getChildAt(i);
			final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) vw.getLayoutParams();
			int vHeight = vw.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;
			int vWidth = vw.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
			if(menuItemStartPos == 0 || (menuItemStartPos == 1 && i > 0)) {
				totalWidth += vWidth;
			}
			maxChildHeight = Math.max(maxChildHeight, vHeight);
		}

		totalWidth += mlp.leftMargin + mlp.rightMargin;
		maxChildHeight += (mlp.topMargin + mlp.bottomMargin);

		if (specModeW == MeasureSpec.UNSPECIFIED) {
			maxWidth = totalWidth;
		} else if (specModeW == MeasureSpec.AT_MOST) {
			maxWidth = Math.min(maxWidth, totalWidth);
		}

		if (specModeH == MeasureSpec.UNSPECIFIED) {
			maxHeight = maxChildHeight;
		} else if (specModeH == MeasureSpec.AT_MOST) {
			maxHeight = Math.min(maxHeight, maxChildHeight);
		}
		setMeasuredDimension(maxWidth, maxHeight);
	}

	protected int menuItemStartPos = 0;
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();
		int childTop = 0;
		int childLeft = mPinnedXPosition;
		int focusIndicatorWidth = 0;
		if(bkgFocusIndicator != null) {
			View view = getChildAt(0);
			focusIndicatorWidth = view.getMeasuredWidth();
			int vHeight = view.getMeasuredHeight();
			childTop = (getHeight() - vHeight)/2;
			childLeft -= (focusIndicatorWidth/2);
			view.layout(childLeft, childTop, childLeft + focusIndicatorWidth, childTop + vHeight);
		}
		
		childLeft = mPinnedXPosition - focusIndicatorWidth/2;
		for (int i = menuItemStartPos; i < count; i++) {
			View view = getChildAt(i);
			MarginLayoutParams mlp = (MarginLayoutParams) view.getLayoutParams();
			int vWidth = view.getMeasuredWidth() + mlp.rightMargin ;
			int vHeight = view.getMeasuredHeight();
			childTop = (getHeight() - vHeight)/2;
			childLeft += mlp.leftMargin;
			view.layout(childLeft, childTop, childLeft + vWidth, childTop + vHeight);
			childLeft += vWidth;
		}
	}
	
	public void addMenuItem(MenuItemEx menuItem) {
		menuItems.add(menuItem);
		addMenu(menuItem, menuItems.size()-1);
	}
	
	public void clearAll() {
		menuItems.clear();
		removeAllViews();
	}

	public void addMenuItems(final List<MenuItemEx> items) {
		menuItems.addAll(items);
		createChildViews();
	}

	private void createChildViews() {
		removeAllViews();
		for (int i = 0; i < menuItems.size(); i++) {
			MenuItemEx menuItem = menuItems.get(i);
			addMenu(menuItem, i);
		}
	}

	private void addMenu(MenuItemEx menuItem, int index) {
		LayoutInflater inflater;
		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(menuItem.viewId, this, false);
		LayoutTransition lt = ((ViewGroup) view).getLayoutTransition();
		if (lt != null) {
			lt.setDuration(LayoutTransition.APPEARING,
					ANIMATION_DURATION_LAYOUT_TRANSITION);
			lt.setDuration(LayoutTransition.CHANGE_DISAPPEARING, 10);
			lt.setDuration(LayoutTransition.CHANGE_APPEARING, 10);
			lt.setStagger(LayoutTransition.APPEARING, 10);
			lt.setStagger(LayoutTransition.CHANGE_APPEARING, 1);
		}

		// insert background focus indicator first
		if(bkgFocusIndicatorResIdOnFocus != 0 && bkgFocusIndicator == null) {
			bkgFocusIndicator = inflate(getContext(), bkgFocusIndicatorLayoutId, null);
			addView(bkgFocusIndicator);
			menuItemStartPos = 1;
		}

		view.setFocusable(false);
		view.setId( menuItem.id );
		addView(view);
		inflater = null;
		notifyMenuItemAdded(menuItem, view, index);
		if(index == 0) {
			ImageView iv = (ImageView) bkgFocusIndicator.findViewById(bkgImageItemId);
			iv.setImageResource(bkgFocusIndicatorResIdOnFocus);
		}
	}
	
	@Override
	public MarginLayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

	@Override
	protected MarginLayoutParams generateDefaultLayoutParams() {
		return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	
    @Override
    protected MarginLayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }

	int mScrollDistance = 0;

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		boolean hasKeyConsumed = false;
		if (event.getAction() == KeyEvent.ACTION_DOWN && isFocused()) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if (mSelectedIndex > 0) {
					mPreviousSelectedIndex = mSelectedIndex;
					mSelectedIndex--;
					slideRight();
					notifyMenuItemNavigated(mPreviousSelectedIndex, false);
					notifyMenuItemNavigated(mSelectedIndex, true);
					notifyMenuItemFocusChanged(mPreviousSelectedIndex, false);
					notifyMenuItemFocusChanged(mSelectedIndex, true);
				}
				hasKeyConsumed = true;
				break;

			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if (mSelectedIndex < menuItems.size() - 1) {
					mPreviousSelectedIndex = mSelectedIndex;
					mSelectedIndex++;
					slideLeft();
					notifyMenuItemNavigated(mPreviousSelectedIndex, false);
					notifyMenuItemNavigated(mSelectedIndex, true);
					notifyMenuItemFocusChanged(mPreviousSelectedIndex, false);
					notifyMenuItemFocusChanged(mSelectedIndex, true);
				}
				hasKeyConsumed = true;
				break;

			case KeyEvent.KEYCODE_BUTTON_SELECT:
			case KeyEvent.KEYCODE_DPAD_CENTER:
				Log.e(MenuLayoutEx.class.getName(), "Menu Item selected "
						+ mSelectedIndex);
				notifyMenuItemSelected(mSelectedIndex);
				break;
			}
		}
		return hasKeyConsumed;
	}

	private void notifyMenuItemAdded(MenuItemEx menuItem, View view, int index) {
		if (menuListener != null) {
			menuListener.onMenuItemAdded(menuItem, view, index);
		}
	}

	private void notifyMenuItemNavigated(int index, boolean hasFocus) {
		if (menuListener != null) {
			final MenuItemEx menuItem = menuItems.get(index);
			View view = getChildAt(index + menuItemStartPos);
			menuListener.onMenuItemChanged(menuItem, view, index, hasFocus);
		}
	}

	private void notifyMenuItemFocusChanged(int index, boolean hasFocus) {
		if (menuListener != null) {
			final MenuItemEx menuItem = menuItems.get(index);
			View view = getChildAt(index + menuItemStartPos);
			menuListener.onMenuItemFocusChanged(menuItem, view, index, hasFocus);
		}
	}

	private void notifyMenuItemSelected(int index) {
		if (menuListener != null) {
			final MenuItemEx menuItem = menuItems.get(index);
			View view = getChildAt(index + menuItemStartPos);
			menuListener.onMenuItemSelected(menuItem, view, index);
		}
	}

	ViewTreeObserver vto;
	OnGlobalLayoutListener gll;

	@SuppressLint("NewApi")
	private void slideLeft() {
		if(animSet != null) {
			animSet.cancel();
		}

		vto = this.getViewTreeObserver();
		if(vto != null && vto.isAlive() && gll != null) {
			if (Build.VERSION.SDK_INT >= 16) {
				vto.removeOnGlobalLayoutListener(gll);
			}else {
				vto.removeGlobalOnLayoutListener( gll );
			}
		}

		gll = new OnGlobalLayoutListener() {
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				if (Build.VERSION.SDK_INT >= 16) {
					vto.removeOnGlobalLayoutListener(gll);
				}else {
					vto.removeGlobalOnLayoutListener( gll );
				}
				int distance = getScrollDistanceLeft();
				slideLeftInternal(distance);
			}
		};
		if(vto != null && vto.isAlive()) {
			vto.addOnGlobalLayoutListener(gll);
		}
	}

	protected int getScrollDistanceLeft() {
		View view = getChildAt(mSelectedIndex + menuItemStartPos);
		ViewGroup vg = (ViewGroup) view;
		View cv = vg.getChildAt(0);
		int centerX = cv.getMeasuredWidth()/2;
		int pCx = (int) (view.getLeft() + centerX + view.getTranslationX());

		int distance = Math.abs(pCx - mPinnedXPosition);
		return distance;
	}

	private void slideLeftInternal(int distance) {
		slide(-1, distance);
	}

	@SuppressLint("NewApi")
	private void slideRight() {
		if(animSet != null) {
			animSet.cancel();
		}

		vto = this.getViewTreeObserver();
		if(vto != null && vto.isAlive() && gll != null) {
			if (Build.VERSION.SDK_INT >= 16) {
				vto.removeOnGlobalLayoutListener(gll);
			}else {
				vto.removeGlobalOnLayoutListener( gll );
			}
		}

		gll = new OnGlobalLayoutListener() {
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				if (Build.VERSION.SDK_INT >= 16) {
					vto.removeOnGlobalLayoutListener(gll);
				}else {
					vto.removeGlobalOnLayoutListener( gll );
				}

				int distance = getScrollDistanceRight();
				slideRightInternal(distance);
			}
		};

		if(vto != null && vto.isAlive()) {
			vto.addOnGlobalLayoutListener(gll);
		}
	}

	protected int getScrollDistanceRight() {
		View view = getChildAt(mSelectedIndex + menuItemStartPos);
		ViewGroup vg = (ViewGroup) view;
		View cv = vg.getChildAt(0);
		int centerX = cv.getMeasuredWidth()/2;
		int pCx = (int) (view.getLeft() + centerX + view.getTranslationX());
		int distance = Math.abs(pCx - mPinnedXPosition);
		return distance;
	}

	protected void slideRightInternal(int distance) {
		slide(1, distance);
	}

	AnimatorSet animSet;

	protected void slide(int dir, int distance) {
		ArrayList<Animator> set = new ArrayList<Animator>();
		mScrollDistance = (distance * dir);
		
		int count = getChildCount();
		for (int i = menuItemStartPos; i < count; i++) {
			final View child = getChildAt(i);
			int mx = (int) child.getTranslationX();
			ValueAnimator anim = ObjectAnimator.ofFloat(child, "translationX",
					mx, mx + mScrollDistance);
			anim.setDuration(ANIMATION_DURATION_SLIDE_LEFT_RIGHT);
			anim.setInterpolator(new DecelerateInterpolator(1.2f));
			set.add(anim);
		}

		animSet = new AnimatorSet();
		animSet.playTogether(set);
		animSet.start();
	}

	@Override
	public void onFocusChange(View arg0, boolean focussed) {
		ImageView iv = (ImageView) bkgFocusIndicator.findViewById(bkgImageItemId);
		if(focussed) {
			iv.setImageResource(bkgFocusIndicatorResIdOnFocus);
		}else {
			iv.setImageResource(bkgFocusIndicatorResIdOnNonFocus);
		}

		notifyMenuItemFocusChanged(mSelectedIndex, focussed);
	}
}
