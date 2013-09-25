package com.comcast.xidio.model;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.xfinity.xidio.core.URLFactory;

public class GetUserData
{
	private static GetUserData instance;

	public static synchronized GetUserData getInstance() 
	{
		if (instance == null)
			instance = new GetUserData();
		return instance;
	}

	public JSONObject getUserData()
	{

		try {
			return new UserInformationAsynTask().execute(URLFactory.getUserDataUrl()).get();

		} catch (Exception e)
		{
			Log.e(this.getClass().getCanonicalName(),"Failed to getUserData", e);
		}
		return null;

	}
	

	public class UserInformationAsynTask extends AsyncTask<String, Void, JSONObject> 
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
