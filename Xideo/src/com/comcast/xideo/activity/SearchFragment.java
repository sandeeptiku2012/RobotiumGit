package com.comcast.xideo.activity;

import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import no.knowit.misc.GoldenAsyncTask;
import no.sumo.api.entity.vman.enums.ProgramSortBy;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.vo.category.RestCategory;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

import com.comcast.xideo.IChannel;
import com.comcast.xideo.R;
import com.comcast.xideo.SearchTextEvent;
import com.comcast.xideo.XideoApplication;
import com.comcast.xideo.viewmodel.Category;
import com.comcast.xideo.viewmodel.ChannelInfoViewModel;
import com.comcast.xideo.viewmodel.ContentPanelViewModel;
import com.comcast.xideo.viewmodel.SearchViewModel;
import com.comcast.xideo.viewmodel.SearchViewModel.ViewType;
import com.comcast.xideo.viewmodel.XideoModel;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.squareup.otto.Subscribe;
import com.vimond.entity.CategoryReference;
import com.vimond.entity.ChannelImpl;
import com.vimond.entity.ContentPanelType;
import com.vimond.entity.TimelineType;
import com.vimond.service.StbCategoryService;
import com.vimond.service.StbSearchService;

@SuppressLint("NewApi")
public class SearchFragment extends BaseFragment implements OnFocusChangeListener, OnClickListener {

	ViewType currentView = ViewType.POPULATITY_VIEW;

	@Inject
	SearchViewModel model;
	ViewFlipper vf;

	ImageButton image1, image2, image3;

