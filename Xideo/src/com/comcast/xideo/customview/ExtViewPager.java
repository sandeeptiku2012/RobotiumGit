package com.comcast.xideo.customview;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class ExtViewPager extends ViewPager {

	public ExtViewPager(Context context) {
		super(context);
		init();
	}

	public ExtViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	@Override
	public boolean executeKeyEvent(KeyEvent event) {
		return false;
	}
	
	private ScrollerCustomDuration mScroller = null;

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private void init() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ScrollerCustomDuration(getContext(), new DecelerateInterpolator(1.2f));
            scroller.set(this, mScroller);
        } catch (Exception e) {
        }
        
        this.setPageTransformer(false, new FadeInTransformer() );
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }
    
    private static class FadeInTransformer implements PageTransformer{

		@Override
		public void transformPage(View view, float position) {
			
			if(position > 1 || position < -1){
				return;
			}
			view.setTranslationX(view.getWidth() * position * -3/4);
			float alpha = 1f - Math.abs(position);
			view.setAlpha(alpha);
		}
    	
    }
}
