package com.comcast.xidio.testCases.video;

import java.util.concurrent.ExecutionException;

import android.app.Instrumentation;
import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.playerplatform.primetime.android.enums.PlayerStatus;
import com.comcast.playerplatform.primetime.android.events.AbstractPlayerPlatformVideoEventListener;
import com.comcast.playerplatform.primetime.android.player.PlayerPlatformAPI;
import com.comcast.xidio.core.common.GetCatagoryLists;
import com.comcast.xidio.core.common.GetSolo;
import com.comcast.xidio.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;
import com.xfinity.xidio.R;
import com.xfinity.xidio.core.XidioApplication;
import com.xfinity.xidio.views.VideoPlayerView;

public class XideoVideoFastForward extends ActivityInstrumentationTestCase2<FirstRun> {
	
	
	private Solo solo;
	private boolean flag = false;
	private long before_videoTime = 0;
	private long after_videoTime = 0;
	private PlayerPlatformAPI platformApi;
	private int keyToPress=-1;
	private Boolean adCompleted=false;
	public XideoVideoFastForward() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception {
		GetSolo.getInstance().setUpSolo(getInstrumentation(), getActivity());
		solo = GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(XidioApplication.getLastLoggedInUser(),XidioApplication.getLastSessionId());
		super.setUp();
	}

	public void testXideoVideoForwardSeek() {
		solo.waitForActivity(TestConstants.FIRST_RUN);

		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(1000);
		solo.sleep(2000);

		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(2000);

		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
		solo.waitForActivity(TestConstants.DETAILS_ACTIVITY);
		solo.sleep(1000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(1000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
		solo.sleep(1000);
		assertTrue(solo.waitForActivity(TestConstants.VIDEOPLAYER_ACTIVITY));
		solo.sleep(1000);
		
		VideoPlayerView vView = (VideoPlayerView) solo.getCurrentActivity().findViewById(R.id.video_player_view);
		platformApi = vView.getPlatformAPI();
		platformApi.addEventListener(platformEventListener);

		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(1000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
		
		while(!adCompleted)
		{}
		
		solo.sleep(5000);
		before_videoTime=platformApi.getCurrentPosition();
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		
		solo.sleep(2000);
		after_videoTime=platformApi.getCurrentPosition();
		
		if(after_videoTime-before_videoTime>29000)
			assertTrue(true);
		else
			assertTrue(false);
		
		solo.sleep(5000);
	}
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
		super.tearDown();
	}
	
	private final AbstractPlayerPlatformVideoEventListener platformEventListener = new AbstractPlayerPlatformVideoEventListener() {

		@Override
		public void adComplete(long id) {
			adCompleted=true;
			super.adComplete(id);
		}

		

	};


}
