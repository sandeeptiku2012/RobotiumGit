package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import no.sumo.api.entity.sumo.enums.LevelType;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.category.RestCategoryIndex;
import no.sumo.api.vo.platform.RestPlatform;

@Consumes( MediaType.WILDCARD )
@Path( "/{platform}/categories" )
public interface ICategoriesService {

	/**
	 * Gets an A-Z index of the sub categories (recursive) of this category.
	 *
	 * @param platform
	 * @param level
	 * @return
	 * @throws NotFoundException
	 */
	@GET
	@Formatted
	@Path( "/{level}" )
	public RestCategoryIndex getCategoriesOfLevelType( @PathParam( "platform" ) RestPlatform platform, @PathParam( "level" ) @DefaultValue( "show" ) LevelType levelType ) throws NotFoundException;

	/**
	 * Gets an A-Z index of the sub categories (recursive) of this category.
	 *
	 * @param platform
	 * @param level
	 * @return
	 * @throws NotFoundException
	 */
	@GET
	@Formatted
	@Path( "/{defaultCategory}/{level}" )
	public RestCategoryIndex getDefaultCategoriesOfLevelType( @PathParam( "platform" ) RestPlatform platform, @PathParam( "defaultCategory" ) Long defaultTree, @PathParam( "level" ) @DefaultValue( "show" ) LevelType levelType ) throws NotFoundException;
}
