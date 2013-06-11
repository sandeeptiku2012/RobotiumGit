package com.comcast.xideo.viewmodel;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;

import java.util.ArrayList;
import java.util.List;

import no.knowit.misc.GoldenAsyncTask;
import roboguice.event.EventManager;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.comcast.xideo.R;
import com.comcast.xideo.utils.BitmapLoader;
import com.comcast.xideo.viewmodel.VideoInfoViewModel;
import com.goldengate.guice.DataReadyEvent;
import com.goldengate.guice.VideoInfoViewModelFactory;
import com.google.inject.Inject;
import com.vimond.entity.AssetProgress;
import com.vimond.entity.Video;
import com.vimond.service.StbFavoriteService;

public class FavoritesViewModel {
	public final ArrayListObservable<VideoInfoViewModel> videos = new ArrayListObservable<VideoInfoViewModel>( VideoInfoViewModel.class );
	public final IntegerObservable itemCount = new IntegerObservable();
	public final Observable<VideoInfoViewModel> selected = new Observable<VideoInfoViewModel>( VideoInfoViewModel.class );
	public final IntegerObservable selectedIndex = new IntegerObservable();
	public final BooleanObservable isLoading = new BooleanObservable( false );

	@Inject
	private Context context;

	@Inject
	private AssetProgress progressService;

	@Inject
	private VideoInfoViewModelFactory videoInfoViewModelFactory;

	@Inject
	private EventManager eventManager;

	public final Command show = new Command() {
		@Override
		public void Invoke( View view, Object... args ) {
			if( args.length > 0 && args[ 0 ] instanceof VideoInfoViewModel ) {
				showVideo( (VideoInfoViewModel)args[ 0 ] );
			}
		}
	};

	protected void showVideo( VideoInfoViewModel video ) {
		selectedIndex.set( videos.indexOf( video ) + 1 );
		selected.set( video );
	}

	protected void isLoading( Boolean isloading ) {
		isLoading.set( isloading );
	}

	private final BitmapLoader bitmapLoader;

	@Inject
	private StbFavoriteService favoriteService;

	@Inject
	public FavoritesViewModel( BitmapLoader bitmapLoader ) { // NO_UCD (unused code)
		this.bitmapLoader = bitmapLoader;
	}

	public void startFavorites() {
		isLoading( true );
		downloadFavorites();
	}

	private void downloadFavorites() {
		new GoldenAsyncTask<List<Video>>() {
			@Override
			public List<Video> call() throws Exception {
				return favoriteService.getFavoriteVideos( 40 );
			}

			@Override
			protected void onException( Exception e ) throws RuntimeException {
				Toast.makeText( context, R.string.could_not_find_favorite_videos_, Toast.LENGTH_SHORT ).show();
			};

			@Override
			protected void onSuccess( List<Video> result ) throws Exception {
				setVideos( result );
			};

			@Override
			protected void onFinally() throws RuntimeException {
				isLoading( false );
			};
		}.execute();
	}

	protected List<VideoInfoViewModel> convert( List<Video> videoList ) {
		ArrayList<VideoInfoViewModel> result = new ArrayList<VideoInfoViewModel>();
		ArrayList<Long> assetIds = new ArrayList<Long>( videoList.size() );

		for( Video video : videoList ) {
			assetIds.add( video.getId() );
			VideoInfoViewModel vm = videoInfoViewModelFactory.create( video );
			vm.onGainFocus = show;
			vm.updateProgress( progressService );
			bitmapLoader.loadImage( vm.image, video );
			result.add( vm );
		}

		for( VideoInfoViewModel vm : result ) {
			vm.setPlaylist( assetIds );
		}
		return result;
	}

	private void setVideos( List<Video> result ) {
		videos.setAll( convert( result ) );
		itemCount.set( videos.size() );
		selected.set( videos.get( 0 ) );
		eventManager.fire( new DataReadyEvent( FavoritesViewModel.this ) );
	}

	Command playVideoCommand = new Command() {
		@Override
		public void Invoke( View view, Object... args ) {
		}
	};
}
