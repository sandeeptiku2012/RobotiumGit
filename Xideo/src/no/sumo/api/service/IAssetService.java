package no.sumo.api.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import no.sumo.api.entity.vman.enums.ProgramLiveStatus;
import no.sumo.api.exception.InvalidGeoRegionException;
import no.sumo.api.exception.ThirdPartyErrorException;
import no.sumo.api.exception.asset.AssetNotFoundException;
import no.sumo.api.exception.asset.comment.AssetCommentNotFoundException;
import no.sumo.api.exception.asset.item.AssetItemNotFoundException;
import no.sumo.api.exception.asset.playback.InvalidLogDataException;
import no.sumo.api.exception.asset.playback.UnsupportedVideoFormatException;
import no.sumo.api.exception.asset.property.AssetPropertyNotFoundException;
import no.sumo.api.exception.asset.rating.AssetRatingNotFoundException;
import no.sumo.api.exception.asset.rating.PrematureRatingException;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.exception.category.CategoryNotFoundException;
import no.sumo.api.exception.generic.DataIncompleteException;
import no.sumo.api.exception.generic.DataValidationException;
import no.sumo.api.exception.generic.InvalidDateException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.exception.user.UserNotFoundException;
import no.sumo.api.service.annotations.EncryptedLogData;
import no.sumo.api.service.annotations.Wrapped;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.asset.RestAsset;
import no.sumo.api.vo.asset.RestAssetTypeList;
import no.sumo.api.vo.asset.RestPlatformPublishInfo;
import no.sumo.api.vo.asset.comment.RestAssetComment;
import no.sumo.api.vo.asset.comment.RestAssetCommentList;
import no.sumo.api.vo.asset.item.RestAssetItem;
import no.sumo.api.vo.asset.item.RestAssetItemList;
import no.sumo.api.vo.asset.playback.RestPlayProgress;
import no.sumo.api.vo.asset.playback.RestPlayback;
import no.sumo.api.vo.asset.property.RestAssetProperty;
import no.sumo.api.vo.asset.property.RestAssetPropertyList;
import no.sumo.api.vo.asset.rating.RestAssetRating;
import no.sumo.api.vo.asset.rating.RestAssetRatingList;
import no.sumo.api.vo.platform.RestPlatform;
import no.sumo.api.vo.platform.RestPlatformList;
import no.sumo.api.vo.product.RestProduct;
import no.sumo.api.vo.product.RestProductGroupList;

@Consumes( MediaType.WILDCARD )
@Path( "/{platform}/asset" )
public interface IAssetService {
	@GET
	@Formatted
	@Wrapped( element = "publishing" )
	@Path( "/{assetId}/publish" )
	public List<RestPlatformPublishInfo> getPublishingInfo( @PathParam( "platform" ) RestPlatformList platforms, @PathParam( "assetId" ) Long assetId ) throws AssetNotFoundException;

	@DELETE
	@Formatted
	@Wrapped( element = "publishing" )
	@Path( "/{assetId}/{operation:publish|unpublish}/" )
	public List<RestPlatformPublishInfo> deleteUnpublishAsset( @PathParam( "platform" ) RestPlatformList platforms, @PathParam( "assetId" ) Long assetId ) throws AssetNotFoundException;

	@PUT
	@Formatted
	@Wrapped( element = "publishing" )
	@Path( "/{assetId}/{operation:publish|unpublish}/{liveStatus}" )
	public List<RestPlatformPublishInfo> putPublishAsset( @PathParam( "platform" ) RestPlatformList platforms, @PathParam( "liveStatus" ) ProgramLiveStatus liveStatus, @PathParam( "assetId" ) Long assetId, @PathParam( "operation" ) String operation, RestPlatformPublishInfo info ) throws AssetNotFoundException;

	@PUT
	@Formatted
	@Wrapped( element = "publishing" )
	@Path( "/{assetId}/publish" )
	public List<RestPlatformPublishInfo> putPublishAsset( @PathParam( "platform" ) RestPlatformList platforms, @PathParam( "assetId" ) Long assetId, RestPlatformPublishInfo info ) throws AssetNotFoundException;

	@POST
	@Formatted
	@Path( "/{assetId}/license" )
	public String getLicense( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @QueryParam( "account" ) String account, @QueryParam( "timeStamp" ) String timeStamp, @QueryParam( "contract" ) String token ) throws ThirdPartyErrorException;

