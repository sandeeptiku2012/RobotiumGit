package com.comcast.xideo.viewmodel;

import gueei.binding.Command;
import gueei.binding.DependentObservable;
import gueei.binding.IObservable;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BitmapObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import no.knowit.misc.GoldenAsyncTask;
import no.sumo.api.exception.asset.AssetNotFoundException;
import no.sumo.api.exception.generic.MethodNotAllowedException;

import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.spi.UnauthorizedException;

import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.util.Ln;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.comcast.xideo.IChannel;
import com.comcast.xideo.Constants;
import com.comcast.xideo.events.CloseActivityEvent;
import com.comcast.xideo.events.ShowMyChannelsEvent;
import com.comcast.xideo.events.VideoChangedEvent;
import com.comcast.xideo.utils.BitmapLoader;
import com.comcast.xideo.videoplayer.ChannelSelectorItem;
import com.comcast.xideo.videoplayer.VideoQueue;
import com.comcast.xideo.viewmodel.MediaPlayerViewModel.MediaPlayerBuffering;
import com.comcast.xideo.viewmodel.MediaPlayerViewModel.MediaPlayerPrepared;
import com.comcast.xideo.viewmodel.MediaPlayerViewModel.PlayStatusChangedEvent;
import com.comcast.xideo.viewmodel.MediaPlayerViewModel.VideoIsCompleteEvent;
import com.google.inject.Inject;
import com.vimond.entity.AssetProgress;
import com.vimond.entity.ChannelReference;
import com.vimond.entity.Playback;
import com.vimond.entity.Video;
import com.vimond.entity.VideoReference;
import com.vimond.service.PreviewNotFoundException;
import com.vimond.service.StbChannelService;
import com.vimond.service.StbVideoService;

public class VideoPlayerViewModel implements OnItemSelectedListener, Callback, IChannelListener {
	public static class ReceivedProgressInfo {
		private final long progressPosition;

		public ReceivedProgressInfo( long progressPosition ) {
			this.progressPosition = progressPosition;
		}

		public long getProgressPosition() {
			return progressPosition;
		}
	}

	public static class FatalErrorOccurredEvent {
		private final Object source;
		private final String message;

		public FatalErrorOccurredEvent( Object source, String message ) {
			this.source = source;
			this.message = message;
		}

		public Object getSource() {
			return source;
		}

		public String getMessage() {
			return message;
		}
	}

	private final class TimeObservable extends DependentObservable<String> {
		private TimeObservable( IObservable<Integer> observable ) {
			super( String.class, observable );
		}

		@Override
		public String calculateValue( Object... objects ) throws Exception {
			Integer position = (Integer)objects[ 0 ];
			if( position == null ) {
				return "00:00:00";
			}
			int progress = position / 1000;
			return String.format( "%02d:%02d:%02d", progress / 3600, progress / 60 % 60, progress % 60 );
		}
	}

	static final int SEEK_INTERVAL = 30 * 1000;

	@Inject
	EventManager eventManager;

	@Inject
	Context app;

	@Inject
	StbVideoService videoService;

	@Inject
	StbChannelService channelService;

	@Inject
	BitmapLoader bitmaploader;

	@Inject
	private AssetProgress progressService;

	private Boolean hasLoadedChannels;

	public final BooleanObservable isVideoDetailsVisible = new BooleanObservable();
	public final BooleanObservable showingPlayerControls = new BooleanObservable();
	public final BooleanObservable isPlaying = new BooleanObservable( false );
	public final ArrayListObservable<ChannelSpinnerInfoViewModel> channels = new ArrayListObservable<ChannelSpinnerInfoViewModel>( ChannelSpinnerInfoViewModel.class );
	public final ArrayListObservable<VideoInfoViewModel> videos = new ArrayListObservable<VideoInfoViewModel>( VideoInfoViewModel.class );
	public final BooleanObservable isLoading = new BooleanObservable();
	public final StringObservable currentVideoDate = new StringObservable();
	public final StringObservable currentVideoTitle = new StringObservable();

	public final StringObservable nextVideotitle = new StringObservable();
	public final BitmapObservable nextVideoChannelLogo = new BitmapObservable();
	public final BitmapObservable nextNewestVideoImage = new BitmapObservable();

