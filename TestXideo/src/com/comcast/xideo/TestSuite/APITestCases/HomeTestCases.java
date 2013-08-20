package com.comcast.xideo.TestSuite.APITestCases;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

public class HomeTestCases extends TestSuite {
	public static Test suite()
	{
		return new TestSuiteBuilder(HomeTestCases.class).includePackages(new String[] { "com.comcast.xideo.testCases.home" }).build();
		
	}
}
