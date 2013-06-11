/*************************************************************************
*
* ADOBE CONFIDENTIAL
* ___________________
*
*  Copyright 2012 Adobe Systems Incorporated
*  All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains
* the property of Adobe Systems Incorporated and its suppliers,
* if any.  The intellectual and technical concepts contained
* herein are proprietary to Adobe Systems Incorporated and its
* suppliers and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material
* is strictly forbidden unless prior written permission is obtained
* from Adobe Systems Incorporated.
**************************************************************************/

package com.comcast.videoplayer.ui;

import gueei.binding.Binder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adobe.mediacore.timeline.Timeline;
import com.adobe.mediacore.timeline.TimelineMarker;
import com.comcast.media.utils.MarkableSeekBar;
import com.comcast.playerplatform.primetime.android.enums.PlayerStatus;
import com.comcast.xideo.R;

public class PlayerControlBar extends LinearLayout implements OnFocusChangeListener, OnClickListener, OnKeyListener {

	private final String LOG_TAG = "[XideoApplication]::PlayerControlBar";

	private final long STAY_VISIBLE_DURATION = 5000;
	private final int ANIMATION_DURATION_SLIDE = 1000;

	private final int SEEK_BAR_MAX_LEN = 1000;

	private final static int MS_IN_SECOND = 1000;
	private final static int MS_IN_MINUTE = 60 * MS_IN_SECOND;
	private final static int MS_IN_HOUR = 60 * MS_IN_MINUTE;

	private float seekBarStep;

	private long duration;
	private long position;

	private MarkableSeekBar seekBar;
	private ImageButton btnBack;
	private ImageButton btnFastRewind;
	private ImageButton btnPlayPause;
	private ImageButton btnFastForward;
	private ImageButton btnSettings;
	private TextView txtCurrentTime, txtTotalTime;
	private View view;
	View controlsContainer, progressContainer;

	private PlayerStatus playerStatus;

	private Timer fadeoutTimer;
	private Handler handler = new Handler();
	View hopper;
	View lastFocused = null;

	public enum Event {
		PLAY("play"),
		PAUSE("pause"),
		STOP("stop"),
		SEEK("seek"),
		BACK("back"),
		FAST_REWIND("fast_rewind"),
		FAST_FORWARD("fast_forward"),
		SETTINGS("settings");

		private String name;

		Event(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public PlayerControlBar(Context context) {
		this( context, null );
	}

	public PlayerControlBar(Context context, AttributeSet attrs) {
		this( context, attrs, 0 );
	}

	public PlayerControlBar(Context context, AttributeSet attrs, int defStyle) {
		super( context, attrs, defStyle );
		if (!isInEditMode()) {
			init();
		}
	}

	public View init() {
		// setFocusable( true );
		// setDescendantFocusability( FOCUS_AFTER_DESCENDANTS );

		setLayoutParams( new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT ) );
		setGravity( Gravity.BOTTOM );
		setOrientation( LinearLayout.VERTICAL );
		Binder.InflateResult result = Binder.inflateView( getContext(), R.layout.player_control_bar, this, true );
		view = result.rootView;

		txtCurrentTime = (TextView) view.findViewById(R.id.txtCurrentTime);
		txtTotalTime = (TextView) view.findViewById(R.id.txtTotalTime);

		btnBack = (ImageButton) view.findViewById( R.id.ic_player_ctrl_back );
		btnFastRewind = (ImageButton) view.findViewById( R.id.ic_player_ctrl_fast_rewind );
		btnPlayPause = (ImageButton) view.findViewById( R.id.ic_player_ctrl_play_pause );
		btnFastForward = (ImageButton) view.findViewById( R.id.ic_player_ctrl_fast_forward );
		btnSettings = (ImageButton) view.findViewById( R.id.ic_player_ctrl_settings );

		btnBack.setOnFocusChangeListener( this );
		btnFastRewind.setOnFocusChangeListener( this );
		btnPlayPause.setOnFocusChangeListener( this );
		btnFastForward.setOnFocusChangeListener( this );
		btnSettings.setOnFocusChangeListener( this );

		btnBack.setOnClickListener( this );
		btnFastRewind.setOnClickListener( this );
		btnPlayPause.setOnClickListener( this );
		btnFastForward.setOnClickListener( this );
		btnSettings.setOnClickListener( this );

		btnBack.setOnKeyListener( this );
		btnFastRewind.setOnKeyListener( this );
		btnPlayPause.setOnKeyListener( this );
		btnFastForward.setOnKeyListener( this );
		btnSettings.setOnKeyListener( this );

		seekBar = (MarkableSeekBar) view.findViewById(R.id.sbPlayerControlSeekBar);
		seekBar.setMax(SEEK_BAR_MAX_LEN);

		controlsContainer = view.findViewById( R.id.player_controls_container );
		progressContainer = view.findViewById( R.id.progressbar_container );
		updatePlayTimeDisplay();

		hopper = findViewById( R.id.focusHopper );
		return view;
	}

