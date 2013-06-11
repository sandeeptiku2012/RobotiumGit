package com.comcast.xideo.videoplayer;

import gueei.binding.Command;
import gueei.binding.observables.BitmapObservable;
import gueei.binding.observables.StringObservable;
import android.view.View;

import com.comcast.xideo.IChannel;
import com.comcast.xideo.viewmodel.IChannelListener;
import com.vimond.entity.ChannelReference;

public class ChannelSelectorItem {
	public final StringObservable channelTitle = new StringObservable();
	public final BitmapObservable channelLogo = new BitmapObservable();

	public final StringObservable videoTitle = new StringObservable();
	public final BitmapObservable videoImage = new BitmapObservable();

	public Command onClick = new Command() {
		@Override
		public void Invoke( View view, Object... args ) {
			if( listener != null ) {
				listener.onChannelSelected( getChannel() );
			}
		}
	};

	private IChannel channel;

	private IChannelListener listener;

	public void setOnChannelSelectedListener( IChannelListener listener ) {
		this.listener = listener;
	}

	public ChannelSelectorItem( IChannel channel ) {
		this.channel = channel;
		channelTitle.set( channel.getTitle() );
	}

	public IChannel getChannel() {
		return channel;
	}
}
