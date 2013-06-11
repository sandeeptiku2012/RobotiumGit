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
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.*;
import android.view.ViewGroup;
import android.view.animation.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuLayout extends ViewGroup implements OnKeyListener,
		OnFocusChangeListener, android.view.ViewTreeObserver.OnGlobalLayoutListener {

	public static final String DEFAULT_MENU_FONT_NAME = "fonts/Flama-Thin.otf";
	public static final int ANIMATION_DURATION_SLIDE_LEFT_RIGHT = 250;
	public static final int ANIMATION_DURATION_LAYOUT_TRANSITION = 750;
	public static final int DEFAULT_MENU_LABEL_FONT_SIZE = 70; // in px
	public static final int DEFAULT_MENU_PINNED_X_POSITION = 200; // in px
	public static final int DEFAULT_MENU_ICON_WIDTH_HEIGHT = 100; // in px

	public static class MenuItem {
		public int id;
		public String itemLabel;
		public Drawable iconDrawable;
		public Class<?> activityClass;
	}

	public interface OnMenuListener {
		void onMenuItemSelected(MenuItem menuItem);

		void onMenuItemChanged(MenuItem menuItem, int index);
	}

	static Typeface mMenuItemFont;

	ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

	int selectedChildIndex = 0;
	int mPinnedXPosition = DEFAULT_MENU_PINNED_X_POSITION;
	int scrollWidth = 0;

	int mIconWidth = DEFAULT_MENU_ICON_WIDTH_HEIGHT;
	String mMenuLabelFontName = DEFAULT_MENU_FONT_NAME;
	int mMenuLabelColor = Color.WHITE;
	int mMenuLabelFontSize = DEFAULT_MENU_LABEL_FONT_SIZE;

	OnMenuListener menuListener;
	IFocusHopperListener listener;

	public MenuLayout(Context context) {
		this(context, null);
	}

	public MenuLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MenuLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuLayout, defStyle, 0);

		mIconWidth = (int) a.getDimension(R.styleable.MenuLayout_menu_icon_width, DEFAULT_MENU_ICON_WIDTH_HEIGHT);
		mMenuLabelFontName = a.getString(R.styleable.MenuLayout_menu_labelFont);
		mMenuLabelColor = (int) a.getColor(R.styleable.MenuLayout_menu_labelColor, Color.WHITE);
		mMenuLabelFontSize = (int) a.getDimension(R.styleable.MenuLayout_menu_labelFontSize, DEFAULT_MENU_LABEL_FONT_SIZE);
		mPinnedXPosition = (int) a.getInteger(R.styleable.MenuLayout_menu_pinnedXPosition, DEFAULT_MENU_PINNED_X_POSITION);
		a.recycle();
		init();
	}

	public void setMenuListener(OnMenuListener listener) {
		this.menuListener = listener;
	}

	public void setBlobListener(IFocusHopperListener listener) {
		this.listener = listener;
	}

	@SuppressLint("InlinedApi")
	private void init() {
		ViewTreeObserver vto = getViewTreeObserver();
		vto.addOnGlobalLayoutListener(this);
		setFocusable(true);
		setOnKeyListener(this);
		setOnFocusChangeListener(this);
		if (!isInEditMode()) {
			try {
				if (mMenuLabelFontName != null) {
					mMenuItemFont = Typeface.createFromAsset(getContext()
							.getAssets(), mMenuLabelFontName);
				} else {
					mMenuItemFont = Typeface.createFromAsset(getContext()
							.getAssets(), DEFAULT_MENU_FONT_NAME);
				}
			} catch (Exception ex) {
				Log.e(MenuLayout.class.getName(),
						"Could not load typeface from asset");
			}
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

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
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();

		int maxChildHeight = 0;
		totalWidth += mlp.leftMargin + mlp.rightMargin;

		for (int i = 0; i < count; i++) {
			View vw = getChildAt(i);
			int vHeight = vw.getMeasuredHeight() + mlp.topMargin
					+ mlp.bottomMargin;
			int vWidth = vw.getMeasuredWidth();
			totalWidth += vWidth;
			maxChildHeight = Math.max(maxChildHeight, vHeight);
			if (i == 0) {
				scrollWidth = vw.findViewById(R.id.imageframe1)
						.getMeasuredWidth();
			}
		}

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

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();

		MarginLayoutParams mlpParent = (MarginLayoutParams) getLayoutParams();
		int childTop = mlpParent.topMargin;

		int childLeft = mPinnedXPosition;
		for (int i = 0; i < count; i++) {
			View view = getChildAt(i);
			MarginLayoutParams mlp = (MarginLayoutParams) view
					.getLayoutParams();
			int vWidth = view.getMeasuredWidth() + mlp.leftMargin;
			int vHeight = view.getMeasuredHeight();// + mlp.bottomMargin;
			view.layout(childLeft, childTop, childLeft + vWidth, vHeight);
			childLeft += vWidth;
		}
	}

	public void addMenuItems(final List<MenuItem> items) {
		menuItems.addAll(items);
		removeAllViews();
		createMenuItemChildViews();
		toggleMenuItemSelection(selectedChildIndex, true);
	}

	private void createMenuItemChildViews() {
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT);

		LayoutInflater inflater;
		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		for (int i = 0; i < menuItems.size(); i++) {
			View view = inflater.inflate(R.layout.menu_item, null);
			TextView tv = (TextView) view.findViewById(R.id.text1);

			MenuItem menuItem = menuItems.get(i);
			tv.setText(menuItem.itemLabel);
			tv.setTypeface(mMenuItemFont);
			tv.setTextColor(mMenuLabelColor);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mMenuLabelFontSize);

			ViewGroup vg = (ViewGroup) view.findViewById(R.id.imageframe1);
			LinearLayout.LayoutParams llp = (android.widget.LinearLayout.LayoutParams) vg.getLayoutParams();
			llp.width = mIconWidth;
			llp.height = mIconWidth;

			ImageView iv = (ImageView) view.findViewById(R.id.image1);
			iv.setImageDrawable(menuItem.iconDrawable);
			view.setLayoutParams(lp);
			LayoutTransition lt = ((ViewGroup) view).getLayoutTransition();
			if (lt != null) {
				lt.setDuration(LayoutTransition.APPEARING,
						ANIMATION_DURATION_LAYOUT_TRANSITION);
				lt.setDuration(LayoutTransition.CHANGE_DISAPPEARING, 10);
				lt.setStagger(LayoutTransition.APPEARING, 0);
			}

			addView(view, lp);
		}

		inflater = null;
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

	int mScrollDistance = 0;

	@SuppressLint("NewApi")
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		boolean hasKeyConsumed = false;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if (selectedChildIndex > 0) {
					toggleMenuItemSelection(selectedChildIndex, false);
					selectedChildIndex--;
					toggleMenuItemSelection(selectedChildIndex, true);
					ArrayList<Animator> set = slideRight();
					notifyMenuItemNavigated(selectedChildIndex);
					notifyChildFocusChangeForBlob(selectedChildIndex+1, false, set);
					AnimatorSet animSet = new AnimatorSet();
					animSet.playTogether(set);
					animSet.start();
				}
				hasKeyConsumed = true;
				break;

			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if (selectedChildIndex < menuItems.size() - 1) {
					toggleMenuItemSelection(selectedChildIndex, false);
					selectedChildIndex++;
					toggleMenuItemSelection(selectedChildIndex, true);
					ArrayList<Animator> set = slideLeft();
					notifyMenuItemNavigated(selectedChildIndex);
					notifyChildFocusChangeForBlob(selectedChildIndex -1, true, set);
					AnimatorSet animSet = new AnimatorSet();
					animSet.playTogether(set);
					animSet.start();
				}
				hasKeyConsumed = true;
				break;

			case KeyEvent.KEYCODE_BUTTON_SELECT:
			case KeyEvent.KEYCODE_DPAD_CENTER:
				Log.e(MenuLayout.class.getName(), "Menu Item selected "
						+ selectedChildIndex);
				notifyMenuItemSelected(selectedChildIndex);
				break;
			}
		}
		return hasKeyConsumed;
	}

	private void notifyMenuItemNavigated(int index) {
		if (menuListener != null) {
			final MenuItem menuItem = menuItems.get(index);
			menuListener.onMenuItemChanged(menuItem, index);
		}
	}

	private void notifyMenuItemSelected(int index) {
		if (menuListener != null) {
			final MenuItem menuItem = menuItems.get(index);
			menuListener.onMenuItemSelected(menuItem);
		}
	}

	private void toggleMenuItemSelection(int index, boolean isSelected) {
		View view = getChildAt(index);
		if (view == null)
			return;

//		View imgView = view.findViewById(R.id.image2);
		final View tv = view.findViewById(R.id.text1);
		if (!isSelected) {
//			imgView.setVisibility(View.GONE);
			tv.setVisibility(View.GONE);
		} else {
//			imgView.setVisibility(View.VISIBLE);
			tv.setVisibility(View.VISIBLE);
		}

	}

	private ArrayList<Animator> slideLeft() {
		ArrayList<Animator> set = new ArrayList<Animator>();

		if (selectedChildIndex < 0)
			return null;

		mScrollDistance -= scrollWidth;

		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);

			ValueAnimator anim = ObjectAnimator.ofFloat(child, "translationX",
					mScrollDistance);
			anim.setDuration(ANIMATION_DURATION_SLIDE_LEFT_RIGHT);
			anim.setInterpolator(new DecelerateInterpolator(1.2f));
			set.add(anim);
		}
		return set;
	}

	private ArrayList<Animator> slideRight() {
		ArrayList<Animator> set = new ArrayList<Animator>();
		mScrollDistance += scrollWidth;

		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			ValueAnimator anim = ObjectAnimator.ofFloat(child, "translationX",
					mScrollDistance);
			anim.setDuration(ANIMATION_DURATION_SLIDE_LEFT_RIGHT);
			anim.setInterpolator(new DecelerateInterpolator(1.2f));
			set.add(anim);
		}
		return set;
	}

	int[] location = new int[2];
	@Override
	public void onFocusChange(View arg0, boolean focussed) {
		View view = getChildAt(selectedChildIndex);
		View imgView3 = view.findViewById(R.id.image3);

		if (focussed) {
			imgView3.setVisibility(View.GONE);
		} else {
			imgView3.setVisibility(View.VISIBLE);
		}

		notifyFocusChangeForBlob(selectedChildIndex, focussed);
	}

	private void notifyFocusChangeForBlob(int childIndex, boolean focussed){
		if (this.listener != null) {
			int x = location[0] + scrollWidth/2;
			int y = location[1] ;

			Point p1 = new Point(x, y);
			View view = getChildAt(childIndex);
			ImageView imgView2 = (ImageView) view.findViewById(R.id.image1);

			listener.onFocusChanged(focussed, new Point[] { p1}, imgView2.getDrawable(), null);
		}
	}

	private void notifyChildFocusChangeForBlob(int childIndex, boolean toRight, ArrayList<Animator> set) {
		if (this.listener != null) {
			int x = location[0] + scrollWidth/2;
			int y = location[1] ;
			View view = getChildAt(childIndex);
			ImageView imgView2 = (ImageView) view.findViewById(R.id.image1);

			Point p1 = new Point(x, y);
			if (toRight) {
				Point p2 = new Point(x - scrollWidth, y);
				listener.onChildFocusChanged(new Point[] { p1, p2, p1 }, imgView2.getDrawable(), set);
			} else {
				Point p2 = new Point(x + scrollWidth, y);
				listener.onChildFocusChanged(new Point[] { p1, p2, p1 }, imgView2.getDrawable(), set);
			}
		}
	}

	@Override
	public void onGlobalLayout() {
		if(location[0] ==0 && location[1]==0){
			View childView = getChildAt(selectedChildIndex);
			childView.getLocationOnScreen(location);
			location[1] += childView.getMeasuredHeight()/2;
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}
}
