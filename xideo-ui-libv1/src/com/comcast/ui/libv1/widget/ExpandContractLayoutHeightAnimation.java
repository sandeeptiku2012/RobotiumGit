package com.comcast.ui.libv1.widget;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ExpandContractLayoutHeightAnimation extends Animation {

	View view;
	int startHeight, offsetHeight;
	
	public ExpandContractLayoutHeightAnimation(View view, int startHeight, int offsetHeight){
		this.view = view;
		this.startHeight = startHeight;
		this.offsetHeight = offsetHeight;
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		int height = (int) ((float)this.offsetHeight * interpolatedTime);
		view.getLayoutParams().height = startHeight + height;

		if(interpolatedTime == 1.0f){
			view.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
		}
		view.requestLayout();
	}
	
	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
	}
	
	@Override
	public boolean willChangeBounds() {
		return true;
	}
}
