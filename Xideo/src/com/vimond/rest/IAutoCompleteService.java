package com.vimond.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.platform.RestPlatform;

import org.jboss.resteasy.annotations.cache.Cache;

@Consumes( MediaType.WILDCARD )
@Path( "/{platform}/search" )
public interface IAutoCompleteService {
	
	//view-source:http://api.projecthelen.net/api/web/search/suggestions?text=t
	
	@GET
	@Formatted
	@Path( "/suggestions" )
	@Cache( isPrivate = true, maxAge = 15 )
	public RestStringList getSuggestions( @PathParam( "platform" ) RestPlatform platform, @QueryParam( "text" ) String text ) throws NotFoundException;
}