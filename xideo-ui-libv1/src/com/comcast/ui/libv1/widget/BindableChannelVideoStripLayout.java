package com.comcast.ui.libv1.widget;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.BindingLog;
import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.ConstantObservable;
import gueei.binding.IBindableView;
import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;
import gueei.binding.Binder.InflateResult;
import gueei.binding.ISyntaxResolver.SyntaxResolveException;
import gueei.binding.InnerFieldObservable;
import gueei.binding.Observer;
import gueei.binding.collections.ObservableCollection;
import gueei.binding.utility.ObservableMultiplexer;
import gueei.binding.utility.WeakList;
import gueei.binding.viewAttributes.templates.Layout;
import gueei.binding.viewAttributes.templates.LayoutItem;
import gueei.binding.viewAttributes.templates.SingleTemplateLayout;
import gueei.binding.ViewAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;
import android.view.View.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

@RemoteView
public class BindableChannelVideoStripLayout extends ViewGroup implements IBindableView<BindableChannelVideoStripLayout>, OnFocusChangeListener {

	protected int mSelectedIndex = 0;
	protected int mPreviousSelectedIndex = 0;
	protected int mOffsetLeft;
	// protected int mItemCount;
	ConcurrentLinkedQueue<Point> pendingList = new ConcurrentLinkedQueue<Point>();

	static final int ANIMATION_DURATION_SLIDE_LEFT_RIGHT = 350; // milliseconds
	static final int ANIMATION_DURATION_SLIDE_UPDOWN = 200; // milliseconds
	static final int ANIMATION_DURATION_FOCUS_CHANGE = 200; // milliseconds

	public static final String DEFAULT_FONT_NAME = "fonts/Flama-Basic.otf";

	public IFocusHopperListener listener;

	private WeakList<Object> currentList = null;
	private ObservableCollection<Object> itemList = null;
	private Object channelDataSource = null;
	private int channelLayoutId = 0;
	private LayoutItem channelLayout = null;
	private LayoutItem layout = null;
	private WeakList<View> videoListViews = null;

	public interface OnSelectionChangeListener {
		void onSelectionChanged(ObservableCollection<Object> itemList, int index, View child, boolean selected);

		void onItemSelected(Object obj, int index, View child);

		void onFocusChanged(View child, boolean hasFocus);
	}

	public OnSelectionChangeListener changeListener;

	public BindableChannelVideoStripLayout(Context context) {
		this(context, null);
	}

