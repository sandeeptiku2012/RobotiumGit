package com.comcast.videoplayer.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import no.knowit.misc.GoldenAsyncTask;
import no.sumo.api.contracts.IPlaybackItem;
import no.sumo.api.exception.InvalidGeoRegionException;
import no.sumo.api.exception.asset.playback.UnsupportedVideoFormatException;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.vo.asset.playback.RestPlayback;
import no.sumo.api.vo.asset.playback.RestPlaybackItem;

import org.jboss.resteasy.spi.UnauthorizedException;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.ave.drm.DRMManager;
import com.adobe.ave.drm.DRMMetadata;
import com.comcast.cim.httpcomponentsandroid.impl.client.DefaultHttpClient;
import com.comcast.playerplatform.primetime.android.ads.VideoAd;
import com.comcast.playerplatform.primetime.android.ads.VideoAdBreak;
import com.comcast.playerplatform.primetime.android.asset.Asset;
import com.comcast.playerplatform.primetime.android.asset.CimVodAsset;
import com.comcast.playerplatform.primetime.android.enums.PlayerStatus;
import com.comcast.playerplatform.primetime.android.enums.ZoomSetting;
import com.comcast.playerplatform.primetime.android.events.AbstractPlayerPlatformVideoEventListener;
import com.comcast.playerplatform.primetime.android.player.PlayerPlatformAPI;
import com.comcast.playerplatform.primetime.android.primetime.PlayerAudioTrack;
import com.comcast.playerplatform.primetime.android.primetime.PlayerClosedCaptionsTrack;
import com.comcast.playerplatform.util.android.Range;
import com.comcast.playerplatform.util.android.StringUtil;
import com.comcast.videoplayer.ui.PlayerControlBar.Event;
import com.comcast.xideo.R;
import com.comcast.xideo.XideoApplication;
import com.comcast.xideo.activity.BaseActivity;
import com.google.inject.Inject;
import com.vimond.service.StbVideoService;

public class PlayerActivity extends BaseActivity implements OnKeyListener {
	private final String LOG_TAG = "[XideoApplication]::PlayerActivity";

	private final String CURRENT_PLAYER_POSITION = "current_player_position";
	private final String CURRENT_PLAYER_STATE = "current_player_state";

    private long mLastKnownTime;
	private PlayerStatus mLastKnownStatus;
	private PlayerStatus mSavedPlayerState;
    private String mDeviceID;

    private FrameLayout mPlayerFrame;
    private PlayerControlBar mControlBar;
	// private ProgressBar mSpinner;
	// private PubOverlay mPubOverlay;
    private PlayerQos mPlayerQos;

    private final static float CONTROL_BAR_RATIO = 0.6f, ACTION_BAR_RATIO = 0.2f;

	private boolean isRestored, isPrepared, playerIsBuffering, isLive;

    private int selectedClosedCaptions;
    private int selectedAlternateAudio;

    private Handler handler = new Handler();

    private PlayerControlBar.Event lastIgnoredEvent;
	private EventInfo lastIgnoredEventInfo;

	@Inject
	StbVideoService videoService;

	long assetId;

