package com.comcast.xideo.viewmodel;

import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;

import java.util.List;

import no.knowit.misc.GoldenAsyncTask;

import com.comcast.xideo.IChannel;
import com.comcast.xideo.utils.BitmapLoader;
import com.comcast.xideo.videoplayer.ChannelSelectorItem;
import com.google.inject.Inject;
import com.vimond.entity.ChannelReference;
import com.vimond.entity.Video;
import com.vimond.service.StbChannelService;

public class ChannelSelectorViewModel implements IChannelListener {
	@Inject
	private BitmapLoader bitmapLoader;

	@Inject
	StbChannelService channelService;

	public final BooleanObservable isVisible = new BooleanObservable( false );
	public final ArrayListObservable<ChannelSelectorItem> channels = new ArrayListObservable<ChannelSelectorItem>( ChannelSelectorItem.class );

	private IChannelListener listener;

	public void setChannels( List<IChannel> channels ) {
		this.channels.clear();
		for( IChannel channel : channels ) {
			addChannel( channel );
		}
	}

	private void addChannel( IChannel channel ) {
		ChannelSelectorItem item = new ChannelSelectorItem( channel );
		item.setOnChannelSelectedListener( this );
		bitmapLoader.loadImage( item.channelLogo, channel );
		getNewestVideo( item );
		channels.add( item );
	}

	private void getNewestVideo( final ChannelSelectorItem item ) {
		new GoldenAsyncTask<Video>() {
			@Override
			public Video call() throws Exception {
				return channelService.getNewestVideo( item.getChannel() );
			}

			@Override
			protected void onSuccess( Video video ) throws Exception {
				setNewestVideo( item, video );
			};
		}.execute();
	}

	public void setOnChannelSelectedListener( IChannelListener listener ) {
		this.listener = listener;
	}

	@Override
	public void onChannelSelected( ChannelReference channel ) {
		isVisible.set( false );
		if( listener != null ) {
			listener.onChannelSelected( channel );
		}
	}

	private void setNewestVideo( final ChannelSelectorItem item, Video video ) {
		item.videoTitle.set( video.getTitle() );
		bitmapLoader.loadImage( item.videoImage, video );
	}
}
