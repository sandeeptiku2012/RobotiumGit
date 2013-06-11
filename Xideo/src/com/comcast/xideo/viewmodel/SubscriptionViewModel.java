package com.comcast.xideo.viewmodel;

import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;

import java.util.List;

import no.knowit.misc.GoldenAsyncTask;
import roboguice.event.EventManager;
import roboguice.util.Ln;

import com.comcast.xideo.IChannel;
import com.comcast.xideo.NetworkError;
import com.comcast.xideo.utils.BitmapLoaderEx;
import com.goldengate.guice.DataReadyEvent;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vimond.service.StbChannelService;

public class SubscriptionViewModel {
	public final BooleanObservable isLoading = new BooleanObservable( true );
	public final ArrayListObservable<ChannelRowViewModel> channels = new ArrayListObservable<ChannelRowViewModel>( ChannelRowViewModel.class );

	private final StbChannelService channelService;
	private final BitmapLoaderEx bitmapLoader;

	@Inject
	Provider<ChannelRowViewModel> channelRowViewModelProvider;

	@Inject
	private EventManager eventManager;

	@Inject
	public SubscriptionViewModel(StbChannelService channelService, BitmapLoaderEx bitmapLoader) {
		this.channelService = channelService;
		this.bitmapLoader = bitmapLoader;
	}

	public void start() {
		downloadMyChannels();
	}

	public void resume() {
		downloadMyChannels();
	}

	private void downloadMyChannels() {
		isLoading.set(true);
		new GoldenAsyncTask<List<IChannel>>() {
			@Override
			protected void onPreExecute() throws Exception {
				super.onPreExecute();
				channels.clear();
				isLoading.set(true);
			}

			@Override
			public List<IChannel> call() throws Exception {
				return channelService.getMyChannels();
			}

			@Override
			protected void onSuccess( List<IChannel> channels ) throws Exception {
				setMyChannels( channels );
			}

			@Override
			protected void onException( Exception e ) throws RuntimeException {
				eventManager.fire( new NetworkError() );
			}

			@Override
			protected void onFinally() throws RuntimeException {
				isLoading.set(false);
			}
		}.execute();
	}

	private void setMyChannels( List<IChannel> channels ) {
		Ln.i( "Received %d channels", channels.size() );
		ArrayListObservable<ChannelRowViewModel> destination = SubscriptionViewModel.this.channels;
		for( IChannel channel : channels ) {
			ChannelRowViewModel vm;
			vm = channelRowViewModelProvider.get();
			vm.setChannel( channel );
			vm.title.set( channel.getTitle() );
			destination.add( vm );
			bitmapLoader.loadImage( vm.logo, channel );
			// getNumberOfNewVideos( vm );
		}
		isLoading.set(false);
		notifyDataReady();
	}

	private void notifyDataReady(){
		eventManager.fire(new DataReadyEvent(this));
	}
	
	public void getNumberOfNewVideos( final ChannelRowViewModel channelRow ) {
		new GoldenAsyncTask<Long>() {
			@Override
			public Long call() throws Exception {
				return channelService.getNumberOfNewVideos( channelRow.getChannel() );
			}

			@Override
			protected void onSuccess( Long number ) throws Exception {
				channelRow.badgeCount.set( number );
			};
		}.execute();
	}
}