	public BindableChannelVideoStripLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BindableChannelVideoStripLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new ViewGroup.MarginLayoutParams(getContext(), attrs);
	}

	@Override
	protected ViewGroup.MarginLayoutParams generateDefaultLayoutParams() {
		return new ViewGroup.MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	public void setBlobListener(IFocusHopperListener listener) {
		this.listener = listener;
	}

	public void setOnSelectionChangeListener(OnSelectionChangeListener listener) {
		this.changeListener = listener;
	}

	private void init() {
		setFocusable(true);
		setClickable(true);
		setOnFocusChangeListener(this);
		setScrollContainer(true);
		mOffsetLeft = 60;
	}

	ViewGroup.MarginLayoutParams oldMlp;
	protected boolean needScrollCorrection = false;

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (getChildCount() == 0)
			return;
		needScrollCorrection = true;
		if (hasFocus) {
			oldMlp = (ViewGroup.MarginLayoutParams) getLayoutParams();
			ViewGroup.MarginLayoutParams mlp = new MarginLayoutParams(oldMlp);
			mlp.leftMargin = 0;
			setLayoutParams(mlp);
		} else {
			if (oldMlp != null) {
				setLayoutParams(oldMlp);
			}
		}
		for (int i = 0; i < getChildCount(); i++) {
			notifyFocusChanged(i, hasFocus);
			notifySelectionChanged(i, (i == mSelectedIndex) && hasFocus);
		}

		if (hasFocus) {
			setChildSelections();
		} else {
			unsetChildSelections();
		}
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable("instanceState", super.onSaveInstanceState());
		bundle.putInt("mPreviousSelectedIndex", mPreviousSelectedIndex);
		bundle.putInt("mSelectedIndex", mSelectedIndex);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			this.mPreviousSelectedIndex = bundle.getInt("mPreviousSelectedIndex");
			this.mSelectedIndex = bundle.getInt("mSelectedIndex");
			super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
			return;
		}

		super.onRestoreInstanceState(state);
	}

	private CollectionObserver collectionObserver = new CollectionObserver() {
		@Override
		public void onCollectionChanged(IObservableCollection<?> collection, CollectionChangedEventArg args, Collection<Object> initiators) {
			listChanged(args, collection);
		}
	};

	private void createItemSourceList(ObservableCollection<Object> newList) {
		if (itemList != null && collectionObserver != null)
			itemList.unsubscribe(collectionObserver);

		itemList = newList;
		videoListViews = new WeakList<View>();
		if (newList == null)
			return;

		currentList = null;

		itemList.subscribe(collectionObserver);
		newList(newList);
	}

	public ObservableCollection<Object> getItemList() {
		return itemList;
	}

	@Override
	protected void onDetachedFromWindow() {
		currentList = null;
		channelDataSource = null;
		super.onDetachedFromWindow();
	}

	@Override
	public ViewAttribute<BindableChannelVideoStripLayout, ?> createViewAttribute(String attributeId) {
		if (attributeId.equals("itemSource"))
			return ItemSourceAttribute;
		if (attributeId.equals("itemLayout"))
			return ItemLayoutAttribute;

		if (attributeId.equals("channelSource"))
			return ChannelSourceAttribute;
		if (attributeId.equals("channelLayout"))
			return ChannelLayoutAttribute;
		return null;
	}

	private void listChanged(CollectionChangedEventArg e, IObservableCollection<?> collection) {
		if (e == null)
			return;

		int pos = -1;
		switch (e.getAction()) {
			case Add:
				pos = e.getNewStartingIndex();
				for (Object item : e.getNewItems()) {
					insertItem(pos, item);
					pos++;
				}
				break;
			case Remove:
				removeItems(e.getOldItems());
				break;
			case Replace:
				removeItems(e.getOldItems());
				pos = e.getNewStartingIndex();
				if (pos < 0)
					pos = 0;
				for (Object item : e.getNewItems()) {
					insertItem(pos, item);
					pos++;
				}
				break;
			case Reset:
				newList(collection);
				break;
			case Move:
				// currently the observable array list doesn't create this
				// action
				throw new IllegalArgumentException("move not implemented");
			default:
				throw new IllegalArgumentException("unknown action " + e.getAction().toString());
		}

		if (collection == null)
			return;

		currentList = new WeakList<Object>();
		for (pos = 0; pos < collection.size(); pos++) {
			Object item = collection.getItem(pos);
			currentList.add(item);
		}
		prepareChildViews();
	}

	private void newList(IObservableCollection<?> list) {
		observableItemsLayoutID.clear();
		videoListViews = new WeakList<View>();
		if (list == null) {
			currentList = null;
		} else {
			currentList = new WeakList<Object>();
			for (int pos = 0; pos < list.size(); pos++) {
				Object item = list.getItem(pos);
				// Log.e(VideoStripLayout.class.getName(),
				// "item " + item.toString());
				insertItem(pos, item);
				currentList.add(item);
			}
		}
	}

	private ViewAttribute<BindableChannelVideoStripLayout, Object> ItemSourceAttribute = new ViewAttribute<BindableChannelVideoStripLayout, Object>(
			Object.class, BindableChannelVideoStripLayout.this, "ItemSource") {
		@SuppressWarnings("unchecked")
		@Override
		protected void doSetAttributeValue(Object newValue) {
			if (!(newValue instanceof ObservableCollection<?>))
				return;

			if (layout != null)
				createItemSourceList((ObservableCollection<Object>) newValue);
		}

		@Override
		public Object get() {
			return itemList;
		}
	};

	private ViewAttribute<BindableChannelVideoStripLayout, Object> ChannelSourceAttribute = new ViewAttribute<BindableChannelVideoStripLayout, Object>(
			Object.class, BindableChannelVideoStripLayout.this, "ChannelSource") {
		@SuppressWarnings("unchecked")
		@Override
		protected void doSetAttributeValue(Object newValue) {
			BindableChannelVideoStripLayout.this.setDatasource(newValue);
		}

		@Override
		public Object get() {
			return channelDataSource;
		}
	};

	protected void setDatasource(Object newValue) {
		channelDataSource = newValue;
		channelIconInflateResult = null;
		setChannelIcon();
		refreshDrawableState();
	}

	private ViewAttribute<BindableChannelVideoStripLayout, Object> ItemLayoutAttribute = new ViewAttribute<BindableChannelVideoStripLayout, Object>(
			Object.class, BindableChannelVideoStripLayout.this, "ItemLayout") {
		@Override
		protected void doSetAttributeValue(Object newValue) {
			layout = null;
			if (newValue instanceof LayoutItem) {
				layout = (LayoutItem) newValue;
			} else if (newValue instanceof Layout) {
				layout = new LayoutItem(((Layout) newValue).getDefaultLayoutId());
			} else if (newValue instanceof Integer) {
				layout = new LayoutItem((Integer) newValue);
			} else {
				layout = new LayoutItem(newValue.toString());
			}

			if (itemList != null)
				createItemSourceList(itemList);
		}

		@Override
		public Object get() {
			return layout;
		}
	};

	private ViewAttribute<BindableChannelVideoStripLayout, Object> ChannelLayoutAttribute = new ViewAttribute<BindableChannelVideoStripLayout, Object>(
			Object.class, BindableChannelVideoStripLayout.this, "ChannelLayout") {
		@Override
		protected void doSetAttributeValue(Object newValue) {
			int newLayoutId = 0;

			if (newValue instanceof SingleTemplateLayout) {
				SingleTemplateLayout layout = (SingleTemplateLayout) newValue;
				newLayoutId = layout.getDefaultLayoutId();
			} else {
				if ((newValue != null) && (newValue.toString().length() > 0)) {
					try {
						newLayoutId = Integer.parseInt(newValue.toString());
					} catch (Exception e) {
					}
				}
			}

			channelLayoutId = newLayoutId;
			BindableChannelVideoStripLayout.this.setChannelIcon();
		}

		@Override
		public Object get() {
			return channelLayoutId;
		}
	};

	InflateResult channelIconInflateResult = null;

	private void setChannelIcon() {

		if (channelLayoutId <= 0)
			return;

		if (channelIconInflateResult == null || channelDataSource == null)
			channelIconInflateResult = Binder.inflateView(BindableChannelVideoStripLayout.this.getContext(), channelLayoutId,
					BindableChannelVideoStripLayout.this, false);

		if (isChannelLayoutAdded) {
			removeViewAt(0);
		}

		// BindableChannelVideoStripLayout.this.addView(inflateResult.rootView,
		// 0);
		isChannelLayoutAdded = true;

		if (channelDataSource == null) {
			Binder.bindView(BindableChannelVideoStripLayout.this.getContext(), channelIconInflateResult, null);
		} else {
			Binder.bindView(BindableChannelVideoStripLayout.this.getContext(), channelIconInflateResult, channelDataSource);
		}
	}

	// private ObservableMultiplexer<Object> observableChannelItemsLayoutId =
	// new ObservableMultiplexer<Object>(
	// new Observer() {
	// @Override
	// public void onPropertyChanged(IObservable<?> prop,
	// Collection<Object> initiators) {
	// if (initiators == null || initiators.size() < 1)
	// return;
	// Object parent = initiators.toArray()[0];
	// insertChannelItem(parent);
	// }
	// });

	private ObservableMultiplexer<Object> observableItemsLayoutID = new ObservableMultiplexer<Object>(new Observer() {
		@Override
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			if (initiators == null || initiators.size() < 1)
				return;
			Object parent = initiators.toArray()[0];
			int pos = currentList.indexOf(parent);
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(parent);
			removeItems(list);
			insertItem(pos, parent);
		}
	});

	private void removeItems(List<?> deleteList) {
		if (deleteList == null || deleteList.size() == 0 || currentList == null)
			return;

		ArrayList<Object> currentPositionList = new ArrayList<Object>(Arrays.asList(currentList.toArray()));

		for (Object item : deleteList) {
			int pos = currentPositionList.indexOf(item);
			observableItemsLayoutID.removeParent(item);
			currentPositionList.remove(item);
			if (pos > -1 && pos < this.getChildCount()) {
				// this.removeViewAt(pos);
				videoListViews.remove(pos);
			}
		}
	}

	boolean isChannelLayoutAdded = false;

	// @SuppressWarnings({ "rawtypes", "unchecked" })
	// private boolean insertChannelItem(Object item) {
	// if (channelLayout == null)
	// return false;
	//
	// if(isChannelLayoutAdded) {
	// removeViewAt(0);
	// }
	//
	// int layoutId = channelLayout.getLayoutId();
	// if (layoutId < 1 && channelLayout.getLayoutName() != null) {
	// IObservable<?> observable = null;
	// InnerFieldObservable ifo = new
	// InnerFieldObservable(channelLayout.getLayoutName());
	// if (ifo.createNodes(item)) {
	// observable = ifo;
	// } else {
	// Object rawField;
	// try {
	// rawField =
	// Binder.getSyntaxResolver().getFieldForModel(channelLayout.getLayoutName(),
	// item);
	// } catch (SyntaxResolveException e) {
	// BindingLog.exception("BindableLinearLayout.insertItem()", e);
	// return false;
	// }
	// if (rawField instanceof IObservable<?>)
	// observable = (IObservable<?>) rawField;
	// else if (rawField != null)
	// observable = new ConstantObservable(rawField.getClass(), rawField);
	// }
	//
	// if (observable != null) {
	// observableChannelItemsLayoutId.add(observable,channelDataSource);
	// Object obj = observable.get();
	// if (obj instanceof Integer)
	// layoutId = (Integer) obj;
	// }
	// }
	//
	// View child = null;
	//
	// if (layoutId < 1) {
	// TextView textView = new TextView(getContext());
	// textView.setText("binding error: has no channel layout");
	// textView.setTextColor(Color.RED);
	// child = textView;
	// } else {
	// Binder.InflateResult result = Binder.inflateView(getContext(), layoutId,
	// this, false);
	// for (View view : result.processedViews) {
	// AttributeBinder.getInstance().bindView(getContext(), view,
	// channelDataSource);
	// }
	//
	// child = result.rootView;
	// }
	//
	// this.addView(child, 0);
	// isChannelLayoutAdded = true;
	// return true;
	// }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void insertItem(int pos, Object item) {
		if (layout == null)
			return;

		int layoutId = layout.getLayoutId();
		if (layoutId < 1 && layout.getLayoutName() != null) {
			IObservable<?> observable = null;
			InnerFieldObservable ifo = new InnerFieldObservable(layout.getLayoutName());
			if (ifo.createNodes(item)) {
				observable = ifo;
			} else {
				Object rawField;
				try {
					rawField = Binder.getSyntaxResolver().getFieldForModel(layout.getLayoutName(), item);
				} catch (SyntaxResolveException e) {
					BindingLog.exception("BindableLinearLayout.insertItem()", e);
					return;
				}
				if (rawField instanceof IObservable<?>)
					observable = (IObservable<?>) rawField;
				else if (rawField != null)
					observable = new ConstantObservable(rawField.getClass(), rawField);
			}

			if (observable != null) {
				observableItemsLayoutID.add(observable, item);
				Object obj = observable.get();
				if (obj instanceof Integer)
					layoutId = (Integer) obj;
			}
		}
		View child = null;

		if (layoutId < 1) {
			TextView textView = new TextView(getContext());
			textView.setText("binding error - pos: " + pos + " has no layout - please check binding:itemPath or the layout id in viewmodel");
			textView.setTextColor(Color.RED);
			child = textView;
		} else {
			Binder.InflateResult result = Binder.inflateView(getContext(), layoutId, this, false);
			for (View view : result.processedViews) {
				AttributeBinder.getInstance().bindView(getContext(), view, item);
			}

			child = result.rootView;
		}

		videoListViews.add(child);
		// this.addView(child, pos);
	}

	private void prepareChildViews() {
		removeAllViews();
		for (int i = 0; i < videoListViews.size(); i++) {
			View view = videoListViews.get(i);
			this.addView(view);
		}

		if (channelIconInflateResult != null && channelIconInflateResult.rootView != null) {
			this.addView(channelIconInflateResult.rootView);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		int specModeW = MeasureSpec.getMode(widthMeasureSpec);
		int specSizeW = MeasureSpec.getSize(widthMeasureSpec);

		int specModeH = MeasureSpec.getMode(heightMeasureSpec);
		int specSizeH = MeasureSpec.getSize(heightMeasureSpec);
		int maxWidth = specSizeW;
		int maxHeight = specSizeH;
		int maxChildHeight = 0;
		int totalWidth = mOffsetLeft;

		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();

			int vHeight = child.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;
			int vWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
			maxChildHeight = Math.max(maxChildHeight, vHeight);
			totalWidth += vWidth;
		}

		ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
		totalWidth += lp.leftMargin + lp.rightMargin;

		maxChildHeight += lp.topMargin + lp.bottomMargin;

		if (specModeH == MeasureSpec.UNSPECIFIED) {
			maxHeight = maxChildHeight;
		} else if (specModeH == MeasureSpec.AT_MOST) {
			maxHeight = Math.min(maxHeight, maxChildHeight);
		}

		maxWidth = Math.max(specSizeW, totalWidth);
		setMeasuredDimension(maxWidth, maxHeight);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		final int paddingTop = getPaddingTop();
		if (needScrollCorrection) {
			slide(null, 1, false);
			needScrollCorrection = false;
			pendingList.clear();
		}

		int childTop = paddingTop;
		int childLeft = mOffsetLeft;

		View iconView = channelIconInflateResult.rootView;
		int childWidth = iconView.getMeasuredWidth();
		int childHeight = iconView.getMeasuredHeight();

		ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
		childLeft += lp.leftMargin;

		ViewGroup.MarginLayoutParams lpChild = (ViewGroup.MarginLayoutParams) iconView.getLayoutParams();
		childTop = getHeight() - childHeight - lpChild.bottomMargin;
		childLeft += (lpChild.leftMargin);
		setChildFrame(iconView, childLeft, childTop, childWidth, childHeight);
		childLeft += (childWidth + lpChild.rightMargin + lpChild.leftMargin);
		// childLeft -= mScrollDistance;

		for (int i = 0; i < videoListViews.size(); i++) {
			final View child = getChildAt(i);
			childWidth = child.getMeasuredWidth();
			childHeight = child.getMeasuredHeight();

			lpChild = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
			childTop = getHeight() - childHeight - lpChild.bottomMargin;
			childLeft += (lpChild.leftMargin);
			setChildFrame(child, childLeft, childTop, childWidth, childHeight);
			childLeft += (childWidth + lpChild.rightMargin);
		}
	}

	private void setChildFrame(View child, int left, int top, int width, int height) {
		child.layout(left, top, left + width, top + height);
	}

	void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
		measureChildWithMargins(child, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
	}

	// protected ViewGroup.MarginLayoutParams getChildLayoutMargin(int i, View
	// view) {
	// MarginLayoutParams mlp = new MarginLayoutParams(view.getLayoutParams());
	// mlp.bottomMargin = 0;
	// mlp.rightMargin = 0;
	// mlp.topMargin = 0;
	// mlp.leftMargin = 0;
	// return new ViewGroup.MarginLayoutParams(mlp);
	// }

	@Override
	protected void setChildrenDrawingOrderEnabled(boolean enabled) {
		enabled = true;
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		return childCount - i - 1;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (!executeKeyEvent(event))
			return super.dispatchKeyEvent(event);
		else
			return true;
	}

	boolean mReceivedInvokeKeyDown;

	public boolean executeKeyEvent(KeyEvent event) {
		boolean handled = false;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {

				case KeyEvent.KEYCODE_DPAD_LEFT:

					if (movePrevious()) {
						setChildSelections();
					}
					handled = true;
					break;

				case KeyEvent.KEYCODE_DPAD_RIGHT:
					if (moveNext()) {
						setChildSelections();
					}
					handled = true;
					break;

				case KeyEvent.KEYCODE_DPAD_CENTER:
				case KeyEvent.KEYCODE_ENTER:
					mReceivedInvokeKeyDown = true;
					if (this.changeListener != null) {
						View child = getChildAt(mSelectedIndex);
						Object obj = itemList.getItem(mSelectedIndex);
						changeListener.onItemSelected(obj, mSelectedIndex, child);
					}
					handled = true;
					break;
			}
		}
		return handled;
	}

	private int getVideoCount() {
		if (videoListViews != null)
			return videoListViews.size();

		return 0;
	}

	boolean animationRunning = false;

	boolean movePrevious() {
		boolean status = false;
		int mItemCount = getVideoCount();
		if (mItemCount > 0 && mSelectedIndex > 0) {
			mPreviousSelectedIndex = mSelectedIndex;
			mSelectedIndex--;
			status = true;
			pendingList.add(new Point(mPreviousSelectedIndex, mSelectedIndex));
			triggerSlide();
		}
		return status;
	}

	boolean moveNext() {
		boolean status = false;
		int mItemCount = getVideoCount();
		if (mItemCount > 0 && mSelectedIndex < mItemCount - 1) {
			mPreviousSelectedIndex = mSelectedIndex;
			mSelectedIndex++;
			status = true;
			pendingList.add(new Point(mPreviousSelectedIndex, mSelectedIndex));
			triggerSlide();
		}
		return status;
	}

	private void notifySelectionChanged(int index, boolean selected) {
		View child = getChildAt(index);
		if (this.changeListener != null) {
			changeListener.onSelectionChanged(itemList, index, child, selected);
		}
	}

	private void notifyFocusChanged(int index, boolean hasFocus) {
		if (this.changeListener != null) {
			View child = getChildAt(index);
			changeListener.onFocusChanged(child, hasFocus);
		}
	}

	void setChildSelections() {
		View view;

		view = getChildAt(mPreviousSelectedIndex);
		if (view != null)
			view.setSelected(false);

		view = getChildAt(mSelectedIndex);
		if (view != null)
			view.setSelected(true);
	}

	void unsetChildSelections() {
		View view;
		view = getChildAt(mPreviousSelectedIndex);
		if (view != null)
			view.setSelected(false);

		view = getChildAt(mSelectedIndex);
		if (view != null)
			view.setSelected(false);
	}

	private synchronized void triggerSlide() {
		View nChild = getChildAt(mSelectedIndex);
		notifySelectionChanged(mPreviousSelectedIndex, false);
		notifySelectionChanged(mSelectedIndex, true);
		slide(nChild, ANIMATION_DURATION_SLIDE_LEFT_RIGHT, true);
	}

	ValueAnimator va;

	int mScrollDistance = 0;

	public synchronized boolean slide(View selectedChild, final int animationDuration, final boolean triggerOnFinish) {
		if (selectedChild == null) {
			selectedChild = getChildAt(mSelectedIndex);
		}

		while (pendingList.size() > 0) {
			Point p = pendingList.poll();
			if (p != null) {
				int prevIndex = p.x;
				int nextIndex = p.y;
				View previousChild = getChildAt(prevIndex);
				View nextChild = getChildAt(nextIndex);
				if (prevIndex < nextIndex) {
					ObjectAnimator anim2 = ObjectAnimator.ofFloat(previousChild, "alpha", 0.15f);
					anim2.setDuration(ANIMATION_DURATION_SLIDE_LEFT_RIGHT);
					anim2.setInterpolator(new AccelerateInterpolator());
					anim2.start();
				} else {
					ObjectAnimator anim3 = ObjectAnimator.ofFloat(nextChild, "alpha", 1.0f);
					anim3.setDuration(ANIMATION_DURATION_SLIDE_LEFT_RIGHT);
					anim3.setInterpolator(new AccelerateInterpolator());
					anim3.start();
				}
			}
		}
		View iconView = channelIconInflateResult.rootView;
		ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) iconView.getLayoutParams();

		for (int i = 0; i < getChildCount() - 1; i++) {
			View child = getChildAt(i);
			int currentOffsetPos = getOffsetLeftPos(child);
			int realOffsetPos = getRealOffsetPosition(child);
			if(i < mSelectedIndex /*&& mPreviousSelectedIndex < mSelectedIndex*/) {
				realOffsetPos -= iconView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
			}
			
			ObjectAnimator anim = ObjectAnimator.ofFloat(child, "translationX", currentOffsetPos, realOffsetPos);
			anim.setDuration(animationDuration);
			anim.setInterpolator(new AccelerateDecelerateInterpolator());
			if (i == 0 && triggerOnFinish) {
				anim.addListener(new AnimatorListener() {

					@Override
					public void onAnimationCancel(Animator animation) {

					}

					@Override
					public void onAnimationEnd(Animator animation) {
						onFinishedMovement();
					}

					@Override
					public void onAnimationRepeat(Animator animation) {

					}

					@Override
					public void onAnimationStart(Animator animation) {

					}
				});

			}
			anim.start();
		}

		return true;
	}

	private int getOffsetLeftPos(View view) {
		return (int) (view.getX() - view.getLeft());
	}

	private int getRealOffsetPosition(View view) {
		ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
		int offsetPosition = -(view.getMeasuredWidth() + lp.leftMargin + lp.rightMargin) * mSelectedIndex;
		return offsetPosition;
	}

	private void onFinishedMovement() {
		notifySelectionChanged(mPreviousSelectedIndex, false);
		notifySelectionChanged(mSelectedIndex, true);
	}
}
