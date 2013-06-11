package com.comcast.xideo.viewmodel;

import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vimond.entity.CategoryReference;

@Singleton
public class XideoModel {
	@Inject
	public XideoCategoriesModel categoryModel;

	@Inject
	public XideoUserContentModel userContentModel;

	public final BooleanObservable isLoading = new BooleanObservable();
	private CategoryReference rootCategory = new Category( 100 );
	private IntegerObservable callCount = new IntegerObservable( 0 );

	@Inject
	public XideoModel() {
	}

	public void sync() {
		isLoading.set( true );
		downloadModelData( rootCategory );
	}

	Observer observerCategoryRecursive = new Observer() {
		@Override
		public void onPropertyChanged( IObservable<?> arg0, Collection<Object> arg1 ) {
			if (categoryModel.isLoading.get() == false) {
				callCount.set( callCount.get() - 1 );
				categoryModel.isLoading.unsubscribe( observerCategoryRecursive );
			}
		}
	};

	Observer observerUserContent = new Observer() {
		@Override
		public void onPropertyChanged( IObservable<?> arg0, Collection<Object> arg1 ) {
			if (userContentModel.isLoading.get() == false) {
				callCount.set( callCount.get() - 1 );
				userContentModel.isLoading.unsubscribe( this );
			}
		}
	};

	Observer callCountObserver = new Observer() {
		@Override
		public void onPropertyChanged( IObservable<?> arg0, Collection<Object> arg1 ) {
			if (callCount.get() == 0) {
				isLoading.set( false );
				callCount.unsubscribe( this );
			} else {
				isLoading.set( true );
			}
		}
	};

	private void downloadModelData( final CategoryReference categoryRef ) {
		categoryModel.isLoading.unsubscribe( observerCategoryRecursive );
		userContentModel.isLoading.unsubscribe( observerUserContent );
		callCount.unsubscribe( callCountObserver );

		categoryModel.isLoading.subscribe( observerCategoryRecursive );
		userContentModel.isLoading.subscribe( observerUserContent );
		callCount.subscribe( callCountObserver );
		callCount.set( 2 );
		categoryModel.sync();
		userContentModel.sync();
	}
}
