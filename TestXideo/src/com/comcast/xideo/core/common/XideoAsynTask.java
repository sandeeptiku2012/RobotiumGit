package com.comcast.xideo.core.common;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.comcast.xideo.core.utils.RestServiceUtil;

public class XideoAsynTask extends AsyncTask<String, Void, JSONObject>
{
	
private	JSONObject jsonObj =null;
	@Override
	protected JSONObject doInBackground(String... params)
	{
		String popularResponse = null;
		HttpGet get = new HttpGet(params[0]);
        get.setHeader("Content-Type", "application/json");
        get.setHeader("Accept", "application/json;fields=data+counts");          
        try{  
        		RestServiceUtil restApi = new RestServiceUtil();
        		popularResponse = restApi.executeHTTPGet(get);
            	Log.v("popularResponse ", popularResponse);
            	
        	}
            catch (Exception e)
	            {
	               Log.e(this.getClass().getCanonicalName(), "Failed : " + params[0], e);
	               popularResponse =null;
	
	            }
        if(popularResponse!= null)
        {
        	
        	JSONObject jObjElements = null;
	        try {
	        	jsonObj = new JSONObject(popularResponse);
	        	Log.v("jsonObj details ", jsonObj.toString());
	        	
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
	       
		   
        }
	        return jsonObj;
	 
	}

}
