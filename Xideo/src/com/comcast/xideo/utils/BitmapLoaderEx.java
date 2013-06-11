package com.comcast.xideo.utils;

import gueei.binding.Observable;
import gueei.binding.observables.BitmapObservable;
import gueei.binding.observables.BooleanObservable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

import roboguice.util.Ln;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import com.comcast.xideo.Constants;
import com.comcast.xideo.IChannel;
import com.comcast.xideo.ImageLocation;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vimond.entity.ImagePackReference;
import com.vimond.entity.Video;
import com.vimond.entity.VideoReference;
import com.vimond.math.Size;

@Singleton
public class BitmapLoaderEx implements Runnable {
	static MessageDigest md5Instance;
	static LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>( 50 );
	static final int BUFFER_SIZE = 8 * 1024;
	static final int MAX_URL_KEY_ENTRIES = 500;
	static String defaultImageCacheKey;
	static LruCache<String, String> URLKeyCache = new LruCache<String, String>( MAX_URL_KEY_ENTRIES );

	private static String md5Sum( final String url ) throws UnsupportedEncodingException {
		String cacheKey = URLKeyCache.get( url );
		if (cacheKey == null) {
			md5Instance.update( url.getBytes( "UTF-8" ) );
			byte messageDigest[] = md5Instance.digest();

			StringBuffer hexString = new StringBuffer();
			for (byte element : messageDigest) {
				hexString.append( Integer.toHexString( 0xFF & element ) );
			}

			cacheKey = hexString.toString();
			URLKeyCache.put( url, cacheKey );
		}
		return cacheKey;
	}

