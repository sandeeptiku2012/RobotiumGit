package com.comcast.xidio.model;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.comcast.xidio.core.common.XideoAsynTask;
import com.xfinity.xidio.core.URLFactory;
/**
 * 
 * @author Dillip Lenka
 *
 */
public class GetFeaturedList 
{
	private static GetFeaturedList instance;

	public static synchronized GetFeaturedList getInstance()
	{
		if (instance == null)
			instance = new GetFeaturedList();
		return instance;
	}

	public JSONArray getFeaturedList()
	{
		
		try {
			JSONObject temp=new XideoAsynTask().execute(URLFactory.getFeaturedContentURL()).get();
			
			return temp.has("contentPanelElements")?(temp.getJSONObject("contentPanelElements").has("contentPanelElement")?temp.getJSONObject("contentPanelElements").getJSONArray("contentPanelElement"):null):null;
		} catch (Exception e) 
		{
			Log.e(this.getClass().getCanonicalName(), "Failed to getFeaturedList ", e);
		}
		return null;

	}

}
