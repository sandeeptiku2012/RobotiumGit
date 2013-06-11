package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import no.sumo.api.entity.vman.enums.ProgramSortBy;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.asset.RestSearchAssetList;
import no.sumo.api.vo.category.RestSearchCategoryList;
import no.sumo.api.vo.platform.RestPlatform;

@Consumes(MediaType.WILDCARD)
@Path("/{platform}/user")
public interface IUserContentService {

	
	@GET
	@Formatted
	@Path("/{userId}/access/categories")
	public RestSearchCategoryList getSubscribedChannels(@PathParam("platform") RestPlatform platform, @PathParam("userId") Long userId,
			@QueryParam("sort") @DefaultValue("category") ProgramSortBy sortBy, @QueryParam("start") @DefaultValue("0") int firstPosition,
			@QueryParam("size") @DefaultValue("250") int size, @Context Response response) throws MethodNotAllowedException, NotFoundException;

	@GET
	@Formatted
	@Path("/{userId}/access/categories")
	public RestSearchCategoryList getAccessibleCategories (
			@PathParam("platform") RestPlatform platform,
			@PathParam("userId") Long userId, 
			@QueryParam("sort") @DefaultValue("category") ProgramSortBy sortBy,			
			@QueryParam("start") @DefaultValue("0") int firstPosition,
			@QueryParam("size") @DefaultValue("250") int size,
			@Context Response response) 
	throws MethodNotAllowedException, NotFoundException;
	
	@GET
	@Formatted
	@Path("/{userId}/access/assets")
	public RestSearchAssetList getAccessibleAssets (
			@PathParam("platform") RestPlatform platform, 
			@PathParam("userId") Long userId, 
			@QueryParam("query") String query,
			@QueryParam("sort") @DefaultValue("date+desc") ProgramSortBy sortBy,
			@QueryParam("start") @DefaultValue("0") int firstPosition, 
			@QueryParam("size") @DefaultValue("250")int size, 
			@Context Response response) throws NotFoundException, MethodNotAllowedException;
}