	@GET
	@Formatted
	@Path( "/{assetId}/widevinelicense" )
	@Produces( MediaType.TEXT_PLAIN )
	public String getWidevineLicense( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @QueryParam( "account" ) String account, @QueryParam( "timeStamp" ) String timeStamp, @QueryParam( "contract" ) String contract, @FormParam( "mk" ) String _mk, @FormParam( "md" ) String _md, @FormParam( "ver" ) String _ver, @FormParam( "userdata" ) String _userdata, @FormParam( "messageid" ) String _messageid, @FormParam( "token" ) String _token, @FormParam( "divInfo" ) String _divInfo, @FormParam( "extra" ) String _extra, @FormParam( "assetid" ) String _assetid, @FormParam( "clientid" ) String _clientid, @FormParam( "sessionid" ) String _sessionid, @FormParam( "time" ) String _time, @FormParam( "version" ) String _version, @FormParam( "wvcss" ) String _wvcss, @FormParam( "ackurl" ) String _ackurl, @FormParam( "hburl" ) String _hburl, @FormParam( "hbint" ) String _hbint, @FormParam( "setduration" ) String _setduration ) throws ThirdPartyErrorException;

	@POST
	@Formatted
	@Path( "/{assetId}/widevinelicense" )
	@Produces( MediaType.TEXT_PLAIN )
	public String getPostWidevineLicense( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @FormParam( "account" ) String account, @FormParam( "timeStamp" ) String timeStamp, @FormParam( "contract" ) String token, @FormParam( "mk" ) String _mk, @FormParam( "md" ) String _md, @FormParam( "ver" ) String _ver, @FormParam( "userdata" ) String _userdata, @FormParam( "messageid" ) String _messageid, @FormParam( "token" ) String _token, @FormParam( "divInfo" ) String _divInfo, @FormParam( "extra" ) String _extra, @FormParam( "assetid" ) String _assetid, @FormParam( "clientid" ) String _clientid, @FormParam( "sessionid" ) String _sessionid, @FormParam( "time" ) String _time, @FormParam( "version" ) String _version, @FormParam( "wvcss" ) String _wvcss, @FormParam( "ackurl" ) String _ackurl, @FormParam( "hburl" ) String _hburl, @FormParam( "hbint" ) String _hbint, @FormParam( "setduration" ) String _setduration ) throws ThirdPartyErrorException;

	/**
	 * Create a new {@link RestAsset asset}
	 *
	 * @param platforms
	 *            The {@link RestPlatformList platforms} to publish the
	 *            {@link RestAsset asset} to
	 * @param An
	 *            {@link RestAsset asset} object that contains the data used to
	 *            create the new {@link RestAsset asset}. A new Id will be
	 *            assigned to the {@link RestAsset asset}.
	 * @return The created {@link RestAsset asset}.
	 * @throws AssetNotFoundException
	 *             TODO: FIX ME
	 */
	@POST
	@Formatted
	@Path( "" )
	public RestAsset postAsset( @PathParam( "platform" ) RestPlatformList platforms, RestAsset restAsset ) throws AssetNotFoundException;

	/**
	 * Update an {@link RestAsset asset}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that should be updated
	 * @param restAsset
	 *            An {@link RestAsset asset} object with the new data
	 * @return The updated {@link RestAsset asset}
	 * @throws DataValidationException
	 *             If the {@link RestAsset asset} contains invalid/illegal
	 *             values
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 */
	@PUT
	@Formatted
	@Path( "/{assetId}" )
	public RestAsset putAsset( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, RestAsset restAsset ) throws DataValidationException, AssetNotFoundException;

	/**
	 * Create a new {@link RestAsset asset} based on an existing
	 * {@link RestAsset asset}. TODO: Explain host_prog_id here?
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that should be copied
	 * @return A new {@link RestAsset asset}
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 * @throws CategoryNotFoundException
	 */
	@POST
	@Formatted
	@Path( "/{assetId}/copy" )
	public RestAsset postCopyAsset( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId ) throws AssetNotFoundException, CategoryNotFoundException;

