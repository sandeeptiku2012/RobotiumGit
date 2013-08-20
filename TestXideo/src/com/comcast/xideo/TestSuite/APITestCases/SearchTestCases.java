package com.comcast.xideo.TestSuite.APITestCases;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

public class SearchTestCases extends TestSuite {
	public static Test suite() {

		// Class[]
		// apiTest={XideoDetailsActivityChange.class,XideoDetailsActivityChangeTitle.class,XideoHomeActivityFeaturedListTitle.class};

		// TestSuite suite = new TestSuite();
		return new TestSuiteBuilder(SearchTestCases.class).includePackages(
				new String[] { "com.comcast.xideo.testCases.Search" })

		.build();
		// suite.addTest(new XideoDetailsActivityChange());
		// return suite;
	}
}
