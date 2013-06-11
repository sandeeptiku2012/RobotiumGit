package com.comcast.xideo.customview;

import gueei.binding.collections.ObservableCollection;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RemoteViews.RemoteView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.comcast.ui.libv1.widget.BindableChannelVideoStripLayout;
import com.comcast.xideo.ItemSelectedEvent;
import com.comcast.xideo.R;
import com.comcast.xideo.XideoApplication;
import com.comcast.xideo.events.VideoItemEvent;
import com.comcast.xideo.viewmodel.VideoInfoViewModel;

@RemoteView
public class BindableChannelVideoStripLayoutEx extends BindableChannelVideoStripLayout implements BindableChannelVideoStripLayout.OnSelectionChangeListener {

	public BindableChannelVideoStripLayoutEx(Context context) {
		this(context, null);
	}

	public BindableChannelVideoStripLayoutEx(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("NewApi")
	public BindableChannelVideoStripLayoutEx(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		setOnSelectionChangeListener(this);
//		LayoutTransition lt = new LayoutTransition();
//		if (Build.VERSION.SDK_INT >= 16) {
//			lt.enableTransitionType(LayoutTransition.CHANGING);
//			Animator animator = AnimatorInflater.loadAnimator(context, R.anim.slide_bottom_to_top);
//			lt.setAnimator(LayoutTransition.CHANGING, animator);
//        }
//		setLayoutTransition(lt);
	}

	@Override
	public void onSelectionChanged(ObservableCollection<Object> itemList,
			int index, final View view, final boolean selected) {
		
		Activity m = (Activity) getContext();
		m.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				View v1, v2, v3, v4, v5;
				v1 = view.findViewById(R.id.text2);
				v2 = view.findViewById(R.id.text3);
				v3 = view.findViewById(R.id.text4);
				v4 = view.findViewById(R.id.text5);
				v5 = view.findViewById(R.id.video_status_image);

				ViewSwitcher vs = (ViewSwitcher) view.findViewById(R.id.viewSwitcher1);
				
				if (selected) {
					int top = view.getTop();
					if (v1 != null)
						v1.setVisibility(View.VISIBLE);
					if (v2 != null)
						v2.setVisibility(View.VISIBLE);
					if (v3 != null)
						v3.setVisibility(View.VISIBLE);
					if (v4 != null)
						v4.setVisibility(View.VISIBLE);
					if (v5 != null)
						v5.setVisibility(View.VISIBLE);
					if (vs != null && vs.getCurrentView().getId() == R.id.image2)
						vs.showNext();

					ObjectAnimator anim = ObjectAnimator.ofFloat(view,
							"translationY", top, 0);
					anim.setDuration(100);
					anim.setInterpolator(new AccelerateDecelerateInterpolator());
					// anim.start();
				} else {
					if (v1 != null)
						v1.setVisibility(View.GONE);
					if (v2 != null)
						v2.setVisibility(View.GONE);
					if (v3 != null)
						v3.setVisibility(View.GONE);
					if (v4 != null)
						v4.setVisibility(View.GONE);
					if (v5 != null)
						v5.setVisibility(View.GONE);

					if (vs != null && vs.getCurrentView().getId() == R.id.image3)
						vs.showPrevious();
				}
				if (selected) {
					ViewFlipper vf = (ViewFlipper) view.findViewById(R.id.viewflipper1);
					View iv = vf.getCurrentView().findViewById(R.id.image1);
					XideoApplication.getBus().post(new ItemSelectedEvent(this, iv));
				}
			}
		});
	}

	@Override
	public void onItemSelected(Object obj, int index, View child) {

		VideoItemEvent evt = null;
		if (obj instanceof VideoInfoViewModel) {
			evt = new VideoItemEvent(this, (VideoInfoViewModel) obj);
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
	public void onFocusChanged(View view, boolean hasFocus) {
		ViewFlipper vf = (ViewFlipper) view.findViewById(R.id.viewflipper1);
		if (hasFocus) {
			if (vf.getCurrentView().getId() != R.id.view2) {
				vf.showNext();
			}
		} else {
			if (vf.getCurrentView().getId() != R.id.view1) {
				vf.showPrevious();
			}

			needScrollCorrection = true;
		}
	}
}