	public final BooleanObservable showNextVideo = new BooleanObservable( true );
	public final BooleanObservable isFavorite = new BooleanObservable( false );

	public final BooleanObservable isLiked = new BooleanObservable( false );

	final IntegerObservable mediaPlayerPosition = new IntegerObservable();
	final IntegerObservable mediaPlayerDuration = new IntegerObservable();

	public final Observable<Float> currentVideoProgress = new DependentObservable<Float>( Float.class, mediaPlayerPosition, mediaPlayerDuration ) {
		@Override
		public Float calculateValue( Object... values ) throws Exception {
			Integer position = (Integer)values[ 0 ];
			Integer duration = (Integer)values[ 1 ];
			if( position != null && duration != null && duration != 0 ) {
				return (float)position / duration;
			}
			return null;
		}
	};

	public final Observable<String> currentPosition = new TimeObservable( mediaPlayerPosition );

	public final Observable<String> currentDuration = new TimeObservable( mediaPlayerDuration );

	private SurfaceHolder holder;
	private VideoQueue videoQueue;

	@Inject
	public ChannelSelectorViewModel channelSelector;

	@Inject
	public MediaPlayerViewModel mediaPlayerViewModel;

	public Command channelSelectorLostFocus = new Command() { // NO_UCD (unused code)
		@Override
		public void Invoke( View view, Object... args ) {
			channelSelector.isVisible.set( false );
		}
	};

	@Inject
	public VideoPlayerViewModel( EventManager eventManager ) {
		this.eventManager = eventManager;

		showingPlayerControls.set( false );
		isPlaying.set( false );
		hasLoadedChannels = false;
	}

	public void openChannelSelector() {
		Ln.i( "openChannelSelector" );
		channelSelector.isVisible.set( true );
	}

	public Boolean allowHideControls() {
		return true;
	}

	public Command openMyChannels = new Command() { // NO_UCD (unused code)
		@Override
		public void Invoke( View view, Object... args ) {
			eventManager.fire( new ShowMyChannelsEvent() );
		}
	};

	void checkFavorite( final VideoReference video ) {
		new GoldenAsyncTask<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return videoService.isFavorite( video );
			}

			@Override
			protected void onSuccess( Boolean result ) throws Exception {
				isFavorite.set( result );
			}

