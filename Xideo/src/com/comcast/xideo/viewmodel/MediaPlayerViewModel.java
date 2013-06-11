package com.comcast.xideo.viewmodel;

import gueei.binding.observables.FloatObservable;

import java.io.IOException;

import roboguice.event.EventManager;
import roboguice.util.Ln;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.view.SurfaceHolder;

import com.comcast.xideo.viewmodel.VideoPlayerViewModel.FatalErrorOccurredEvent;
import com.google.inject.Inject;
import com.vimond.entity.Playback;
import com.vimond.entity.PlaybackStream;

public class MediaPlayerViewModel {
	public final FloatObservable currentBufferProgress = new FloatObservable( 0.0f );

	public static class VideoIsCompleteEvent {
		private final MediaPlayerViewModel viewModel;
		private final MediaPlayer mediaPlayer;

		public VideoIsCompleteEvent( MediaPlayerViewModel viewModel, MediaPlayer mediaPlayer ) {
			this.viewModel = viewModel;
			this.mediaPlayer = mediaPlayer;
		}

		public MediaPlayer getMediaPlayer() {
			return mediaPlayer;
		}

		public MediaPlayerViewModel getSource() {
			return viewModel;
		}
	}

	public static class PlayStatusChangedEvent {
	}

	public static class MediaPlayerPrepared {
		private final MediaPlayer player;

		public MediaPlayerPrepared( MediaPlayer mediaPlayer ) {
			player = mediaPlayer;
		}

		public MediaPlayer getPlayer() {
			return player;
		}
	}

	public static class MediaPlayerBuffering {
		private final int percent;
		private MediaPlayer player;

		public MediaPlayerBuffering( MediaPlayer player, int percent ) {
			this.player = player;
			this.percent = percent;
		}

		public int getPercent() {
			return percent;
		}

		public MediaPlayer getPlayer() {
			return player;
		}
	}

	@Inject
	EventManager eventManager;
	MediaPlayer mediaPlayer;

	OnErrorListener onErrorListener = new OnErrorListener() {
		@Override
		public boolean onError( MediaPlayer mediaPlayer, int what, int extra ) {
			Ln.i( "ERROR: %s %s %s", mediaPlayer, what, extra );
			eventManager.fire( new FatalErrorOccurredEvent( this, "Could not load video" ) );
			Ln.i( "eventManager: %s", eventManager );
			return true;
		}
	};

	private SurfaceHolder holder;

	void playOrPauseMediaPlayer() {
		if( mediaPlayer.isPlaying() ) {
			mediaPlayer.pause();
		} else {
			mediaPlayer.start();
		}
		eventManager.fire( new PlayStatusChangedEvent() );
	}

	void discardVideoPlayer() {
		if( mediaPlayer != null ) {
			stop();
			mediaPlayer.reset();
			mediaPlayer.release();
		}
	}

	public void stop() {
		if( mediaPlayer.isPlaying() ) {
			mediaPlayer.stop();
		}
	}

	void rewind() {
		mediaPlayer.seekTo( mediaPlayer.getCurrentPosition() - VideoPlayerViewModel.SEEK_INTERVAL );
	}

	void forward() {
		mediaPlayer.seekTo( mediaPlayer.getCurrentPosition() + VideoPlayerViewModel.SEEK_INTERVAL );
	}

	public void play( Playback playback ) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
		PlaybackStream path = playback.getStreamURLs().get( 0 );

		if( mediaPlayer != null ) {
			mediaPlayer.reset();
			mediaPlayer.release();
		}

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType( AudioManager.STREAM_MUSIC );

		mediaPlayer.setOnErrorListener( onErrorListener );

		mediaPlayer.setOnSeekCompleteListener( new OnSeekCompleteListener() {
			@Override
			public void onSeekComplete( MediaPlayer mp ) {
				mp.start();
			}
		} );

		mediaPlayer.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion( MediaPlayer mp ) {
				Ln.i( "onCompletion" );
				eventManager.fire( new VideoIsCompleteEvent( MediaPlayerViewModel.this, mp ) );
			}
		} );

		mediaPlayer.setLooping( false );
		Ln.d( "MediaPlayer media path: " + path.getUrl() );
		mediaPlayer.setDataSource( path.getUrl() );
		mediaPlayer.setDisplay( getHolder() );
		mediaPlayer.setOnPreparedListener( new OnPreparedListener() {
			@Override
			public void onPrepared( MediaPlayer mp ) {
				eventManager.fire( new MediaPlayerPrepared( mp ) );
			}
		} );
		mediaPlayer.prepareAsync();

		mediaPlayer.setOnBufferingUpdateListener( new OnBufferingUpdateListener() {
			@Override
			public void onBufferingUpdate( MediaPlayer mediaPlayer, int percent ) {
				eventManager.fire( new MediaPlayerBuffering( mediaPlayer, percent ) );
			}
		} );
	}

	public SurfaceHolder getHolder() {
		return holder;
	}

	public void setHolder( SurfaceHolder holder ) {
		this.holder = holder;
	}

	public void startFromBeginning() {
		mediaPlayer.seekTo( 0 );
	}

	void startFrom( long progressPosition ) {
		mediaPlayer.seekTo( (int)(progressPosition * 1000) );
	}
}
