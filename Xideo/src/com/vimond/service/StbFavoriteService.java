package com.vimond.service;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.exception.asset.AssetNotFoundException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.service.IPlayqueueService;
import no.sumo.api.vo.platform.RestPlatform;
import no.sumo.api.vo.playqueue.RestPlaybackQueue;
import no.sumo.api.vo.playqueue.RestPlaybackQueueItem;
import no.sumo.api.vo.playqueue.RestPlaybackQueueList;
import roboguice.util.Ln;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vimond.entity.Video;
import com.vimond.entity.VideoReference;
import com.vimond.rest.Converters;

@Singleton
public class StbFavoriteService {
	@Inject
	private IPlayqueueService playqueueService;

	@Inject
	private RestPlatform platform;

	private Long favoriteListId = null;

	@Inject
	public StbFavoriteService( RestPlatform platform, IPlayqueueService playqueueService ) {
		this.platform = platform;
		this.playqueueService = playqueueService;
	}

	Long getFavoriteListId() throws MethodNotAllowedException {
		if( favoriteListId == null ) {
			RestPlaybackQueueList q = playqueueService.getPlayqueues( platform );
			for( RestPlaybackQueue qu : q.getQueues() ) {
				if( qu != null ) {
					if( "Favorites".equalsIgnoreCase( qu.getName() ) ) {
						String uri = qu.getUri();
						favoriteListId = Long.parseLong( uri.substring( uri.lastIndexOf( '/' ) + 1 ) );
						break;
					}
				}
			}
		}
		return favoriteListId;
	}

	public List<Video> getFavoriteVideos() throws MethodNotAllowedException {
		return getFavoriteVideos( 4 );
	}

	public List<Video> getFavoriteVideos( int limit ) throws MethodNotAllowedException {
		ArrayList<Video> videos = new ArrayList<Video>();
		Long id = null;
		try {
			id = getFavoriteListId();
		} catch( Exception e ) {
			Ln.e( "Cannot find favoriteId" );
		}
		if( id != null ) {
			RestPlaybackQueue playqueue = playqueueService.getPlayqueue( platform, id );
			List<RestPlaybackQueueItem> items = playqueue.getRestListToShow();
			int count = 0;
			for( RestPlaybackQueueItem item : items ) {
				if( count++ < limit ) {
					videos.add( Converters.convert( item ) );
				}
			}
		}
		return videos;
	}

	void add( VideoReference video ) throws MethodNotAllowedException {
		Long favoriteListId = getFavoriteListId();
		playqueueService.postPlayqueueItem( platform, favoriteListId, video.getId() );
	}

	public void remove( VideoReference video ) throws MethodNotAllowedException, AssetNotFoundException {
		Long favoriteListId = getFavoriteListId();
		RestPlaybackQueue playqueue = playqueueService.getPlayqueue( platform, favoriteListId );
		RestPlaybackQueueItem item = findPlayqueueItem( playqueue, video );
		playqueueService.deletePlayqueueItem( platform, favoriteListId, item.getId() );
	}

	public boolean isFavorite( VideoReference video ) throws MethodNotAllowedException {
		long id = getFavoriteListId();
		RestPlaybackQueue playqueue = playqueueService.getPlayqueue( platform, id );
		try {
			return findPlayqueueItem( playqueue, video ) != null;
		} catch( AssetNotFoundException e ) {
		}
		return false;
	}

	private RestPlaybackQueueItem findPlayqueueItem( RestPlaybackQueue playqueue, VideoReference video ) throws AssetNotFoundException {
		List<RestPlaybackQueueItem> items = playqueue.getRestListToShow();
		for( RestPlaybackQueueItem item : items ) {
			if( item.getProgramId() == video.getId() ) {
				return item;
			}
		}
		throw new AssetNotFoundException( video.getId() );
	}
}
