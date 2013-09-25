package com.comcast.xidio.testCases.home.featured;

import org.json.JSONArray;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;

import com.comcast.xidio.core.common.GetCatagoryLists;
import com.comcast.xidio.core.common.GetSolo;
import com.comcast.xidio.core.constant.TestConstants;
import com.comcast.xidio.model.GetEpisodesList;
import com.comcast.xidio.model.GetShowContent;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;
import com.xfinity.xidio.core.XidioApplication;

public class XidioDetailsActivityFeaturedEpisodeList extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;

	public XidioDetailsActivityFeaturedEpisodeList() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception {
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(XidioApplication.getLastLoggedInUser(), XidioApplication.getLastSessionId());
		super.setUp();
	}

	public void testXidioDetailsActivityFeaturedEpisodeList() 
	{
		solo.waitForActivity(TestConstants.FIRST_RUN);
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(TestConstants.SLEEP_TIME_5000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(TestConstants.SLEEP_TIME_2000);
		try{
		JSONArray featuredJsonArray = GetCatagoryLists.getInstance().getFeaturedList();
		if(featuredJsonArray!=null && featuredJsonArray.length()>0)
		{
			
			for(int j=0;j<featuredJsonArray.length();j++)
			{
				JSONObject currElement=featuredJsonArray.getJSONObject(j);
				if(currElement.has("category"))
				{	if(currElement.getJSONObject("category").has("level"))
						{
							if(currElement.getJSONObject("category").getString("level").trim().equalsIgnoreCase("SUB_SHOW"))
								{   solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
									solo.sleep(1000);
									assertTrue(solo.waitForActivity(TestConstants.DETAILS_ACTIVITY));
									
									JSONArray spisodeListArray = GetEpisodesList.getInstance().getEpisodeList(currElement.getString("contentKey"));
									if(spisodeListArray!=null)
									{
										solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
				
									for (int k = 0; k < spisodeListArray.length(); k++) 
									{
										solo.sleep(TestConstants.SLEEP_TIME_500);
										JSONObject currentEpisode = spisodeListArray.getJSONObject(k);
										String episodeTitle = currentEpisode.getString("title");
										assertTrue(solo.searchText(episodeTitle));
										solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
										}
								}
									solo.sleep(TestConstants.SLEEP_TIME_2000);
									solo.sendKey(KeyEvent.KEYCODE_BACK);
									solo.sleep(TestConstants.SLEEP_TIME_500);
									assertTrue(solo.waitForActivity(TestConstants.MAIN_ACTIVITY));
									solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
									continue;
								}
							else if(currElement.getJSONObject("category").getString("level").trim().equalsIgnoreCase("SHOW")){
								solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
								solo.sleep(TestConstants.SLEEP_TIME_2000);
								assertTrue(solo.waitForActivity(TestConstants.DETAILS_ACTIVITY));

								String channelContentKey = currElement.getString("contentKey");
								JSONArray ShowContent = GetShowContent.getInstance().getShowContent(channelContentKey);
								solo.sleep(TestConstants.SLEEP_TIME_2000);

								if (ShowContent == null) {
									solo.sendKey(KeyEvent.KEYCODE_BACK);
									solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
									solo.sleep(TestConstants.SLEEP_TIME_500);
									solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
									continue;

								} else {
									for (int p = 0; p < ShowContent.length(); p++)
									{
										solo.sleep(TestConstants.SLEEP_TIME_2000);

										JSONObject currShow = ShowContent.getJSONObject(p);
										JSONArray episodeListArray = GetEpisodesList.getInstance().getEpisodeList(currShow.getString("@id"));
										String showTitle = currShow.getString("title");
										assertTrue(solo.searchText(showTitle));
										
										if (p == 0) {
											solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
											
										}
										solo.sleep(TestConstants.SLEEP_TIME_1000);
										solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);									
										if(episodeListArray!=null){
										

										for (int k = 0; k < episodeListArray.length(); k++)
										{
											solo.sleep(TestConstants.SLEEP_TIME_500);
											JSONObject currentEpisode = episodeListArray.getJSONObject(k);
											String episodeTitle = currentEpisode.getString("title");
											assertTrue(solo.searchText(episodeTitle));
											if(k+1!=episodeListArray.length())
												solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
											else
												solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);

										}
									}
										else
										{
											solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
											
										}
									}

								}

								solo.sleep(TestConstants.SLEEP_TIME_2000);
								solo.sendKey(KeyEvent.KEYCODE_BACK);

								assertTrue(solo.waitForActivity(TestConstants.MAIN_ACTIVITY));
								solo.sleep(TestConstants.SLEEP_TIME_500);
								solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
								continue;

							}
							}
				
				}			
				else if(currElement.has("asset"))
				{	
					solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
					continue;
				}
				solo.sleep(TestConstants.SLEEP_TIME_500);
			}
			
			
			
			
			
			
		}	

	}
		
	catch(Exception e)
	{
		Log.d("Exception from test XidioDetailsActivityFeaturedEpisodeList = ", e.getLocalizedMessage());
	}
	
	}
	
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}
}


