package com.comcast.xideo.activity;

import gueei.binding.Binder;
import gueei.binding.Binder.InflateResult;
import roboguice.fragment.RoboFragment;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends RoboFragment {

	protected View mRootView;

	protected View setAndBindRootView( int layoutId, ViewGroup container, Object... contentViewModel ) {
		if( mRootView == null ) {
//			Log.e("BaseFragment", "rootview created", new Throwable());
			InflateResult result = Binder.inflateView( getActivity(), layoutId, container, false );
			mRootView = result.rootView;
			if(contentViewModel != null){
				for( Object element : contentViewModel ) {
					Binder.bindView( getActivity(), result, element );
				}
			}
		}
		return mRootView;
	}

}
