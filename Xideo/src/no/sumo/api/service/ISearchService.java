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
import no.sumo.api.service.annotations.Wrapped;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.StringList;
import no.sumo.api.vo.asset.RestSearchAssetList;
import no.sumo.api.vo.asset.item.RestAssetItemList;
import no.sumo.api.vo.category.RestSearchCategoryList;
import no.sumo.api.vo.platform.RestPlatform;

@Consumes( MediaType.WILDCARD )
@Path( "/{platform}/search" )
public interface ISearchService {

	@GET
	@Formatted
	@Path( "/{subCategory}" )
	@Wrapped( element = "assets" )
	public RestSearchAssetList getAssets( @PathParam( "platform" ) RestPlatform platform, @PathParam( "subCategory" ) String subCategory, @QueryParam( "query" ) String query, @QueryParam( "sort" ) @DefaultValue( "date+desc" ) ProgramSortBy sortBy, @QueryParam( "start" ) @DefaultValue( "0" ) int firstPosition, @QueryParam( "size" ) @DefaultValue( "25" ) int size, @QueryParam( "text" ) @DefaultValue( "" ) String text, @Context Response response ) throws NotFoundException;

	@GET
	@Formatted
	@Path( "/categories/{subCategory}" )
	public RestSearchCategoryList getCategories( @PathParam( "platform" ) RestPlatform platform, @PathParam( "subCategory" ) String subCategory, @QueryParam( "query" ) String query, @QueryParam( "sort" ) @DefaultValue( "category" ) ProgramSortBy sortBy, @QueryParam( "start" ) @DefaultValue( "0" ) int firstPosition, @QueryParam( "size" ) @DefaultValue( "25" ) int size, @QueryParam( "text" ) @DefaultValue( "" ) String text, @Context Response response ) throws NotFoundException;

	// API v2 --> list of shows from a channel
	@GET
	@Formatted
	@Path("/categories/{channelId}")
	public RestSearchCategoryList getShows(@PathParam("platform") RestPlatform platform, @PathParam("channelId") String channelId,
			@QueryParam("size") @DefaultValue("25") String size, @QueryParam("start") @DefaultValue("0") String start,
			@QueryParam("sort") @DefaultValue("asc") String sortOrder, @Context Response response) throws NotFoundException;

	// API v2 --> list of assets from a show
	@GET
	@Formatted
	@Path("/categories/{showId}/assets")
	public RestSearchAssetList getAssets( @PathParam("platform") RestPlatform platform, @PathParam("showId") String showId,
			@QueryParam("size") @DefaultValue("25") String size, @QueryParam("start") @DefaultValue("0") String start, @Context Response response)
			throws NotFoundException;

	@GET
	@Formatted
	@Path( "/categories/{subCategory}/assets" )
	@Wrapped( element = "assets" )
	public RestSearchAssetList getSubAssets( @PathParam( "platform" ) RestPlatform platform, @PathParam( "subCategory" ) String subCategory, @QueryParam( "query" ) String query, @QueryParam( "sort" ) @DefaultValue( "date+desc" ) ProgramSortBy sortBy, @QueryParam( "start" ) @DefaultValue( "0" ) int firstPosition, @QueryParam( "size" ) @DefaultValue( "25" ) int size, @QueryParam( "text" ) @DefaultValue( "" ) String text, @Context Response response ) throws NotFoundException;

	@GET
	@Formatted
	@Path( "/categories/{subCategory}/items" )
	public RestAssetItemList getItems( @PathParam( "platform" ) RestPlatform platform, @PathParam( "subCategory" ) String subCategory, @QueryParam( "query" ) String query, @QueryParam( "sort" ) @DefaultValue( "date+desc" ) ProgramSortBy sortBy, @QueryParam( "start" ) @DefaultValue( "0" ) int firstPosition, @QueryParam( "size" ) @DefaultValue( "25" ) int size, @QueryParam( "text" ) @DefaultValue( "" ) String text, @Context Response response ) throws NotFoundException;

    @GET
    @Formatted
    @Path("/suggestions")
    public StringList getSuggestions(
            @PathParam("platform") RestPlatform platform,
            @QueryParam("text") @DefaultValue("") String text,
            @QueryParam("size") @DefaultValue("25") int numberOfSuggestions,
            @Context Response response
    );
}
