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
import gueei.binding.ISyntaxResolver.SyntaxResolveException;
import gueei.binding.InnerFieldObservable;
import gueei.binding.Observer;
import gueei.binding.collections.ObservableCollection;
import gueei.binding.utility.ObservableMultiplexer;
import gueei.binding.utility.WeakList;
import gueei.binding.viewAttributes.templates.Layout;
import gueei.binding.viewAttributes.templates.LayoutItem;
import gueei.binding.ViewAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.comcast.ui.libv1.R;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;
import android.view.View.*;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

@RemoteView
public class BindableVideoStripContainerLayout extends ViewGroup implements
		IBindableView<BindableVideoStripContainerLayout>, OnFocusChangeListener {

	protected int mSelectedIndex = 0;
	protected int mPreviousSelectedIndex = 0;
	protected int mOffsetTop;
	protected int mItemCount;
	ConcurrentLinkedQueue<Point> pendingList = new ConcurrentLinkedQueue<Point>();

	static final int ANIMATION_DURATION_SLIDE_UPDOWN = 500; // milliseconds
	static final int DEFAULT_OFFSET_TOP_POSITION = 100;
	int animationDuration;

	public interface OnSelectionChangeListener {
		void onSelectionChanged(ObservableCollection<Object> itemList, int index,
				View child, boolean selected);
		void onItemSelected(Object obj, int index, View child);
		void onFocusChanged(View child, boolean hasFocus);
	}

	public OnSelectionChangeListener changeListener;

	public BindableVideoStripContainerLayout(Context context) {
		this(context, null);
	}

	public BindableVideoStripContainerLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BindableVideoStripContainerLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BindableVideoStripContainerLayout, defStyle, 0);
		mOffsetTop = (int) a.getDimension(R.styleable.BindableVideoStripContainerLayout_offsetTop, DEFAULT_OFFSET_TOP_POSITION);
		a.recycle();

		init();
	}

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ViewGroup.MarginLayoutParams(getContext(), attrs);
    }

	@Override
	protected ViewGroup.MarginLayoutParams generateDefaultLayoutParams() {
		return new ViewGroup.MarginLayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

	public void setOnSelectionChangeListener(OnSelectionChangeListener listener) {
		this.changeListener = listener;
	}

	private void init() {
		setFocusable(true);
		setClickable(true);
		setOnFocusChangeListener(this);
		setScrollContainer(true);
		setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		View view = getChildAt(mSelectedIndex);
		if(view == null)
			return;

		for (int i = 0; i < getChildCount(); i++) {
			notifyFocusChanged(i, hasFocus);
			notifySelectionChanged(i, (i == mSelectedIndex) && hasFocus);
		}

		if (hasFocus) {
			view.requestFocus();
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
			this.mPreviousSelectedIndex = bundle
					.getInt("mPreviousSelectedIndex");
			this.mSelectedIndex = bundle.getInt("mSelectedIndex");
			super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
			return;
		}

		super.onRestoreInstanceState(state);
	}

	private WeakList<Object> currentList = null;
	private ObservableCollection<Object> itemList = null;
	private CollectionObserver collectionObserver = new CollectionObserver() {
		@Override
		public void onCollectionChanged(IObservableCollection<?> collection,
				CollectionChangedEventArg args, Collection<Object> initiators) {
			listChanged(args, collection);
		}
	};

	private void createItemSourceList(ObservableCollection<Object> newList) {
		if (itemList != null && collectionObserver != null)
			itemList.unsubscribe(collectionObserver);

		itemList = newList;

		if (newList == null)
			return;

		currentList = null;

		itemList.subscribe(collectionObserver);
		newList(newList);
	}

	public ObservableCollection<Object> getItemList(){
		return itemList;
	}
	
	@Override
	protected void onDetachedFromWindow() {
		currentList = null;
		super.onDetachedFromWindow();
	}

	@Override
	public ViewAttribute<BindableVideoStripContainerLayout, ?> createViewAttribute(
			String attributeId) {
		if (attributeId.equals("itemSource"))
			return ItemSourceAttribute;
		if (attributeId.equals("itemLayout"))
			return ItemLayoutAttribute;
		return null;
	}

	private void newList(IObservableCollection<?> list) {
		this.removeAllViews();
		observableItemsLayoutID.clear();
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

	private void listChanged(CollectionChangedEventArg e,
			IObservableCollection<?> collection) {
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
			mSelectedIndex = 0;
			mPreviousSelectedIndex = 0;
			scrollTo(0,0);
			newList(collection);
			break;
		case Move:
			// currently the observable array list doesn't create this action
			throw new IllegalArgumentException("move not implemented");
		default:
			throw new IllegalArgumentException("unknown action "
					+ e.getAction().toString());
		}

		if (collection == null)
			return;

		currentList = new WeakList<Object>();
		for (pos = 0; pos < collection.size(); pos++) {
			Object item = collection.getItem(pos);
			currentList.add(item);
		}
	}

	private ViewAttribute<BindableVideoStripContainerLayout, Object> ItemSourceAttribute = new ViewAttribute<BindableVideoStripContainerLayout, Object>(
			Object.class, BindableVideoStripContainerLayout.this, "ItemSource") {
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

	private LayoutItem layout = null;

	private ViewAttribute<BindableVideoStripContainerLayout, Object> ItemLayoutAttribute = new ViewAttribute<BindableVideoStripContainerLayout, Object>(
			Object.class, BindableVideoStripContainerLayout.this, "ItemLayout") {
		@Override
		protected void doSetAttributeValue(Object newValue) {
			layout = null;
			if (newValue instanceof LayoutItem) {
				layout = (LayoutItem) newValue;
			} else if (newValue instanceof Layout) {
				layout = new LayoutItem(
						((Layout) newValue).getDefaultLayoutId());
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

	private ObservableMultiplexer<Object> observableItemsLayoutID = new ObservableMultiplexer<Object>(
			new Observer() {
				@Override
				public void onPropertyChanged(IObservable<?> prop,
						Collection<Object> initiators) {
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

		ArrayList<Object> currentPositionList = new ArrayList<Object>(
				Arrays.asList(currentList.toArray()));

		for (Object item : deleteList) {
			int pos = currentPositionList.indexOf(item);
			observableItemsLayoutID.removeParent(item);
			currentPositionList.remove(item);
			if (pos > -1 && pos < this.getChildCount())
				this.removeViewAt(pos);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void insertItem(int pos, Object item) {
		if (layout == null)
			return;

		int layoutId = layout.getLayoutId();
		if (layoutId < 1 && layout.getLayoutName() != null) {
			IObservable<?> observable = null;
			InnerFieldObservable ifo = new InnerFieldObservable(
					layout.getLayoutName());
			if (ifo.createNodes(item)) {
				observable = ifo;
			} else {
				Object rawField;
				try {
					rawField = Binder.getSyntaxResolver().getFieldForModel(
							layout.getLayoutName(), item);
				} catch (SyntaxResolveException e) {
					BindingLog
							.exception("BindableLinearLayout.insertItem()", e);
					return;
				}
				if (rawField instanceof IObservable<?>)
					observable = (IObservable<?>) rawField;
				else if (rawField != null)
					observable = new ConstantObservable(rawField.getClass(),
							rawField);
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
			textView.setText("binding error - pos: "
					+ pos
					+ " has no layout - please check binding:itemPath or the layout id in viewmodel");
			textView.setTextColor(Color.RED);
			child = textView;
		} else {
			Binder.InflateResult result = Binder.inflateView(getContext(),
					layoutId, this, false);
			for (View view : result.processedViews) {
				AttributeBinder.getInstance()
						.bindView(getContext(), view, item);
			}
			
			child = result.rootView;
		}

		this.addView(child, pos);
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
		int maxChildWidth = 0;
		int totalHeight = mOffsetTop;
		int skipCount = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if(child.getVisibility() == View.GONE) {
				child.setFocusable( false );
				skipCount++;
				continue;
			}
			if(((ViewGroup) child).getChildCount() == 0) {
				child.setFocusable( false );
				skipCount++;
				continue;
			}

			final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();

			int vHeight = child.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;
			int vWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
			
			totalHeight += vHeight;
			maxChildWidth = Math.max(maxChildWidth, vWidth);
		}

		if (specModeH == MeasureSpec.UNSPECIFIED) {
			maxHeight = totalHeight;
		} else if (specModeH == MeasureSpec.AT_MOST) {
			maxHeight = Math.max(maxHeight, totalHeight);
		}

		if(specModeW == MeasureSpec.UNSPECIFIED){
			maxWidth = maxChildWidth;
		}else if (specModeW == MeasureSpec.AT_MOST){
			maxWidth = Math.max(maxWidth, maxChildWidth);
		}
		
		setMeasuredDimension(maxWidth, maxHeight);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		final int paddingTop = getPaddingTop();

		int childTop = mOffsetTop;
		int childLeft = 0;
		mItemCount = getChildCount();
		int height = getBottom() - getTop();
		int childBottom = height - getPaddingBottom();

		for (int i = 0; i < mItemCount; i++) {
			int childIndex = i;
			View child = getChildAt(childIndex);
			if(child.getVisibility() == View.GONE)
				continue;
			ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child
			.getLayoutParams();

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight() + lp.bottomMargin;

			childTop += (lp.topMargin);
			setChildFrame(child, childLeft, childTop, childWidth, childHeight);
			childTop += (childHeight);
		}
	}

	private void setChildFrame(View child, int left, int top, int width,
			int height) {
		child.layout(left, top, left + width, top + height);
	}

	void measureChildBeforeLayout(View child, int childIndex,
			int widthMeasureSpec, int totalWidth, int heightMeasureSpec,
			int totalHeight) {
		measureChildWithMargins(child, widthMeasureSpec, totalWidth,
				heightMeasureSpec, totalHeight);
	}

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Let the focused view and/or our descendants get the key first
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    
	boolean mReceivedInvokeKeyDown;

	public boolean executeKeyEvent(KeyEvent event) {
		boolean handled = false;
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			switch (event.getKeyCode()) {
	
			case KeyEvent.KEYCODE_DPAD_UP:
				
				if (movePrevious()) {
					setChildSelections();
					handled = true;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if (moveNext()) {
					setChildSelections();
					handled = true;
				}
				break;
			}
		}
		return handled;
	}

	boolean movePrevious() {
		if (mItemCount > 0 && mSelectedIndex > 0) {
			mPreviousSelectedIndex = mSelectedIndex;
			mSelectedIndex--;
			pendingList.add(new Point(mPreviousSelectedIndex, mSelectedIndex));
			animationDuration = ANIMATION_DURATION_SLIDE_UPDOWN;
			triggerSlide();
			return true;
		}
		return false;
	}

	boolean moveNext() {
		if (mItemCount > 0 && mSelectedIndex < mItemCount - 1) {
			mPreviousSelectedIndex = mSelectedIndex;
			mSelectedIndex++;
			pendingList.add(new Point(mPreviousSelectedIndex, mSelectedIndex));
			ViewGroup child = (ViewGroup) getChildAt(mSelectedIndex);
			// this is only required if children make noise while animating
			animationDuration = 250 * child.getChildCount() + 250;
			triggerSlide();
			return true; 
		} 
		return false;
	}

	private void notifySelectionChanged(int index, boolean selected){
		if(this.changeListener != null){
			View child = getChildAt(index);
			changeListener.onSelectionChanged(itemList, index, child, selected);
		}
	}
	
	private void notifyFocusChanged(int index, boolean hasFocus){
		if(this.changeListener != null){
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

	OnGlobalLayoutListener gll;

	private synchronized void triggerSlide() {
		Point p = pendingList.poll();
		if(p == null)
			return;

		final int prevIndex = p.x;
		final int nextIndex = p.y;

		final View previousSelectedChild = getChildAt(prevIndex);
		final View nextChild = getChildAt(nextIndex);

		if(va != null && va.isRunning()) {
			va.cancel();
		}
		slide(nextChild);
		if (prevIndex < nextIndex) {
			ObjectAnimator anim2 = ObjectAnimator.ofFloat(previousSelectedChild,
					"alpha", 0.25f);
			anim2.setDuration(ANIMATION_DURATION_SLIDE_UPDOWN);
			anim2.setInterpolator(new AccelerateInterpolator());
			anim2.start();
		}else{
			ObjectAnimator anim3 = ObjectAnimator.ofFloat(nextChild,
					"alpha", 1.0f);
			anim3.setDuration(ANIMATION_DURATION_SLIDE_UPDOWN);
			anim3.setInterpolator(new AccelerateInterpolator());
			anim3.start();
		}

		previousSelectedChild.setSelected(false);
		nextChild.setSelected(true);
		notifySelectionChanged(prevIndex, false);
		notifySelectionChanged(nextIndex, true);
		nextChild.requestFocus();
		previousSelectedChild.postInvalidate();
		nextChild.postInvalidate();
	}

	ValueAnimator va;

	public synchronized boolean slide(final View selectedChild) {
		int distance = getScrollY() - selectedChild.getTop() + mOffsetTop;
		if(distance == 0)
			return false;

		// give more time and shorter steps
		if(mPreviousSelectedIndex < mSelectedIndex) {
			distance /= 2;
		}

		va = ValueAnimator.ofInt(0, distance);
		va.setDuration(animationDuration);
		va.setInterpolator(new AccelerateDecelerateInterpolator());
		va.addUpdateListener(new AnimatorUpdateListener() {
			int lastValue = 0;
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				final int amount = (Integer) animation.getAnimatedValue() - lastValue;
				scrollBy(0, -amount);
				lastValue = (Integer) animation.getAnimatedValue();
				//only required if child views make noise while animating
				int d = getScrollY() - selectedChild.getTop() + mOffsetTop;
				ViewGroup child = (ViewGroup) getChildAt(mSelectedIndex);
				int drag = child.getChildCount() * 10;
				if(d>=-drag && d <=drag) {
					va.cancel();

					// correct scroll position
					ValueAnimator tva = ValueAnimator.ofInt( 0, -d );
					tva.setDuration( 100 );
					tva.addUpdateListener( new AnimatorUpdateListener( ) {
						int lastValue = 0;
						@Override
						public void onAnimationUpdate( ValueAnimator animation ) {
							final int amount = (Integer) animation.getAnimatedValue() - lastValue;
							scrollBy(0, amount);
							lastValue = (Integer) animation.getAnimatedValue();
						}
					});
					tva.start();
				}
			}
		});

		AnimatorListener animationListener = new AnimatorListener() {
			
			@Override
			public void onAnimationCancel(Animator animation) {
				va.removeAllListeners();
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				va.removeAllUpdateListeners();
				onFinishedMovement();
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
			}
		};
		va.addListener(animationListener);
		va.start();

		return true;
	}

	private void onFinishedMovement() {
		notifySelectionChanged(mPreviousSelectedIndex, false);
		notifySelectionChanged(mSelectedIndex, true);
	}
}