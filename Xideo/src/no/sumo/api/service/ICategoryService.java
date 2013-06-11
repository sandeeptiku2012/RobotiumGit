package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import no.sumo.api.entity.vman.enums.ProgramLiveStatus;
import no.sumo.api.entity.vman.enums.ProgramSortBy;
import no.sumo.api.exception.SectionNotFoundException;
import no.sumo.api.exception.asset.InvalidAssetCategoryException;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.exception.base.ValidationException;
import no.sumo.api.exception.category.CategoryNotFoundException;
import no.sumo.api.exception.category.rating.CategoryRatingFoundException;
import no.sumo.api.exception.category.rating.CategoryRatingNotFoundException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.exception.user.UserNotFoundException;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.service.vo.CategorySort;
import no.sumo.api.vo.article.RestArticleList;
import no.sumo.api.vo.asset.RestAssetList;
import no.sumo.api.vo.category.RestCategory;
import no.sumo.api.vo.category.RestCategoryList;
import no.sumo.api.vo.category.rating.RestCategoryRating;
import no.sumo.api.vo.category.rating.RestCategoryRatingList;
import no.sumo.api.vo.category.rating.RestCategoryRatingStatusList;
import no.sumo.api.vo.platform.RestPlatform;
import no.sumo.api.vo.product.RestProductGroupList;

import org.jboss.resteasy.annotations.cache.Cache;

@Consumes({MediaType.TEXT_XML,MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.WILDCARD})
@Path( "/{platform}/category" )
public interface ICategoryService {

	/**
	 * Returns the category object with the specified categoryId.
	 *
	 * @param platform
	 *            String representation of platform name.
	 * @param categoryId
	 * @return RestCategory object
	 * @throws NotFoundException
	 *             if no category with the specified Id can be found.
	 */
	@GET
	@Formatted
	@Path( "/{categoryId}" )
	@Cache( isPrivate = true, maxAge = 15 )
	public RestCategory getCategory( @PathParam( "platform" ) RestPlatform platform, @PathParam( "categoryId" ) Long categoryId ) throws NotFoundException;

	/**
	 * Returns the category specified in
	 * {@link ISiteContext#getHomeCategoryId()}
	 */
	@GET
	@Formatted
	@Path( "/root" )
	@Cache( isPrivate = true, maxAge = 15 )
	public RestCategory getRootCategory( @PathParam( "platform" ) RestPlatform platform ) throws NotFoundException;

	@GET
	@Formatted
	@Path( "/{categoryId}/recursive/categories+assets/{presentation}" )
	@Cache( isPrivate = true, maxAge = 15 )
	public RestCategoryList getSubCategoriesAndAssetsRecursive( @PathParam("platform") RestPlatform platform, @PathParam("categoryId") Long categoryId,
			@QueryParam("sort") @DefaultValue("priority+desc") ProgramSortBy sortBy,
			@QueryParam("liveStatus") @DefaultValue("all") ProgramLiveStatus liveStatus, @QueryParam("start") @DefaultValue("0") int firstPosition,
			@QueryParam("length") @DefaultValue("25") int length, @QueryParam("countAssetsAndViews") @DefaultValue("false") Boolean countAssetsAndViews,
			@PathParam("presentation") @DefaultValue("flat") String style ) throws CategoryNotFoundException;

	@GET
	@Formatted
	@Path( "/{categoryId}/recursive/categories/{presentation}" )
	@Cache( isPrivate = true, maxAge = 15 )
	public RestCategoryList getSubCategoriesRecursive( @PathParam("platform") RestPlatform platform, @PathParam("categoryId") Long categoryId,
			@QueryParam("sort") @DefaultValue("priority+desc") CategorySort.Field sortBy,
			@PathParam("presentation") @DefaultValue("flat") String style, @QueryParam("expand") @DefaultValue("") String expand )
			throws CategoryNotFoundException;

	@GET
	@Formatted
	@Path( "/{categoryId}/categories" )
	@Cache( isPrivate = true, maxAge = 15 )
	public RestCategoryList getSubCategories( @PathParam("platform") RestPlatform platform, @PathParam("categoryId") Long categoryId,
			@QueryParam("sort") @DefaultValue("priority+desc") CategorySort.Field sortBy, @QueryParam("start") @DefaultValue("0") int firstPosition,
			@QueryParam("length") @DefaultValue("25") int length, @QueryParam("expand") @DefaultValue("") String expand ) throws NotFoundException;

	/**
	 * Retrieve a CMS section based on the Category's sectionId. Currently only
	 * forwards the same xml we get from the CMS, since this is the agreed
	 * format.
	 *
	 * @param platform
	 * @param categoryId
	 * @return
	 * @throws SectionNotFoundException
	 * @throws NotFoundException
	 * @throws NotFoundException
	 * @throws Exception
	 */
	@GET
	@Formatted
	@Path( "/{categoryId}/articles" )
	public RestArticleList getArticles( @PathParam( "platform" ) RestPlatform platform, @PathParam( "categoryId" ) Long categoryId ) throws SectionNotFoundException, NotFoundException;

	/**
	 * Retrieve a CMS section based on the Category's sectionId. Currently only
	 * forwards the same xml we get from the CMS, since this is the agreed
	 * format.
	 *
	 * @param platform
	 * @param categoryId
	 * @return
	 * @throws NotFoundException
	 * @throws Exception
	 */
	@GET
	@Formatted
	@Path( "/{categoryId}/section" )
	public String getSection( @PathParam( "platform" ) RestPlatform platform, @PathParam( "categoryId" ) Long categoryId ) throws NotFoundException, Exception;

