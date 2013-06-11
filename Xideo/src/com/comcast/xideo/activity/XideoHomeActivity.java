package com.comcast.xideo.activity;

import gueei.binding.IObservable;
import gueei.binding.Observer;

import java.util.Collection;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import roboguice.event.EventThread;
import roboguice.event.Observes;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.comcast.ui.libv1.widget.AnimatedFocusHopperView;
import com.comcast.ui.libv1.widget.MenuLayoutEx.MenuItemEx;
import com.comcast.ui.libv1.widget.MenuLayoutEx.OnMenuExListener;
import com.comcast.xideo.DetailActivity;
import com.comcast.xideo.FocusChangeEvent;
import com.comcast.xideo.ImageEffects;
import com.comcast.xideo.ItemSelectedEvent;
import com.comcast.xideo.MenuChangedEvent;
import com.comcast.xideo.MissingUserError;
import com.comcast.xideo.NetworkError;
import com.comcast.xideo.R;
import com.comcast.xideo.XideoApplication;
import com.comcast.xideo.customview.XidioMenuLayout;
import com.comcast.xideo.events.ChannelItemEvent;
import com.comcast.xideo.viewmodel.ChannelInfoViewModel;
import com.comcast.xideo.viewmodel.XideoModel;
import com.google.inject.Inject;
import com.squareup.otto.Subscribe;
import com.vimond.service.StbAuthenticationService;

public class XideoHomeActivity extends BaseActivity implements OnMenuExListener {

	@Inject
	private XideoModel model;

	@Inject
	private StbAuthenticationService authservice;

	@Inject
	FragmentManager fragmentManager;
	
	AnimatedFocusHopperView blobvw1;
	
	@InjectView (R.id.viewpager1)
	ViewPager viewpager1;

	PagerAdapter adapter;

	@InjectView(R.id.menuEx1)
	XidioMenuLayout menu;

	View rootBackgroundView;

	boolean firstLaunch = true;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAndBindRootView( R.layout.activity_xideo_home, model );
		
		blobvw1 = (AnimatedFocusHopperView) findViewById(R.id.blobvw1);
		if(blobvw1 != null)
			// blobvw1.setBlobBitmap(R.drawable.blob_hexagon_white_trans_bkng);
			blobvw1.setContentLayout(R.layout.focus_hopper);
	
		if(viewpager1 != null){
			adapter = new PagerAdapter(fragmentManager);
			viewpager1.setAdapter(adapter);
			viewpager1.setFocusable(false);
			viewpager1.setOffscreenPageLimit(3);
		}

		rootBackgroundView = findViewById(R.id.bkgContent);
		setupBackgroundAnimations();
		model.isLoading.subscribe( new Observer() {
			@Override
			public void onPropertyChanged( IObservable<?> arg0, Collection<Object> arg1 ) {
				if (model.isLoading.get() == false) {
					menu.setVisibility( View.VISIBLE );
					menu.requestFocus();
				}
			}
		} );