	/**
	 * Create a new {@link RestAsset asset} based on an existing
	 * {@link RestAssetItem asset item}.
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param itemId
	 *            The id of the {@link RestAssetItem asset item} that the copy
	 *            should be based on
	 * @return A new {@link RestAsset asset}
	 * @throws AssetItemNotFoundException
	 *             If the asset item id does not exist
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 * @throws CategoryNotFoundException
	 */
	@POST
	@Formatted
	@Path( "/item/{itemId}/copy" )
	public RestAsset postCopyItem( @PathParam( "platform" ) RestPlatform platform, @PathParam( "itemId" ) Long itemId ) throws AssetItemNotFoundException, AssetNotFoundException, CategoryNotFoundException;

	/**
	 * Return a collection of {@link RestAssetItem asset items}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetItem items} belong to
	 * @return A {@link RestAssetItemList asset items} collection. If the
	 *         {@link RestAsset asset} does not have any {@link RestAssetItem
	 *         items}, an empty collection will be returned.
	 * @throws AssetItemNotFoundException
	 *             If the asset item id does not exist
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 */
	@GET
	@Formatted
	@Path( "/{assetId}/items" )
	public RestAssetItemList getAssetItems( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId ) throws AssetItemNotFoundException, AssetNotFoundException;

	/**
	 * Delete all {@link RestAssetItem asset items} on the {@link RestAsset
	 * asset}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetItem items} belong to
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 */
	@DELETE
	@Formatted
	@Path( "/{assetId}/items" )
	public void deleteAssetItems( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId ) throws AssetNotFoundException;

	/**
	 * Returns a single {@link RestAssetItem asset item}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetItem item} belong to
	 * @param itemId
	 *            The id of the {@link RestAssetItem asset item}
	 * @return An {@link RestAssetItem asset item}
	 * @throws AssetItemNotFoundException
	 *             If the asset item id does not exist
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 */
	@GET
	@Formatted
	@Path( "/{assetId}/item/{itemId}" )
	public RestAssetItem getAssetItem( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @PathParam( "itemId" ) Long itemId ) throws AssetItemNotFoundException, AssetNotFoundException;

	/**
	 * Create a new {@link RestAssetItem asset item}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetItem item} belongs to
	 * @param item
	 *            An {@link RestAssetItem asset item} object with the new data
	 * @return The created {@link RestAssetItem asset item}
	 * @throws AssetItemNotFoundException
	 *             If the asset item id does not exist
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 * @throws InvalidDateException
	 *             TODO: Explain encoder_start_time
	 */
	@POST
	@Formatted
	@Path( "/{assetId}/item" )
	public RestAssetItem postAssetItem( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, RestAssetItem item ) throws AssetNotFoundException, DataIncompleteException, InvalidDateException;

	/**
	 * Update a single {@link RestAssetItem asset item}.
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetItem item} is connected to
	 * @param itemId
	 *            The id of the {@link RestAssetItem asset item} that should be
	 *            updated
	 * @param item
	 *            An {@link RestAssetItem asset item} object with the new data
	 * @return The updated {@link RestAssetItem asset item}
	 * @throws AssetNotFoundException
	 * @throws AssetItemNotFoundException
	 */
	@PUT
	@Formatted
	@Path( "/{assetId}/item/{itemId}" )
	public RestAssetItem putAssetItem( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @PathParam( "itemId" ) Long itemId, RestAssetItem item ) throws AssetItemNotFoundException, AssetNotFoundException;

	/**
	 * Deletes a single {@link RestAssetItem asset item}.
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetItem item} is connected to
	 * @param itemId
	 *            The id of the {@link RestAssetItem asset item} that should be
	 *            updated
	 * @throws AssetNotFoundException
	 * @throws AssetItemNotFoundException
	 */
	@DELETE
	@Formatted
	@Path( "/{assetId}/item/{itemId}" )
	public void deleteAssetItem( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @PathParam( "itemId" ) Long itemId ) throws AssetItemNotFoundException, AssetNotFoundException;

	/**
	 * Returns a single {@link RestAsset asset}
	 *
	 * @param platform
	 *            The {@link RestPlatform platform} TODO: Explain
	 * @param assetId
	 *            The id of the requested {@link RestAsset asset}
	 * @return A single {@link RestAsset asset}
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 * @throws CategoryNotFoundException
	 */
	@GET
	@Formatted
	@Path( "/{assetId}" )
	public RestAsset getAsset( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @QueryParam("expand") @DefaultValue("") String expand ) throws AssetNotFoundException, CategoryNotFoundException;

