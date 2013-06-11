package com.vimond.service;

import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.service.IUserContentService;
import no.sumo.api.vo.category.RestSearchCategoryList;
import no.sumo.api.vo.platform.RestPlatform;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class StbUserContentService {
	@Inject
	public StbUserContentService(IUserContentService service) {
		this.userContentService = service;
	}

	@Inject
	private IUserContentService userContentService;

	@Inject
	RestPlatform platform;

	@Inject
	StbAuthenticationService authService;

	public RestSearchCategoryList getSubscribedChannels() {
		long userId = authService.getUser().getId();
		try {
			return userContentService.getAccessibleCategories( platform, userId, null, 0, 50, null );
		} catch (MethodNotAllowedException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}
