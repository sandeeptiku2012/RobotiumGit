package com.comcast.xidio.core.common;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class FilterObject
{

	private static FilterObject instance;

	public static FilterObject getInstance() 
	{
		return (instance == null) ? new FilterObject() : instance;
	}

	public ArrayList<JSONObject> getFilteredObjectList(JSONArray jArray,String filterText)
	{
		
		ArrayList<JSONObject> retItems=new ArrayList<JSONObject>();
		if(jArray!=null)
		{
			for (int i = 0; i < jArray.length(); i++) 
			{
				JSONObject currentChannel = null;
				String ChannelTitle;
				try 
				{
					currentChannel = jArray.getJSONObject(i);
					ChannelTitle = currentChannel.getString("title");
					if(ChannelTitle.toLowerCase().contains(filterText.toLowerCase()))
						retItems.add(currentChannel);
					
					
				} catch (Exception e)
				{
					 Log.e(this.getClass().getCanonicalName(), "Failed Invoking getFilteredObjectList " , e);
				}			
			
			}
			return retItems;
		}
		else 
			return null;
		
	}
}
