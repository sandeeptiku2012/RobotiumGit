package com.comcast.xideo.TestSuite.LoginSuite;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

public class LoginTestCases extends TestSuite {
	public static Test suite() {

		// Class[]
		// apiTest={XideoDetailsActivityChange.class,XideoDetailsActivityChangeTitle.class,XideoHomeActivityFeaturedListTitle.class};
		// TestSuite suite = new TestSuite();

		return new TestSuiteBuilder(LoginTestCases.class).includePackages(
				new String[] { "com.comcast.xideo.testCases.loginScreen", "com.comcast.xideo.testCases.Authentication"}).build();
		
		
		// suite.addTest(new XideoDetailsActivityChange());
		// return suite;
	}
}
