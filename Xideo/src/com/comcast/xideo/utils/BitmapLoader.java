package com.comcast.xideo.utils;

import gueei.binding.Observable;
import gueei.binding.observables.BitmapObservable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import no.knowit.misc.GoldenAsyncTask;
import roboguice.util.Ln;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.text.TextUtils;
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
public class BitmapLoader {
	private AssetManager assets;
	static MessageDigest md;
	static LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>( 50 );
	static final int BUFFER_SIZE = 8 * 1024;
	static final int MAX_URL_KEY_ENTRIES = 500;

	@Inject
	Context context;

	@Inject
	public BitmapLoader( AssetManager assets ) {
		this.assets = assets;
		try {
			md = MessageDigest.getInstance( "MD5" );
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	public Bitmap getBitmap( String imageurl ) {
		if( imageurl == null ) {
			return null;
		}

		try {
			URL url = new URL( imageurl );
			if( url.getProtocol().equals( "file" ) ) {
				List<String> segments = Uri.parse( imageurl ).getPathSegments();
				if( segments.size() > 0 ) {
					if( segments.get( 0 ).equalsIgnoreCase( "android_asset" ) ) {
						String path = TextUtils.join( "/", segments.subList( 1, segments.size() ) );
						Ln.e( "Using cached bitmap, '%s'", imageurl );
						return BitmapFactory.decodeStream( assets.open( path ) );
					}
				}
				return null;
			}
			Ln.e( "Will download bitmap, '%s'", imageurl );
			return BitmapFactory.decodeStream( url.openStream() );
		} catch( MalformedURLException e ) {
			Ln.e( e );
		} catch( IOException e ) {
			Ln.e( e );
		}
		Ln.e( "No bitmap (null): %s", imageurl );
		return null;
	}

	public static boolean downloadImageToFile( URL url, String filePath, Context c ) throws IOException {

		InputStream input = null;
		FileOutputStream output = null;
		try {
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

	public void loadBitmapAsync( String url, final Observable<Bitmap> observable ) {
		Ln.i("Downloading image: %s", url);
		if( url == null ) {
			url = Constants.DEFAULT_VIDEO_IMAGE;
		}

		String mcacheKey = null;
		try {
			mcacheKey = md5Sum( url );
			Bitmap bitmap = memoryCache.get( mcacheKey );
			if (bitmap != null) {
				observable.set( bitmap );
				return;
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		final String myurl = url;
		final String cacheKey = mcacheKey;
		final BitmapFactory.Options options = new Options();
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		options.inSampleSize = 2;
		options.inTempStorage = new byte[16 * 1024];


		new GoldenAsyncTask<Bitmap>() {
			Boolean fileCacheExists = false;
			String cacheFilename = "";

			protected void onPreExecute() throws Exception {
				try {
					cacheFilename = context.getCacheDir().getPath() + "/" + cacheKey;
					Ln.i( "Cachefile %s", cacheFilename );
					File cacheFile = new File( cacheFilename );
					fileCacheExists = (cacheFile.exists() && cacheFile.length() > 10)
							&& System.currentTimeMillis() - cacheFile.lastModified() <= Constants.IMAGE_CACHE_LIFETIME_MS;
				} catch (Exception e) {
					e.printStackTrace();
				}
			};

			@Override
			public Bitmap call() throws Exception {
				Bitmap bitmap = null;
				try {
					if (fileCacheExists) {
						bitmap = BitmapFactory.decodeFile( cacheFilename, options );
					} else {
						downloadImageToFile( new URL( myurl ), cacheFilename, context );
						bitmap = BitmapFactory.decodeFile( cacheFilename, options );
						if (bitmap == null) {
							String mcacheKey = md5Sum( Constants.DEFAULT_VIDEO_IMAGE );
							bitmap = memoryCache.get( mcacheKey );
							if (bitmap == null) {
								cacheFilename = context.getCacheDir().getPath() + "/" + mcacheKey;
								downloadImageToFile( new URL( Constants.DEFAULT_VIDEO_IMAGE ), cacheFilename, context );
								bitmap = BitmapFactory.decodeFile( cacheFilename, options );
							}
						}
					}
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				} catch (IOException ioex) {
				}
				return bitmap;
			}

			@Override
			protected void onSuccess( Bitmap result ) {
				observable.set( result );
				if (result != null)
					memoryCache.put( cacheKey, result );
			}

			@Override
			protected void onException( Exception e ) throws RuntimeException {
				Ln.e( e );
			}
		}.execute();
	}

	static LruCache<String, String> URLKeyCache = new LruCache<String, String>( MAX_URL_KEY_ENTRIES );

	private String md5Sum( final String url ) throws UnsupportedEncodingException {
		String cacheKey = URLKeyCache.get( url );
		if (cacheKey == null) {
			md.update( url.getBytes( "UTF-8" ) );
			byte messageDigest[] = md.digest();

			StringBuffer hexString = new StringBuffer();
			for (byte element : messageDigest) {
				hexString.append( Integer.toHexString( 0xFF & element ) );
			}

			cacheKey = hexString.toString();
			URLKeyCache.put( url, cacheKey );
		}
		return cacheKey;
	}

	@Deprecated
	public void loadVideoThumbnail( final Observable<Bitmap> observable, final VideoReference video ) {
		String url = String.format( Constants.VIDEO_THUMBNAIL_URL, video.getId() );
		loadBitmapAsync( url, observable );
		Ln.w( "Using deprecated method to retrieve video image, AssetID = %s", video.getId() );
	}

	public void loadImage(final Observable<Bitmap> target, ImagePackReference imagePack, ImageLocation location, Size size) {
		if (imagePack != null) {
			String locationName = getLocationName( location );
			// updating calling syntax
			String url = String.format( "%s/%s?location=%s&width=%d&height=%d", Constants.VIMOND_IMAGE_SERVICE_BASE_URI, imagePack.getId(), locationName,
					size.getWidth(), size.getHeight() );
			Log.e( "", "image url:" + url );
			loadBitmapAsync( url, target );
		} else {
			loadBitmapAsync( null, target );
		}
	}

	private String getLocationName( ImageLocation location ) {
		switch( location ) {
			case VIDEO_THUMBNAIL:
				return "thumb-iptv";
			case CHANNEL_LOGO:
				return "channel-logo-iptv";
			case BANNER_DOUBLE:
				return "banner-double-iptv";
			case BANNER_SINGLE:
				return "banner-single-iptv";
			default:
				return "main";
		}
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
}
