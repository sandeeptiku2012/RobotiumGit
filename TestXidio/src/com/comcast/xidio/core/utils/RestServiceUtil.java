package com.comcast.xidio.core.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;


public class RestServiceUtil 
{
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
