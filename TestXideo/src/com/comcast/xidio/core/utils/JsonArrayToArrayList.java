package com.comcast.xidio.core.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class JsonArrayToArrayList
{

	private static JsonArrayToArrayList instance;

	public static JsonArrayToArrayList getInstance()
	{
		return (instance == null) ? new JsonArrayToArrayList() : instance;
	}

	public ArrayList<JSONObject> convert(JSONArray jArray) {

		ArrayList<JSONObject> retItems = new ArrayList<JSONObject>();
		if (jArray != null) {
			for (int i = 0; i < jArray.length(); i++) {
				try {
					
					retItems.add(jArray.getJSONObject(i));

				} catch (Exception e) 
				{
					Log.e(this.getClass().getCanonicalName(), "Failed Invoking convert Json array " , e);
				}


			}
			return retItems;
		} else
			return null;

	}

}
