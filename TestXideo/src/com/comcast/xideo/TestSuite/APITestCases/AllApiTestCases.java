package com.comcast.xideo.TestSuite.APITestCases;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

public class AllApiTestCases extends TestSuite {
	public static Test suite() {

		// Class[]
		// apiTest={XideoDetailsActivityChange.class,XideoDetailsActivityChangeTitle.class,XideoHomeActivityFeaturedListTitle.class};
		// TestSuite suite = new TestSuite();

		return new TestSuiteBuilder(AllApiTestCases.class).includePackages(
				new String[] { "com.comcast.xideo.testCases" }).build();
		
		
		// suite.addTest(new XideoDetailsActivityChange());
		// return suite;
	}
}
