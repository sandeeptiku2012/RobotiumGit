package com.comcast.xidio.model;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.xfinity.xidio.core.URLFactory;



public class GetSubscriptionList 
{
	private static GetSubscriptionList instance;
	
	
	public static synchronized GetSubscriptionList getInstance() 
	{
		if (instance == null)
			instance = new GetSubscriptionList();
		return instance;
	}
	
	
	
	public JSONArray getSubscriptionList(String id) 
	{
		
		try 
		{
				JSONObject tempSubscription =new SubscriptionAsynTask().execute(URLFactory.getSubscriptions(id)).get();
				Log.v("Subscrotion response ", tempSubscription.toString());
				return tempSubscription.has("categories")?(tempSubscription.getJSONObject("categories").has("category")?tempSubscription.getJSONObject("categories").getJSONArray("category"):null):null;

		} 
		catch (Exception e)
		{
			Log.e(this.getClass().getCanonicalName(), "Failed to get subscription list ", e);
		}
		return null;

	}
		
	
	public class SubscriptionAsynTask extends AsyncTask<String, Void, JSONObject> 
	{
	
	
		public String convertStreamToString(java.io.InputStream is)
		{
		    Scanner s = new Scanner(is).useDelimiter("\\A");
		    return s.hasNext() ? s.next() : "";
		}
	
	
		@Override
		protected JSONObject doInBackground(String... params)
		{
            try

            {
            	URL url = new URL(params[0]);
            	HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            	try {
            			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            			String abc=convertStreamToString(in);              
            			return new JSONObject(abc); 
              
            		} 
            		catch(Exception e)
            		{
            			 Log.e(this.getClass().getCanonicalName(), "Failed to get subscription lis " + params[0], e);
            		}
                   	finally {
                   		urlConnection.disconnect();
                   	}
            	}

            	catch (Exception e)
            	{

                   Log.e(this.getClass().getCanonicalName(), "Failed to get subscription list " + params[0], e);

                   return null;
            	}
			
            return null;
			
		}
	}
	
	
}
