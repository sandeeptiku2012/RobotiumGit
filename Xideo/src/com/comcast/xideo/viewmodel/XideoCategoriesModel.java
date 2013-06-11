package com.comcast.xideo.viewmodel;

import gueei.binding.observables.BooleanObservable;

import java.util.Collections;
import java.util.Comparator;

import no.knowit.misc.GoldenAsyncTask;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.service.vo.PresentationStyle;
import no.sumo.api.vo.category.RestCategory;
import no.sumo.api.vo.category.RestCategoryList;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vimond.entity.CategoryReference;
import com.vimond.service.StbCategoryService;

@Singleton
public class XideoCategoriesModel {
	@Inject
	private StbCategoryService categoryService;

	public final BooleanObservable isLoading = new BooleanObservable();

	private CategoryReference rootCategory = new Category( 100 );

	private RestCategoryList allCategories;

	public enum CategoryLevelType {
		GENRE,
		SUB_GENRE,
		CHANNEL,
		SHOWS,
		SEASONS,
		VIDEO,
		BUNDLE,
		UNKNOWN
	}

	@Inject
	public XideoCategoriesModel(StbCategoryService cs) {
		categoryService = cs;
	}

	public void sync() {
		downloadAllCategoriesRecursive( rootCategory );
	}

	private void downloadAllCategoriesRecursive( final CategoryReference categoryRef ) {
		isLoading.set( true );
		new GoldenAsyncTask<RestCategoryList>() {
			protected void onPreExecute() throws Exception {
			};

			@Override
			public RestCategoryList call() {
				try {
					RestCategory restcategory = new RestCategory();
					restcategory.setId( categoryRef.getId() );
					return categoryService.getSubCategoriesRecursiveAsRestCategoryList( restcategory, PresentationStyle.FLAT, "metadata" );
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onSuccess( final RestCategoryList result ) throws Exception {
				allCategories = result;
				Collections.sort( allCategories.getCategories(), CategoryIdComparator );
			}

			protected void onException( Exception e ) {
				e.printStackTrace();
			};

			protected void onFinally() throws RuntimeException {
				isLoading.set( false );
			};
		}.execute();
	}

	public RestCategory getCategory( long id ) {
		RestCategory cat = new RestCategory();
		cat.setId( id );
		int index = Collections.binarySearch( allCategories.getCategories(), cat, CategoryIdComparator );
		if (index >= 0) {
			return allCategories.getCategories().get( index );
		}
		return null;
	}

	public CategoryLevelType getCategoryLevelType( long id ) {
		RestCategory cat = new RestCategory();
		cat.setId( id );
		int index = Collections.binarySearch( allCategories.getCategories(), cat, CategoryIdComparator );
		if (index >= 0) {
			RestCategory ref = allCategories.getCategories().get( index );
			if (ref.getId() == id) {
				if (ref.getIsGenre())
					return CategoryLevelType.GENRE;

				if (ref.getIsSubGenre())
					return CategoryLevelType.SUB_GENRE;

				if (ref.getIsChannel())
					return CategoryLevelType.CHANNEL;
			}
		}

		return CategoryLevelType.UNKNOWN;
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

	public CategoryLevelType getCategoryContentType( long id ) {
		RestCategory cat = new RestCategory();
		cat.setId( id );
		int index = Collections.binarySearch( allCategories.getCategories(), cat, CategoryIdComparator );
		if (index >= 0) {
			cat = allCategories.getCategories().get( index );
			return getCategorylevelType( cat.getLevel() );
		}
		return CategoryLevelType.UNKNOWN;
	}

	public CategoryLevelType getCategorylevelType( String level ) {
		if ("MAIN_CATEGORY".equals( level ))
			return CategoryLevelType.GENRE;

		if ("CATEGORY".equals( level ))
			return CategoryLevelType.SUB_GENRE;

		if ("SHOW".equals( level ))
			return CategoryLevelType.CHANNEL;

		if ("SUB_SHOW".equals( level ))
			return CategoryLevelType.SHOWS;

		if ("BUNDLE".equals( level ))
			return CategoryLevelType.BUNDLE;

		if ("VIDEO".equals( level ))
			return CategoryLevelType.VIDEO;

		return CategoryLevelType.UNKNOWN;
	}

	// set the correct level by looking up in cached categories
	public void setCategoryContentType( RestCategory cat ) {
		RestCategory linkedCategory = new RestCategory();
		linkedCategory.setId( cat.getId() );
		int index = Collections.binarySearch( allCategories.getCategories(), linkedCategory, CategoryIdComparator );
		if (index >= 0) {
			RestCategory result = allCategories.getCategories().get( index );
			cat.setLevel( result.getLevel() );
			cat.setLevelTypeId( result.getLevelTypeId() );
		}
	}
}
