package com.comcast.xideo.model;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.comcast.xideo.core.common.XideoAsynTask;
import com.xfinity.xidio.core.URLFactory;

public class GetUpNextList {
	private static GetUpNextList instance;

	public static GetUpNextList getInstance() {
		if (instance == null)
			instance = new GetUpNextList();
		return instance;
	}

	public JSONArray getUpNextList(String sessionId,String userId) {
		
		try {
			JSONObject temp=new XideoAsynTask()
			.execute(URLFactory.getUpNextUrl(userId, sessionId)).get();
			//getJSONArray("items");
			return temp.has("items")?temp.getJSONArray("items"):null;
		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), "Failed to getUpNextList "+"Session id "+sessionId+" userId "+userId, e);
		}
		return null;

	}

}


