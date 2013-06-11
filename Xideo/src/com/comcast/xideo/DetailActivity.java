package com.comcast.xideo;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.comcast.videoplayer.ui.PlayerActivity;
import com.comcast.xideo.activity.BaseActivity;
import com.comcast.xideo.customview.DetailsHeaderView;
import com.comcast.xideo.customview.DetailsHeaderView.ActionName;
import com.comcast.xideo.customview.DetailsHeaderView.OnActionListener;
import com.comcast.xideo.events.VideoItemEvent;
import com.comcast.xideo.viewmodel.DetailsActivityViewModel;
import com.comcast.xideo.viewmodel.VideoInfoViewModel;
import com.google.inject.Inject;
import com.squareup.otto.Subscribe;

public class DetailActivity extends BaseActivity implements OnActionListener {

	View rootBackgroundView;
	String categoryLevelType;
	long categoryId;

	@Inject
	DetailsActivityViewModel model;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setAndBindRootView( R.layout.activity_detail, model );
		DetailsHeaderView view;

		int w_focus = (int) getResources().getDimension( R.dimen.details_header_image_thumb_width );
		int h_focus = (int) getResources().getDimension( R.dimen.details_header_image_thumb_height );
		int w_nofocus = (int) getResources().getDimension( R.dimen.details_header_image_thumb_width_no_focus );
		int h_nofocus = (int) getResources().getDimension( R.dimen.details_header_image_thumb_height_no_focus );

		view = (DetailsHeaderView) findViewById( R.id.details1 );
		view.setImageWidthHeight( w_focus, h_focus, true );
		view.setImageWidthHeight( w_nofocus, h_nofocus, false );

		view.setActionListener( this );

		rootBackgroundView = findViewById( R.id.bkgContent );
		setupBackgroundAnimations();

		categoryLevelType = getIntent().getStringExtra( "categoryLevelType" );
		categoryId = getIntent().getLongExtra( "categoryId", 211 );
		// categoryId = 2011136021;
		if ("SHOW".equalsIgnoreCase( categoryLevelType )) {
			view.setButtonExtra3Visibility( View.GONE );
		}

		view.requestFocus( View.FOCUS_RIGHT );
		model.setCategoryId( categoryId );
	}

	ObjectAnimator animIn;

	void setupBackgroundAnimations() {
		animIn = ObjectAnimator.ofFloat( rootBackgroundView, "alpha", 0.0f, 1.0f );
		animIn.setDuration( 300 );
		animIn.setInterpolator( new AccelerateInterpolator() );
	}

	BackgroundBitmapLoaderAsync backgroundBitmapLoader = new BackgroundBitmapLoaderAsync();

	ImageView sourceImageView;

	@Subscribe
	public void onShowSelected( VideoItemEvent e ) {
		Object obj = e.getValue();
		long assetId = 0;
		if (obj instanceof VideoInfoViewModel) {
			obj = (VideoInfoViewModel) obj;
			assetId = ((VideoInfoViewModel) obj).assetId.get();

			Intent intent = new Intent( this, PlayerActivity.class );
			intent.putExtra( "assetId", assetId );
			Log.e( "DetailActivity", "assetId:" + assetId );
			startActivity( intent );
		}
	}

	@Subscribe
	public void onNewItemSelected( ItemSelectedEvent e ) {
		Object obj = e.getValue();
		if (obj instanceof ImageView) {
			ImageView sourceIv = (ImageView) obj;

			animIn.cancel();
			if (backgroundBitmapLoader != null) {
				backgroundBitmapLoader.cancel( true );
				backgroundBitmapLoader = null;
			}

			sourceImageView = sourceIv;
			backgroundBitmapLoader = new BackgroundBitmapLoaderAsync();
			backgroundBitmapLoader.execute();
		}
	}

	@SuppressLint("NewApi")
	void applyBitmapEffect( ImageView sourceIv, View destView ) {
		Bitmap result;
		Drawable background;
		BitmapDrawable bd = (BitmapDrawable) sourceIv.getDrawable();
		Bitmap srcBitmap = null;
		if (bd == null) {
			bd = (BitmapDrawable) getResources().getDrawable( R.drawable.default_background_image );
			srcBitmap = bd.getBitmap();
		} else {
			try {
				srcBitmap = bd.getBitmap();
			} catch (Exception ex) {
			}
		}

		if (srcBitmap != null) {
			// soften brightness
			Bitmap tresult = ImageEffects.soften( srcBitmap, 40 );
			// apply Gaussian blur
			result = ImageEffects.applyBlur( tresult, 20 );
			tresult.recycle();
			background = new BitmapDrawable( this.getResources(), result );

			try {
				if (Build.VERSION.SDK_INT >= 16) {
					destView.setBackground( background );
				} else {
					destView.setBackgroundDrawable( background );
				}
			} catch (Exception ex) {
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		XideoApplication.getBus().unregister( this );
	}

	@Override
	protected void onResume() {
		super.onResume();
		XideoApplication.getBus().register( this );
	}

	class BackgroundBitmapLoaderAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground( Void... params ) {
			try {
				Thread.sleep( 4000 );
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute( Void result ) {
			if (!isCancelled()) {
				if (sourceImageView != null) {
					// calling the old method which runs on main thread
					applyBitmapEffect( sourceImageView, rootBackgroundView );
					// need to work on this later
					animIn.start();
				}
			}
		}
	}


	@Override
	public void onActionSelected( ActionName action ) {
		switch (action) {
			case BACK:
				finish();
				break;

			// handle subscribe / unsubscribe here
			case EXTRA_2:
				break;

			// handle preview action here
			case EXTRA_1:
				break;

			// handle channel/show view
			case ICON:
				break;
		}
	}
}
