package com.comcast.xidio.core.common;

import org.json.JSONArray;

import com.comcast.xidio.model.GetFeaturedList;
import com.comcast.xidio.model.GetPopularList;
import com.comcast.xidio.model.GetUpNextList;

public class GetCatagoryLists  
{
	
	public static GetCatagoryLists instance;
	
	private static JSONArray featuredList=null;
	private static JSONArray popularList=null;
	private static JSONArray upNextList=null;
	

	public static GetCatagoryLists getInstance()
	{
		if (instance == null)
			instance = new GetCatagoryLists();
		return instance;
	}

	public void storeBasicLists(String sessionId,String userId)
	{
		featuredList = GetFeaturedList.getInstance().getFeaturedList();
		popularList = GetPopularList.getInstance().getPopularList();
		upNextList = GetUpNextList.getInstance().getUpNextList(sessionId,userId);
		
	}

	public JSONArray getFeaturedList()
	{
		return featuredList==null?null:featuredList;
	}

	public JSONArray getPopularList()
	{
		return popularList==null?null:popularList;
	}
	

	public JSONArray getUpNextList()
	{
		return upNextList==null?null:upNextList;
	}
	
}