			@Override
			protected void onException( Exception e ) throws RuntimeException {
				Ln.e( e );
			};
		}.execute();
	}

	private void checkFavorite() {
		checkFavorite( videoQueue.getCurrentVideo() );
	}

	private void checkLike( final VideoReference video ) {
		new GoldenAsyncTask<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return videoService.likes( video );
			}

			@Override
			protected void onSuccess( Boolean userLikesThisVideo ) throws Exception {
				isLiked.set( userLikesThisVideo );
			}
		}.execute();
	}

	private void checkLike() {
		checkLike( videoQueue.getCurrentVideo() );
	}

	public final Command addFavorite = new Command() { // NO_UCD (unused code)
		@Override
		public void Invoke( View arg0, Object... arg1 ) {
			toggleFavoriteAsync();
		};
	};

	public final Command addLike = new Command() { // NO_UCD (unused code)
		@Override
		public void Invoke( View arg0, Object... arg1 ) {
			likeVideoAsync();
		};
	};

	public final Command playOrPauseVideo = new Command() { // NO_UCD (unused code)
		@Override
		public void Invoke( View view, Object... arg1 ) {
			playOrPauseVideo();
		}
	};

	public void playOrPauseVideo() {
		mediaPlayerViewModel.playOrPauseMediaPlayer();
	}

	public void onPlayStatusChanged( @Observes PlayStatusChangedEvent event ) {
		isPlaying.set( mediaPlayerViewModel.mediaPlayer.isPlaying() );
	}

	public final Command forwardVideo = new Command() {
		@Override
		public void Invoke( View arg0, Object... arg1 ) {
			mediaPlayerViewModel.forward();
		}
	};

	public final Command rewindVideo = new Command() {
		@Override
		public void Invoke( View arg0, Object... arg1 ) {
			mediaPlayerViewModel.rewind();
		}
	};

	private void getAssetInfoAndStartPlayback( final VideoReference video ) {
		new GoldenAsyncTask<Playback>() {
			@Override
			public Playback call() throws Exception {
				try {
					Playback assetInfo = videoService.getAssetInfo( video );
					try {
						videoService.logPlayback( video );
					} catch( Exception ex ) {
					}
					return assetInfo;
				} catch( UnauthorizedException ex ) {
					return videoService.getPreviewAssetInfo( video );
				}
			}

			@Override
			protected void onSuccess( Playback t ) throws Exception {
				startVideoPlayback( t );
			}

			@Override
			protected void onException( Exception e ) throws RuntimeException {
				handlePlaybackError( video, e );
			}
		}.execute();
	}

	public void onPrepared( @Observes MediaPlayerPrepared event ) {
		final MediaPlayer player = event.getPlayer();
		new GoldenAsyncTask<Long>() {
			@Override
			public Long call() throws Exception {
				return progressService.getProgress( videoQueue.getCurrentVideo() );
			}

			@Override
			protected void onSuccess( final Long progressPosition ) throws Exception {
				if( progressPosition != null && progressPosition > 0 ) {
					eventManager.fire( new ReceivedProgressInfo( progressPosition ) );
				} else {
					player.start();
				}
			};

			@Override
			protected void onException( Exception e ) throws RuntimeException {
				player.start();
			}
		}.execute();

		mediaPlayerViewModel.mediaPlayer.setLooping( false );
		isLoading.set( false );

		checkFavorite();
		checkLike();
		updateSpinner();
	}

	public void start( SurfaceView surfaceView, long[] assetId ) {
		videoQueue = new VideoQueue( assetId );

		if( surfaceView != null ) {
			holder = surfaceView.getHolder();
			holder.addCallback( this );
			mediaPlayerViewModel.setHolder( holder );
		}

		getAssetInfoAndStartPlayback( videoQueue.getCurrentVideo() );

		channelSelector.setOnChannelSelectedListener( this );
	}

	public void onVideoCompleted( @Observes VideoIsCompleteEvent event ) {
		final VideoReference currentVideo = videoQueue.getCurrentVideo();
		event.getSource().stop();
		new GoldenAsyncTask<Void>() {
			@Override
			public Void call() throws Exception {
				progressService.removeProgress( currentVideo );
				return null;
			}
		}.execute();
		playNextVideo();
	}

	public void playNextVideo() {
		if( videoQueue.hasNext() ) {
			videoQueue.moveToNext();
			getAssetInfoAndStartPlayback( videoQueue.getCurrentVideo() );
		} else {
			onPlaylistReachedEnd();
		}
	}

	private void onPlaylistReachedEnd() {
		eventManager.fire( new CloseActivityEvent() );
	}

	@Override
	public void surfaceChanged( SurfaceHolder holder, int format, int width, int height ) {
		Ln.d( "surfaceChanged" );
	}

	@Override
	public void surfaceCreated( SurfaceHolder holder ) {
		Ln.d( "surfaceCreated" );
	}

	@Override
	public void surfaceDestroyed( SurfaceHolder holder ) {
		Ln.d( "surfaceDestroyed" );
		mediaPlayerViewModel.discardVideoPlayer();
	}

	public void onBufferingUpdate( @Observes MediaPlayerBuffering event ) {
		onBufferingUpdate( event.getPlayer(), event.getPercent() );
	}

	public void onBufferingUpdate( MediaPlayer mp, int percent ) {
		if( mp != null ) {
			Ln.i( "onBufferingUpdate: %s %s %s %s", mp, percent, mp.getCurrentPosition(), mp.getDuration() );
		} else {
			Ln.i( "onBufferingUpdate: %s %s", mp, percent );
		}

		mediaPlayerViewModel.currentBufferProgress.set( percent / 100f );
	}

	private void updateSpinner() {
		if( channels.get().size() <= 0 ) {
			downloadMyChannelsAsync();
		} else {
			getAndShowNextUpVideo();
		}
	}

	private void downloadMyChannelsAsync() {
		new GoldenAsyncTask<List<IChannel>>() {

			@Override
			public List<IChannel> call() throws Exception {
				return channelService.getMyChannels();
			}

			@Override
			protected void onSuccess( List<IChannel> channelList ) throws Exception {
				setChannels( channelList );
			}
		}.execute();
	}

	public final Command playNext = new Command() {
		@Override
		public void Invoke( View view, Object... args ) {
			mediaPlayerViewModel.stop();
			playNextVideo();
		}
	};

	@SuppressLint( "NewApi" )
	private void getAndShowNextUpVideo() {
		if( videoQueue.hasNext() ) {
			new GoldenAsyncTask<Video>() {

				@Override
				public Video call() throws Exception {
					return videoService.getVideo( videoQueue.getNextVideo() );
				}

				@Override
				protected void onSuccess( final Video video ) throws Exception {
					setNextUpVideo( video );
				}
			}.execute();
		} else {
			nextVideotitle.set( null );
			nextVideoChannelLogo.set( null );
			nextNewestVideoImage.set( null );
			showNextVideo.set( false );
		}
	}

	private void getChannelLogoAsync( final ChannelReference channel ) {
		new GoldenAsyncTask<IChannel>() {
			@Override
			public IChannel call() throws Exception {
				return channelService.getChannel( channel );
			}

			@Override
			protected void onSuccess( IChannel channel ) throws Exception {
				bitmaploader.loadImage( nextVideoChannelLogo, channel );
			}

		}.execute();
	}

	@Override
	public void onItemSelected( AdapterView<?> adapterView, View view, final int position, long id ) {
		Ln.i( "onItemSelected: adapterView = %s, view = %s, position = %s, id = %s", adapterView, view, position, id );
		if( hasLoadedChannels ) {
			new GoldenAsyncTask<Video>() {
				@Override
				public Video call() throws Exception {
					ChannelInfoViewModel channelModel = channels.getItem( position );
					return channelService.getNewestVideo( channelModel.getChannel() );
				}

				@Override
				protected void onSuccess( Video newAssetId ) throws Exception {
					getAssetInfoAndStartPlayback( newAssetId );
					updateVideos( channels.getItem( position ) );
				}
			}.execute();
		} else {
			hasLoadedChannels = true;
		}
	}

	private void updateVideos( final ChannelReference channel ) {
		isLoading.set( true );
		getPlaylist( channel );
	}

	private void getPlaylist( final ChannelReference channel ) {
		new GoldenAsyncTask<List<Video>>() {
			@Override
			public List<Video> call() throws Exception {
				return channelService.getVideos( channel, 10 );
			}

			@Override
			protected void onSuccess( List<Video> t ) throws Exception {
				setPlaylist( t );
			}
		}.execute();
	}

	@Override
	public void onNothingSelected( AdapterView<?> arg0 ) {

	}

	private void updateProgress() {
		if( mediaPlayerViewModel.mediaPlayer != null ) {
			if( mediaPlayerViewModel.mediaPlayer.isPlaying() ) {
				int position = mediaPlayerViewModel.mediaPlayer.getCurrentPosition();
				int duration = mediaPlayerViewModel.mediaPlayer.getDuration();
				Ln.i( "updateProgress: %s / %s", position, duration );
				mediaPlayerPosition.set( position );
				mediaPlayerDuration.set( duration );

				VideoReference video = videoQueue.getCurrentVideo();
				sendProgress( video, position / 1000 );
			}
		}
	}

	VideoReference lastVideo;
	int lastPosition;

	private void sendProgress( final VideoReference video, final int position ) {
		if( lastVideo == null || lastVideo.getId() != lastVideo.getId() || Math.abs( lastPosition - position ) > 5 ) {
			lastVideo = video;
			lastPosition = position;
			new GoldenAsyncTask<Void>() {
				@Override
				public Void call() throws Exception {
					progressService.setProgress( video, position );
					return null;
				}
			}.execute();
		}
	}

	public final TimerTask updateProgressTask = new TimerTask() {
		@Override
		public void run() {
			try {
				((Activity)app).runOnUiThread( new Runnable() {
					@Override
					public void run() {
						updateProgress();
					}
				} );
			} catch( Throwable t ) {
			}
		}
	};

	@Override
	public void onChannelSelected( ChannelReference channel ) {
		updateVideos( channel );
	}

	private void setChannels( List<IChannel> t ) {
		ChannelSpinnerInfoViewModel viewmodel;
		channels.clear();
		for( IChannel channel : t ) {
			viewmodel = new ChannelSpinnerInfoViewModel( channel, channelService, bitmaploader );
			bitmaploader.loadImage( viewmodel.logo, channel );
			channels.add( viewmodel );
		}

		getAndShowNextUpVideo();

		channelSelector.setChannels( t );
	}

	private Boolean toggleFavorite() throws MethodNotAllowedException, AssetNotFoundException {
		VideoReference video = videoQueue.getCurrentVideo();
		if( isFavorite.get() ) {
			videoService.removeFromFavorites( video );
			return false;
		} else {
			videoService.addToFavorites( video );
			return true;
		}
	}

	private void toggleFavoriteAsync() {
		new GoldenAsyncTask<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return toggleFavorite();
			}

			@Override
			protected void onSuccess( Boolean t ) throws Exception {
				isFavorite.set( t );
				checkFavorite();
			};
		}.execute();
	}

	private void likeVideoAsync() {
		final VideoReference video = videoQueue.getCurrentVideo();
		new GoldenAsyncTask<Long>() {
			@Override
			public Long call() throws Exception {
				return videoService.like( video );
			}

			@Override
			protected void onSuccess( Long t ) throws Exception {
				checkLike();
			};
		}.execute();
	}

	private void startVideoPlayback( Playback t ) throws IOException {
		if( t.getStreamURLs().size() > 0 ) {
			Date liveDate = t.getLiveBroadcastTime();

			SimpleDateFormat dateFormatter = new SimpleDateFormat( Constants.DATE_FORMAT );

			currentVideoDate.set( dateFormatter.format( liveDate ) );
			currentVideoTitle.set( t.getTitle() );
			eventManager.fire( new VideoChangedEvent( this ) );
			mediaPlayerViewModel.play( t );
			isLoading.set( true );
		} else {
			eventManager.fire( new FatalErrorOccurredEvent( this, "Could not start. No streams for asset, " + t.getTitle() ) );
		}
	}

	private void handlePlaybackError( final VideoReference video, Exception e ) {
		Ln.e( e );
		if( e instanceof ClientResponseFailure ) {
			ClientResponseFailure fail = (ClientResponseFailure)e;
			Ln.e( fail.getResponse().getStatus() );
			if( fail.getResponse().getStatus() == 401 ) {
				eventManager.fire( new FatalErrorOccurredEvent( this, "No access to video #" + video.getId() ) );
			} else if( fail.getResponse().getStatus() == 404 ) {
				eventManager.fire( new FatalErrorOccurredEvent( this, "Video not found" ) );
			} else {
				eventManager.fire( new FatalErrorOccurredEvent( this, "Could not load video" ) );
			}
		} else if( e instanceof PreviewNotFoundException ) {
			eventManager.fire( new FatalErrorOccurredEvent( this, "No preview available" ) );
		}
	}

	private void setNextUpVideo( final Video video ) {
		nextVideotitle.set( video.getTitle() );
		bitmaploader.loadImage( nextNewestVideoImage, video );

		getChannelLogoAsync( video.getChannel() );
		showNextVideo.set( true );
	}

	private void setPlaylist( List<Video> t ) {
		videoQueue.setPlaylist( t );
		getAndShowNextUpVideo();
		getAssetInfoAndStartPlayback( videoQueue.getCurrentVideo() );
	}

	public void startFrom( long progressPosition ) {
		mediaPlayerViewModel.startFrom( progressPosition );
	}

	public void start() {
		mediaPlayerViewModel.startFromBeginning();
	}
}

class ChannelSpinnerInfoViewModel extends ChannelInfoViewModel {

	public final StringObservable newestVideoTitle = new StringObservable();

	public ChannelSpinnerInfoViewModel( final IChannel channel, final StbChannelService channelService, final BitmapLoader bitmaploader ) {
		this.channelService = channelService;
		setChannel( channel );

		new GoldenAsyncTask<Video>() {
			@Override
			public Video call() throws Exception {
				return channelService.getNewestVideo( channel );
			}

			@Override
			protected void onSuccess( Video video ) throws Exception {
				setNewestVideo( bitmaploader, video );
			}
		}.execute();
	}

	private void setNewestVideo( final BitmapLoader bitmaploader, Video video ) {
		if( video != null ) {
			newestVideoTitle.set( video.getTitle() );
			bitmaploader.loadImage( newestVideoImage, video );
		}
	}
}
