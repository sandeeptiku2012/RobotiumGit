package com.comcast.xidio.testCases.settings;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xidio.core.common.GetSolo;
import com.comcast.xidio.core.constant.TestConstants;
import com.comcast.xidio.model.GetLoginResponse;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;

public class XidioSettingsLabelCheck extends ActivityInstrumentationTestCase2<FirstRun>{

	private Solo solo;	
	
	public XidioSettingsLabelCheck() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception {

		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		super.setUp();
	}

	public void testXidioSettingsLabelCheck() 
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
		assertTrue(solo.searchText("DISPLAY"));
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		assertTrue(solo.searchText("NETWORKING"));
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		assertTrue(solo.searchText("CLEAR CACHE"));
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		assertTrue(solo.searchText("Information"));	
		
		
	}
	
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}

}