	/**
	 * Gets a collection of {@link RestProduct products} that gives access to
	 * the {@link RestAsset asset}
	 *
	 * @param platform
	 *            The {@link RestPlatform platform}
	 * @param assetId
	 *            The id of the requested {@link RestAsset asset}
	 * @return A collection of {@link RestProductGroupList products}
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 */
	@GET
	@Formatted
	@Path( "/{assetId}/productgroups" )
	public RestProductGroupList getProductGroups( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId ) throws AssetNotFoundException;

	/**
	 * Gets information about {@link RestPlayback playback}
	 *
	 * @param platform
	 *            The {@link RestPlatform platform}
	 * @param assetId
	 *            The id of the requested {@link RestAsset asset} that should be
	 *            played
	 * @param videoFormat
	 *            The requested {@link MediaFormat video format}
	 * @return A {@link RestPlayback playback} object
	 * @throws NotFoundException
	 *             If the asset id does not exist //TODO: Change to
	 *             AssetNotFoundException
	 * @throws InvalidGeoRegionException
	 *             If the asset is not available for the region
	 * @throws MethodNotAllowedException
	 *             TODO: ???
	 * @throws no.sumo.api.exception.asset.playback.UnsupportedVideoFormatException
	 */
	@GET
	@Formatted
	@Path( "/{assetId}/play" )
	public RestPlayback getAssetPlayback( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @QueryParam( "videoFormat" ) String videoFormat, @QueryParam( "protocol" ) String protocol ) throws NotFoundException, InvalidGeoRegionException, MethodNotAllowedException, UnsupportedVideoFormatException;

	@GET
	@Formatted
	@Path( "/{assetId}/playProgress" )
	public RestPlayProgress getPlayProgress(
		@PathParam("platform") RestPlatform platform,
		@PathParam("assetId") Long assetId
	) throws NotFoundException, MethodNotAllowedException;

	@PUT
	@Formatted
	@Path( "/{assetId}/playProgress" )
	public void putPlayProgress(
		@PathParam("platform") RestPlatform platform,
		@PathParam("assetId") Long assetId,
		RestPlayProgress playProgress
	) throws MethodNotAllowedException;


	/**
	 * Gets information about {@link RestPlayback playback}
	 *
	 * @param platform
	 *            The {@link RestPlatform platform}
	 * @param itemId
	 *            The id of the {@link RestAssetItem asset item} that should be
	 *            played
	 * @param videoFormat
	 *            The requested {@link MediaFormat video format}
	 * @return A {@link RestPlayback playback} object
	 * @throws NotFoundException
	 *             If the asset id does not exist //TODO: Change to
	 *             AssetNotFoundException
	 * @throws InvalidGeoRegionException
	 *             If the asset is not available for the region
	 * @throws MethodNotAllowedException
	 *             TODO: ???
	 * @throws no.sumo.api.exception.asset.playback.UnsupportedVideoFormatException
	 */
	@GET
	@Formatted
	@Path( "/item/{itemId}/play" )
	public RestPlayback getAssetItemPlayback( @PathParam( "platform" ) RestPlatform platform, @PathParam( "itemId" ) Long itemId, @QueryParam( "videoFormat" ) String videoFormat, @QueryParam( "protocol" ) String protocol ) throws NotFoundException, InvalidGeoRegionException, MethodNotAllowedException, UnsupportedVideoFormatException;

	/**
	 * Returns all assetTypes
	 *
	 * @return List of all assetTypes
	 */
	@GET
	@Formatted
	@Path( "/types" )
	public RestAssetTypeList getAssetTypes( @PathParam( "platform" ) RestPlatformList platforms );

	@POST
	@Path( "/{assetId}/play/log/{fileId}/" )
	public void postLogAssetPlayback( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @PathParam( "fileId" ) Long fileId, @FormParam( "payload" ) @EncryptedLogData Object logData ) throws AssetNotFoundException, InvalidLogDataException;

	/**
	 * Returns a single {@link RestAssetComment asset comment}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetComment comment} belong to
	 * @param commentId
	 *            The id of the {@link RestAssetComment asset comment}
	 * @return An {@link RestAssetComment asset comment}
	 * @throws AssetCommentNotFoundException
	 *             If the asset comment id does not exist
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 */
	@GET
	@Formatted
	@Path( "/{assetId}/comment/{commentId}" )
	public RestAssetComment getAssetComment( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @PathParam( "commentId" ) Long commentId ) throws AssetCommentNotFoundException, AssetNotFoundException;

