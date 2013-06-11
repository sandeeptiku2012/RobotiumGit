package no.sumo.api.service;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.vo.category.RestCategory;
import no.sumo.api.vo.platform.RestPlatform;

import org.jboss.resteasy.annotations.cache.Cache;

@Path( "/{platform}/category" )
public interface IAutoCompleteService {
	@Path( "/suggestions" )
	@Cache( isPrivate = true, maxAge = 15 )
	public RestCategory getSuggestions( @PathParam( "platform" ) RestPlatform platform, @QueryParam( "text" ) String prefix ) throws NotFoundException;

}
