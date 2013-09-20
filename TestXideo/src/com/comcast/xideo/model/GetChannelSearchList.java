package com.comcast.xideo.model;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.comcast.xideo.core.common.XideoAsynTask;
import com.xfinity.xidio.core.URLFactory;
/**
 * Get list of channels from search text.
 * 
 * @author Dillip Lenka
 *
 */
public class GetChannelSearchList
{
	private static GetChannelSearchList instance;

	public static synchronized GetChannelSearchList getInstance() 
	{
		return (instance == null) ? new GetChannelSearchList() : instance;
	}

	public JSONArray getChannelSearchList(String filterText)
	{
	
		try {
			JSONObject temp=new XideoAsynTask().execute(URLFactory.SearchForChannelAndShow(filterText)).get();
			
			return temp.has("categories")?(temp.getJSONObject("categories").has("category")?temp.getJSONObject("categories").getJSONArray("category"):new JSONArray()):new JSONArray();
			}
			catch (Exception e)
			{
				Log.e("Exception occured in get episodes list from search criteria", e.getLocalizedMessage());			
			}
		return null;

	}
}
