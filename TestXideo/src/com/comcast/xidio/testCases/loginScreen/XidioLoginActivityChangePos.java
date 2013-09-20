package com.comcast.xidio.testCases.loginScreen;

import java.util.Locale;

import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.EditText;

import com.comcast.xidio.core.common.GetSolo;
import com.comcast.xidio.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;

public class XidioLoginActivityChangePos extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;

	public XidioLoginActivityChangePos() {
		super(FirstRun.class);

	}

	@Override
	protected void setUp() throws Exception {
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		super.setUp();
	}

	public void testXideoLoginActivityChangePos() 
	{
	
		assertTrue(solo.waitForActivity(TestConstants.FIRST_RUN));
		solo.sleep(TestConstants.SLEEP_TIME_1000);
		if (!Build.MODEL.toLowerCase(Locale.US).contains("comcast")) 
		{
			solo.enterText((EditText) solo.getCurrentActivity().findViewById(com.xfinity.xidio.R.id.first_run_username),"test_115");
			solo.enterText((EditText) solo.getCurrentActivity().findViewById(com.xfinity.xidio.R.id.first_run_password),"Demo1111");
			solo.sleep(TestConstants.SLEEP_TIME_500);
			solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
			solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
			solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
			assertTrue(solo.waitForActivity(TestConstants.MAIN_ACTIVITY));
			solo.sleep(TestConstants.SLEEP_TIME_1000);
		}

	}
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}
	}