	@GET
	@Formatted
	@Path( "/{categoryId}/assets" )
	@Cache( isPrivate = true, maxAge = 15 )
	public RestAssetList getAssets( @PathParam("platform") RestPlatform platform, @PathParam("categoryId") Long categoryId,
			@QueryParam("sort") @DefaultValue("date+desc") ProgramSortBy sortBy, @QueryParam("liveStatus") @DefaultValue("all") ProgramLiveStatus liveStatus,
			@QueryParam("start") @DefaultValue("0") int firstPosition, @QueryParam("length") @DefaultValue("25") int length,
			@QueryParam("minVoteCount") @DefaultValue("10") int minVoteCount, @Context Response response ) throws NotFoundException;

	@GET
	@Formatted
	@Path( "/{categoryId}/recursive/assets/" )
	@Cache( isPrivate = true, maxAge = 15 )
	public RestAssetList getAssetsRecursive( @PathParam("platform") RestPlatform platform, @PathParam("categoryId") Long categoryId,
			@QueryParam("sort") @DefaultValue("date+desc") ProgramSortBy sortBy, @QueryParam("liveStatus") @DefaultValue("all") ProgramLiveStatus liveStatus,
			@QueryParam("start") @DefaultValue("0") int firstPosition, @QueryParam("length") @DefaultValue("25") int length,
			@QueryParam("minVoteCount") @DefaultValue("10") int minVoteCount ) throws CategoryNotFoundException, InvalidAssetCategoryException;

	@GET
	@Formatted
	@Path( "/{categoryId}/assets/live" )
	@Cache( isPrivate = true, maxAge = 15 )
	public RestAssetList getLiveAssets( @PathParam("platform") RestPlatform platform, @PathParam("categoryId") Long categoryId,
			@QueryParam("sort") @DefaultValue("date+desc") ProgramSortBy sortBy, @QueryParam("liveStatus") @DefaultValue("all") ProgramLiveStatus liveStatus,
			@QueryParam("start") @DefaultValue("0") int firstPosition, @QueryParam("length") @DefaultValue("25") int length,
			@QueryParam("minVoteCount") @DefaultValue("10") int minVoteCount, @Context Response response ) throws NotFoundException;

	@GET
	@Formatted
	@Path( "/{categoryId}/assets/od" )
	@Cache( isPrivate = true, maxAge = 15 )
	public RestAssetList getOndemandAssets( @PathParam("platform") RestPlatform platform, @PathParam("categoryId") Long categoryId,
			@QueryParam("sort") @DefaultValue("date+desc") ProgramSortBy sortBy, @QueryParam("liveStatus") @DefaultValue("all") ProgramLiveStatus liveStatus,
			@QueryParam("start") @DefaultValue("0") int firstPosition, @QueryParam("length") @DefaultValue("25") int length,
			@QueryParam("minVoteCount") @DefaultValue("10") int minVoteCount, @Context Response response ) throws NotFoundException;

	@GET
	@Path("/{categoryId}/productgroups")
	@Cache(isPrivate = true, maxAge = 15)
	public RestProductGroupList getProductGroups(
		@PathParam("platform") RestPlatform platform,
		@PathParam("categoryId") Long categoryId);

	@GET
	@Path("/{categoryId}/ratings")
	public RestCategoryRatingList getRatings(
		@PathParam("platform") RestPlatform platform,
		@PathParam("categoryId") Long categoryId) throws CategoryNotFoundException;

	@GET
	@Formatted
	@Path("/{categoryId}/rating/{ratingId}")
	public RestCategoryRating getRating(
		@PathParam("platform") RestPlatform platform,
		@PathParam("categoryId") Long categoryId,
		@PathParam("ratingId") Long ratingId) throws CategoryRatingNotFoundException, CategoryNotFoundException;

	@POST
	@Formatted
	@Path("/{categoryId}/rating/")
	public RestCategoryRating postRating(
		@PathParam("platform") RestPlatform platform,
		@PathParam("categoryId") Long categoryId,
		RestCategoryRating rating) throws CategoryRatingFoundException, CategoryNotFoundException, UserNotFoundException;

	@DELETE
	@Formatted
	@Path("/{categoryId}/rating/{ratingId}")
	public void deleteRating(
		@PathParam("platform") RestPlatform platform,
		@PathParam("categoryId") Long categoryId,
		@PathParam("ratingId") Long ratingId) throws CategoryNotFoundException, CategoryRatingNotFoundException;
	@GET
	@Formatted
	@Path("{categoryId}/ratingForUser/{userId}")
	public RestCategoryRating getRatingByUser(@PathParam("platform") RestPlatform platform, @PathParam("categoryId") Long categoryId, @PathParam("userId") Long UserId);

	@GET
	@Path("/{categoryId}/ratingsCount")
	public RestCategoryRatingStatusList getRatingsCount(
		@PathParam("platform") RestPlatform platform,
		@PathParam("categoryId") Long categoryId) throws CategoryNotFoundException;

	@POST
	@Formatted
	@Path("/")
	public RestCategory postCategory(@PathParam("platform") RestPlatform platform,
		RestCategory restCategory ) throws ValidationException, MethodNotAllowedException, NotFoundException;

}
