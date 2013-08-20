package com.comcast.xideo.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.xfinity.xidio.core.URLFactory;



public class GetSubscriptionList 
{
	private static GetSubscriptionList instance;
	
	public static GetSubscriptionList getInstance() 
	{
		if (instance == null)
			instance = new GetSubscriptionList();
		return instance;
	}
	
	Context con;
	
	public JSONArray getSubscriptionList(String id,Context con) 
	{
		
		try {
			//GetLoginResponse.getInstance().getLoginResponse();
			this.con=con;
			
			//Log.v("Last Logged in user ", XidioApplication.getLastLoggedInUser());
			//JSONObject tempSubscription =new SubscriptionAsynTask().execute(URLFactory.getSubscriptions("1792275")).get();
			JSONObject tempSubscription =new SubscriptionAsynTask().execute(URLFactory.getSubscriptions(id)).get();
			Log.v("Subscrotion response ", tempSubscription.toString());
			return tempSubscription.has("categories")?(tempSubscription.getJSONObject("categories").has("category")?tempSubscription.getJSONObject("categories").getJSONArray("category"):null):null;

			
		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), "Failed to get subscription list ", e);
		}
		return null;

	}
	
	
	
	
	
	
	
	
	public class SubscriptionAsynTask extends AsyncTask<String, Void, JSONObject> {
		//private JSONObject jArrayElements;
	private	JSONObject jsonObj =null;
	
	public String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	
		@Override
		protected JSONObject doInBackground(String... params)
		{
            try

            {URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
              InputStream in = new BufferedInputStream(urlConnection.getInputStream());
              String abc=convertStreamToString(in);
              
              return new JSONObject(abc); 
              
            } 
            catch(Exception e)
            {
            	e.printStackTrace();
            }
            
            finally {
              urlConnection.disconnect();
            }
            }

            catch (Exception e)

            {

                            Log.e(this.getClass().getCanonicalName(), "Failed Adding Follow Up URL " + params[0], e);

                           return null;

            }
			
			
			/*
			
			
			String response =null;
			HttpPost post = new HttpPost(params[0]);
			post.setHeader("content-type", "application/json; charset=UTF-8");
			//post.setHeader("Accept", "application/json;fields=data+counts");   
            try{          
	            	RestServiceUtil restApi = new RestServiceUtil();
	            	response = restApi.executeHTTPPost(post);
	        	   	Log.v("popularResponse ", response);
	            	
	        	}
	            catch (Exception e)
		            {
		               Log.e(this.getClass().getCanonicalName(), "Failed Invoking popular list " + params[0], e);
		               response =null;
		
		            }
	        if(response!= null)
	        {
	        	
	        	JSONObject jObjElements = null;
		        try {
		        	jsonObj = new JSONObject(response);
		        	Log.v("jsonObj details ", jsonObj.toString());
		        	
		        } catch (JSONException e) {
		            Log.e("JSON Parser", "Error parsing data " + e.toString());
		        }
		       
			   
	        }
		        return jsonObj;	*/
            return null;
			
		}
	}
	
	
}
