package com.comcast.xidio.core.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * Util class will be used for Rest service call.
 * 
 * @author Dillip.Lenka
 *
 */
public class RestServiceUtil 
{
	/**
	 * A generic method to execute all of HTTP Get request 
	 * 
	 * @param {@link HttpGet}
	 * 
	 * @return {@link HttpResponse}
	 */
	public String executeHTTPGet(HttpGet get) throws Exception
	{
		HttpClient httpClient = new DefaultHttpClient();
				
		try
		{
			ResponseHandler<String> responseHandler = new BasicResponseHandler(); 
			String responseBody = httpClient.execute(get, responseHandler); 
		    
			return responseBody;
		} 
		catch (Exception e) 
		{
		    Log.e(getClass().getSimpleName(), "HTTP protocol error", e);
		    throw e; 
		} 
	}
	
	/**
	 * A generic method to execute all of HTTP Get request 
	 * 
	 * @param {@link HttpGet}
	 * 
	 * @return {@link HttpResponse}
	 */
	public String executeHTTPPost(HttpPost post) throws Exception
	{
		HttpClient httpClient = new DefaultHttpClient();
				
		try
		{
			ResponseHandler<String> responseHandler = new BasicResponseHandler(); 
			String responseBody = httpClient.execute(post, responseHandler); 
		    
			return responseBody;
		} 
		catch (Exception e) 
		{
		    Log.e(getClass().getSimpleName(), "HTTP protocol error", e);
		    throw e; 
		} 
	}
}
