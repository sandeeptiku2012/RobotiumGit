package com.comcast.xideo.testCases.Settings;

import java.io.File;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.comcast.xideo.model.GetLoginResponse;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;

public class ClearCacheButton extends ActivityInstrumentationTestCase2<FirstRun>{

	private Solo solo;	
	
	public ClearCacheButton() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception {

		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		super.setUp();
	}

	public void testClearCache() 
	{
		solo.waitForActivity(TestConstants.FIRST_RUN);
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(1000);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		GetLoginResponse.getInstance().getLoginResponse(TestConstants.USERNAME, TestConstants.PASSWORD);
		
		solo.sleep(4000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		
		assertTrue(solo.searchText(TestConstants.SETTINGS));
		solo.sleep(1000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		assertTrue(solo.searchText("CLEAR CACHE"));
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
		solo.sleep(1000);
		
		File dir = getActivity().getExternalFilesDir(null);
		if (dir != null && dir.isDirectory()) 
				assertTrue(dir.list().length==0);		
		
		
	}
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
		super.tearDown();
	}

}
