package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.service.annotations.Wrapped;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.favorite.RestFavorite;
import no.sumo.api.vo.favorite.RestFavoriteList;
import no.sumo.api.vo.platform.RestPlatform;

@Consumes( MediaType.WILDCARD )
@Path( "/" )
public interface IFavoriteService {

	@GET
	@Formatted
	@Wrapped( element = "favorites" )
	@Path( "/{platform}/user/favorites" )
	public RestFavoriteList getUserFavorites( @PathParam( "platform" ) RestPlatform platform ) throws MethodNotAllowedException;

	@POST
	@Formatted
	@Path( "/{platform}/user/favorites" )
	public RestFavorite postFavorite( RestFavorite _restFavoite, @PathParam( "platform" ) RestPlatform platform ) throws MethodNotAllowedException, NotFoundException;

	@DELETE
	@Formatted
	@Path( "/{platform}/user/favorites/{categoryId}" )
	public void removeFavorite( @PathParam( "categoryId" ) Long categoryId, @PathParam( "platform" ) RestPlatform platform ) throws MethodNotAllowedException, NotFoundException;

}