package com.comcast.xideo.viewmodel;

import gueei.binding.Command;
import gueei.binding.observables.BitmapObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.FloatObservable;
import gueei.binding.observables.LongObservable;
import gueei.binding.observables.StringObservable;

import java.text.DateFormat;
import java.util.List;

import no.knowit.misc.GoldenAsyncTask;
import roboguice.util.Ln;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.comcast.xideo.Constants;
import com.google.inject.Inject;
import com.vimond.entity.AssetProgress;
import com.vimond.entity.Video;

public class VideoInfoViewModel {
	private DateFormat dateFormat;
	private Video video;
	private Class<?> activityClass;
	
	@Inject
	public VideoInfoViewModel( DateFormat dateFormat ) {
		this.dateFormat = dateFormat;
	}

	public void setActivityClass(Class<?> clazz){
		this.activityClass = clazz;
	}
	
	public void setVideo( Video video ) {
		this.video = video;
		assetId.set( video.getId() );
		title.set( video.getTitle() );
		likes.set( video.getLikes() );
		description.set( video.getDescription() );
		try {
			date.set( dateFormat.format( video.getDate() ) );
		} catch( NullPointerException e ) {
			date.set( "No date" );
		}

		publisher.set( video.getPublisher() );
		duration.set(video.getDuration());
		rating.set( video.getRating() );
	}

	public List<Long> getPlaylist() {
		return playlist;
	}

	public void setPlaylist( List<Long> playlist ) {
		this.playlist = playlist;
	}

	public final LongObservable assetId = new LongObservable();
	public final StringObservable title = new StringObservable();
	public final StringObservable publisher = new StringObservable();
	public final BitmapObservable image = new BitmapObservable();
	public final LongObservable likes = new LongObservable();
	public final StringObservable description = new StringObservable();
	public final StringObservable date = new StringObservable();
	public final LongObservable duration = new LongObservable();
	public final BooleanObservable isNew = new BooleanObservable( false );
	public final FloatObservable videoProgress = new FloatObservable( 0f );
	public final BooleanObservable isFocused = new BooleanObservable( false );
	public final StringObservable rating = new StringObservable();

	private List<Long> playlist;

	public Command openVideo = new Command() {
		@Override
		public void Invoke( View view, Object... arg1 ) {
			Ln.i( "openVideo: %s", assetId );

			int pos = playlist.indexOf( assetId.get() );
			List<Long> useList = playlist.subList( pos, playlist.size() );

			long[] assetIds = new long[ useList.size() ];

			int index = 0;
			for( Long val : useList ) {
				assetIds[ index ] = val;
				index++;
			}
			Ln.i( "indexes " + assetIds );
			Activity act = (Activity)view.getContext();
			act.startActivityForResult( getIntent( assetIds, view.getContext() ), Constants.IntentIdentifiers.SHOW_MY_CHANNELS_CODE );
		}
	};

	public Intent getIntent( long[] assetIds, Context context ) {
		Intent intent = new Intent( context, activityClass);
		intent.putExtra( "assetIds", assetIds );
		return intent;
	}

	public Command onGainFocus = new Command() {
		@Override
		public void Invoke( View paramView, Object... paramArrayOfObject ) {
		}
	};

	public Command onFocusChanged = new Command() {
		@Override
		public void Invoke( View view, Object... args ) {
			Boolean hasFocus = (Boolean)args[ 0 ];
			if( hasFocus && onGainFocus != null ) {
				onGainFocus.InvokeCommand( view, VideoInfoViewModel.this );
			}
			isFocused.set( hasFocus );
		}
	};

	public void updateProgress( final AssetProgress progressService ) {
		new GoldenAsyncTask<Long>() {
			@Override
			public Long call() throws Exception {
				return progressService.getProgress( video );
			}

			@Override
			protected void onSuccess( Long progress ) throws Exception {
				long duration = video.getDuration();
				videoProgress.set( (float)progress / duration );
			}

			@Override
			protected void onException( Exception e ) throws RuntimeException {
				videoProgress.set( null );
			}
		}.execute();
	}
}
