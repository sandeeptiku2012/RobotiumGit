package com.comcast.xidio.model;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.comcast.xidio.core.common.XideoAsynTask;
import com.xfinity.xidio.core.URLFactory;
/**
 * Get List of episodes from search list.
 * 
 * @author Dillip Lenka
 *
 */
public class GetEpisodeSearchList 
{
	private static GetEpisodeSearchList instance;

	public static synchronized GetEpisodeSearchList getInstance() {
		return (instance == null) ? new GetEpisodeSearchList() : instance;
	}

	public JSONArray getEpisodeSearchList(String filterText)
	{
	
		try 
		{
			JSONObject temp=new XideoAsynTask().execute(URLFactory.SearchForEpisode(filterText)).get();
			return temp.has("assets")?(temp.getJSONObject("assets").has("asset")?temp.getJSONObject("assets").getJSONArray("asset"):new JSONArray()):new JSONArray();
			
		} catch (Exception e)
		{
			Log.e("Exception occured in get episodes list from search criteria", e.getLocalizedMessage());			
			e.printStackTrace();
		}
		return null;

	}
}
