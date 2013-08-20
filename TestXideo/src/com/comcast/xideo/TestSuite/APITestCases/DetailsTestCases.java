package com.comcast.xideo.TestSuite.APITestCases;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

public class DetailsTestCases extends TestSuite {
	public static Test suite() 
	{
		return new TestSuiteBuilder(DetailsTestCases.class).includePackages(new String[] { "com.comcast.xideo.testCases.details" }).build();		
		
	}
}
