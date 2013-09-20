package com.comcast.xidio.listener;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.AttributeSet;

import com.xfinity.xidio.views.VideoPlayerView;

public class VideoPlayerListner extends VideoPlayerView implements OnPreparedListener, OnCompletionListener, OnErrorListener
{

	   private PlayPauseListener mListener;
	   private VideoPlayerView videoPlayer;
	   private Context mContext;
	   private String mediaUrl;
	   private String assetId;

	   
	 
	   /**
	 * @return the mListener
	 */
	public PlayPauseListener getmListener() {
		return mListener;
	}

	/**
	 * @param mListener the mListener to set
	 */
	public void setmListener(PlayPauseListener mListener) {
		this.mListener = mListener;
	}

	/**
	 * @return the mediaUrl
	 */
	public String getMediaUrl() {
		return mediaUrl;
	}

	/**
	 * @param mediaUrl the mediaUrl to set
	 */
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	/**
	 * @return the assetId
	 */
	public String getAssetId() {
		return assetId;
	}

	/**
	 * @param assetId the assetId to set
	 */
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public VideoPlayerListner(Context context) 
	   {
	       super(context);
	       this.mContext =context;
	       setMeasuredDimension(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	   }

	   public VideoPlayerListner(Context context, AttributeSet attrs) {
	       super(context, attrs);
	       setMeasuredDimension(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	   }

	   public VideoPlayerListner(Context context, AttributeSet attrs, int defStyle) 
	   {
	       super(context, attrs, defStyle);
	       setMeasuredDimension(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	   }

	   public void setPlayPauseListener(PlayPauseListener listener) {
	       mListener = listener;
	    }

	   /**
	    * Pause the video.
	    */
	   public void pause()
	   {
	       super.pauseVideo();
	       if (mListener != null)
	       {
	           mListener.onPause();
	       }
	   }
	   /**
	    * Start the video again
	    * 
	    */
	   public void start() {
	       super.play(mediaUrl, assetId);
	       if (mListener != null) {
	           mListener.onPlay();
	       }
	   }

	   public void resume()
	   {
		   super.resumeVideo();
		   if (mListener != null) {
	           mListener.onResume();
	       }
	   }

	   /**
	    * Get the current volume of the video;
	    * @return
	    */
	   public int getVolume()
	   {
		   AudioManager audMgr = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
		   int volume = audMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		   return volume;
	   }
	  
	   
		public VideoPlayerView getVideoPlayer() {
			return videoPlayer;
		}

		public void setVideoPlayer(VideoPlayerView videoPlayer)
		{
			this.videoPlayer = videoPlayer;
		}

		 public Context getmContext()
		 {
				return mContext;
		 }

		public void setmContext(Context mContext)
			{
				this.mContext = mContext;
			}
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			
			return false;
		}

		@Override
		public void onCompletion(MediaPlayer mp) {
			
			
		}

		@Override
		public void onPrepared(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			
		}

		

}
