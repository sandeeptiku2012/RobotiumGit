package com.vimond.service;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.entity.vman.enums.ProgramSortBy;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.service.ISearchService;
import no.sumo.api.vo.category.RestCategory;
import no.sumo.api.vo.category.RestSearchCategory;
import no.sumo.api.vo.category.RestSearchCategoryList;
import no.sumo.api.vo.platform.RestPlatform;

import com.comcast.xideo.IChannel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vimond.entity.CategoryReference;
import com.vimond.rest.Converters;

@Singleton
public class StbSearchService {

	@Inject
	RestPlatform platform;

	@Inject
	private ISearchService searchService;

	private RestCategory rootCategory;

	public List<IChannel> getAllChannels( CategoryReference categoryRef, String searchText, ProgramSortBy sortBy ) throws NotFoundException {
		List<IChannel> channels = new ArrayList<IChannel>();
		RestSearchCategoryList categories = searchService
				.getCategories( platform, Long.toString( categoryRef.getId() ), null, sortBy, 0, 100, searchText, null );
		for (final RestSearchCategory category : categories.getCategories()) {
			channels.add( Converters.convert( category ) );
		}
		return channels;
	}

	public List<IChannel> getAllChannelsByTimeline( CategoryReference categoryRef, String query, String searchText, ProgramSortBy sortBy )
			throws NotFoundException {
		List<IChannel> channels = new ArrayList<IChannel>();
		RestSearchCategoryList categories = searchService
				.getCategories( platform, Long.toString( categoryRef.getId() ), query, sortBy, 0, 100, searchText, null );
		for (final RestSearchCategory category : categories.getCategories()) {
			channels.add( Converters.convert( category ) );
		}
		return channels;
	}
}
