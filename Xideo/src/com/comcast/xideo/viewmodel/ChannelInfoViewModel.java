package com.comcast.xideo.viewmodel;

import gueei.binding.Command;
import gueei.binding.observables.BitmapObservable;
import gueei.binding.observables.LongObservable;
import gueei.binding.observables.StringObservable;
import no.knowit.misc.GoldenAsyncTask;
import android.app.Activity;
import android.view.View;

import com.comcast.xideo.IChannel;
import com.google.inject.Inject;
import com.vimond.entity.ChannelReference;
import com.vimond.service.StbChannelService;

public class ChannelInfoViewModel implements ChannelReference {
	private IChannel channel;

	public final StringObservable title = new StringObservable();
	public final StringObservable publisher = new StringObservable();
	public final BitmapObservable logo = new BitmapObservable();
	public final BitmapObservable logoLarge = new BitmapObservable();
	public final LongObservable likes = new LongObservable();
	public final StringObservable description = new StringObservable();
	public final BitmapObservable newestVideoImage = new BitmapObservable();
	public final LongObservable numberOfVideos = new LongObservable();

	@Inject
	protected StbChannelService channelService;

	public Command openChannel = new Command() {
		@Override
		public void Invoke( View view, Object... arg1 ) {
			Activity act = (Activity)view.getContext();
//			act.startActivityForResult( ChannelActivity.createIntent( view.getContext(), getChannel() ), FrontPageActivity.SHOW_MY_CHANNELS_CODE );
		}
	};

	public Command onGainFocus = new Command() {
		@Override
		public void Invoke( View arg0, Object... arg1 ) {
			// TextView t = (TextView)arg0.findViewById( R.id.title );
			// Ln.i( t.getText().toString() );
			// t.setEllipsize(TruncateAt.MARQUEE);
			// t.setSelected( true );
		}
	};

	@Inject
	public ChannelInfoViewModel() {
	}

	public ChannelInfoViewModel( final IChannel channel, final StbChannelService channelService ) {
		this( channel );
		this.channelService = channelService;
		// updateNumberOfVideos( channel, channelService );
	}

	private void updateNumberOfVideos( final IChannel channel, final StbChannelService channelService ) {
		new GoldenAsyncTask<Long>() {
			@Override
			public Long call() throws Exception {
				return channelService.getNumberOfVideos( channel );
			};

			@Override
			protected void onSuccess( Long result ) throws Exception {
				numberOfVideos.set( result );
			};
		}.execute();
	}

	public ChannelInfoViewModel( final IChannel channel ) {
		setChannel( channel );
	}

	public void setChannel( final IChannel channel ) {
		this.channel = channel;

		title.set( channel.getTitle() );
		likes.set( channel.getLikes() );
		publisher.set( channel.getPublisher() );
		description.set( channel.getDescription() );

		// updateNumberOfVideos( channel, channelService );
	}

	@Override
	public long getId() {
		return getChannel().getId();
	}

	public IChannel getChannel() {
		return channel;
	}
}
