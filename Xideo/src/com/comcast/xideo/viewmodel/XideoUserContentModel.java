package com.comcast.xideo.viewmodel;

import gueei.binding.observables.BooleanObservable;

import java.util.Collections;
import java.util.Comparator;

import no.knowit.misc.GoldenAsyncTask;
import no.sumo.api.vo.category.RestCategory;
import no.sumo.api.vo.category.RestSearchCategory;
import no.sumo.api.vo.category.RestSearchCategoryList;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vimond.service.StbUserContentService;

@Singleton
public class XideoUserContentModel {
	@Inject
	private StbUserContentService categoryService;

	public final BooleanObservable isLoading = new BooleanObservable();

	private RestSearchCategoryList subscribedCategories;

	@Inject
	public XideoUserContentModel(StbUserContentService cs) {
		categoryService = cs;
	}

	public void sync() {
		downloadSubscribedCategories();
	}

	private void downloadSubscribedCategories() {
		isLoading.set( true );
		new GoldenAsyncTask<RestSearchCategoryList>() {
			protected void onPreExecute() throws Exception {
			};

			@Override
			public RestSearchCategoryList call() {
				return categoryService.getSubscribedChannels();
			}

			@Override
			protected void onSuccess( final RestSearchCategoryList result ) throws Exception {
				subscribedCategories = result;
				Collections.sort( subscribedCategories.getCategories(), CategoryIdComparator );
			}

			protected void onException( Exception e ) {
				e.printStackTrace();
			};

			protected void onFinally() throws RuntimeException {
				isLoading.set( false );
			};
		}.execute();
	}

	public RestSearchCategory getCategory( long id ) {
		RestCategory cat = new RestCategory();
		cat.setId( id );
		int index = Collections.binarySearch( subscribedCategories.getCategories(), cat, CategoryIdComparator );
		if (index >= 0) {
			return subscribedCategories.getCategories().get( index );
		}
		return null;
	}

	public static Comparator<RestCategory> CategoryIdComparator = new Comparator<RestCategory>() {
		@Override
		public int compare( RestCategory lhs, RestCategory rhs ) {
			if (lhs.getId() == rhs.getId())
				return 0;

			if (lhs.getId() > rhs.getId())
				return 1;

			return -1;
		}
	};

	public boolean getIsSubscribed( long id ) {
		return getCategory( id ) != null;
	}
}
