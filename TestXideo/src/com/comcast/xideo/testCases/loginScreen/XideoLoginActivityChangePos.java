package com.comcast.xideo.testCases.loginScreen;

import java.util.Locale;

import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.EditText;

import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;

public class XideoLoginActivityChangePos extends ActivityInstrumentationTestCase2<FirstRun> {
	private Solo solo;

	public XideoLoginActivityChangePos() {
		super(FirstRun.class);

	}

	@Override
	protected void setUp() throws Exception {
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		super.setUp();
	}

	public void testXXideoLoginActivityChangePos() 
	{
	
		assertTrue(solo.waitForActivity(TestConstants.FIRST_RUN));
		solo.sleep(1000);
		
		if (!Build.MODEL.toLowerCase(Locale.US).contains("comcast")) 
		{
			
			solo.enterText((EditText) solo.getCurrentActivity().findViewById(com.xfinity.xidio.R.id.first_run_username),"test_115");
			solo.enterText((EditText) solo.getCurrentActivity().findViewById(com.xfinity.xidio.R.id.first_run_password),"Demo1111");
			solo.sleep(500);
			solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
			solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
			solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
			assertTrue(solo.waitForActivity(TestConstants.MAIN_ACTIVITY));

			
			solo.sleep(1000);
		}

	}
	}
