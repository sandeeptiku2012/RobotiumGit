package com.comcast.xideo.viewmodel;

import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import no.knowit.misc.GoldenAsyncTask;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.vo.category.RestCategory;
import no.sumo.api.vo.category.RestCategoryList;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vimond.service.StbCategoryService;

public class DetailsActivityViewModel {
	public final Observable<DetailsHeaderViewModel> headerDetails = new Observable<DetailsHeaderViewModel>( DetailsHeaderViewModel.class );
	public final ArrayListObservable<ShowViewModel> shows = new ArrayListObservable<ShowViewModel>( ShowViewModel.class );
	public final BooleanObservable isLoading = new BooleanObservable( false );

	private long categoryId;

	@Inject
	StbCategoryService categoryService;

	@Inject
	private Injector injector;

	@Inject
	public DetailsActivityViewModel(DetailsHeaderViewModel model) {
		headerDetails.set( model );
		model.setButtonTextDefaults();
	}

	public void setCategoryId( long id ) {
		categoryId = id;
		start();
	}

	public void start() {
		isLoading.set( true );
		headerDetails.get().setCategoryId( categoryId );
		syncChannelInfo( categoryId );
	}

	public void syncChannelInfo( final long id ) {
		new GoldenAsyncTask<RestCategoryList>() {
			protected void onPreExecute() throws Exception {
			};

			@Override
			public RestCategoryList call() {
				RestCategory category = new RestCategory();
				category.setId( id );

				try {
					return categoryService.getSubCategoriesAsRestCategoryList( category );
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onSuccess( final RestCategoryList result ) throws Exception {
				if (result != null && result.getCategories().size() > 0) {
					for (RestCategory show : result.getCategories()) {
						ShowViewModel model = new ShowViewModel();
						injector.injectMembers( model );
						model.showId.set( show.getId() );
						model.showTitle.set( show.getTitle());
						model.description.set( show.getMetadata().get( "description-short" ) );
						model.start();
						shows.add( model );
					}
				} else {
					// dummy content
					dummyContent();
				}
				isLoading.set( false );
			}

			protected void onException( Exception e ) {
				e.printStackTrace();
			};

			protected void onFinally() throws RuntimeException {
			};
		}.execute();
	}

	private void dummyContent() {
		for (int i = 1; i <= 5; i++) {
			ShowViewModel model = new ShowViewModel();
			injector.injectMembers( model );
			injector.injectMembers( model );
			model.showId.set( (long) i );
			model.showTitle.set( "Dummy title " + i );
			model.description.set( "Dummy short description .... dummy short description " + i );
			model.start();
			shows.add( model );
		}
	}
}
