package com.comcast.xidio.core.common;

import android.app.Activity;
import android.app.Instrumentation;

import com.jayway.android.robotium.solo.Solo;

public class GetSolo 
{
	
	private static GetSolo instance;
	private static Solo solo=null;

	public static GetSolo getInstance()
	{
		if (instance == null)
			instance = new GetSolo();
		return instance;
	}

	public void setUpSolo(Instrumentation inst,Activity activity)
	{
		solo = new Solo(inst, activity);
		
	}
	
	public Solo getSoloObject()
	{ 
		return solo;
	}

}
