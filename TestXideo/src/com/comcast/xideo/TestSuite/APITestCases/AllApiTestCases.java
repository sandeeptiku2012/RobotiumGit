package com.comcast.xideo.TestSuite.APITestCases;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

public class AllApiTestCases extends TestSuite 
{
	public static Test suite() 
	{
		return null;//new TestSuiteBuilder(AllApiTestCases.class).includePackages(new String[] { "com.comcast.xideo.testCases" }).build();
		
	}
}