	public void setFocusedControl( Event evt ) {
		switch (evt) {
			case PLAY:
			case PAUSE:
				btnPlayPause.requestFocus();
				lastFocused = btnPlayPause;
				break;

			case SETTINGS:
				btnSettings.requestFocus();
				lastFocused = btnSettings;
				break;
		}

	}

	public void onFocusChange( View v, boolean hasFocus ) {
		if (hasFocus) {
			animateHopper( v );
			lastFocused = v;
		}
	}

	public void animateHopper( View v ) {

		int x = (v.getLeft() + v.getWidth() / 2);
		int y = (v.getTop() + v.getHeight() / 2);

		int px = (int) (hopper.getLeft() + hopper.getMeasuredWidth() / 2 + hopper.getTranslationX());
		int py = (int) (hopper.getTop() + hopper.getMeasuredHeight() / 2 + hopper.getTranslationY());

		int dx = x - px;
		int dy = y - py;

		ObjectAnimator oax = ObjectAnimator.ofFloat( hopper, "translationX", hopper.getTranslationX(), hopper.getTranslationX() + dx );
		oax.setDuration( 300 );
		oax.setInterpolator( new DecelerateInterpolator( 1.2f ) );
		oax.start();

		ObjectAnimator oay = ObjectAnimator.ofFloat( hopper, "translationY", hopper.getTranslationY(), hopper.getTranslationY() + dy );
		oay.setDuration( 300 );
		oay.setInterpolator( new DecelerateInterpolator( 1.2f ) );
		oay.start();
	}

	@Override
	public void onClick( View v ) {
		v.requestFocus();
		switch (v.getId()) {
			case R.id.ic_player_ctrl_play_pause:
				if (playerStatus == PlayerStatus.PLAYING)
					dispatchEvent( Event.PAUSE, null );
				else
					dispatchEvent( Event.PLAY, null );
				break;

			case R.id.ic_player_ctrl_fast_forward:
				dispatchEvent( Event.FAST_FORWARD, new SeekEventInfo( position ) );
				break;

			case R.id.ic_player_ctrl_fast_rewind:
				dispatchEvent( Event.FAST_REWIND, new SeekEventInfo( position ) );
				break;

			case R.id.ic_player_ctrl_back:
				dispatchEvent( Event.BACK, new SeekEventInfo( position ) );
				break;

			case R.id.ic_player_ctrl_settings:
				dispatchEvent( Event.SETTINGS, new SeekEventInfo( position ) );
				break;
		}
	}

	public void seek(long position) {
		// setPlayPauseButtonVisibility(false);
		dispatchEvent(Event.SEEK, new SeekEventInfo(position));
	}

	public void showStatus( PlayerStatus status ) {
		playerStatus = status;
		if (status == PlayerStatus.PLAYING) {
			showPauseButton();
		} else if (status == PlayerStatus.PAUSED) {
			showPlayButton();
		}
	}

	synchronized public void setPlaybackDuration(long duration) {
		if (duration == 0) {
			throw new IllegalArgumentException("Playback duration cannot be 0.");
		}

		this.duration = duration;
		seekBarStep = duration / (float) SEEK_BAR_MAX_LEN;
		updateTotalPlaybackTime();
	}

	synchronized public void setPosition(long position) {
		this.position = position;
		updatePlayTimeDisplay();

		if (seekBarStep != 0) {
			seekBar.setProgress( (int) (position / seekBarStep) );
		} else {
			seekBar.setProgress( 0 );
		}
	}

	public void showWithAnimation( AnimatorSet set, AnimatorListener listener ) {
		if (set == null) {
			ObjectAnimator alphaAnim = ObjectAnimator.ofFloat( view, "alpha", 1.0f );
			ObjectAnimator translateAnim = ObjectAnimator.ofFloat( view, "translationY", 0 );

			set = new AnimatorSet();
			set.playTogether( alphaAnim, translateAnim );
			set.setInterpolator( new DecelerateInterpolator( 1.2f ) );
			set.setDuration( ANIMATION_DURATION_SLIDE );
		}

		if (listener != null) {
			set.addListener( listener );
		}
		set.start();
	}

