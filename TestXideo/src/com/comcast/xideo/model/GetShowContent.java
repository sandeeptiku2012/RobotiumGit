package com.comcast.xideo.model;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.comcast.xideo.core.common.XideoAsynTask;
import com.xfinity.xidio.core.URLFactory;
import com.xfinity.xidio.vimond.models.BaseObject;

public class GetShowContent 
{
	private static GetShowContent instance;

	public static GetShowContent getInstance()
	{
		if (instance == null)
			instance = new GetShowContent();
		return instance;
	}

	public JSONArray getShowContent(String showId) {
		BaseObject showObj = new BaseObject();
		showObj.setId(Long.parseLong(showId));
		try {
			JSONObject temp=new XideoAsynTask()
					.execute(URLFactory.getShowContentURL(showObj)).get();
			
			
			return temp.has("categories")?(temp.getJSONObject("categories").has("category")?temp.getJSONObject("categories").getJSONArray("category"):null):null;

		} catch (Exception e) {
			Log.e("Exception occured in get show content", e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;

	}

}

