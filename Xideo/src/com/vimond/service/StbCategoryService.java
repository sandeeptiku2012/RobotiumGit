package com.vimond.service;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.entity.vman.enums.ProgramSortBy;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.service.ICategoryService;
import no.sumo.api.service.IContentPanelService;
import no.sumo.api.service.ISearchService;
import no.sumo.api.vo.category.RestCategory;
import no.sumo.api.vo.category.RestCategoryList;
import no.sumo.api.vo.category.RestSearchCategory;
import no.sumo.api.vo.category.RestSearchCategoryList;
import no.sumo.api.vo.contentpanel.RestContentPanel;
import no.sumo.api.vo.contentpanel.RestContentPanelElement;
import no.sumo.api.vo.platform.RestPlatform;
import roboguice.util.Ln;

import com.comcast.xideo.Constants;
import com.comcast.xideo.IChannel;
import com.comcast.xideo.viewmodel.Category;
import com.comcast.xideo.viewmodel.ContentPanelElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vimond.entity.CategoryReference;
import com.vimond.entity.ContentPanelType;
import com.vimond.entity.MainCategory;
import com.vimond.entity.Publisher;
import com.vimond.entity.SubCategory;
import com.vimond.rest.Converters;

@Singleton
public class StbCategoryService {
	@Inject
	public StbCategoryService( ICategoryService categoryService ) {
		this.categoryService = categoryService;
	}

	@Inject
	private ICategoryService categoryService;

	@Inject
	RestPlatform platform;

	@Inject
	private IContentPanelService contentPanelService;

	@Inject
	private ISearchService searchService;

	private RestCategory rootCategory;

	public List<MainCategory> getMainCategories() throws NotFoundException {
		List<MainCategory> result = new ArrayList<MainCategory>();
		RestCategory rootCategory = getRootCategory();

		if( rootCategory != null ) {
			Category category = new Category( rootCategory.getId() );
			category.setTitle( rootCategory.getTitle() );
			result.add( category );

			RestCategoryList subCategories = categoryService.getSubCategories( platform, rootCategory.getId(), null, 0, 25, "metadata" );
			if( subCategories != null ) {
				for( RestCategory c : subCategories.getCategories() ) {
					result.add( Converters.convert( c ) );
				}
			}
		}
		return result;
	}

	public List<SubCategory> getSubCategories( CategoryReference category ) throws NotFoundException {
		List<SubCategory> result = new ArrayList<SubCategory>();
		RestCategoryList subCategories = categoryService.getSubCategories( platform, category.getId(), null, 0, 25, "metadata" );
		if( subCategories != null ) {
			for( RestCategory c : subCategories.getCategories() ) {
				result.add( Converters.convert( c ) );
			}
		}
		return result;
	}

	public RestCategoryList getSubCategoriesAsRestCategoryList( RestCategory category ) throws NotFoundException {
		RestCategoryList subCategories = categoryService.getSubCategories( platform, category.getId(), null, 0, 25, "metadata" );
		return subCategories;
	}

	public RestCategoryList getSubCategoriesRecursiveAsRestCategoryList( RestCategory category, String presentationStyle, String expand )
			throws NotFoundException {
		RestCategoryList subCategories = categoryService.getSubCategoriesRecursive( platform, category.getId(), null, presentationStyle, expand );
		return subCategories;
	}

	public List<ContentPanelElement> getContentPanel( CategoryReference category, ContentPanelType type ) {
		List<ContentPanelElement> result = new ArrayList<ContentPanelElement>();
		String name = String.format( "%d_%s", category.getId(), getContentPanelTypeName( type ) );
		RestContentPanel panel = contentPanelService.getContentPanelByName( name );
		for( RestContentPanelElement element : panel.getElements() ) {
			ContentPanelElement item = Converters.convert( element );
			Long categoryId = Long.valueOf( item.getKey() );
			try {
				if( item.getType() == null ) {
					RestCategory c = categoryService.getCategory( platform, categoryId );
					item.setType( c.getLevel() );
				}
			} catch( NotFoundException e ) {
				Ln.e( e );
			}
			result.add( item );
		}
		return result;
	}

