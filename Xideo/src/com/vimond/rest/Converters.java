package com.vimond.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.sumo.api.vo.asset.RestAsset;
import no.sumo.api.vo.asset.RestSearchAsset;
import no.sumo.api.vo.asset.RestSearchAssetList;
import no.sumo.api.vo.category.RestCategory;
import no.sumo.api.vo.category.RestSearchCategory;
import no.sumo.api.vo.contentpanel.RestContentPanelElement;
import no.sumo.api.vo.metadata.RestMetadata;
import no.sumo.api.vo.playqueue.RestPlaybackQueueItem;

import com.comcast.xideo.ParentalGuidelines;
import com.comcast.xideo.viewmodel.Category;
import com.comcast.xideo.viewmodel.ContentPanelElement;
import com.vimond.entity.ChannelImpl;
import com.vimond.entity.ChannelReferenceImpl;
import com.vimond.entity.ImagePackReference;
import com.vimond.entity.ShowImpl;
import com.vimond.entity.Video;
import com.vimond.entity.VideoImpl;

public class Converters {
	public static Video convert( final RestAsset asset ) {
		final RestMetadata meta = asset.getMetadata();
		VideoImpl video = new VideoImpl( asset.getId() );

		video.setTitle( meta != null && meta.get( "title" ) != null ? meta.get( "title" ) : asset.getTitle() );
		video.setDescription( asset.getDescription() );
		video.setDate( asset.getLiveBroadcastTime() );
		video.setDuration( asset.getDuration() );
		if( asset.getVoteCount() != null ) {
			video.setLikes( asset.getVoteCount() );
		}
		if( meta != null ) {
			if( meta.get( "short-description" ) != null ) {
				video.setDescription( meta.get( "short-description" ) );
			} else if (meta.get( "description-short" ) != null) {
				video.setDescription( meta.get( "description-short" ) );
			}

			video.setPublisher( meta.get( "publisher" ) );
			if( meta.get( "image-pack" ) != null ) {
				video.setImagePack( new ImagePackReference( meta.get( "image-pack" ) ) );
			}
		}

		if( asset.getCategoryId() != null ) {
			video.setChannel( new ChannelReferenceImpl( asset.getCategoryId() ) );
		}
		return video;
	}

	public static ChannelImpl convert( final RestSearchCategory category ) {
		final RestMetadata meta = category.getMetadata();
		ChannelImpl result = new ChannelImpl( category.getId() );
		result.setTitle( category.getTitle() );
		result.setParentCategoryId( category.getParentId() );
		result.setCategoryPath( category.getCategoryPath() );
		if( meta != null ) {
			// Log.e("Channel Meta:", meta.getProperties().toString());
			result.setTelevisionRating( ParentalGuidelines.getTelevisionRating( meta.get( "parental-guidance" ) ) );
			result.setEditorsPick( Boolean.parseBoolean( meta.get( "editors-pick" ) ) );
			// result.setPublisherid( Long.parseLong( meta.get("publisher-id") ) );
			result.setTrending( Boolean.parseBoolean( meta.get( "trending" ) ) );
			if( meta.get( "image-pack" ) != null ) {
				result.setImagePack( new ImagePackReference( meta.get( "image-pack" ) ) );
			}
			result.setDescription( meta.get( "description-short" ) );
			result.setPublisher( meta.get( "publisher" ) );
			if( meta.get( "title" ) != null ) {
				result.setTitle( meta.get( "title" ) );
			}
		}
		result.setLikes( category.getVoteCount() );
		return result;
	}

	public static ShowImpl convertToShow(final RestSearchCategory category) {
		final RestMetadata meta = category.getMetadata();
		ShowImpl result = new ShowImpl(category.getId());
		result.setName(category.getTitle());
		if (meta != null) {
			Map<String, String> map = meta.getProperties();

			// Log.e("Show Meta:", map.toString());
			if (map.containsKey(ShowImpl.PARENTAL_GUIDANCE)) {
				result.setTelevisionRating(ParentalGuidelines.getTelevisionRating(meta.get(ShowImpl.PARENTAL_GUIDANCE)));
				result.setTVRatingText(meta.get(ShowImpl.PARENTAL_GUIDANCE));
			}

			if (map.containsKey(ShowImpl.PUBLISHER_ID))
				result.setPublisherid(Long.parseLong(meta.get(ShowImpl.PUBLISHER_ID)));

			if (map.containsKey(ShowImpl.THUMBNAIL_BIG))
				result.setBigThumbnailImagePack(new ImagePackReference(meta.get(ShowImpl.THUMBNAIL_BIG)));

			if (map.containsKey(ShowImpl.THUMBNAIL_MINI))
				result.setMiniThumbnailImagePack(new ImagePackReference(meta.get(ShowImpl.THUMBNAIL_MINI)));

			if (map.containsKey(ShowImpl.SHORT_DESC))
					result.setDescription(meta.get(ShowImpl.SHORT_DESC));
			
			if(map.containsKey(ShowImpl.PUBLISHER))
				result.setPublisher(meta.get(ShowImpl.PUBLISHER));

			if (map.containsKey(ShowImpl.NAME)) 
				result.setName(meta.get(ShowImpl.NAME));
			
		}
		return result;
	}

	public static Category convert( final RestCategory category ) {
		Category result = new Category( category.getId() );
		RestMetadata meta = category.getMetadata();
		result.setTitle( meta != null && meta.get( "title" ) != null ? meta.get( "title" ) : category.getTitle() );
		return result;
	}

	public static Video convert( final RestPlaybackQueueItem item ) {
		final RestAsset asset = item.getAsset();

		VideoImpl video = new VideoImpl( item.getProgramId() );
		if( asset != null ) {
			video.setTitle( asset.getTitle() );
			video.setDescription( video.getDescription() );
			video.setLikes( asset.getVoteCount() != null ? asset.getVoteCount() : 0 );
			video.setDate( asset.getLiveBroadcastTime() );
			video.setDuration( asset.getDuration() );

			final RestMetadata meta = asset.getMetadata();
			if( meta != null ) {
				video.setTitle( meta.get( "title" ) );
				video.setPublisher( meta.get( "publisher" ) );
				video.setDescription( meta.get( "description-short" ) );
				video.setImagePack( new ImagePackReference( meta.get( "image-pack" ) ) );
			}
		}
		return video;
	}

	public static List<Video> convertToVideoList( RestSearchAssetList assets ) {
		List<Video> result = new ArrayList<Video>();
		for( RestSearchAsset asset : assets.getAssets() ) {
			result.add( convert( asset ) );
		}
		return result;
	}

	public static ContentPanelElement convert( RestContentPanelElement element ) {
		ContentPanelElement result = new ContentPanelElement();
		result.setTitle( element.getTitle() );
		result.setType( element.getContentType() );
		result.setTargetType( element.getContentType() );
		result.setKey( element.getContentKey() );

		if( element.getImagePackId() != null ) {
			result.setImagePack( new ImagePackReference( element.getImagePackId() ) );
		} else {
			String imagePack = extractImagePackFromUrl( element.getImageUrl() );
			if( imagePack != null ) {
				result.setImagePack( new ImagePackReference( imagePack ) );
			}
		}
		if( result.getImagePack() == null ) {
			result.setUrl( element.getImageUrl() );
		}

		return result;
	}

	private static String extractImagePackFromUrl( String url ) {
		Pattern pattern = Pattern.compile( "/image/([0-9a-f+]+)/" );
		Matcher m = pattern.matcher( url );
		if( m.find() && m.groupCount() >= 1 ) {
			return m.group( 1 );
		}
		return null;
	}
}
