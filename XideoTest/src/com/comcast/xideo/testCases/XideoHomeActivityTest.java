package com.comcast.xideo.testCases;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.videoplayer.ui.PlayerActivity;
import com.comcast.xideo.activity.XideoHomeActivity;
import com.comcast.xideo.core.TestConstants;
import com.jayway.android.robotium.solo.Solo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class XideoHomeActivityTest extends ActivityInstrumentationTestCase2<XideoHomeActivity> {
	private Solo solo;
	
	public XideoHomeActivityTest() {
		super(XideoHomeActivity.class);
		
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
		String text = TestConstants.FEATURE;
		solo.sleep( 2000 );
		assertTrue( solo.waitForText( text ) );
		//String text2 ="Divas of Music";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text) );
		//String text3 ="Trailer Mania";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text ) );
		//String text4 ="Vidar international film";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text ) );
		solo.sleep( 200 );
		
		
		String text2 = TestConstants.POPULAR;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text2 ) );
		//String text6 ="Divas of Music";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text2 ) );
		//String text7 ="Moods of Norway";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text2 ) );
		//String text8 ="Cinematics";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text2 ) );
		
		
		String text3 =TestConstants.RECOMMENDED;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text3 ) );
		//String text10 ="Moods of Norway";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text3 ) );
		//String text11 ="Divas of Music";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text3) );
		String text4 ="New Music";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text3 ) );
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
		solo.sleep( 500 );
		assertTrue( solo.waitForText( text4 ) );
		
		
		String text13 = TestConstants.SAMPLE_TITLE_1;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 500 );
		assertTrue( solo.waitForText( text13 ) );
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
		solo.sleep( 2000 );
		
		/*Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), PlayerActivity.class);
		intent.putExtra( "assetId",1L );
		setActivityIntent(intent);
		solo = new Solo(getInstrumentation(), getActivity());
		solo.sleep( 50000 );*/
	}
	
	
	
	
}
