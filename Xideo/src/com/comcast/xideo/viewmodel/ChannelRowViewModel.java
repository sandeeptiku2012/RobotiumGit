package com.comcast.xideo.viewmodel;

import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.LongObservable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.knowit.misc.GoldenAsyncTask;

import com.comcast.xideo.IChannel;
import com.comcast.xideo.utils.BitmapLoaderEx;
import com.goldengate.guice.VideoInfoViewModelFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vimond.entity.AssetProgress;
import com.vimond.entity.Video;
import com.vimond.entity.VideoReference;
import com.vimond.service.StbChannelService;

public class ChannelRowViewModel extends ChannelInfoViewModel {
	private static final int MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;

	public final LongObservable badgeCount = new LongObservable();
	public final ArrayListObservable<VideoInfoViewModel> videos = new ArrayListObservable<VideoInfoViewModel>( VideoInfoViewModel.class );

	@Inject
	protected StbChannelService channelService;

	@Inject
	BitmapLoaderEx bitmapLoader;

	@Inject
	VideoInfoViewModelFactory videoInfoViewModelFactory;

	@Inject
	private AssetProgress progressService;

	@Inject
	private Provider<VideoInfoViewModel> videoInfoViewModelProvider;

	@Override
	public void setChannel( IChannel channel ) {
		super.setChannel( channel );
		if (bitmapLoader != null) {
			bitmapLoader.loadImage(logo, channel);
			bitmapLoader.loadBigChannelLogo(logoLarge, channel);
		}
		downloadNewVideosInChannel( channel, videos );
	}

	private void downloadNewVideosInChannel( final IChannel channel, final ArrayListObservable<VideoInfoViewModel> videos ) {
		new GoldenAsyncTask<List<Video>>() {
			@Override
			public List<Video> call() throws Exception {
				return channelService.getVideos( channel, 10 );
			}

			@Override
			protected void onSuccess( List<Video> result ) throws Exception {
				setVideos( result );
			};

			@Override
			protected void onException(Exception e) throws RuntimeException {
				// super.onException(e);
			}
		}.execute();
	}

	private void setVideos( List<Video> result ) {
		int index = 0;
		for( Video video : result ) {
			VideoInfoViewModel vm;
			if( index < videos.size() ) {
				vm = videos.get( index );
			} else {
				vm = videoInfoViewModelProvider.get();
				videos.add( vm );
			}
			vm.setVideo( video );
			vm.publisher.set( null );
//			updateNewStatus( video, vm );
			bitmapLoader.loadImage( vm.image, video );
//			vm.updateProgress( progressService );
			index++;
		}
		while( videos.size() > result.size() ) {
			videos.remove( videos.size() - 1 );
		}

		updatePlaylists( videos, result );
	}

	private void updateNewStatus( final Video video, final VideoInfoViewModel vm ) {
		new GoldenAsyncTask<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return isNew( video ) && !hasSeen( video );
			}

			@Override
			protected void onSuccess( Boolean t ) throws Exception {
				vm.isNew.set( t );
			}
		}.execute();
	}

	private void updatePlaylists( final ArrayListObservable<VideoInfoViewModel> videos, List<Video> result ) {
		ArrayList<Long> assetIds = new ArrayList<Long>( videos.size() );
		for( VideoReference video : result ) {
			assetIds.add( video.getId() );
		}
		for( VideoInfoViewModel videoModel : videos ) {
			videoModel.setPlaylist( assetIds );
		}
	}

	private boolean isNew( Video video ) {
		long time = (new Date().getTime() / MILLISECONDS_PER_DAY - 7) * MILLISECONDS_PER_DAY;
		return video.getDate().getTime() >= time;
	}

	private boolean hasSeen( Video video ) {
		return progressService.hasSeen( video );
	}
}
