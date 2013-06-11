package com.comcast.xideo.viewmodel;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.LongObservable;
import gueei.binding.observables.StringObservable;

import java.util.List;

import no.knowit.misc.GoldenAsyncTask;
import no.sumo.api.entity.vman.enums.ProgramSortBy;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.util.Ln;
import android.view.View;

import com.comcast.xideo.utils.BitmapLoader;
import com.comcast.xideo.IChannel;
import com.comcast.xideo.R;
import com.comcast.xideo.SortButtonList;
import com.comcast.xideo.SortEvent;
import com.goldengate.guice.DataReadyEvent;
import com.goldengate.guice.VideoInfoViewModelFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vimond.entity.AssetProgress;
import com.vimond.entity.CategoryReference;
import com.vimond.entity.ChannelReference;
import com.vimond.service.StbCategoryService;
import com.vimond.service.StbChannelService;

public class CategoryViewModel {
	public final StringObservable title = new StringObservable();
	public final IntegerObservable itemCount = new IntegerObservable();
	public final ArrayListObservable<ChannelInfoViewModel> channels = new ArrayListObservable<ChannelInfoViewModel>( ChannelInfoViewModel.class );
	public final Observable<ChannelInfoViewModel> selected = new Observable<ChannelInfoViewModel>( ChannelInfoViewModel.class );
	public final IntegerObservable selectedIndex = new IntegerObservable();
	public final BooleanObservable isLoading = new BooleanObservable( false );

	@Inject
	public SortButtonList sortButtons;

	@Inject
	private AssetProgress progressService;

	@Inject
	private VideoInfoViewModelFactory videoInfoViewModelFactory;

	@Inject
	private EventManager eventManager;

	public final Command show = new Command() {
		@Override
		public void Invoke( View view, Object... args ) {
			Ln.i( "showChannel: " + args.length );
			if( args.length > 0 ) {
				Object arg0 = args[ 0 ];
				Ln.i( "showChannel: " + arg0 );
				if( arg0 instanceof ChannelInfoViewModel ) {
					showChannel( (ChannelInfoViewModel)arg0 );
				}
				if( arg0 instanceof Long ) {
					showChannel( (Integer)arg0 );
				}
			}
		}
	};

	protected void showChannel( int index ) {
		showChannel( channels.get( index ) );
	}

	protected void showChannel( ChannelInfoViewModel channel ) {
		selectedIndex.set( channels.indexOf( channel ) + 1 );
		selected.set( channel );

		if( selected.get().numberOfVideos.isNull() ) {
			getNumberOfVideos( channel, selected.get().numberOfVideos );
		}
	}

	final BitmapLoader bitmapLoader;

	final StbChannelService channelService;

	final StbCategoryService categoryService;

	private CategoryReference category;

	@Inject
	Provider<ChannelInfoViewModel> channelInfoViewModelProvider;

	@Inject
	public CategoryViewModel( BitmapLoader bitmapLoader, StbCategoryService categoryService, StbChannelService channelService ) {
		this.bitmapLoader = bitmapLoader;
		this.categoryService = categoryService;
		this.channelService = channelService;
	}

	public void start( final CategoryReference category ) {
		addSortButtons();

		isLoading.set( true );
		this.category = category;
		downloadCategory( category );
		downloadChannels( category, sortButtons.getSortKey() );
	}

	private void addSortButtons() {
		sortButtons.clear();

		sortButtons.addButton( R.string.a_to_z, ProgramSortBy.CATEGORY_NAME );
		sortButtons.addButton( R.string.newest, ProgramSortBy.PRIORITY );
		sortButtons.addButton( R.string.recently_updated, ProgramSortBy.CHANNEL_PUBLISHED_DATE );
		sortButtons.addButton( R.string.most_viewed, ProgramSortBy.CHANNEL_VIEW_COUNT );
		sortButtons.addButton( R.string.most_popular, ProgramSortBy.CHANNEL_SUBSCRIBERS );

		sortButtons.setSortKey( ProgramSortBy.CATEGORY_NAME );
	}

	public void onSort( @Observes SortEvent event ) { // NO_UCD (unused code)
		if( event.getSource() == sortButtons ) {
			downloadChannels( category, event.getSortKey() );
		}
	}

	void downloadCategory( final CategoryReference category ) {
		new GoldenAsyncTask<Category>() {
			@Override
			public Category call() throws Exception {
				return categoryService.getCategory( category );
			}

			@Override
			protected void onSuccess( Category category ) {
				title.set( category.getTitle() );
			}

			@Override
			protected void onFinally() throws RuntimeException {
				isLoading.set( false );
			}
		}.execute();
	}

	void downloadChannels( final CategoryReference category, final ProgramSortBy sorting ) {
		isLoading.set( true );

		channels.clear();
		new GoldenAsyncTask<List<IChannel>>() {
			@Override
			public List<IChannel> call() throws Exception {
				return categoryService.getChannels( category, sorting );
			}

			@Override
			protected void onSuccess( List<IChannel> result ) {
				setChannels( result );
				notifyDataReady();
			}

			@Override
			protected void onFinally() throws RuntimeException {
				isLoading.set( false );
			}
		}.execute();
	}

	private void getNumberOfVideos( final ChannelReference channel, final LongObservable destination ) {
		new GoldenAsyncTask<Long>() {
			@Override
			public Long call() throws Exception {
				return channelService.getNumberOfVideos( channel );
			}

			@Override
			protected void onSuccess( Long numberOfVideos ) throws Exception {
				destination.set( numberOfVideos );
			};
		}.execute();
	}

	private void notifyDataReady() {
		eventManager.fire( new DataReadyEvent( this ) );
	}

	void setChannels( List<IChannel> result ) {
		for( IChannel channel : result ) {
			ChannelInfoViewModel vm = channelInfoViewModelProvider.get();
			vm.setChannel( channel );
			vm.onGainFocus = show;

			channels.add( vm );
			if( bitmapLoader != null ) {
				bitmapLoader.loadImage( vm.logo, channel );
			}
		}

		itemCount.set( channels.size() );
		if( channels.size() > 0 ) {
			selected.set( channels.get( 0 ) );
		}
	}
}