	/**
	 * Create a new {@link RestAssetComment asset comment}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetComment comment} belongs to
	 * @param comment
	 *            An {@link RestAssetComment asset comment} object with the new
	 *            data
	 * @return The created {@link RestAssetComment asset comment}
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 * @throws UserNotFoundException
	 *             If the asset comment member nickname does not exist
	 */
	@POST
	@Formatted
	@Path( "/{assetId}/comment" )
	public RestAssetComment postAssetComment( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, RestAssetComment comment ) throws AssetNotFoundException, UserNotFoundException, DataIncompleteException;

	/**
	 * Update a single {@link RestAssetComment asset comment}.
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetComment comment} is connected to
	 * @param commentId
	 *            The id of the {@link RestAssetComment asset comment} that
	 *            should be updated
	 * @param comment
	 *            An {@link RestAssetComment asset comment} object with the new
	 *            data
	 * @return The updated {@link RestAssetComment asset comment}
	 * @throws AssetCommentNotFoundException
	 * @throws AssetNotFoundException
	 */
	@PUT
	@Formatted
	@Path( "/{assetId}/comment/{commentId}" )
	public RestAssetComment putAssetComment( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @PathParam( "commentId" ) Long commentId, RestAssetComment comment ) throws AssetCommentNotFoundException, AssetNotFoundException;

	/**
	 * Delete a single {@link RestAssetComment asset comment}.
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetComment comment} is connected to
	 * @param commentId
	 *            The id of the {@link RestAssetComment asset comment} that
	 *            should be deleted
	 * @return
	 * @throws AssetCommentNotFoundException
	 * @throws AssetNotFoundException
	 */
	@DELETE
	@Formatted
	@Path( "/{assetId}/comment/{commentId}" )
	public void deleteAssetComment( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @PathParam( "commentId" ) Long commentId ) throws AssetCommentNotFoundException, AssetNotFoundException;

	/**
	 * Return a collection of {@link RestAssetComment asset comments}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier.
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetComment comments} belong to
	 * @return A {@link RestAssetCommentList asset comments} collection. If the
	 *         {@link RestAsset asset} does not have any
	 *         {@link RestAssetComment comments}, an empty collection will be
	 *         returned.
	 * @throws AssetCommentNotFoundException
	 *             If the asset comment id does not exist
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 */
	@GET
	@Formatted
	@Path( "/{assetId}/comments" )
	public RestAssetCommentList getAssetComments( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId ) throws AssetCommentNotFoundException, AssetNotFoundException;

	@GET
	@Formatted
	@Path( "/{assetId}/commentscount" )
	public RestAssetProperty getAssetCommentsCount( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId ) throws AssetNotFoundException, AssetPropertyNotFoundException;

	/**
	 * Returns a single {@link RestAssetRating asset rating}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetRating rating} belong to
	 * @param ratingId
	 *            The id of the {@link RestAssetRating asset rating}
	 * @return An {@link RestAssetRating asset rating}
	 * @throws AssetRatingNotFoundException
	 *             If the asset rating id does not exist
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 */
	@GET
	@Formatted
	@Path( "/{assetId}/rating/{ratingId}" )
	public RestAssetRating getAssetRating( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @PathParam( "ratingId" ) Long ratingId ) throws AssetRatingNotFoundException, AssetNotFoundException;

	/**
	 * Create a new {@link RestAssetRating asset rating}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetRating rating} belongs to
	 * @param rating
	 *            An {@link RestAssetRating asset rating} object with the new
	 *            data
	 * @return The created {@link RestAssetRating asset rating}
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 * @throws UserNotFoundException
	 *             If the asset comment member nickname does not exist
	 */
	@POST
	@Formatted
	@Path( "/{assetId}/rating" )
	public RestAssetRating postAssetRating( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, RestAssetRating rating ) throws AssetNotFoundException, UserNotFoundException, PrematureRatingException, DataIncompleteException;

