package com.comcast.xideo.viewmodel;

import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BitmapObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.LongObservable;
import gueei.binding.observables.StringObservable;

import java.util.ArrayList;
import java.util.List;

import no.knowit.misc.GoldenAsyncTask;
import no.sumo.api.vo.asset.RestSearchAssetList;
import roboguice.event.EventManager;

import com.comcast.xideo.NetworkError;
import com.comcast.xideo.R;
import com.comcast.xideo.utils.BitmapLoaderEx;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vimond.entity.ImagePackReference;
import com.vimond.entity.Video;
import com.vimond.entity.VideoImpl;
import com.vimond.rest.Converters;
import com.vimond.service.StbChannelService;

public class ShowViewModel {
	public final LongObservable showId = new LongObservable();
	public final StringObservable showTitle = new StringObservable();
	public final BitmapObservable showImage = new BitmapObservable();

	public final StringObservable partnerName = new StringObservable();
	public final StringObservable description = new StringObservable();
	public final ArrayListObservable<VideoInfoViewModel> videos = new ArrayListObservable<VideoInfoViewModel>(VideoInfoViewModel.class);

	public final BooleanObservable isLoading = new BooleanObservable( false );

	@Inject
	private StbChannelService channelService;

	@Inject
	private Provider<VideoInfoViewModel> videoInfoViewModelProvider;

	@Inject
	private EventManager eventManager;

	@Inject
	BitmapLoaderEx bitmapLoader;

	@Inject
	Provider<ChannelInfoViewModel> channelInfoViewModelProvider;

	public void start() {
		isLoading.set( true );
		downloadAssets();
	}

	private void downloadAssets() {
		new GoldenAsyncTask<RestSearchAssetList>() {
			@Override
			protected void onPreExecute() throws Exception {
				super.onPreExecute();
				videos.clear();
				isLoading.set(true);
			}

			@Override
			public RestSearchAssetList call() throws Exception {
				return channelService.getAssets( showId.get() );
			}

			@Override
			protected void onSuccess( RestSearchAssetList assets ) throws Exception {
				setAssets( assets );
			}

			@Override
			protected void onException(Exception e) throws RuntimeException {
				eventManager.fire(new NetworkError());
			}

			@Override
			protected void onFinally() throws RuntimeException {
				isLoading.set(false);
			}

		}.execute();
	}

	private void setAssets( RestSearchAssetList assets ) {
		if (assets != null && assets.getAssets().size() > 0) {
			List<Video> videoList = Converters.convertToVideoList( assets );
			for (Video video : videoList) {
				VideoInfoViewModel vm;
				vm = videoInfoViewModelProvider.get();
				videos.add( vm );
				vm.setVideo( video );
				// vm.publisher.set( null );
				bitmapLoader.setDefaultImageResource( vm.image, R.drawable.xf_store_icon_film );
				bitmapLoader.loadImage( vm.image, video );
			}
		} else {
			ImagePackReference imgRef = new ImagePackReference( "4fef55116c4474e3cbfdcdc4" );
			List<Video> videoList = new ArrayList<Video>();
			for (int i = 1; i <= 10; i++) {
				VideoImpl video = new VideoImpl( i );
				video.setDescription( "demo video description " + i );
				video.setTitle( "sample title " + i );
				video.setPublisher( "sample publisher" );
				video.setImagePack( imgRef );
				video.setDuration( 3000L );
				video.setRating( "TV-MA" );
				videoList.add( video );
			}


			for (Video video : videoList) {
				VideoInfoViewModel vm;
				vm = videoInfoViewModelProvider.get();
				videos.add( vm );
				vm.setVideo( video );
				// vm.publisher.set( null );
				bitmapLoader.setDefaultImageResource( vm.image, R.drawable.xf_store_icon_film );
				bitmapLoader.loadImage( vm.image, "http://peowagstrom.files.wordpress.com/2010/03/struts-31.jpg" );
			}
		}
	}
}
