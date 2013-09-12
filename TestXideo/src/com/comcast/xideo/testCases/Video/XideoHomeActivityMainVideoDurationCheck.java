package com.comcast.xideo.testCases.Video;

import org.json.JSONArray;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.Toast;

import com.comcast.playerplatform.primetime.android.ads.VideoAd;
import com.comcast.playerplatform.primetime.android.events.AbstractPlayerPlatformVideoEventListener;
import com.comcast.playerplatform.primetime.android.player.PlayerPlatformAPI;
import com.comcast.xideo.core.common.GetCatagoryLists;
import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;
import com.xfinity.xidio.R;
import com.xfinity.xidio.core.XidioApplication;
import com.xfinity.xidio.views.VideoPlayerView;

public class XideoHomeActivityMainVideoDurationCheck extends ActivityInstrumentationTestCase2<FirstRun> {

	private Solo solo;
	private boolean endVideo = false;
	private long video_Duration = 0;
	private long video_Start_Time = 0;
	private long video_End_Time = 0;

	public XideoHomeActivityMainVideoDurationCheck() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception {
		GetSolo.getInstance().setUpSolo(getInstrumentation(), getActivity());
		solo = GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(
				XidioApplication.getLastLoggedInUser(),
				XidioApplication.getLastSessionId());
		super.setUp();
	}

	public void testMainVideoDuration() {
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
		PlayerPlatformAPI platformApi = vView.getPlatformAPI();
		platformApi.addEventListener(platformEventListener);
		video_Duration=platformApi.getDuration();
		Toast.makeText(solo.getCurrentActivity(), "Video Duration : "+video_Duration, Toast.LENGTH_SHORT).show();
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
		
		while (!endVideo) {}
		

		solo.sleep(2000);
	}

	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
		super.tearDown();
	}

	private final AbstractPlayerPlatformVideoEventListener platformEventListener = new AbstractPlayerPlatformVideoEventListener() {

		@Override
		public void mediaEnded() {
			video_End_Time=System.currentTimeMillis();
			if((video_End_Time-video_Start_Time)<=video_Duration+5000)
				assertTrue(true);
			else 
				assertTrue(false);
							
			endVideo = true;
			super.mediaEnded();
		}

		@Override
		public void adComplete(long id) {
			video_Start_Time = System.currentTimeMillis();
			/*
			 * ad_End_Time=System.currentTimeMillis();
			 * 
			 * if ((ad_End_Time-ad_Start_Time)<=(ad_Duration+3000))
			 * assertTrue(true); else assertTrue(false); endVideo = true;
			 */
			super.adComplete(id);
		}

		@Override
		public void adStart(VideoAd ad) {
			
			/*
			 * ad_Duration = ad.getDuration();
			 * ad_Start_Time=System.currentTimeMillis();
			 */
			super.adStart(ad);
		}

	};

}