	public List<IChannel> getChannelsInContentPanel( CategoryReference category, ContentPanelType contentPanelType ) {
		List<IChannel> result = new ArrayList<IChannel>();
		String name = String.format( "%d_%s", category.getId(), getContentPanelTypeName( contentPanelType ) );
		RestContentPanel panel = contentPanelService.getExpandableContentPanelByName( platform, name, "content", null );
		for( RestContentPanelElement element : panel.getElements() ) {
			RestSearchCategory c = element.getCategory();
			if( c != null ) {
				IChannel item = Converters.convert( c );
				result.add( item );
			}
		}
		return result;
	}

	public RestContentPanel getEntriesInContentPanel( CategoryReference category, ContentPanelType contentPanelType ) {
		List<IChannel> result = new ArrayList<IChannel>();
		String name = String.format( "%d_%s", category.getId(), getContentPanelTypeName( contentPanelType ) );
		RestContentPanel panel = contentPanelService.getExpandableContentPanelByName( platform, name, "content", null );
		return panel;
	}

	private String getContentPanelTypeName( ContentPanelType type ) {
		switch( type ) {
			case FEATURED:
				return "featured";
			case RECOMMENDED:
				return "noteworthy";
			case POPULAR:
				return "popular";
		}
		throw new IllegalArgumentException( "Unrecognized content panel type, '" + type + "'" );
	}

	public List<IChannel> getChannels( CategoryReference categoryRef ) throws NotFoundException {
		return getChannels( categoryRef, (ProgramSortBy)null );
	}

	public List<IChannel> getChannels( CategoryReference categoryRef, String sort ) throws NotFoundException {
		ProgramSortBy sorting = null;
		try {
			sorting = ProgramSortBy.valueOf( sort );
		} catch( IllegalArgumentException e ) {
		}
		return getChannels( categoryRef, sorting );
	}

	public List<IChannel> getChannels( CategoryReference categoryRef, ProgramSortBy sorting ) throws NotFoundException {
		RestSearchCategoryList categories = searchService.getCategories( platform, Long.toString( categoryRef.getId() ), null, sorting, 0, 25, null, null );
		List<IChannel> result = new ArrayList<IChannel>();
		for( final RestSearchCategory category : categories.getCategories() ) {
			result.add( Converters.convert( category ) );
		}
		return result;
	}

	public Category getCategory( CategoryReference category ) throws NotFoundException {
		return Converters.convert( categoryService.getCategory( platform, category.getId() ) );
	}

	public boolean isRootCategory( CategoryReference category ) {
		if( category == null ) {
			return false;
		}
		return category.getId() == getRootCategoryId();
	}

	private long getRootCategoryId() {
		try {
			return getRootCategory().getId();
		} catch( Exception e ) {
			return Constants.MAIN_CATEGORY;
		}
	}

	private RestCategory getRootCategory() throws NotFoundException {
		if( rootCategory == null ) {
			rootCategory = categoryService.getRootCategory( platform );
		}
		return rootCategory;
	}

	public List<IChannel> getChannels( Publisher publisher, ProgramSortBy sorting ) throws NotFoundException {
		String query = String.format( "categoryMeta_publisher_text:\"%s\"", publisher.getTitle().replace( "\"", "" ) );
		RestSearchCategoryList searchCategoryList = searchService.getCategories( platform, Long.toString( getRootCategoryId() ), query, sorting, 0, 1000, null, null );
		List<IChannel> channels = new ArrayList<IChannel>();
		for( RestSearchCategory category : searchCategoryList.getCategories() ) {
			channels.add( Converters.convert( category ) );
		}
		return channels;
	}
}