	static {
		try {
			md5Instance = MessageDigest.getInstance( "MD5" );
			defaultImageCacheKey = md5Sum( Constants.DEFAULT_VIDEO_IMAGE );
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static class BitmapEntry {
		public Observable<Bitmap> bo;
		public String url;
		public BooleanObservable isLoading = new BooleanObservable( false );
		public String cacheKey;
		public String cachedFileName;
		public Bitmap bitmap;
	}

	ConcurrentLinkedQueue<BitmapEntry> pendingList = new ConcurrentLinkedQueue<BitmapEntry>();

	@Inject
	Context context;

	public static boolean downloadImageToFile( URL url, String filePath, Context c ) throws IOException {

		InputStream input = null;
		FileOutputStream output = null;
		try {
			Log.e( "", "downloadfilepath:" + filePath );
			File file = new File( filePath );
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();

			input = url.openConnection().getInputStream();
			output = new FileOutputStream( filePath, false );

			int read;
			byte[] data = new byte[BUFFER_SIZE];
			while ((read = input.read( data )) != -1)
				output.write( data, 0, read );

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;

		} finally {
			if (output != null)
				output.close();
			if (input != null)
				input.close();
		}
	}

	@Deprecated
	public void loadVideoThumbnail( final Observable<Bitmap> observable, final VideoReference video ) {
		String url = String.format( Locale.US, Constants.VIDEO_THUMBNAIL_URL, video.getId() );
		addImageUrlToQueue( url, observable );
		Ln.w( "Using deprecated method to retrieve video image, AssetID = %s", video.getId() );
	}

	public static final String urlFormat = "%s/%s?location=%s&width=%d&height=%s";
	public void loadImage(final Observable<Bitmap> target, ImagePackReference imagePack, ImageLocation location, Size size) {
		if (imagePack != null) {
			String locationName = location.getName();
			String url = String.format( Locale.US, urlFormat, Constants.VIMOND_IMAGE_SERVICE_BASE_URI, imagePack.getId(), locationName,
					size.getWidth(), "" );
			Log.e( "", "image url:" + url );
			addImageUrlToQueue( url, target );
		}
	}

	public void loadImage( final Observable<Bitmap> target, String url ) {
		addImageUrlToQueue( url, target );
	}

	public void loadImage( final Observable<Bitmap> observable, final Video video ) {
		if( video.getImagePack() != null ) {
			loadImage( observable, video.getImagePack(), ImageLocation.VIDEO_THUMBNAIL, new Size( 192, 108 ) );
		} else {
			loadVideoThumbnail( observable, video );
		}
	}

	public void loadImage( final Observable<Bitmap> observable, final IChannel channel ) {
		if( channel.getImagePack() != null ) {
			loadImage( observable, channel.getImagePack(), ImageLocation.CHANNEL_LOGO, new Size( 192, 108 ) );
		}
	}

	public void loadBigChannelLogo( BitmapObservable observable, IChannel channel ) {
		if( channel.getImagePack() != null ) {
			loadImage( observable, channel.getImagePack(), ImageLocation.CHANNEL_LOGO, new Size( 320, 180 ) );
		}
	}

	public void loadFeaturedImage( BitmapObservable logo, ImagePackReference imagePack, boolean doubled ) {
		ImageLocation location = doubled ? ImageLocation.BANNER_DOUBLE : ImageLocation.BANNER_SINGLE;
		loadImage( logo, imagePack, location );
	}

	public void loadImage( BitmapObservable logo, ImagePackReference imagePack, ImageLocation location ) {
		Size size = location == ImageLocation.BANNER_DOUBLE ? new Size( 790, 258 ) : new Size( 390, 258 );
		loadImage( logo, imagePack, location, size );
	}
	
	public void loadVideoThumbnailImage( BitmapObservable logo, ImagePackReference imagePack, Size size ) {
		loadImage( logo, imagePack, ImageLocation.VIDEO_THUMBNAIL, size );
	}

	public void loadChannelImage( BitmapObservable logo, ImagePackReference imagePack, Size size ) {
		loadImage( logo, imagePack, ImageLocation.CHANNEL_LOGO, size );
	}

	Thread thread;

	private void addImageUrlToQueue( String url, Observable<Bitmap> bo ) {
		if (url == null) {
			url = Constants.DEFAULT_VIDEO_IMAGE;
		}

		BitmapEntry entry = new BitmapEntry();
		entry.url = url;
		entry.bo = bo;
		try {
			entry.cacheKey = md5Sum( url );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		pendingList.add( entry );
		if (thread == null || pendingList.size() == 1) {
			thread = new Thread( this );
			thread.start();
		}
	}

	@Override
	public void run() {
		ConcurrentLinkedQueue<BitmapEntry> cachedFiles = new ConcurrentLinkedQueue<BitmapEntry>();
		ConcurrentLinkedQueue<BitmapEntry> nonCachedFiles = new ConcurrentLinkedQueue<BitmapEntry>();
		final BitmapFactory.Options options = new Options();
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		options.inSampleSize = 2;
		options.inTempStorage = new byte[16 * 1024];
		Bitmap bitmap;

		while (pendingList.size() > 0) {
			while (pendingList.size() > 0) {
				BitmapEntry entry = pendingList.poll();
				if (entry != null) {
					bitmap = memoryCache.get( entry.cacheKey );
					if (bitmap != null) {
						entry.bitmap = bitmap;
						showBitmap( entry );
						continue;
					} else {
						entry.cachedFileName = getCacheFileName( entry.cacheKey );
						if (isCacheFileExists( entry.cachedFileName )) {
							cachedFiles.add( entry );
						} else {
							nonCachedFiles.add( entry );
						}
					}
				}
			}

			while (cachedFiles.size() > 0) {
				BitmapEntry entry = cachedFiles.poll();
				if (entry != null) {
					bitmap = BitmapFactory.decodeFile( entry.cachedFileName, options );
					entry.bitmap = bitmap;
					showBitmap( entry );
					// Log.e( "bitmaploader", entry.cachedFileName +
					// " memory cache - hit" );
					if (bitmap != null) {
						memoryCache.put( entry.cacheKey, bitmap );
					}
				}
			}

			while (nonCachedFiles.size() > 0) {
				bitmap = null;
				BitmapEntry entry = nonCachedFiles.poll();
				if (entry != null) {
					try {
						entry.isLoading.set( true );
						downloadImageToFile( new URL( entry.url ), entry.cachedFileName, context );
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (isCacheFileExists( entry.cachedFileName )) {
						bitmap = BitmapFactory.decodeFile( entry.cachedFileName, options );
					}
					if (bitmap == null) {
						// Log.e( "bitmaploader", entry.cachedFileName +
						// " no bitmap" );
						bitmap = memoryCache.get( defaultImageCacheKey );
						if (bitmap == null) {
							// Log.e( "bitmaploader", defaultImageCacheKey +
							// " memory cache - default bitmap does not exists"
							// );
							entry.cacheKey = defaultImageCacheKey;
							entry.url = Constants.DEFAULT_VIDEO_IMAGE;
							pendingList.add( entry );
							// Log.e( "bitmaploader", defaultImageCacheKey +
							// " adding in pending queue to download in next run"
							// );
						} else {
							// Log.e( "bitmaploader", entry.cachedFileName +
							// " file cache - hit" );
							entry.bitmap = bitmap;
							showBitmap( entry );
						}
					} else {
						// Log.e( "bitmaploader", entry.cachedFileName +
						// " memory cache - hit" );
						entry.bitmap = bitmap;
						showBitmap( entry );
						memoryCache.put( entry.cacheKey, bitmap );
					}
					entry.isLoading.set( false );
				}
			}
		}
	}

	private String getCacheFileName( String cacheKey ) {
		return context.getCacheDir().getPath() + "/" + cacheKey;
	}

	private boolean isCacheFileExists( String cacheFileName ) {
		Boolean fileCacheExists = false;
		try {
			// Ln.i( "Cachefile %s", cacheFileName );
			File cacheFile = new File( cacheFileName );
			fileCacheExists = (cacheFile.exists() && cacheFile.length() > 10)
					&& System.currentTimeMillis() - cacheFile.lastModified() <= Constants.IMAGE_CACHE_LIFETIME_MS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Log.e( "bitmaploader", cacheFileName + ", file exists:" +
		// fileCacheExists );
		return fileCacheExists;
	}
	
	private void showBitmap( BitmapEntry entry ) {
		Message msg = handler.obtainMessage( 100, entry );
		handler.sendMessage( msg );
	}

	static Handler handler = new Handler() {
		@Override
		public void handleMessage( Message msg ) {
			BitmapEntry entry = (BitmapEntry) msg.obj;
			if (entry.bitmap != null) {
				entry.bo.set( entry.bitmap );
			}
			// Log.e( "bitmaploader", entry.cachedFileName + ", bitmap found:" +
			// (entry.bitmap != null) );
		}
	};

	BitmapDrawable defaultDrawable;
	int lastDefaultDrawableResId;

	public void setDefaultImageResource( Observable<Bitmap> bo, int resId ) {
		try {
			if (defaultDrawable == null || resId != lastDefaultDrawableResId) {
				defaultDrawable = (BitmapDrawable) context.getResources().getDrawable( resId );
			}
			if (defaultDrawable != null) {
				bo.set( defaultDrawable.getBitmap() );
				lastDefaultDrawableResId = resId;
			}
		} catch (Exception ex) {
		}
	}
}
