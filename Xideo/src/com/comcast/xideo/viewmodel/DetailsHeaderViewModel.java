package com.comcast.xideo.viewmodel;

import gueei.binding.observables.BitmapObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.StringObservable;
import no.sumo.api.vo.category.RestCategory;

import com.comcast.xideo.XideoApplication;
import com.comcast.xideo.utils.BitmapLoaderEx;
import com.google.inject.Inject;
import com.vimond.entity.ImagePackReference;
import com.vimond.math.Size;

public class DetailsHeaderViewModel {
	public final StringObservable categoryLevelType = new StringObservable();
	public final BitmapObservable imageThumb = new BitmapObservable();
	public final BooleanObservable isLoading = new BooleanObservable( false );

	public final StringObservable headerText1 = new StringObservable();
	public final StringObservable headerText2 = new StringObservable();
	public final StringObservable headerText3 = new StringObservable();

	public final StringObservable buttonText1 = new StringObservable();
	public final StringObservable buttonText2 = new StringObservable();
	public final StringObservable buttonText3_1 = new StringObservable();
	public final StringObservable buttonText3_2 = new StringObservable();

	private long categoryId;

	@Inject
	private BitmapLoaderEx bitmapLoader;

	public DetailsHeaderViewModel() {
		headerText1.set( "" );
		headerText2.set( "" );
		headerText3.set( "" );

		buttonText3_1.set( "" );
		buttonText3_2.set( "" );
	}

	public void setCategoryId( long id ) {
		categoryId = id;
		start();
	}

	public void setButtonTextDefaults() {
		buttonText2.set( "BACK" );
		buttonText2.set( "PREVIEW" );
		buttonText3_1.set( "SUBSCRIBE" );
	}

	public void start() {
		syncChannelInfo();
	}

	public void syncChannelInfo() {
		isLoading.set( true );
		XideoCategoriesModel model = XideoApplication.getModel().categoryModel;
		RestCategory category = null;
		if (model != null)
			category = model.getCategory( categoryId );

		if (category != null) {
			String title = category.getMetadata().get( "title" );
			if ("".equals( title ) || null == title) {
				title = category.getTitle();
			}
			headerText2.set( title );

			String desc = category.getMetadata().get( "description-short" );
			if (desc == null)
				desc = "";
			headerText3.set( desc );

			String publisher = category.getMetadata().get( "publisher" );
			if (null != publisher) {
				publisher = publisher.toUpperCase();
			} else
				publisher = "";
			headerText1.set( publisher );

			categoryLevelType.set( category.getLevel() );
			String imagePackId = category.getDefaultImagePackReference();
			if (null != imagePackId) {
				ImagePackReference ref = new ImagePackReference( imagePackId );
				bitmapLoader.loadChannelImage( imageThumb, ref, new Size( 320, 180 ) );
			}
		}
	}
}
