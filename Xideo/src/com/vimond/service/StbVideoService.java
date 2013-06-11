package com.vimond.service;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.contracts.IPlaybackItem;
import no.sumo.api.entity.sumo.enums.ProgramRatingType;
import no.sumo.api.exception.InvalidGeoRegionException;
import no.sumo.api.exception.asset.AssetNotFoundException;
import no.sumo.api.exception.asset.playback.InvalidLogDataException;
import no.sumo.api.exception.asset.playback.UnsupportedVideoFormatException;
import no.sumo.api.exception.asset.rating.AssetRatingNotFoundException;
import no.sumo.api.exception.asset.rating.PrematureRatingException;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.exception.category.CategoryNotFoundException;
import no.sumo.api.exception.generic.DataIncompleteException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.exception.user.UserNotFoundException;
import no.sumo.api.service.IAssetService;
import no.sumo.api.service.ISearchService;
import no.sumo.api.vo.asset.RestAsset;
import no.sumo.api.vo.asset.RestSearchAsset;
import no.sumo.api.vo.asset.RestSearchAssetList;
import no.sumo.api.vo.asset.playback.RestPlayProgress;
import no.sumo.api.vo.asset.playback.RestPlayback;
import no.sumo.api.vo.asset.playback.RestPlaybackItem;
import no.sumo.api.vo.asset.rating.RestAssetRating;
import no.sumo.api.vo.metadata.RestMetadata;
import no.sumo.api.vo.platform.RestPlatform;

import org.apache.http.HttpStatus;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.spi.UnauthorizedException;

import roboguice.util.Ln;

import com.comcast.xideo.Constants;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vimond.entity.Playback;
import com.vimond.entity.UserReference;
import com.vimond.entity.Video;
import com.vimond.entity.VideoReference;
import com.vimond.rest.Converters;

@Singleton
public class StbVideoService {
	@Inject
	RestPlatform platform;

	@Inject
	IAssetService assetService;

	@Inject
	private StbAuthenticationService authenticationService;

	@Inject
	private StbFavoriteService favoriteService;

	@Inject
	private ISearchService searchService;

	public Long like( VideoReference video ) throws AssetNotFoundException, UserNotFoundException, PrematureRatingException, DataIncompleteException {
		UserReference user = authenticationService.getUser();
		RestAssetRating rating = new RestAssetRating();
		rating.setMemberId( user.getId() );
		rating.setRating( ProgramRatingType.RATING_5 );
		rating.setCrumb( Long.toString( user.getId() ) );
		RestAssetRating postAssetRating = assetService.postAssetRating( platform, video.getId(), rating );
		return postAssetRating.getId();
	}

	public void unlike( VideoReference video ) throws AssetNotFoundException, UserNotFoundException, PrematureRatingException, DataIncompleteException, AssetRatingNotFoundException {
		UserReference user = authenticationService.getUser();
		RestAssetRating rating = new RestAssetRating();
		rating.setMemberId( user.getId() );
		rating.setRating( ProgramRatingType.RATING_5 );
		rating.setCrumb( Long.toString( user.getId() ) );
		assetService.deleteAssetRating( platform, video.getId(), assetService.getRatingForUser( platform, video.getId(), user.getId() ).getId() );
	}

	public Boolean likes( final VideoReference video ) {
		RestAssetRating assetRating = getCurrentRating( video );
		if( assetRating != null ) {
			return assetRating.getRating() == ProgramRatingType.RATING_5;
		}
		return false;
	}

	public RestAssetRating getCurrentRating( VideoReference video ) {
		UserReference user = authenticationService.getUser();
		return assetService.getRatingForUser( platform, video.getId(), user.getId() );
	}

	public Playback getAssetInfo( VideoReference video ) throws InvalidGeoRegionException, NotFoundException, MethodNotAllowedException, UnsupportedVideoFormatException, UnauthorizedException {
		return new Playback( getAssetInfo( video.getId() ) );
	}