	public void hideWithAnimation( AnimatorSet set, AnimatorListener listener ) {
		if (set == null) {
			ObjectAnimator alphaAnim = ObjectAnimator.ofFloat( view, "alpha", 0.0f );

			DisplayMetrics dm = new DisplayMetrics();

			((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics( dm );

			int[] location = new int[2];
			view.getLocationOnScreen( location );
			float toY;
			toY = dm.heightPixels - location[1];
			ObjectAnimator translateAnim = ObjectAnimator.ofFloat( view, "translationY", toY );

			set = new AnimatorSet();
			set.playTogether( alphaAnim, translateAnim );
			set.setInterpolator( new DecelerateInterpolator( 1.2f ) );
			set.setDuration( ANIMATION_DURATION_SLIDE );
		}

		if (listener != null) {
			set.addListener( listener );
		}

		set.start();
	}

	public void hideControls() {
		ObjectAnimator alphaAnim = ObjectAnimator.ofFloat( controlsContainer, "alpha", 0.0f );

		alphaAnim.setInterpolator( new DecelerateInterpolator( 1.2f ) );
		alphaAnim.setDuration( 2000 );

		alphaAnim.addListener( new AnimatorListener() {

			@Override
			public void onAnimationCancel( Animator animation ) {
			}

			@Override
			public void onAnimationEnd( Animator animation ) {
			}

			@Override
			public void onAnimationRepeat( Animator animation ) {
			}

			@Override
			public void onAnimationStart( Animator animation ) {
				controlsContainer.setAlpha( 1.0f );
				controlsContainer.setVisibility( View.VISIBLE );
			}
		} );
		alphaAnim.start();
	}

	public void hideControlsImmediate() {
		controlsContainer.setVisibility( View.GONE );
	}

	public void showControlsImmediate() {
		controlsContainer.setVisibility( View.VISIBLE );
	}

	public boolean getAreControlsVisible() {
		return controlsContainer.getVisibility() == View.VISIBLE;
	}

	public void showControls() {
		ObjectAnimator alphaAnim = ObjectAnimator.ofFloat( controlsContainer, "alpha", 1.0f );

		alphaAnim.setInterpolator( new DecelerateInterpolator( 1.2f ) );
		alphaAnim.setDuration( 2000 );

		alphaAnim.addListener( new AnimatorListener() {

			@Override
			public void onAnimationCancel( Animator animation ) {
			}

			@Override
			public void onAnimationEnd( Animator animation ) {
			}

			@Override
			public void onAnimationRepeat( Animator animation ) {
			}

			@Override
			public void onAnimationStart( Animator animation ) {
				controlsContainer.setAlpha( 0.0f );
				controlsContainer.setVisibility( View.VISIBLE );
			}
		} );
		alphaAnim.start();
	}

	public void showProgressInfo() {
		ObjectAnimator alphaAnim = ObjectAnimator.ofFloat( progressContainer, "alpha", 1.0f );

		alphaAnim.setInterpolator( new DecelerateInterpolator( 1.2f ) );
		alphaAnim.setDuration( 2000 );

		alphaAnim.addListener( new AnimatorListener() {

			@Override
			public void onAnimationCancel( Animator animation ) {
			}

			@Override
			public void onAnimationEnd( Animator animation ) {
			}

			@Override
			public void onAnimationRepeat( Animator animation ) {
			}

			@Override
			public void onAnimationStart( Animator animation ) {
				progressContainer.setAlpha( 0.0f );
				progressContainer.setVisibility( View.VISIBLE );
			}
		} );
		alphaAnim.start();
	}

	public void showPlayButton() {
		btnPlayPause.setImageResource( R.drawable.ic_player_ctrl_play );
	}

	public void showPauseButton() {
		btnPlayPause.setImageResource(R.drawable.play_ctrl_pause);
	}

	public void startControlsFadeOutTimer() {
		stopControlsFadeOutTimer();
		fadeoutTimer = new Timer();
		fadeoutTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						PlayerControlBar.this.hideControls();
					}
				});
			}
		}, STAY_VISIBLE_DURATION);
	}

	public void stopControlsFadeOutTimer() {
		if (fadeoutTimer != null) {
			fadeoutTimer.cancel();
		}
		controlsContainer.clearAnimation();
		controlsContainer.setAlpha( 1.0f );
		controlsContainer.setVisibility( View.VISIBLE );
	}

	private static String timeStampToText(long timeStamp) {
		if (timeStamp % 1000 >= 500)
			timeStamp += 500;
		long hours = timeStamp / (MS_IN_HOUR);
		long minutes = (timeStamp - (hours * MS_IN_HOUR)) / MS_IN_MINUTE;
		long seconds = (timeStamp - (hours * MS_IN_HOUR) - (minutes * MS_IN_MINUTE)) / MS_IN_SECOND;
		String retString = "";
		if (hours > 0) {
			retString += (leadingZero(hours) + ":");
		}
		retString += (leadingZero(minutes) + ":" + leadingZero(seconds));

		return retString;
	}

	private static String leadingZero(long number) {
		return (number < 10) ? "0" + number : "" + number;
	}

	private void updatePlayTimeDisplay() {
		txtCurrentTime.setText(timeStampToText(position));
	}

	private void updateTotalPlaybackTime() {
		txtTotalTime.setText(timeStampToText(duration));
	}

	private ArrayList<ControlBarEventListener> eventListeners = new ArrayList<ControlBarEventListener>();

	public interface ControlBarEventListener {
		public void handleEvent(Event event, EventInfo eventInfo);
	}

	public void addEventListener(ControlBarEventListener eventListener) {
		eventListeners.add(eventListener);
	}

	public void removeEventListener(ControlBarEventListener eventListener) {
		eventListeners.remove(eventListener);
	}

	private void dispatchEvent(Event event, EventInfo eventInfo) {
		for (ControlBarEventListener listener : eventListeners) {
			listener.handleEvent(event, eventInfo);
		}
	}

	/**
	 * Stops the player rendering.
	 * 
	 * @param showReplayBtn
	 *            When true, displays the replay button. Otherwise, displays the
	 *            play button.
	 * 
	 */
	protected void stop(boolean showReplayBtn) {
		dispatchEvent(Event.STOP, null);
	}

	/**
	 * Sets the ad timeline
	 * 
	 * @param timeline
	 */
	public void setTimeline(Timeline<TimelineMarker> timeline) {
		seekBar.clearMarkers();

		if (timeline == null)
			return;

		// Add the seek bar markers.
		Iterator<TimelineMarker> iterator = timeline.timelineMarkers();
		while (iterator.hasNext()) {
			TimelineMarker marker = iterator.next();
			// Calculate the start/end points on the seek bar
			int start = (int) ((marker.getTime() / (float) duration) * SEEK_BAR_MAX_LEN);
			int end = (int) (((marker.getTime() + marker.getDuration()) / (float) duration) * SEEK_BAR_MAX_LEN);
			if (start == end)
				end = start + 1;
			try {
				seekBar.addMarker(new MarkableSeekBar.Marker(start, end));
			} catch (IllegalArgumentException e) {
				// XideoApplication.logger.e( LOG_TAG + "::setTimeline()",
				// "Failed to add ad marker on the seek bar." );
			}
		}
	}

	/**
     */
	public void setAvailable(int start, int end) {
		try {
			seekBar.setAvailable(start, end);
		} catch (IllegalArgumentException e) {
			// XideoApplication.logger.e( LOG_TAG + "::setAvailable()",
			// e.getMessage() );
		}
	}

	@Override
	protected boolean onRequestFocusInDescendants( int direction, Rect previouslyFocusedRect ) {

		if (lastFocused != null) {
			lastFocused.requestFocus();
			return true;
		}

		if (direction == View.FOCUS_FORWARD) {
			direction = View.FOCUS_RIGHT;
		} else if (direction == View.FOCUS_BACKWARD) {
			direction = View.FOCUS_LEFT;
		}

		final View nextFocus = previouslyFocusedRect == null ?
				FocusFinder.getInstance().findNextFocus( this, null, direction ) :
				FocusFinder.getInstance().findNextFocusFromRect( this, previouslyFocusedRect, direction );

		if (nextFocus == null) {
			return false;
		}

		return nextFocus.requestFocus( direction, previouslyFocusedRect );
	}

	@Override
	public boolean onKey( View v, int keyCode, KeyEvent event ) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
				case KeyEvent.KEYCODE_MEDIA_PAUSE:
				case KeyEvent.KEYCODE_MEDIA_PLAY:
					onClick( btnPlayPause );
					break;
			}
		}
		return false;
	}

}
