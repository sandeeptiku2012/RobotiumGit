package com.comcast.xideo.testCases;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.videoplayer.ui.PlayerActivity;
import com.comcast.xideo.activity.XideoHomeActivity;
import com.comcast.xideo.core.TestConstants;
import com.jayway.android.robotium.solo.Solo;

public class XideoHomeActivityTest3 extends ActivityInstrumentationTestCase2<XideoHomeActivity>{

	private Solo solo;
	public XideoHomeActivityTest3( ) 
	{
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
	public void test()
	{
		String text = TestConstants.FEATURE;
		solo.sleep( 2000 );
		assertTrue( solo.waitForText( text ) );
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 200 );
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep( 500 );
		String text2 =TestConstants.ANIMATION_AND_GAMES;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text2 ) );
		
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 500 );
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 500 );
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 500 );
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 500 );
		
		
		String text3 = TestConstants.ART;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 500 );
		assertTrue( solo.waitForText( text3 ) );
		solo.sleep( 200 );
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		
		String text4 = TestConstants.COMEDY;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text4) );
		//String text3 ="Trailer Mania";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text4 ) );
		//String text4 ="Vidar international film";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text4 ) );
		solo.sleep( 200 );
		
		String text5 = TestConstants.EDUCATION;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text5) );
		//String text3 ="Trailer Mania";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text5 ) );
		//String text4 ="Vidar international film";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text5 ) );
		solo.sleep( 200 );
		
		
		String text6 = TestConstants.FASHOIN_AND_STYLE;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text6 ) );
		//String text6 ="Divas of Music";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text6 ) );
		//String text7 ="Moods of Norway";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text6 ) );
		//String text8 ="Cinematics";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text6 ) );
		
		String text7 = TestConstants.FILM;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text7 ) );
		//String text6 ="Divas of Music";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text7 ) );
		//String text7 ="Moods of Norway";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text7 ) );
		//String text8 ="Cinematics";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text7 ) );
		
		String text8 = TestConstants.MUSIC;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text8 ) );
		//String text6 ="Divas of Music";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text8 ) );
		//String text7 ="Moods of Norway";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text8 ) );
		//String text8 ="Cinematics";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text8) );
		
		
		
		String text9 = TestConstants.NEWS_AND_POLITICS;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text9 ) );
		//String text6 ="Divas of Music";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text9 ) );
		//String text7 ="Moods of Norway";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text9 ) );
		//String text8 ="Cinematics";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text9 ) );
		
		
		String text10 = TestConstants.NON_PROFIT;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text10 ) );
		//String text6 ="Divas of Music";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text10 ) );
		//String text7 ="Moods of Norway";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text10 ) );
		//String text8 ="Cinematics";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text10 ) );
		
		String text11 = TestConstants.SPORTS;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text11 ) );
		//String text6 ="Divas of Music";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text11 ) );
		//String text7 ="Moods of Norway";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text11 ) );
		//String text8 ="Cinematics";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text11 ) );
		
		
		String text12 = TestConstants.TRAVEL_AND_EVENTS;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text12 ) );
		//String text6 ="Divas of Music";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText(text12 ) );
		//String text7 ="Moods of Norway";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text12 ) );
		//String text8 ="Cinematics";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep( 200 );
		assertTrue( solo.waitForText( text12 ) );
		
		String text13 ="Cyprus";
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
		solo.sleep( 500 );
		assertTrue( solo.waitForText( text13 ) );
		
		
		String text14 = TestConstants.SAMPLE_TITLE_1;
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep( 500 );
		assertTrue( solo.waitForText( text14 ) );
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
		solo.sleep( 2000 );
		
		Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), PlayerActivity.class);
		intent.putExtra( "assetId",1L );
		setActivityIntent(intent);
		solo = new Solo(getInstrumentation(), getActivity());
		solo.sleep( 50000 );
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
		solo.sleep(500 );
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
		solo.sleep(2000 );
		
		
	}
	
}


