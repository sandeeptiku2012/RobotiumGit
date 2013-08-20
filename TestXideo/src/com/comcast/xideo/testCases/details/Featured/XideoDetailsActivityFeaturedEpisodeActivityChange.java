package com.comcast.xideo.testCases.details.Featured;

import org.json.JSONArray;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetCatagoryLists;
import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.comcast.xideo.model.GetEpisodesList;
import com.comcast.xideo.model.GetShowContent;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.MainActivity;

public class XideoDetailsActivityFeaturedEpisodeActivityChange  extends ActivityInstrumentationTestCase2<MainActivity> 
{
	private Solo solo;
	
	public XideoDetailsActivityFeaturedEpisodeActivityChange() {
		super(MainActivity.class);
		

	}

	@Override
	protected void setUp() throws Exception 
	{
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(getActivity().getSessionId(), getActivity().getSessionId());
		super.setUp();
	}
	
	 public void testFeaturedEpisodeActivityChange()
		 {
		
			solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
			
			solo.sleep(200);
			solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);

			try {
				JSONObject currentChannel = GetCatagoryLists.getInstance().getFeaturedList().getJSONObject(1);

				solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);

				String ChannelContentKey = currentChannel.getString("contentKey");
				JSONArray ShowContent = GetShowContent.getInstance().getShowContent(ChannelContentKey);
				solo.sleep(2000);

				if (ShowContent == null) {
					solo.sendKey(KeyEvent.KEYCODE_BACK);
					solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
					// solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);

				} else {
					for (int j = 0; j < ShowContent.length(); j++) {
						solo.sleep(2000);

						JSONObject ShowsList = ShowContent.getJSONObject(j);
						String showId = ShowsList.getString("@id");

						JSONArray EpisodeListArray = GetEpisodesList.getInstance().getEpisodeList(showId);
						if (j == 0)
							solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);

						for (int k = 0; k < EpisodeListArray.length(); k++) {
							solo.sleep(200);
							JSONObject currentEpisode = EpisodeListArray
									.getJSONObject(k);
						
							String EpisodeTitle = currentEpisode.getString(TestConstants.TITLE);
							//assertTrue(solo.searchText(EpisodeTitle, 1, true, true));
							solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
							assertTrue(solo.waitForActivity(TestConstants.VIDEOPLAYER_ACTIVITY));
							solo.sleep(500);
							solo.sendKey(KeyEvent.KEYCODE_BACK);
							solo.sleep(200);
							
							
							solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);

						}
						solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
						// solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);

					}

				}

			} catch (Exception e) 
			{
				Log.e(this.getClass().getCanonicalName(), "Failed to complete the tset XideoDetailsActivityFeaturedEpisodeActivityChange " , e);
			}
			
		}

		
	
	
}
