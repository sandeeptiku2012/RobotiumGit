package com.comcast.xideo.viewmodel;

import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;

public class HomeViewModel {
	public final BooleanObservable isLoading = new BooleanObservable( true );
	public final ArrayListObservable<ContentPanelViewModel> contentRows = new ArrayListObservable<ContentPanelViewModel>( ContentPanelViewModel.class );

	public HomeViewModel() {
	}

	public void start() {
	}

	public void resume() {
	}
		
}