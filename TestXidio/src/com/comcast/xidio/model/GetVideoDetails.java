package com.comcast.xidio.model;

import java.util.concurrent.ExecutionException;

import org.apache.http.client.methods.HttpGet;

import android.os.AsyncTask;
import android.util.Log;

import com.comcast.xidio.core.utils.RestServiceUtil;
import com.xfinity.xidio.core.URLFactory;
import com.xfinity.xidio.vimond.models.Episode;

public class GetVideoDetails 
{
	private String videoDetailsRespone;	
	
	/**
	 * @return the videoDetailsRespone
	 */
	public String getVideoDetailsRespone() {
		return videoDetailsRespone;
	}

	/**
	 * @param videoDetailsRespone the videoDetailsRespone to set
	 */
	public void setVideoDetailsRespone(String videoDetailsRespone) {
		this.videoDetailsRespone = videoDetailsRespone;
	}

	public String getVideoDetails(String videoId)
	{
		Episode episode = new Episode();
		episode.setId(Long.valueOf(videoId));
		String videoDetailsUrl =URLFactory.getPlaybackUrl(episode);
		Log.v("videoDetailsUrl ", videoDetailsUrl);
		
		try {
			videoDetailsRespone = new VideoDeailsAsynkTask().execute(videoDetailsUrl).get();
		} 
		catch (InterruptedException e) 
		{
			Log.e(this.getClass().getCanonicalName(), "Failed to getVideoDetails "+"videoId "+videoId, e);
		}
		catch (ExecutionException e) 
		{
			Log.e(this.getClass().getCanonicalName(), "Failed to getVideoDetails "+"videoId "+videoId, e);
		}
			
		return videoDetailsRespone;
	
		
	}
	
	private class  VideoDeailsAsynkTask extends  AsyncTask<String,Void, String>
	{
		private String  videoResponse =null;

		@Override
		protected String doInBackground(String... videoDetailsUrl) 
		{
			
			HttpGet get = new HttpGet(videoDetailsUrl[0]);
            get.setHeader("Content-Type", "application/json");
            get.setHeader("Accept", "application/json;fields=data+counts");   
            try{          

	            	RestServiceUtil restApi = new RestServiceUtil();
	            	videoResponse= restApi.executeHTTPGet(get);
	            	Log.v("videoResponse ", videoResponse);
	            	
            	}catch (Exception e)
		            {
		               Log.e(this.getClass().getCanonicalName(), "Failed Invoking get playback URL " + videoDetailsUrl[0], e);
		               videoResponse =null;
		
		            }
					return videoResponse;
		}
		@Override
		protected void onPostExecute(String  response) {			
			 super.onPostExecute(response);
			GetVideoDetails.this.setVideoDetailsRespone(response);
		}
		
		
	}
}
