package com.comcast.xideo.testCases.details.popular;

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
import com.xfinity.xidio.FirstRun;
import com.xfinity.xidio.MainActivity;
import com.xfinity.xidio.core.XidioApplication;

public class XideoDetailsActivityPopularEpisodeActivityChange extends ActivityInstrumentationTestCase2<FirstRun> 
{
	private Solo solo;

	public XideoDetailsActivityPopularEpisodeActivityChange() 
	{
		super(FirstRun.class);

	}

	@Override
	protected void setUp() throws Exception
	{
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(XidioApplication.getLastLoggedInUser(), XidioApplication.getLastSessionId());
		super.setUp();
	}
	
	public void testXideoDetailsActivityPopularEpisodeActivityChange()
	{	
		
		solo.waitForActivity(TestConstants.FIRST_RUN);
		solo.sleep(1000);
	 	solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(1000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(500);
		
		try {

			JSONArray popularArray =GetCatagoryLists.getInstance().getPopularList();
			
			for(int j=0;j<popularArray.length();j++)
			{
				JSONObject currElement=popularArray.getJSONObject(j);
				if(currElement.has("category"))
				{	if(currElement.getJSONObject("category").has("level"))
							{
							if(currElement.getJSONObject("category").getString("level").trim().equalsIgnoreCase("SUB_SHOW"))
								{
									solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
									solo.sleep(1000);
									continue;
								}
							else if(currElement.getJSONObject("category").getString("level").trim().equalsIgnoreCase("SHOW"))
							{
								
								solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
								String ChannelContentKey = currElement.getString("contentKey");
								JSONArray ShowContent = GetShowContent.getInstance().getShowContent(ChannelContentKey);
								solo.sleep(500);

								if (ShowContent == null) 
								{
									solo.sendKey(KeyEvent.KEYCODE_BACK);
									solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
									assertTrue(true);

								} else {
									solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
									assertTrue(solo.searchText(currElement.getString("title")));
									solo.sleep(1000);
									solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
									for (int p = 0; p < ShowContent.length(); p++) {
										solo.sleep(2000);
										JSONObject ShowsList = ShowContent.getJSONObject(p);
										String showId = ShowsList.getString("@id");
										JSONArray EpisodeListArray = GetEpisodesList.getInstance().getEpisodeList(showId);
										if (p == 0)
											solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
										for (int k = 0; k < EpisodeListArray.length(); k++) {
											solo.sleep(200);
											solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
											assertTrue(solo.waitForActivity(TestConstants.VIDEOPLAYER_ACTIVITY));
											solo.sleep(1000);
											solo.sendKey(KeyEvent.KEYCODE_BACK);
											solo.sleep(200);
											solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
										}
										solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
										solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
									}
								}
								
								break;
							}
						}
				}			
				else
				{
					continue;
				}
				
			}		
		} catch (Exception e) 
		{
			Log.e(this.getClass().getCanonicalName(), "Failed to complete the tset XideoDetailsActivityFeaturedEpisodeActivityChange " , e);
		}
		

	}
	protected void tearDown() throws Exception
	{

		solo.finishOpenedActivities();
		super.tearDown();
	}

}