	public RestPlayback getAssetInfo( Long assetId ) throws InvalidGeoRegionException, NotFoundException, MethodNotAllowedException, UnsupportedVideoFormatException, UnauthorizedException {
		try {
			return assetService.getAssetPlayback( platform, assetId, null, null );
		} catch( ClientResponseFailure ex ) {
			Ln.i( "ClientResponseFailure" );
			if( ex.getResponse().getStatus() == HttpStatus.SC_UNAUTHORIZED ) {
				Ln.i( "UnauthorizedException" );
				throw new UnauthorizedException( ex );
			}
			throw ex;
		}
	}

	public RestAsset getAsset( Long assetId ) throws AssetNotFoundException, CategoryNotFoundException {
		return assetService.getAsset( platform, assetId, "" );
	}

	public void addToFavorites( VideoReference video ) throws MethodNotAllowedException {
		favoriteService.add( video );
	}

	public void removeFromFavorites( VideoReference video ) throws MethodNotAllowedException, AssetNotFoundException {
		favoriteService.remove( video );
	}

	public boolean isFavorite( VideoReference video ) throws MethodNotAllowedException {
		return favoriteService.isFavorite( video );
	}

	public void logPlayback( VideoReference video ) throws InvalidGeoRegionException, NotFoundException, MethodNotAllowedException, UnsupportedVideoFormatException, InvalidLogDataException {
		RestPlayback playback = assetService.getAssetPlayback( platform, video.getId(), null, null );
		List<IPlaybackItem> items = playback.getPlaybackItems();
		if( items.size() >= 1 ) {
			RestPlaybackItem playbackItem = (RestPlaybackItem)items.get( 0 );
			String logData = playback.getLogData();
			assetService.postLogAssetPlayback( platform, video.getId(), playbackItem.getFileId(), logData );
		}
	}

	public Playback getPreviewAssetInfo( VideoReference assetId ) throws InvalidGeoRegionException, NotFoundException, MethodNotAllowedException, UnsupportedVideoFormatException, PreviewNotFoundException {
		return new Playback( getPreviewAssetInfo( assetId.getId() ) );
	}

	public RestPlayback getPreviewAssetInfo( Long assetId ) throws InvalidGeoRegionException, NotFoundException, MethodNotAllowedException, UnsupportedVideoFormatException, PreviewNotFoundException {
		RestAsset asset = assetService.getAsset( platform, assetId, "metadata" );
		try {
			RestMetadata metadata = asset.getMetadata();
			String string = metadata.get( "preview-asset-id" );
			long previewAssetId = Long.valueOf( string );
			return assetService.getAssetPlayback( platform, previewAssetId, null, null );
		} catch( Exception ex ) {
			throw new PreviewNotFoundException( ex );
		}
	}

	public Video getVideo( VideoReference video ) throws AssetNotFoundException, CategoryNotFoundException {
		RestAsset asset = assetService.getAsset( platform, video.getId(), "metadata" );
		return Converters.convert( asset );
	}

	public List<Video> findVideos( String title ) throws NotFoundException {
		ArrayList<Video> videoList = new ArrayList<Video>();
		String query = String.format( "\"%s\"", title.replace( "\"", "" ) );
		RestSearchAssetList assets = searchService.getSubAssets( platform, Long.toString( Constants.MAIN_CATEGORY ), null, null, 0, 5, query, null );
		for( RestSearchAsset asset : assets.getAssets() ) {
			Ln.i( "asset:" + asset );
			videoList.add( Converters.convert( asset ) );
		}
		return videoList;
	}

	public Long getProgress( VideoReference video ) {
		try {
			RestPlayProgress playProgress = assetService.getPlayProgress( platform, video.getId() );
			return playProgress.offsetSeconds;
		} catch( Exception ex ) {
		}
		return null;
	}

	public void setProgress( VideoReference video, Long progress ) {
		RestPlayProgress playProgress = new RestPlayProgress();
		playProgress.offsetSeconds = progress;
		try {
			assetService.putPlayProgress( platform, video.getId(), playProgress );
		} catch( MethodNotAllowedException ex ) {
			Ln.e( ex );
		}
	}
}