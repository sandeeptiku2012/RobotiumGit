package com.comcast.xideo.TestSuite.APITestCases;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

public class NavigationBarTestCases extends TestSuite {
	public static Test suite() 
	{

		return new TestSuiteBuilder(NavigationBarTestCases.class).includePackages(new String[] { "com.comcast.xideo.testCases.navogationBar" }).build();
		
	}
}
