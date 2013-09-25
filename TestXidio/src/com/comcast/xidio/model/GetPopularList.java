package com.comcast.xidio.model;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.comcast.xidio.core.common.XidioAsynTask;
import com.xfinity.xidio.core.URLFactory;

public class GetPopularList 
{
	private static GetPopularList instance;

	public static synchronized GetPopularList getInstance()
	{
		if (instance == null)
			instance = new GetPopularList();
		return instance;
	}

	public JSONArray getPopularList() 
	{
		
		try {
			JSONObject temp=new XidioAsynTask().execute(URLFactory.getPopularContentURL()).get();
			
			return temp.has("contentPanelElements")?(temp.getJSONObject("contentPanelElements").has("contentPanelElement")?temp.getJSONObject("contentPanelElements").getJSONArray("contentPanelElement"):null):null;

			
		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), "Failed to getPopularList ", e);
		}
		return null;

	}

}
