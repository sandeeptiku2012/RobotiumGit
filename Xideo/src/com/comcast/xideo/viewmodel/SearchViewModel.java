package com.comcast.xideo.viewmodel;

import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class SearchViewModel {
	public static enum ViewType {
		POPULATITY_VIEW,
		CATEGORY_VIEW,
		TIMELINE_VIEW
	}

	public final BooleanObservable isLoading = new BooleanObservable( true );
	public final ArrayListObservable<ContentPanelViewModel> popularityContentRows = new ArrayListObservable<ContentPanelViewModel>( ContentPanelViewModel.class );
	public final ArrayListObservable<ContentPanelViewModel> categoryContentRows = new ArrayListObservable<ContentPanelViewModel>( ContentPanelViewModel.class );
	public final ArrayListObservable<ContentPanelViewModel> timelineContentRows = new ArrayListObservable<ContentPanelViewModel>( ContentPanelViewModel.class );

	@Inject
	private Injector injector;

	public SearchViewModel() {
	}

	public void addContentPanel( ContentPanelViewModel panel, ViewType type ) {
		switch (type) {
			case CATEGORY_VIEW:
				categoryContentRows.add( panel );
				break;
			case POPULATITY_VIEW:
				popularityContentRows.add( panel );
				break;

			case TIMELINE_VIEW:
				timelineContentRows.add( panel );
				break;
		}
	}

	public void removeAllPanels( ViewType type ) {
		switch (type) {
			case CATEGORY_VIEW:
				categoryContentRows.clear();
				break;
			case POPULATITY_VIEW:
				popularityContentRows.clear();
				break;

			case TIMELINE_VIEW:
				timelineContentRows.clear();
				break;
		}
	}
}