	/**
	 * Update a single {@link RestAssetRating asset rating}.
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetRating rating} is connected to
	 * @param ratingId
	 *            The id of the {@link RestAssetRating asset rating} that should
	 *            be updated
	 * @param rating
	 *            An {@link RestAssetRating asset rating} object with the new
	 *            data
	 * @return The updated {@link RestAssetRating asset rating}
	 * @throws AssetNotFoundException
	 * @throws AssetRatingNotFoundException
	 */
	@PUT
	@Formatted
	@Path( "/{assetId}/rating/{ratingId}" )
	public RestAssetRating putAssetRating( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @PathParam( "ratingId" ) Long ratingId, RestAssetRating rating ) throws AssetRatingNotFoundException, AssetNotFoundException;

	/**
	 * Delete a single {@link RestAssetRating asset rating}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetRating rating} belong to
	 * @param ratingId
	 *            The id of the {@link RestAssetRating asset rating} that should
	 *            be deleted
	 * @return
	 * @throws AssetRatingNotFoundException
	 *             If the asset rating id does not exist
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 */
	@DELETE
	@Formatted
	@Path( "/{assetId}/rating/{ratingId}" )
	public void deleteAssetRating( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @PathParam( "ratingId" ) Long ratingId ) throws AssetRatingNotFoundException, AssetNotFoundException;


	@GET
	@Formatted
	@Path("/{assetId}/ratingForUser/{userId}")
	public RestAssetRating getRatingForUser(@PathParam("platform") RestPlatform platform, @PathParam("assetId") Long assetId, @PathParam("userId") Long UserId);
	/**
	 * Return a collection of {@link RestAssetRating asset ratings}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier.
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetRating ratings} belong to
	 * @return A {@link RestAssetRatingList asset ratings} collection. If the
	 *         {@link RestAsset asset} does not have any {@link RestAssetRating
	 *         ratings}, an empty collection will be returned.
	 * @throws AssetRatingNotFoundException
	 *             If the asset rating id does not exist
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 */
	@GET
	@Formatted
	@Path( "/{assetId}/ratings" )
	public RestAssetRatingList getAssetRatings( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId ) throws AssetRatingNotFoundException, AssetNotFoundException;

	@GET
	@Formatted
	@Path( "/{assetId}/ratingscount" )
	public RestAssetPropertyList getAssetRatingsCount( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId ) throws AssetNotFoundException;

	/**
	 * Returns a single {@link RestAssetProperty asset property}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier. TODO: ignored?
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetProperty property} belong to
	 * @param propertyId
	 *            The id of the {@link RestAssetProperty asset property}
	 * @return An {@link RestAssetProperty asset property}
	 * @throws AssetPropertyNotFoundException
	 *             If the asset property id does not exist
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 */
	@GET
	@Formatted
	@Path( "/{assetId}/property/{propertyId}" )
	public RestAssetProperty getAssetProperty( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @PathParam( "propertyId" ) Long propertyId ) throws AssetPropertyNotFoundException, AssetNotFoundException;

	/**
	 * Return a collection of {@link RestAssetProperty asset properties}
	 *
	 * @param platform
	 *            {@link RestPlatform platform} identifier.
	 * @param assetId
	 *            The id of the {@link RestAsset asset} that the
	 *            {@link RestAssetProperty properties} belong to
	 * @return A {@link RestAssetPropertyList asset properties} collection. If
	 *         the {@link RestAsset asset} does not have any
	 *         {@link RestAssetProperty properties}, an empty collection will be
	 *         returned.
	 * @throws AssetPropertyNotFoundException
	 *             If the asset property id does not exist
	 * @throws AssetNotFoundException
	 *             If the asset id does not exist
	 */
	@GET
	@Formatted
	@Path( "/{assetId}/properties" )
	public RestAssetPropertyList getAssetProperties( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId ) throws AssetNotFoundException;

	/**
	 * Use this to tell the background media importer to go and get some new
	 * media content for this asset
	 *
	 * @param assetId
	 *            which asset the media content is associated with
	 * @param source
	 *            a string describing the source of this media
	 * @param uri
	 *            a fully qualified uri describing where to download the media
	 *            from.
	 */
	@POST
	@Formatted
	@Path( "/{assetId}/mediaImportQue" )
	public void postMediaImportQue( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId, @FormParam( "source" ) String source, @FormParam( "uri" ) String uri ) throws AssetNotFoundException;

}