		menu.setListener( this );
	}


	ConcurrentLinkedQueue<ImageView> pendingList = new ConcurrentLinkedQueue<ImageView>();
	BackgroundBitmapLoaderAsync backgroundBitmapLoader = new BackgroundBitmapLoaderAsync();

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Subscribe
	public void onNewItemSelected(ItemSelectedEvent e) {
		Object obj = e.getValue();
		if (obj instanceof ImageView) {
			ImageView sourceIv = (ImageView) obj;

			animIn.cancel();
			if (backgroundBitmapLoader != null) {
				backgroundBitmapLoader.cancel( true );
				backgroundBitmapLoader = null;
			}

			pendingList.clear();
			// process only the last one
			pendingList.add( sourceIv );
			backgroundBitmapLoader = new BackgroundBitmapLoaderAsync();
			backgroundBitmapLoader.execute();
		}
	}

	@Subscribe
	public void onChannelSelected( ChannelItemEvent e ) {
		Object obj = e.getValue();
		long categoryId = 0;
		if (obj instanceof ChannelInfoViewModel) {
			obj = (ChannelInfoViewModel) obj;
			categoryId = ((ChannelInfoViewModel) obj).getChannel().getId();
		}
		Intent intent = new Intent( this, DetailActivity.class );
		intent.putExtra( "categoryId", categoryId );
		startActivity( intent );
	}

	class BackgroundBitmapLoaderAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!isCancelled()) {
				while (pendingList.size() > 0) {
					ImageView sourceIv = pendingList.poll();
					// skip till the last imageview reference
					if (pendingList.size() == 0) {
						// calling the old method which runs on main thread
						applyBitmapEffect( sourceIv, rootBackgroundView );
						// need to work on this later
						// applyBitmapEffectAsync(sourceIv, rootView);
						animIn.start();
					}
				}
			}
		}
	}

	ObjectAnimator animIn;

	void setupBackgroundAnimations() {
		animIn = ObjectAnimator.ofFloat(rootBackgroundView, "alpha", 0.0f, 1.0f);
		animIn.setDuration(300);
		animIn.setInterpolator(new AccelerateInterpolator());
	}

	WeakHashMap<String, BitmapDrawable> defaultBkgImageCache = new WeakHashMap<String, BitmapDrawable>();
	final String BKG_IMAGE_CACHE_KEY = "defaultBkgImageCache";
	int lastImageDrawableHashcode;

	@SuppressLint("NewApi")
	void applyBitmapEffect(ImageView sourceIv, View destView) {
		Bitmap result;
		Drawable background;
		BitmapDrawable bd = (BitmapDrawable) sourceIv.getDrawable();
		Bitmap srcBitmap = null;
		if (bd == null) {
			if (defaultBkgImageCache.containsKey(BKG_IMAGE_CACHE_KEY)) {
				bd = defaultBkgImageCache.get(BKG_IMAGE_CACHE_KEY);
			}

			if (bd == null) {
				bd = (BitmapDrawable) getResources().getDrawable(R.drawable.default_background_image);
				defaultBkgImageCache.put(BKG_IMAGE_CACHE_KEY, bd);
			} else {
				// skip reloading the same image
				if (lastImageDrawableHashcode == bd.hashCode())
					return;
			}

			srcBitmap = bd.getBitmap();
		} else {
			try {
				srcBitmap = bd.getBitmap();
				// Bitmap tempBitmap = bd.getBitmap();
				// srcBitmap = tempBitmap.copy( Config.ARGB_8888, true );
				// tempBitmap.recycle();
			} catch (Exception ex) {
			}
		}

		if (srcBitmap != null) {
			lastImageDrawableHashcode = bd.hashCode();

			// soften brightness
			Bitmap tresult = ImageEffects.soften( srcBitmap, 40 );
			// apply Gaussian blur
			result = ImageEffects.applyBlur( tresult, 20 );
			tresult.recycle();
			background = new BitmapDrawable( this.getResources(), result );
			// map.put(key, background);

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

	// experimental:: need to apply blur effect on background thread
	// -- not working
	void applyBitmapEffectAsync(ImageView sourceIv, View destView) {
		BitmapDrawable bd = (BitmapDrawable) sourceIv.getDrawable();
		Bitmap srcBitmap;
		if (bd == null) {
			bd = (BitmapDrawable) getResources().getDrawable(R.drawable.default_background_image);
			srcBitmap = bd.getBitmap();
		} else {
			Bitmap tempBitmap = bd.getBitmap();
			srcBitmap = tempBitmap.copy(Config.ARGB_8888, true);
		}
		ImageEffectAsyncTask t = new ImageEffectAsyncTask(this);
		t.setSource(srcBitmap);
		t.setDestinationView(destView);
		t.execute();
	}

	class ImageEffectAsyncTask extends AsyncTask<Void, Void, Bitmap> {
		Bitmap srcBitmap = null;
		View destView;
		Context context;

		protected ImageEffectAsyncTask(Context context) {
			super();
			this.context = context;
		}

		void setSource(Bitmap srcBitmap) {
			this.srcBitmap = srcBitmap;
		}

		void setDestinationView(View view) {
			this.destView = view;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			/*
			 * getting error on following JNI call: A/libc(5262): Fatal signal
			 * 11 (SIGSEGV) at 0x6254c000 (code=2), thread 5419 (AsyncTask #1)
			 */
			// apply Gaussian blur with radius 20
			Bitmap result = ImageEffects.applyBlur(srcBitmap, 20);
			return result;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Bitmap result) {
			Drawable background = new BitmapDrawable(context.getResources(), result);
			try {
				if (Build.VERSION.SDK_INT >= 16) {
					destView.setBackground(background);
				} else {
					destView.setBackgroundDrawable(background);
				}
			} catch (Exception ex) {
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	Thread thread;

	@Override
	protected void onPause() {
		super.onPause();
		XideoApplication.getBus().unregister(this);
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
		}

		thread = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		XideoApplication.getBus().register(this);
		if (firstLaunch) {
			firstLaunch = false;
			menu.setVisibility( View.VISIBLE );
			menu.requestFocus();
		}
		// thread = new DelayedKeyPressThread( this );
		// thread.start();
	}

	@Subscribe
	public void onNewFocusChangeEvent(FocusChangeEvent e) {
		Point[] points = e.getPoints();
		blobvw1.animateBlob(points);
	}

	public void onNetworkError( @Observes NetworkError error ) {
		Ln.i( "onNetworkError" );
//		startActivity( new Intent( this, NetworkErrorActivity.class ) );
	}

	public void onMissingUserError( @Observes( EventThread.UI ) MissingUserError error ) {
		Ln.i( "onMissingUserError" );
		CommonActions.checkUser( this, authservice );
	}

	public void onMenuItemChanged( @Observes MenuChangedEvent event ) {
	}

	class PagerAdapter extends FragmentPagerAdapter{

		public PagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		private RoboFragment getItemAt(int index){
			RoboFragment fragment = null;

			switch(index){
			case 0:
				fragment = new SearchFragment();
				break;

			case 1:
				fragment = new HomeFragment();
				break;

			case 2:
				fragment = new SubscriptionFragment();
				break;

			case 3:
				fragment = new SettingsFragment();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Fragment getItem(int index) {
			return getItemAt(index);
		}
	}

	@Override
	public void onMenuItemAdded( MenuItemEx menuItem, View view, int index ) {
	}

	@Override
	public void onMenuItemSelected( MenuItemEx menuItem, View view, int index ) {

	}

	@Override
	public void onMenuItemChanged( MenuItemEx menuItem, View view, int index, boolean hasFocus ) {
		if (hasFocus) {
			int pageId = menuItem.id - XidioMenuLayout.MENU_ID_SEARCH;
			viewpager1.setCurrentItem( pageId, true );
		}
	}

	@Override
	public void onMenuItemFocusChanged( MenuItemEx menuItem, View view, int index, boolean hasFocus ) {
	}

	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event ) {
		if(menu.dispatchKeyEvent( event ))
			menu.requestFocus();
		return super.onKeyDown( keyCode, event );
	}

}
