package com.goldengate.guice;

import com.comcast.xideo.viewmodel.VideoInfoViewModel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vimond.entity.Video;

public class VideoInfoViewModelFactory {
	@Inject
	private Provider<VideoInfoViewModel> provider;

	@Inject
	public VideoInfoViewModelFactory() {
	}

	public VideoInfoViewModel create( Video video ) {
		VideoInfoViewModel vm = provider.get();
		vm.setVideo( video );
		return vm;
	}
}
