package com.comcast.xideo.testCases;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.videoplayer.ui.PlayerActivity;
import com.comcast.xideo.activity.XideoHomeActivity;
import com.comcast.xideo.activity.XideoSplashActivity;
import com.comcast.xideo.core.TestConstants;
import com.jayway.android.robotium.solo.Solo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class XideoActivityTest extends ActivityInstrumentationTestCase2<XideoSplashActivity> {
	private Solo solo;
	
	public XideoActivityTest() {
		super(XideoSplashActivity.class);
		
	}
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		Intent intent = new Intent( Intent.ACTION_MAIN );
		intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		setActivityIntent( intent );
		solo = new Solo( getInstrumentation(), getActivity() );
	}

	public void testA()
	{
		
		String text = getActivity().getResources().getString(com.comcast.xideo.R.string.search).toUpperCase();		
		solo.sleep( 200);
		assertTrue( solo.waitForText( text ) );
		solo.sleep( 200);
		
		String text2 = TestConstants.HOME;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 500 );
		assertTrue( solo.waitForText( text2 ) );
		solo.sleep( 200 );
		
		String text3 = TestConstants.SUBSCRIPTIONS;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 500 );
		assertTrue( solo.waitForText( text3 ) );
		solo.sleep( 200 );
		
		String text4 = TestConstants.SETTINGS;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 500 );
		assertTrue( solo.waitForText( text4 ) );
		solo.sleep( 200 );
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep( 500 );
		assertTrue( solo.waitForText( text3 ) );
		solo.sleep( 200 );
		
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep( 500 );
		assertTrue( solo.waitForText( text2 ) );
		solo.sleep( 200 );
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep( 500 );
		assertTrue( solo.waitForText( text ) );
		solo.sleep( 200);
		
		
	}
			
	

}