	public SearchFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return setAndBindRootView(R.layout.search_frag, container, model);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		prefetchChannelsForContentPanels();
		image1 = (ImageButton) mRootView.findViewById( R.id.image_button1 );
		image2 = (ImageButton) mRootView.findViewById( R.id.image_button2 );
		image3 = (ImageButton) mRootView.findViewById( R.id.image_button3 );
		vf = (ViewFlipper) mRootView.findViewById( R.id.viewflipper1 );
	}

	@Inject
	private StbCategoryService categoryService;

	@Inject
	private StbSearchService searchService;

	public final BooleanObservable isPrefetchCompleted = new BooleanObservable( false );
	public final IntegerObservable prefetchCount = new IntegerObservable();

	Observer prefetchObserver = new Observer() {
		@Override
		public void onPropertyChanged( IObservable<?> obs, Collection<Object> arg1 ) {
			if (prefetchCount.get().equals( 0 ))
				isPrefetchCompleted.set( true );
		}
	};

	Observer prefetchCompletedObserver = new Observer() {
		@Override
		public void onPropertyChanged( IObservable<?> arg0, Collection<Object> arg1 ) {
			prefetchCount.unsubscribe( prefetchObserver );
			isPrefetchCompleted.unsubscribe( prefetchCompletedObserver );
			vf.setDisplayedChild( 0 );

			bindPopularityPanel();

			categoryLoader = searchAllChannelsByCategory( category, "" );
			searchAllChannelsByTimelines( category, "" );

			image1.setOnFocusChangeListener( SearchFragment.this );
			image2.setOnFocusChangeListener( SearchFragment.this );
			image3.setOnFocusChangeListener( SearchFragment.this );

			image1.setOnClickListener( SearchFragment.this );
			image2.setOnClickListener( SearchFragment.this );
			image3.setOnClickListener( SearchFragment.this );
		}
	};

	Category category = new Category( 100 );

	private void prefetchChannelsForContentPanels() {
		channelList.clear();
		prefetchCount.set( 0 );
		isPrefetchCompleted.set( false );
		prefetchCount.subscribe( prefetchObserver );
		isPrefetchCompleted.subscribe( prefetchCompletedObserver );

		downloadChannels( category, ContentPanelType.FEATURED );
		downloadChannels( category, ContentPanelType.POPULAR );
		downloadChannels( category, ContentPanelType.RECOMMENDED );
	}

	private void downloadChannels( final CategoryReference category, final ContentPanelType type ) {
		new GoldenAsyncTask<List<IChannel>>() {
			protected void onPreExecute() throws Exception {
				prefetchCount.set( prefetchCount.get() + 1 );
			};

			@Override
			public List<IChannel> call() {
				return categoryService.getChannelsInContentPanel( category, type );
			}

			@Override
			protected void onSuccess( final List<IChannel> result ) throws Exception {
				setChannels( result, type );
			}

			protected void onException( Exception e ) {
				e.printStackTrace();
			};

			protected void onFinally() throws RuntimeException {
				prefetchCount.set( prefetchCount.get() - 1 );
			};
		}.execute();
	}

	HashMap<String, ArrayList<IChannel>> channelList = new HashMap<String, ArrayList<IChannel>>();

	@Inject
	Provider<ChannelInfoViewModel> channelInfoViewModelProvider;

	void setChannels( final List<IChannel> result, ContentPanelType type ) {
		ArrayList<IChannel> channels = channelList.get( type.name() );
		if(channels == null) {
			channels = new ArrayList<IChannel>();
			channelList.put( type.name(), channels );
		} else {
			channels.clear();
		}

		channels.addAll( result );
	}

	@Inject
	private Injector injector;

	private void bindPopularityPanel() {
		ContentPanelViewModel featured = new ContentPanelViewModel( "Featured", ContentPanelType.FEATURED );
		injector.injectMembers( featured );
		model.addContentPanel( featured, ViewType.POPULATITY_VIEW );
		featured.setStartDownload( true );

		ContentPanelViewModel popular = new ContentPanelViewModel( "Popular", ContentPanelType.POPULAR );
		injector.injectMembers( popular );
		model.addContentPanel( popular, ViewType.POPULATITY_VIEW );
		popular.setStartDownload( true );

		ContentPanelViewModel recommended = new ContentPanelViewModel( "Recommended", ContentPanelType.RECOMMENDED );
		injector.injectMembers( recommended );
		model.addContentPanel( recommended, ViewType.POPULATITY_VIEW );
		recommended.setStartDownload( true );

		getPopularityChannelsInPanel( featured );
		getPopularityChannelsInPanel( popular );
		getPopularityChannelsInPanel( recommended );
	}

	private void getPopularityChannelsInPanel( ContentPanelViewModel contentPanelModel ) {
		List<IChannel> channels = channelList.get( contentPanelModel.contentPanelType.name() );
		contentPanelModel.channels.clear();
		contentPanelModel.setChannels( channels );
	}

	private GoldenAsyncTask<List<IChannel>> filterChannelsByPopularity( final CategoryReference categoryRef, final String searchText ) {
		GoldenAsyncTask<List<IChannel>> task = new GoldenAsyncTask<List<IChannel>>() {
			protected void onPreExecute() throws Exception {
			};

			@Override
			public List<IChannel> call() {
				try {
					return searchService.getAllChannels( categoryRef, searchText, ProgramSortBy.CATEGORY_NAME );
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onSuccess( final List<IChannel> result ) throws Exception {
				bindPopularityChannels( result );
			}

			protected void onException( Exception e ) {
				e.printStackTrace();
			};

			protected void onFinally() throws RuntimeException {
			};

		};

		task.execute();
		return task;

	}

	private void bindPopularityChannels( List<IChannel> result ) {
		model.removeAllPanels( ViewType.POPULATITY_VIEW );
		List<IChannel> fchannels = channelList.get( ContentPanelType.FEATURED.name() );
		List<IChannel> list = new ArrayList<IChannel>();
		List<IChannel> remainingList = new ArrayList<IChannel>();
		boolean added = false;
		if (fchannels != null) {
			for (IChannel channel : result) {
				added = false;
				for (IChannel fchannel : fchannels) {
					if (channel.getId() == fchannel.getId()) {
						added = true;
						list.add( channel );
						break;
					}
				}
				if (!added) {
					remainingList.add( channel );
				}
			}

			if (list.size() > 0) {
				ContentPanelViewModel panel = new ContentPanelViewModel( "Featured", ContentPanelType.FEATURED );
				injector.injectMembers( panel );
				model.addContentPanel( panel, ViewType.POPULATITY_VIEW );
				panel.setStartDownload( true );
				panel.setChannels( list );
			}
		} else {
			remainingList = result;
		}

		result = remainingList;
		remainingList = new ArrayList<IChannel>();
		List<IChannel> pchannels = channelList.get( ContentPanelType.POPULAR.name() );
		if (pchannels != null) {
			list = new ArrayList<IChannel>();
			for (IChannel channel : result) {
				added = false;
				for (IChannel pchannel : pchannels) {
					if (channel.getId() == pchannel.getId()) {
						list.add( channel );
						added = true;
						break;
					}
				}
				if (!added) {
					remainingList.add( channel );
				}
			}
			if (list.size() > 0) {
				ContentPanelViewModel panel = new ContentPanelViewModel( "Popular", ContentPanelType.POPULAR );
				injector.injectMembers( panel );
				model.addContentPanel( panel, ViewType.POPULATITY_VIEW );
				panel.setStartDownload( true );
				panel.setChannels( list );
			}
		} else {
			remainingList = result;
		}

		result = remainingList;
		remainingList = new ArrayList<IChannel>();

		List<IChannel> rchannels = channelList.get( ContentPanelType.RECOMMENDED.name() );
		if (rchannels != null) {
			list = new ArrayList<IChannel>();
			for (IChannel channel : result) {
				added = false;
				for (IChannel rchannel : rchannels) {
					if (channel.getId() == rchannel.getId()) {
						list.add( channel );
						added = true;
						break;
					}
				}
				if (!added) {
					remainingList.add( channel );
				}
			}

			if (list.size() > 0) {
				ContentPanelViewModel panel = new ContentPanelViewModel( "Recommended", ContentPanelType.POPULAR );
				injector.injectMembers( panel );
				model.addContentPanel( panel, ViewType.POPULATITY_VIEW );
				panel.setStartDownload( true );
				panel.setChannels( list );
			}
		} else {
			remainingList = result;
		}

		if (remainingList.size() > 0) {
			ContentPanelViewModel panel = new ContentPanelViewModel( "", ContentPanelType.OTHERS );
			injector.injectMembers( panel );
			model.addContentPanel( panel, ViewType.POPULATITY_VIEW );
			panel.setStartDownload( true );
			panel.setChannels( remainingList );
		}

	}

	@Override
	public void onFocusChange( View view, boolean hasFocus ) {
		if (hasFocus) {
			switch (view.getId()) {

				case R.id.image_button1:
					vf.setDisplayedChild( 0 );
					currentView = ViewType.POPULATITY_VIEW;
					break;

				case R.id.image_button2:
					currentView = ViewType.CATEGORY_VIEW;
					vf.setDisplayedChild( 1 );
					break;

				case R.id.image_button3:
					currentView = ViewType.TIMELINE_VIEW;
					vf.setDisplayedChild( 2 );
					break;
			}
		}
	}

	GoldenAsyncTask<List<IChannel>> popularityLoader;
	GoldenAsyncTask<List<IChannel>> categoryLoader;

	private GoldenAsyncTask<List<IChannel>> searchAllChannelsByCategory( final CategoryReference categoryRef, final String searchText ) {
		model.removeAllPanels( ViewType.CATEGORY_VIEW );
		GoldenAsyncTask<List<IChannel>> task = new GoldenAsyncTask<List<IChannel>>() {
			protected void onPreExecute() throws Exception {
			};

			@Override
			public List<IChannel> call() {
				try {
					return searchService.getAllChannels( categoryRef, searchText, ProgramSortBy.CATEGORY_NAME );
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onSuccess( final List<IChannel> result ) throws Exception {
				bindCategorySearchResults( result );
			}

			protected void onException( Exception e ) {
				e.printStackTrace();
			};

			protected void onFinally() throws RuntimeException {
			};
		};

		task.execute();
		return task;
	}

	private void bindCategorySearchResults( List<IChannel> list ) {
		XideoModel xidioModel = XideoApplication.getModel();

		String prevCategoryName = null;
		String categoryName = null;
		for (IChannel channel : list) {
			ChannelImpl ci = (ChannelImpl) channel;
			String categoryPath = ci.getCategoryPath();
			String[] values = categoryPath.split( " " );
			long topMostCategoryId = Long.valueOf( values[4] );
			RestCategory category = xidioModel.categoryModel.getCategory( topMostCategoryId );
			categoryName = category.getTitle();
			ci.setCategoryName( categoryName );
		}

		Collections.sort( list, new CategoryComparator() );

		List<IChannel> channels = new ArrayList<IChannel>();
		for (IChannel channel : list) {
			categoryName = ((ChannelImpl) channel).getCategoryName();
			// Log.i( "", "catid:" + channel.getParentCategoryId() +
			// ", catname:" + categoryName );
			if (prevCategoryName == null || categoryName.equalsIgnoreCase( prevCategoryName )) {
				prevCategoryName = categoryName;
				channels.add( channel );
				continue;
			}

			ContentPanelViewModel panel = new ContentPanelViewModel( prevCategoryName, ContentPanelType.FEATURED );
			injector.injectMembers( panel );
			panel.setStartDownload( true );
			panel.setChannels( channels );
			model.addContentPanel( panel, ViewType.CATEGORY_VIEW );

			channels = new ArrayList<IChannel>();
			channels.add( channel );
			prevCategoryName = categoryName;
		}

		if (channels.size() > 0) {
			ContentPanelViewModel panel = new ContentPanelViewModel( categoryName, ContentPanelType.FEATURED );
			injector.injectMembers( panel );
			model.addContentPanel( panel, ViewType.CATEGORY_VIEW );
			panel.setStartDownload( true );
			panel.setChannels( channels );
		}
	}

	private class CategoryComparator implements Comparator<IChannel> {
		@Override
		public int compare( IChannel lhs, IChannel rhs ) {
			ChannelImpl lh = (ChannelImpl) lhs;
			ChannelImpl rh = (ChannelImpl) rhs;
			return lh.getCategoryName().compareToIgnoreCase( rh.getCategoryName() );
		}
	}

	ArrayList<GoldenAsyncTask<List<IChannel>>> timelineLoaders = new ArrayList<GoldenAsyncTask<List<IChannel>>>();

	private void searchAllChannelsByTimelines( CategoryReference categoryRef, String searchText ) {
		model.removeAllPanels( ViewType.TIMELINE_VIEW );
		timelineLoaders.clear();
		timelineLoaders.add( searchAllChannelsByTimeline( categoryRef, searchText, TimelineType.LAST_24_HOURS ) );
		timelineLoaders.add( searchAllChannelsByTimeline( categoryRef, searchText, TimelineType.LAST_ONE_MONTH ) );
		timelineLoaders.add( searchAllChannelsByTimeline( categoryRef, searchText, TimelineType.LAST_WEEK ) );
		timelineLoaders.add( searchAllChannelsByTimeline( categoryRef, searchText, TimelineType.LAST_YEAR ) );
	}

	private GoldenAsyncTask<List<IChannel>> searchAllChannelsByTimeline( final CategoryReference categoryRef, final String searchText,
			final TimelineType timeline ) {
		GoldenAsyncTask<List<IChannel>> task = new GoldenAsyncTask<List<IChannel>>() {
			protected void onPreExecute() throws Exception {
			};

			@Override
			public List<IChannel> call() {
				try {
					return searchService.getAllChannelsByTimeline( categoryRef, timeline.getQuery(), searchText, ProgramSortBy.CATEGORY_NAME );
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onSuccess( final List<IChannel> result ) throws Exception {
				bindTimelineSearchResults( result, timeline );
			}

			protected void onException( Exception e ) {
				e.printStackTrace();
			};

			protected void onFinally() throws RuntimeException {
			};
		};

		task.execute();
		return task;
	}

	private void bindTimelineSearchResults( List<IChannel> list, TimelineType type ) {
		if (list.size() > 0) {
			ContentPanelViewModel panel = new ContentPanelViewModel( type.getName(), ContentPanelType.FEATURED );
			injector.injectMembers( panel );
			model.addContentPanel( panel, ViewType.TIMELINE_VIEW );
			panel.setStartDownload( true );
			panel.setChannels( list );
		}
	}

	@Override
	public void onClick( View v ) {
		onFocusChange( v, true );
	}

	@Subscribe
	public void onSearhTextEntered( SearchTextEvent event ) {
		stopAllBackgroundLoaders();
		popularityLoader = filterChannelsByPopularity( category, event.getText() );
		categoryLoader = searchAllChannelsByCategory( category, event.getText() );
		searchAllChannelsByTimelines( category, event.getText() );
	}


	private void stopAllBackgroundLoaders() {
		if (popularityLoader != null) {
			popularityLoader.cancel( true );
		}
		if (categoryLoader != null)
			categoryLoader.cancel( true );
		for (GoldenAsyncTask<List<IChannel>> task : timelineLoaders) {
			task.cancel( true );
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		XideoApplication.getBus().register( this );
	}

	@Override
	public void onPause() {
		super.onPause();
		XideoApplication.getBus().unregister( this );
	}

}
