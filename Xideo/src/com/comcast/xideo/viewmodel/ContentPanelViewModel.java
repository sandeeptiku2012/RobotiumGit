package com.comcast.xideo.viewmodel;

import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

import java.util.List;

import no.knowit.misc.GoldenAsyncTask;

import com.comcast.xideo.IChannel;
import com.comcast.xideo.R;
import com.comcast.xideo.utils.BitmapLoaderEx;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vimond.entity.CategoryReference;
import com.vimond.entity.ContentPanelType;
import com.vimond.math.Size;
import com.vimond.service.StbCategoryService;

public class ContentPanelViewModel {
	static final int MAX_NUMBER_OF_CHANNELS = 99;

	public final BooleanObservable isVisible = new BooleanObservable(false);
	public final StringObservable title = new StringObservable();
	public final ArrayListObservable<ChannelInfoViewModel> channels = new ArrayListObservable<ChannelInfoViewModel>(ChannelInfoViewModel.class);
	public final IntegerObservable resultCount = new IntegerObservable(0);

	@Inject
	private StbCategoryService categoryService;

	@Inject
	private BitmapLoaderEx bitmapLoader;

	@Inject
	Provider<ChannelInfoViewModel> channelInfoViewModelProvider;

	public ContentPanelType contentPanelType;
	private CategoryReference category;
	private boolean startImageDownload = true;
	private Size[] imageSizes;
	private int maxChannelCount = 5;

	public ContentPanelViewModel(String string, ContentPanelType type) {
		this(string, type, MAX_NUMBER_OF_CHANNELS);
	}

	public ContentPanelViewModel(String string, ContentPanelType type, int maxChannelsCount) {
		title.set(string);
		contentPanelType = type;
		this.maxChannelCount = maxChannelsCount;
	}

	public void start(final CategoryReference category) {
		start(category, true, null);
	}

	public void start(final CategoryReference category, Size[] imageSizes) {
		start(category, true, imageSizes);
	}

	public void setStartDownload(boolean status) {
		this.startImageDownload = status;
	}

	public void start(final CategoryReference category, boolean startImageDownload, Size[] imageSizes) {
		this.category = category;
		this.startImageDownload = startImageDownload;
		this.imageSizes = imageSizes;
		isVisible.set(false);
		downloadChannels(category);
	}

	private void downloadChannels(final CategoryReference category) {
		new GoldenAsyncTask<List<IChannel>>() {
			@Override
			public List<IChannel> call() {
				return categoryService.getChannelsInContentPanel(category, contentPanelType);
			}

			@Override
			protected void onSuccess(final List<IChannel> result) throws Exception {
				setChannels(result);
			}

			protected void onException(Exception e) {
				e.printStackTrace();
			};

		}.execute();
	}

	public void setChannels( final List<IChannel> result ) {
		channels.clear();
		if (result != null && result.size() > 0) {
			List<IChannel> channelList = result.size() <= maxChannelCount ? result : result.subList( 0, maxChannelCount - 1 );
			resultCount.set( channelList.size() );
			for (IChannel channel : channelList) {
				ChannelInfoViewModel vm = channelInfoViewModelProvider.get();
				vm.setChannel( channel );
				if (startImageDownload) {
					Size imageSize1, imageSize2;
					imageSize1 = new Size( 270, 270 );
					imageSize2 = new Size( 480, 270 );
					if (imageSizes != null) {
						switch (imageSizes.length) {
							case 2:
								imageSize1 = imageSizes[0];
								imageSize2 = imageSizes[1];
								break;
							case 1:
								imageSize1 = imageSizes[0];
								break;
							default:
								break;
						}
					}
					// bitmapLoader.loadChannelImage( vm.logo,
					// channel.getImagePack(), imageSize1 );
					bitmapLoader.setDefaultImageResource( vm.logoLarge, R.drawable.xf_store_icon_film );
					bitmapLoader.loadChannelImage( vm.logoLarge, channel.getImagePack(), imageSize2 );
				}
				channels.add( vm );
			}

			isVisible.set( channels.size() > 0 );
		}
	}
}