	Timer fadeoutTimer;
	private final long STAY_VISIBLE_DURATION = 10000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_player);
		setAndBindRootView( R.layout.activity_player, new Object() );
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initialize(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Destroy the previous media player object.
        destroyPlayer();

        // Reinitialize variables.
		isRestored = isPrepared = playerIsBuffering = isLive = false;
        lastIgnoredEvent = null;
        lastIgnoredEventInfo = null;
        mSavedPlayerState = null;
        mLastKnownStatus = null;
        mDeviceID = null;
        selectedClosedCaptions = 0;
        selectedAlternateAudio = 0;

        // Initialize with the new item.
        initialize(intent);
    }

    private void initialize(Intent intent) {
        mPlayerFrame = (FrameLayout) findViewById(R.id.playerFrame);
		mControlBar = (PlayerControlBar) findViewById( R.id.playerCtrlBarFragment );
		// mPlayerQos = (PlayerQos)
		// getSupportFragmentManager().findFragmentById( R.id.playerQos );
		// mSpinner = (ProgressBar) findViewById(R.id.pbBufferingSpinner);
		// mPubOverlay = new PubOverlay((ViewGroup)
		// findViewById(R.id.rlPubOverlay));
        findViewById(R.id.sbPlayerControlCC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectClosedCaptions();
            }
        });
        findViewById(R.id.sbPlayerControlAA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAlternateAudio();
            }
        });

		assetId = intent.getLongExtra( "assetId", 0 );
		Toast.makeText(PlayerActivity.this, "assetId is"+ String.valueOf(assetId), Toast.LENGTH_LONG).show();
		Log.e( "PlayerActivity", "assetId:" + assetId );
		createPlayer();
		configureAnalytics();
		installListeners();
		prepareMedia( assetId );
		showPlayerControls( true, false, onShowAnimatorListener );
		mControlBar.setFocusedControl( Event.PAUSE );
    }

	private PlayerPlatformAPI platformAPI;
	RestPlayback restPlayback;

    private void createPlayer() {
		platformAPI = new PlayerPlatformAPI( this, new DefaultHttpClient() );
		View playerView = platformAPI.getView();
        ViewParent playerViewParent = playerView.getParent();

        if (playerViewParent != null)
        {
            ((ViewGroup)playerViewParent).removeView(playerView);
        }

        mPlayerFrame.addView(playerView);
        mPlayerFrame.setOnTouchListener(userActionEventListener);
        mPlayerFrame.setOnClickListener(userActionEventListener);
		mPlayerFrame.setFocusable( true );
		mPlayerFrame.setOnKeyListener( this );
		platformAPI.getView().setOnTouchListener( userActionEventListener );

    }

	private void configureAnalytics() {
		mDeviceID = "android:JUNIT:Test";
		platformAPI.configureAnalytics( mDeviceID );
    }

	private void prepareMedia( final long assetId ) {
		new GoldenAsyncTask<RestPlayback>() {
			protected void onPreExecute() throws Exception {
				playbackItems = null;
			};

			@Override
			public RestPlayback call() {
				RestPlayback rpb = null;
				try {
					rpb = videoService.getAssetInfo( assetId );
				} catch (UnauthorizedException e) {
					e.printStackTrace();
				} catch (InvalidGeoRegionException e) {
					e.printStackTrace();
				} catch (NotFoundException e) {
					e.printStackTrace();
				} catch (MethodNotAllowedException e) {
					e.printStackTrace();
				} catch (UnsupportedVideoFormatException e) {
					e.printStackTrace();
				}
				return rpb;
			}

			@Override
			protected void onSuccess( final RestPlayback result ) throws Exception {
				restPlayback = result;
				queueAssets( result );
			}

			protected void onException( Exception e ) {
				e.printStackTrace();
				queueAssets( null );
			};

			protected void onFinally() throws RuntimeException {
			};
		}.execute();
	}

	List<IPlaybackItem> playbackItems;

	private void queueAssets( RestPlayback pb ) {
		if (pb == null) {
			String mediaUrl;
			mediaUrl = "http://www2.projecthelen.com/video_ism//2013-02-01/1359685659834_Avatar___C(2177642_R21ISM)-m3u8-aapl.ism/Manifest(format=m3u8-aapl).m3u8";
			// mediaUrl =
			// "http://devimages.apple.com.edgekey.net/resources/http-streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8";
			List<RestPlaybackItem> pitems = new ArrayList<RestPlaybackItem>();
			RestPlaybackItem rpi = new RestPlaybackItem();
			rpi.setUrl( mediaUrl );
			pitems.add( rpi );
			pb = new RestPlayback();
			pb.setTitle( "Dummy" );
			pb.setPlaybackItems( pitems );
		}

		currentPlaybackItemIndex = -1;
		if (pb != null && pb.getHasItems()) {
			playbackItems = pb.getPlaybackItems();
			playNext();
		}
	}

	int currentPlaybackItemIndex = -1;

	private void playNext() {
		currentPlaybackItemIndex++;
		IPlaybackItem playbackItem = playbackItems.get( currentPlaybackItemIndex );
		play( playbackItem );
	}

	private boolean play( IPlaybackItem playbackItem ) {
		String mediaUrl = null;
		if (playbackItem != null)
			mediaUrl = playbackItem.getUrl();
		else
			return false;
		CimVodAsset asset = new CimVodAsset( mediaUrl, createDrmKey(), "100", "0", assetId + "" );
		Log.e( "PlayerActivity", "mediaUrl:" + mediaUrl );
		return play( asset );
	}

	private boolean play( CimVodAsset asset ) {
		platformAPI.stop();
		platformAPI.setPreferredZoomSetting( ZoomSetting.None );
		platformAPI.setAsset( asset );
		setAbrControlParams();
		return true;
	}

	private String createDrmKey() {
		String drmKey = PreferenceManager.getDefaultSharedPreferences( this ).getString( XideoApplication.SETTINGS_DRM_KEY, StringUtil.EMPTY_STRING );
		if (!StringUtil.isNotNullOrEmpty( drmKey )) {
			return null;
		}

		return "{\"message:id\":\"" +
				getRandomId() +
				"\", \"message:type\":\"clientAccess\", \"client:accessToken\":\"" +
				drmKey + "\"}";
	}

	private Random random = new Random();

	private char getRandomCharacter() {
		String alphabet = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";
		return alphabet.charAt( random.nextInt( alphabet.length() ) );
	}

	private String getRandomId() {
		String randomId = "";

		for (int i = 0; i < 3; i++) {
			randomId += String.format( "%c%c%c-", getRandomCharacter(), getRandomCharacter(), getRandomCharacter() );
		}
		randomId += String.format( "%c%c%c", getRandomCharacter(), getRandomCharacter(), getRandomCharacter() );

		return randomId;
	}

    private void installListeners() {
		if (platformAPI != null) {
			platformAPI.addEventListener( _platformEventListener );
        }

        if (mControlBar != null) {
            mControlBar.addEventListener(controlBarEventListener);
        }
    }

    private void setPreparedValues() {
        prepareControlBar();

		// XideoApplication.logger.i( LOG_TAG +
		// "::MediaPlayer.PlaybackEventListener#onPrepared()", "Media prepared."
		// );

        isPrepared = true;
        runLastIgnoredCommand();

        setDefaultClosedCaption();
        setDefaultAudio();

        if (playbackShouldStart()) {
			// setAbrControlParams();
            //_controlBar.play();
        }
    }

    private boolean playbackShouldStart() {
        boolean shouldPlay = !isRestored;
        if (isRestored) {
			// XideoApplication.logger.d( LOG_TAG +
			// "MediaPlayer.PlayerStateEventListener#onStateChanged",
			// "Initial seek to location: " + _lastKnownTime );
			platformAPI.setPosition( mLastKnownTime, false );

			shouldPlay = (mSavedPlayerState == PlayerStatus.PLAYING);
            if (shouldPlay) {
				mControlBar.showStatus( mSavedPlayerState );
            }
        }
        return shouldPlay;
    }

    private void setDefaultClosedCaption() {
        // select the initial CC track
		platformAPI.setClosedCaptionsEnabled( getClosedCaptionVisibilityPref() );

		platformAPI.setClosedCaptionsOptions( getDefaultClosedCaptionStyle() );
        selectedClosedCaptions = 0;
    }

    private Map<String, String> getDefaultClosedCaptionStyle() {
        HashMap<String,String> style = new HashMap<String, String>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		style.put( "fontSize", preferences.getString( XideoApplication.SETTINGS_CC_STYLE_SIZE, "" ) );
		style.put( "fontStyle", preferences.getString( XideoApplication.SETTINGS_CC_STYLE_FONT, "" ) );
		style.put( "textForegroundColor", preferences.getString( XideoApplication.SETTINGS_CC_STYLE_FONT_COLOR, null ) );
		style.put( "textForegroundOpacity", preferences.getString( XideoApplication.SETTINGS_CC_STYLE_FONT_OPACITY, null ) );
		style.put( "windowFillColor", preferences.getString( XideoApplication.SETTINGS_CC_STYLE_BACKGROUND_COLOR, "" ) );
		style.put( "windowFillOpacity", preferences.getString( XideoApplication.SETTINGS_CC_STYLE_BACKGROUND_OPACITY, "" ) );
		style.put( "windowBorderEdgeColor", preferences.getString( XideoApplication.SETTINGS_CC_STYLE_EDGE_COLOR, "asdfd" ) );
		style.put( "windowBorderEdgeStyle", preferences.getString( XideoApplication.SETTINGS_CC_STYLE_FONT_EDGE, "3" ) );

        return style;
    }

    private void setDefaultAudio() {
        // select the initial AA track
		List<PlayerAudioTrack> availableAudio = platformAPI.getAvailableAudioLanguages();
		platformAPI.setPreferredAudioLanguage( availableAudio.get( 0 ) );
        selectedAlternateAudio = 0;
    }

    private void prepareControlBar() {
		mControlBar.showWithAnimation( null, null );
		mControlBar.setPlaybackDuration( platformAPI.getDuration() );
        mControlBar.setPosition(0);
		mControlBar.setTimeline( platformAPI.getTimeline() );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

		// XideoApplication.logger.i( LOG_TAG + "#onRestoreInstanceState",
		// "Restoring activity state." );

        mLastKnownTime = savedInstanceState.getLong(CURRENT_PLAYER_POSITION, -1);
		// XideoApplication.logger.i( LOG_TAG + "#onRestoreInstanceState",
		// "Restored current player time: " + _lastKnownTime );

		mSavedPlayerState = (PlayerStatus) savedInstanceState.getSerializable( CURRENT_PLAYER_STATE );
		// XideoApplication.logger.d( LOG_TAG + "#onRestoreInstanceState",
		// "Retrieved player state: " + _savedPlayerState );

        isRestored = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestAudioFocus();
        updateClosedCaptionsVisilibty();
		platformAPI.setClosedCaptionsOptions( getDefaultClosedCaptionStyle() );
    }

    private void updateQoS() {
		int bitrate = platformAPI.getCurrentBitrate();
        mPlayerQos.updateQosInformation(bitrate);
    }

    private void updateClosedCaptionsVisilibty() {
		if (platformAPI != null && mLastKnownStatus != PlayerStatus.RELEASED)
			platformAPI.setClosedCaptionsEnabled( getClosedCaptionVisibilityPref() );
    }

    @Override
    protected void onPause() {
		if (platformAPI != null) {
			PlayerStatus status = PlayerStatus.valueOf( platformAPI.getPlayerStatus().name() );
			if (status == PlayerStatus.PLAYING || status == PlayerStatus.PAUSED) {
                mSavedPlayerState = status;
				mLastKnownTime = platformAPI.getCurrentPosition();
            }

			mControlBar.showStatus( status );
        }

		mLastKnownStatus = PlayerStatus.PAUSED;
        abandonAudioFocus();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(CURRENT_PLAYER_POSITION, mLastKnownTime);
        outState.putSerializable(CURRENT_PLAYER_STATE, mSavedPlayerState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        destroyPlayer();
        super.onDestroy();
    }

    private void destroyPlayer() {
        uninstallListeners();

		// mPlayerFrame.removeAllViews();
		if (platformAPI != null) {
			platformAPI.stop();
			platformAPI.destroy();
			platformAPI = null;
		}
		mLastKnownStatus = PlayerStatus.RELEASED;
    }

    private void uninstallListeners() {
        if (mControlBar != null) {
            mControlBar.removeEventListener(controlBarEventListener);
            mControlBar = null;
        }

		if (platformAPI != null) {
			platformAPI.removeEventListener( _platformEventListener );
        }
    }

	private void resizeVideo( int width, int height ) {
		platformAPI.setDimensionsOfVideo( width, height );
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Ignore orientation/keyboard change.
        //setPlayerViewSize(savedMovieWidth, savedMovieHeight);
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Converts the closed captions enum values to a string array.
     *
     * @return array of CC tracks
     */
    private String[] getCCsAsArray() {
        // Get the enum values.
		List<PlayerClosedCaptionsTrack> values = platformAPI.getAvailableClosedCaptionTracks();

        String[] tracks = new String[values.size()];

        for (int i=0; i< values.size(); i++){
            tracks[i] = values.get(i).getName();
        }

        return tracks;
    }

    /**
     * Converts the alternate audio tracks to a string array.
     *
     * @return array of AA track names
     */
    private String[] getAudioTracksAsArray() {
		List<PlayerAudioTrack> audioTracks = platformAPI.getAvailableAudioLanguages();
        String[] tracks = new String[audioTracks.size()];
        for (int i = 0; i < audioTracks.size(); i++){
            tracks[i] = audioTracks.get(i).getLanguage();
        }
        return tracks;
    }

	/**
	 * Displays a chooser dialog, allowing the user to select the desired closed
	 * captions.
	 */
	private void selectClosedCaptions() {
		XideoApplication.logger.i( LOG_TAG + "#selectClosedCaptions", "Displaying closed captions chooser dialog." );

		final List<PlayerClosedCaptionsTrack> trackList = platformAPI.getAvailableClosedCaptionTracks();
		for (PlayerClosedCaptionsTrack track : trackList) {
			if (track.equals( platformAPI.getCurrentClosedCaptionTrack() )) {
				XideoApplication.logger.i( LOG_TAG + "#selectClosedCaptions", "Has current CC: " + track.getName() );
			}
		}

		final String items[] = getCCsAsArray();
		final AlertDialog.Builder ab = new AlertDialog.Builder( this );
		ab.setTitle( R.string.PlayerControlCCDialogTitle );
		ab.setSingleChoiceItems( items, selectedClosedCaptions, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int whichButton ) {
				selectedClosedCaptions = whichButton;
				// Select the new closed captions.
				platformAPI.setClosedCaptionsTrack( trackList.get( whichButton ) );
				// Dismiss dialog.
				dialog.cancel();
			}
		} ).setNegativeButton( R.string.PlayerControlCCDialogCancel, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int whichButton ) {
				// Just cancel the dialog.
			}
		} );
		ab.show();
	}

	/**
	 * Displays a chooser dialog, allowing the user to select the desired closed
	 * alternate audio track.
	 */
    private void selectAlternateAudio() {
		XideoApplication.logger.i( LOG_TAG + "#selectAlternateAudio", "Displaying alternate audio chooser dialog." );

		final String items[] = getAudioTracksAsArray();
		final List<PlayerAudioTrack> trackList = platformAPI.getAvailableAudioLanguages();

		for (PlayerAudioTrack track : trackList) {
			if (track.equals( platformAPI.getCurrentAudioTrack() )) {
				XideoApplication.logger.i( LOG_TAG + "#selectClosedCaptions", "Has current CC: " + track.getLanguage() );
			}
        }

        final AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle(R.string.PlayerControlAADialogTitle);
        ab.setSingleChoiceItems(items, selectedAlternateAudio, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Select the new audio track.
				List<PlayerAudioTrack> tracks = platformAPI.getAvailableAudioLanguages();
				platformAPI.setPreferredAudioLanguage( trackList.get( whichButton ) );
                if (true) {
                    selectedAlternateAudio = whichButton;
                }

                // Dismiss dialog.
                dialog.cancel();
            }
        }).setNegativeButton(R.string.PlayerControlCCDialogCancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Just cancel the dialog.
            }
        });

        ab.show();
    }

    private void requestAudioFocus() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Request audio focus for playback
        int result = am.requestAudioFocus(null,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			XideoApplication.logger.i( LOG_TAG + "#requestAudioFocus()", "Gained audio focus." );
        }
    }

    private void abandonAudioFocus() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(null);
		XideoApplication.logger.i( LOG_TAG + "#abandonAudioFocus()", "Abandoned audio focus." );
    }

    private boolean getClosedCaptionVisibilityPref() {
    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		boolean closedCaptionVisibilityPreference = sharedPreferences.getBoolean(
				XideoApplication.SETTINGS_CC_VISIBILITY,
				XideoApplication.DEFAULT_CC_VISIBILITY );
        return closedCaptionVisibilityPreference;
    }

    private boolean shouldReturnToCatalogOnPlaybackComplete() {
    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		return sharedPreferences.getBoolean( XideoApplication.SETTINGS_RETURN_HOME_ON_PLAY_COMPLETE, XideoApplication.DEFAULT_RETURN_HOME_ON_PLAY_COMPLETE );
    }

    private void setAbrControlParams() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (sharedPreferences.getBoolean( XideoApplication.SETTINGS_ABR_CTRL_ENABLED, false )) {
			int initialBitRate = Integer.parseInt( sharedPreferences.getString( XideoApplication.SETTINGS_ABR_INITIAL_BITRATE, "0" ) );
			int minBitRate = Integer.parseInt( sharedPreferences.getString( XideoApplication.SETTINGS_ABR_MIN_BITRATE, "0" ) );
			int maxBitRate = Integer.parseInt( sharedPreferences.getString( XideoApplication.SETTINGS_ABR_MAX_BITRATE, "0" ) );

            Range bitRange = new Range();
            bitRange.setMax(maxBitRate);
            bitRange.setMin(minBitRate);
			platformAPI.setInitialBitrate( initialBitRate );
			platformAPI.setBitrateRange( bitRange );
        }
    }

    private void showReplayButton() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                mControlBar.stop(true);
            }
        });
    }

    private final AbstractPlayerPlatformVideoEventListener _platformEventListener = new AbstractPlayerPlatformVideoEventListener() {
        @Override
        public void drmMetaDataAvailable(DRMManager drmManager, DRMMetadata drmMetadata){
			// platformAPI.destroy();
            checkCurrentThreadIsMainUiThread();
			XideoApplication.logger.i( LOG_TAG, "DRM Available" );
		}

		@Override
		public void acquiringLicense( Asset asset ) {
			// platformAPI.destroy();
        }

        @Override
        public void onBufferStart() {
			// XideoApplication.logger.i( LOG_TAG, " Buffer Start" );
			// showBufferingSpinner();
			// mControlBar.showWithAnimation( null, onShowAnimatorListener );
			showPlayerControls( true, true, onShowAnimatorListener );
            super.onBufferStart();
        }


        @Override
        public void onBufferComplete() {
			// XideoApplication.logger.i( LOG_TAG, " Buffer Complete" );
			// hideBufferingSpinner();
			startFadeOutTimer();
            super.onBufferComplete();
        }

        @Override
		public void adBreakStart( VideoAdBreak adBreak, VideoAd ad ) {
			super.adBreakStart( adBreak, ad ); // To change body of overridden
												// methods use File | Settings |
												// File Templates.
		}

		@Override
		public void onSeekStart() {
			super.onSeekStart(); // To change body of overridden methods use
									// File | Settings | File Templates.
		}

		@Override
		public void onSeekComplete( long position ) {
			super.onSeekComplete( position ); // To change body of overridden
												// methods use File | Settings |
												// File Templates.
		}

		@Override
		public void adBreakComplete( VideoAdBreak adbreak ) {

			super.adBreakComplete( adbreak ); // To change body of overridden
												// methods use File | Settings |
												// File Templates.
		}

		@Override
		public void adStart( VideoAd ad ) {
			super.adStart( ad ); // To change body of overridden methods use
									// File | Settings | File Templates.
		}

		@Override
		public void adProgress( VideoAdBreak adbreak, VideoAd ad, int progress ) {
			super.adProgress( adbreak, ad, progress ); // To change body of
														// overridden methods
														// use File | Settings |
														// File Templates.
		}

		@Override
		public void adComplete( long id ) {
			super.adComplete( id ); // To change body of overridden methods use
									// File | Settings | File Templates.
		}

		@Override
        public void bitrateChanged(long bitrate, String changeReason, long videoHeight, long videoWidth){
            checkCurrentThreadIsMainUiThread();
			// updateQoS();
        }

        @Override
        public void drmInitializeFailed(long l, long l1, Exception e) {
            checkCurrentThreadIsMainUiThread();
			// XideoApplication.logger.i( LOG_TAG +
			// "::MediaPlayer.PlaybackEventListener#drmInitializeFailed()",
			// "Drm Failed to initialize" );
        }

        @Override
        public void drmInitialized() {
            checkCurrentThreadIsMainUiThread();

			// XideoApplication.logger.i( LOG_TAG +
			// "::MediaPlayer.PlaybackEventListener#drmInitialized()",
			// "Drm Initialized Success" );
        }

        @Override
        public void droppedFPSChanged(long droppedFramesPerSecond){
            checkCurrentThreadIsMainUiThread();

			// XideoApplication.logger.i( LOG_TAG +
			// "::MediaPlayer.PlaybackEventListener#droppedFPSChanged()",
			// "New Dropped FramesPerSecond ["
			// + droppedFramesPerSecond
			// + "]." );
        }

        @Override
        public void durationChanged(long duration){
            checkCurrentThreadIsMainUiThread();

			// XideoApplication.logger.i( LOG_TAG +
			// "::MediaPlayer.PlaybackEventListener#durationChanged()",
			// "New Duration [" + duration + "]." );
        }

        @Override
        public void fpsChanged(long framesPerSecond){
            checkCurrentThreadIsMainUiThread();

			// XideoApplication.logger.i( LOG_TAG +
			// "::MediaPlayer.PlaybackEventListener#fpsChanged()",
			// "New FramesPerSecond [" + framesPerSecond + "]." );
        }

        @Override
        public void mediaEnded() {
            checkCurrentThreadIsMainUiThread();

			long currentTime = platformAPI.getCurrentPosition();
			// XideoApplication.logger.i(
			// LOG_TAG + "::MediaPlayer.PlaybackEventListener#onPlayComplete()",
			// "Time on playback complete [" + String.valueOf( currentTime )
			// + "]." );
            if (shouldReturnToCatalogOnPlaybackComplete())
                PlayerActivity.this.finish();
        }

        @Override
        public void mediaFailed(String code, String description) {
            checkCurrentThreadIsMainUiThread();

			// XideoApplication.logger.e( LOG_TAG +
			// "::MediaPlayer.ErrorEventListener#onError()", "Player error: " +
			// code + " - " + description );
			Toast.makeText(
					PlayerActivity.this,
					"Error loading playback content. Try again." + String.valueOf(
							code ) + ":" + String.valueOf( description ),
					Toast.LENGTH_LONG ).show();
        }

        @Override
        public void mediaOpened(String type, String mediaType, List playbackSpeeds, List availableAudioLanguages, List availableClosedCaptionTracks, long width, long height, long endPosition, double openingLatency, boolean hasCC) {
            checkCurrentThreadIsMainUiThread();

			// XideoApplication.logger.e( LOG_TAG +
			// "::MediaPlayer.PlaybackEventListener#mediaOpened()",
			// "Media Opened" );
			if (mLastKnownStatus != PlayerStatus.PLAYING) {
				platformAPI.play();
            }

        }

        @Override
        public void mediaProgress(long position, double playbackSpeed, long startPosition, long endPosition, int updateInterval) {
            checkCurrentThreadIsMainUiThread();

			if (mControlBar != null && platformAPI != null) {
				if (mLastKnownStatus == PlayerStatus.PLAYING) {
					mControlBar.setPosition( platformAPI.getCurrentPosition() );
                }
            }
        }

        @Override
        public void mediaWarning(String code, String description) {
            checkCurrentThreadIsMainUiThread();

			// XideoApplication.logger.w( LOG_TAG +
			// "::MediaPlayer.ErrorEventListener#onError()", "Player error: " +
			// code + " - " + description );
        }

        @Override
        public void numberOfAlternativeAudioStreamsChanged(int numberOfAlternativeAudioStreams){
            checkCurrentThreadIsMainUiThread();

			// XideoApplication.logger.i( LOG_TAG +
			// "::MediaPlayer.PlaybackEventListener#numberOfAlternativeAudioStreamsChanged()",
			// "New number of Alt. Audio ["
			// + numberOfAlternativeAudioStreams + "]." );
        }

        @Override
		public void playStateChanged( PlayerStatus status ) {
            checkCurrentThreadIsMainUiThread();

			PlayerStatus previousStatus = mLastKnownStatus;
			PlayerStatus currentStatus = PlayerStatus.valueOf( status.name() );

			// XideoApplication.logger.i( LOG_TAG +
			// "::MediaPlayer.PlayerStateEventListener#onStateChanged()",
			// "Player state changed from [" + previousStatus
			// + "] to ["
			// + currentStatus + "]." );

			if (previousStatus == PlayerStatus.COMPLETE) {
                // Start the clock thread.
                //_playbackClock.start();
            }

            mLastKnownStatus = currentStatus;

            switch (mLastKnownStatus) {

                case PREPARING:
					platformAPI.setAutoPlay( true );
                    break;

                case PREPARED:
					mControlBar.setFocusedControl( Event.PLAY );
                    setPreparedValues();
                    break;

                case COMPLETE:
                    completeMedia();
                    break;

				case PAUSED:
					mControlBar.showStatus( PlayerStatus.PAUSED );
					break;

                case PLAYING:
					mControlBar.showStatus( PlayerStatus.PLAYING );
					startFadeOutTimer();
                    runLastIgnoredCommand();
					break;
            }
        }

        private void completeMedia() {
            // Show the replay button.
            showReplayButton();
            // Update the position time.
			mControlBar.setPosition( platformAPI.getEndPosition() );
			long currentTime = platformAPI.getCurrentPosition();

			// XideoApplication.logger.i(
			// LOG_TAG + "::MediaPlayer.PlaybackEventListener#onPlayComplete()",
			// "Time on playback complete [" + String.valueOf( currentTime )
			// + "]." );

            if (shouldReturnToCatalogOnPlaybackComplete())
                PlayerActivity.this.finish();
        }

        @Override
        public void drmComplete(Asset asset, Date start, Date end) {
            checkCurrentThreadIsMainUiThread();
			// XideoApplication.logger.i( LOG_TAG +
			// "::MediaPlayer.PlaybackEventListener#drmComplete",
			// "DRM completed for " + asset.manifestUri );
        }

        @Override
        public void drmFailure(String code, String description, String subCode, String subDescription, Exception ex){
            checkCurrentThreadIsMainUiThread();

			// XideoApplication.logger.e( LOG_TAG +
			// "::MediaPlayer.PlaybackEventListener#drmFailure", "DRM failed: "
			// + description );
        }
	};

	private interface UserActionEventListener extends View.OnTouchListener, View.OnClickListener {
	}

	private final UserActionEventListener userActionEventListener = new UserActionEventListener() {
		@Override
		public boolean onTouch( View v, MotionEvent event ) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
						break;

					case MotionEvent.ACTION_UP:
						float ratio = event.getY() / v.getHeight();
						// XideoApplication.logger.i( LOG_TAG +
						// "::UserActionEventListener#onTouch",
						// "Handling onTouch user action: ratio=" + ratio + "."
						// );

						// Check if user clicked above the control bar
						if (ratio > ACTION_BAR_RATIO && ratio < CONTROL_BAR_RATIO) {
							// if (mLastKnownStatus == PlayerStatus.PLAYING ||
							// mLastKnownStatus == PlayerStatus.PAUSED)
//								mControlBar.togglePlayPause();
						}

						// Show the action bar and the control bar
						// mControlBar.showWithAnimation( null,
						// onShowAnimatorListener );
						showPlayerControls( true, false, onShowAnimatorListener );
						break;
				}

				return false;
			}

		@Override
		public void onClick( View v ) {
		}
	};

	private void runLastIgnoredCommand() {
		if (lastIgnoredEvent == null) {
			return;
		}

		// Perform last ignored event.
		// XideoApplication.logger.i( LOG_TAG + "#runLastIgnoredCommand()",
		// "Performing last ignored event: " + lastIgnoredEvent.getName() );
		controlBarEventListener.handleEvent( lastIgnoredEvent, lastIgnoredEventInfo );

		lastIgnoredEvent = null;
		lastIgnoredEventInfo = null;
	}

	private void displayABRSettingsDialog() {
		// XideoApplication.logger.i( LOG_TAG +
		// "::ActionBarEventListener#handleEvent",
		// "Displaying ABR settings dialog." );
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( this );
		boolean isABRCtrlEnabled = sharedPreferences.getBoolean( XideoApplication.SETTINGS_ABR_CTRL_ENABLED, false );
		final SharedPreferences.Editor editor = sharedPreferences.edit();

		View view = LayoutInflater.from( PlayerActivity.this ).inflate( R.layout.dialog_abr_settings, null );
		TextView disabledMessage = (TextView) view.findViewById( R.id.disabledMessage );
		final EditText minBitRateEdit = (EditText) view.findViewById( R.id.minBitRateEdit );
		final EditText maxBitRateEdit = (EditText) view.findViewById( R.id.maxBitRateEdit );
		final Spinner spinner = (Spinner) view.findViewById( R.id.policySpinner );

		disabledMessage.setVisibility( isABRCtrlEnabled ? View.GONE : View.VISIBLE );

		minBitRateEdit.setText( sharedPreferences.getString( XideoApplication.SETTINGS_ABR_MIN_BITRATE, "0" ) );
		minBitRateEdit.setEnabled( isABRCtrlEnabled );

		maxBitRateEdit.setText( sharedPreferences.getString( XideoApplication.SETTINGS_ABR_MAX_BITRATE, "0" ) );
		maxBitRateEdit.setEnabled( isABRCtrlEnabled );

		int mbrPolicyAsInt = Integer.parseInt( sharedPreferences.getString( XideoApplication.SETTINGS_ABR_POLICY, "0" ) );
		String[] availablePolicies = getResources().getStringArray( R.array.abr_policies );

		ArrayAdapter<String> adapter = new ArrayAdapter<String>( PlayerActivity.this, android.R.layout.simple_spinner_item, availablePolicies );
		adapter.setDropDownViewResource( android.R.layout.simple_dropdown_item_1line );
		spinner.setAdapter( adapter );
		spinner.setSelection( mbrPolicyAsInt );
		spinner.setEnabled( isABRCtrlEnabled );

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder( PlayerActivity.this );
		dialogBuilder
					.setTitle( getResources().getString( R.string.settingsAbr ) )
					.setView( view )
					.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick( DialogInterface dialog, int whichButton ) {
							// ignore, just dismiss
						}
					} );

		if (isABRCtrlEnabled) {
			dialogBuilder.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick( DialogInterface dialog, int which ) {
					int minBitRate, maxBitRate, policyIdx;
					try {
						minBitRate = Integer.parseInt( minBitRateEdit.getText().toString() );
						maxBitRate = Integer.parseInt( maxBitRateEdit.getText().toString() );
					} catch (NumberFormatException e) {
						Toast.makeText( PlayerActivity.this, "Invalid parameter values: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
						return;
					}

					policyIdx = spinner.getSelectedItemPosition();

					editor.putString( XideoApplication.SETTINGS_ABR_MIN_BITRATE, String.valueOf( minBitRate ) );
					editor.putString( XideoApplication.SETTINGS_ABR_MAX_BITRATE, String.valueOf( maxBitRate ) );
					editor.putString( XideoApplication.SETTINGS_ABR_POLICY, String.valueOf( policyIdx ) );

					editor.commit();

					PlayerActivity.this.setAbrControlParams();
				}
			} );
		}

		dialogBuilder.show();
	}

	private final PlayerControlBar.ControlBarEventListener controlBarEventListener = new PlayerControlBar.ControlBarEventListener() {
		@Override
		public void handleEvent( PlayerControlBar.Event event, EventInfo eventInfo ) {
			XideoApplication.logger.i( LOG_TAG + "::PlayerControlBar.ControlBarEventListener#handleEvent()", "Player control bar event: " + event.getName() );
			if (!isPrepared || playerIsBuffering) {
				// The player is not prepared. We will not run commands on it.
				XideoApplication.logger.i(
						LOG_TAG + "::PlayerControlBar.ControlBarEventListener#handleEvent()",
						"Player is not prepapred. Ignoring and saving last command: " + event.getName() );
				lastIgnoredEvent = event;
				lastIgnoredEventInfo = eventInfo;
				return;
			}

			long seekPosition;

			switch (event) {
				case PLAY:
						switch (mLastKnownStatus) {
							case COMPLETE:
								platformAPI.setPosition( platformAPI.getStartPosition(), false );
								mControlBar.setPosition( platformAPI.getStartPosition() );
								break;

							case READY:
							case PREPARED:
							case INITIALIZED:
							case PAUSED:
								XideoApplication.logger.i( LOG_TAG + "::PlayerControlBar.ControlBarEventListener#handleEvent()", "Starting playback." );

								if (isLive)
								{
									platformAPI.seekToLive();
								}

								platformAPI.play();
								break;
						}
						startFadeOutTimer();
						break;

					case PAUSE:
						if (mLastKnownStatus == PlayerStatus.PLAYING)
						{
							XideoApplication.logger.i( LOG_TAG + "::PlayerControlBar.ControlBarEventListener#handleEvent()", "Pausing the player instance." );
							platformAPI.pause();
						}
						mControlBar.showWithAnimation( null, onShowAnimatorListener );
						stopFadeOutTimer();
						break;

					case SEEK:
						seekPosition = ((SeekEventInfo) eventInfo).getPosition();
						platformAPI.setPosition( seekPosition, true );
						if (mLastKnownStatus == PlayerStatus.PLAYING)
						{
							platformAPI.play();
						}
						mControlBar.showWithAnimation( null, onShowAnimatorListener );
						stopFadeOutTimer();
						break;

					case FAST_FORWARD:
						platformAPI.setPositionRelative( 30 );
						if (mLastKnownStatus == PlayerStatus.PLAYING)
						{
							platformAPI.play();
						}
						mControlBar.showWithAnimation( null, onShowAnimatorListener );
						stopFadeOutTimer();
						break;

					case FAST_REWIND:
						platformAPI.setPositionRelative( -30 );
						if (mLastKnownStatus == PlayerStatus.PLAYING)
						{
							platformAPI.play();
						}
						mControlBar.showWithAnimation( null, onShowAnimatorListener );
						stopFadeOutTimer();
						break;

					case STOP:
						platformAPI.stop();
						mControlBar.showWithAnimation( null, onShowAnimatorListener );
						stopFadeOutTimer();
						break;

					case BACK:
						platformAPI.stop();
						finish();
				}
			}
	};

	public void checkCurrentThreadIsMainUiThread() {
		if (!Looper.getMainLooper().getThread().equals( Thread.currentThread() )) {
			throw new RuntimeException( "Method called from non-UI thread" );
		}
	}

	private enum CCTrackType {
		CC1, CC2, CC3, CC4, CS1, CS2, CS3, CS4, CS5, CS6;
	}

	AnimatorListener onShowAnimatorListener = new AnimatorListener() {

		@Override
		public void onAnimationCancel( Animator animation ) {
		}

		@Override
		public void onAnimationEnd( Animator animation ) {
			if (mControlBar != null) {
				if (mControlBar.getAreControlsVisible()) {
					mPlayerFrame.clearFocus();
					mControlBar.requestFocus();
				}
			}
		}

		@Override
		public void onAnimationRepeat( Animator animation ) {
		}

		@Override
		public void onAnimationStart( Animator animation ) {
			mControlBar.setVisibility( View.VISIBLE );
		}
	};

	AnimatorListener onHideAnimatorListener = new AnimatorListener() {

		@Override
		public void onAnimationCancel( Animator animation ) {
		}

		@Override
		public void onAnimationEnd( Animator animation ) {
			if (mControlBar != null) {
				mControlBar.setVisibility( View.GONE );
				mControlBar.clearFocus();
				mPlayerFrame.requestFocus();
			}
		}

		@Override
		public void onAnimationRepeat( Animator animation ) {
		}

		@Override
		public void onAnimationStart( Animator animation ) {
		}
	};

	private void showPlayerControls( boolean showControls, boolean startFadeOutTimer, AnimatorListener listener ) {
		mControlBar.showWithAnimation( null, listener );
		if (showControls)
			mControlBar.showControlsImmediate();
		else
			mControlBar.hideControlsImmediate();

		showPlayerBackground();
		if (startFadeOutTimer)
			startFadeOutTimer();
		else
			stopFadeOutTimer();
	}

	void showPlayerBackground() {
		View bkgView = findViewById( R.id.image1 );
		ObjectAnimator alphaAnim = ObjectAnimator.ofFloat( bkgView, "alpha", 1.0f );
		alphaAnim.setInterpolator( new DecelerateInterpolator( 1.2f ) );
		alphaAnim.setDuration( 300 );
		alphaAnim.start();
	}

	void hidePlayerBackground() {
		View bkgView = findViewById( R.id.image1 );
		ObjectAnimator alphaAnim = ObjectAnimator.ofFloat( bkgView, "alpha", 0.0f );
		alphaAnim.setInterpolator( new DecelerateInterpolator( 1.2f ) );
		alphaAnim.setDuration( 300 );
		alphaAnim.start();
	}

	private void stopFadeOutTimer() {
		if (fadeoutTimer != null) {
			fadeoutTimer.cancel();
		}
	}

	private void startFadeOutTimer() {
		stopFadeOutTimer();
		fadeoutTimer = new Timer();
		fadeoutTimer.schedule( new TimerTask() {
			@Override
			public void run() {
				handler.post( new Runnable() {
					@Override
					public void run() {
						if (mControlBar != null) {
							mControlBar.hideWithAnimation( null, onHideAnimatorListener );
						}
						hidePlayerBackground();
					}
				} );
			}
		}, STAY_VISIBLE_DURATION );
	}

	@Override
	public boolean onKey( View v, int keyCode, KeyEvent event ) {
		boolean handled = false;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_CENTER:
				case KeyEvent.KEYCODE_DPAD_UP:
				case KeyEvent.KEYCODE_DPAD_DOWN:
					showPlayerControls( true, true, onShowAnimatorListener );
					handled = true;
					break;

				case KeyEvent.KEYCODE_DPAD_LEFT:
					platformAPI.setPositionRelative( -10 );
					showPlayerControls( false, true, null );
					handled = true;
					break;

				case KeyEvent.KEYCODE_DPAD_RIGHT:
					platformAPI.setPositionRelative( 30 );
					showPlayerControls( false, true, null );
					handled = true;
					break;

				case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
					if (platformAPI.getPlayerStatus() == PlayerStatus.PLAYING) {
						controlBarEventListener.handleEvent( Event.PAUSE, null );
						showPlayerControls( true, false, onShowAnimatorListener );
					} else {
						if (platformAPI.getPlayerStatus() == PlayerStatus.PAUSED) {
							controlBarEventListener.handleEvent( Event.PLAY, null );
							showPlayerControls( false, true, onShowAnimatorListener );
						}
					}
					handled = true;
					break;

				case KeyEvent.KEYCODE_MEDIA_PAUSE:
					controlBarEventListener.handleEvent( Event.PAUSE, null );
					showPlayerControls( true, false, onShowAnimatorListener );
					handled = true;
					break;

				case KeyEvent.KEYCODE_MEDIA_PLAY:
					controlBarEventListener.handleEvent( Event.PLAY, null );
					showPlayerControls( false, true, onShowAnimatorListener );
					handled = true;
					break;
			}
		}
		return handled;
	}

	// private boolean isAutohideEnabled() {
	// SharedPreferences sharedPreferences =
	// PreferenceManager.getDefaultSharedPreferences(getActivity());
	// return sharedPreferences.getBoolean(
	// XideoApplication.SETTINGS_CONTROLBAR_AUTOHIDE,
	// XideoApplication.DEFAULT_AUTOHIDE_CONTROL_BAR );
	// }

}
