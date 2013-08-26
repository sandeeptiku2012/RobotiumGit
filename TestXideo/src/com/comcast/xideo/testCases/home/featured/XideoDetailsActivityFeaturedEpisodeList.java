package com.comcast.xideo.testCases.home.featured;

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

public class XideoDetailsActivityFeaturedEpisodeList extends ActivityInstrumentationTestCase2<MainActivity>
{
	private Solo solo;

	public XideoDetailsActivityFeaturedEpisodeList() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(getActivity().getSessionId(), getActivity().getSessionId());
		super.setUp();
	}

	public void testDetailsActivityFeaturedEpisodeList() 
	{
	
		
///////////////////////////////////////////////////////////////////
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(2000);
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
									solo.sleep(2000);
									assertTrue(solo.waitForActivity(TestConstants.DETAILS_ACTIVITY));
									
									JSONArray EpisodeListArray = GetEpisodesList.getInstance().getEpisodeList(currElement.getString("contentKey"));
									if(EpisodeListArray!=null)
									{
										solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
				
									for (int k = 0; k < EpisodeListArray.length(); k++) 
									{
										solo.sleep(200);
										JSONObject currentEpisode = EpisodeListArray.getJSONObject(k);
										String episodeTitle = currentEpisode.getString("title");
										assertTrue(solo.searchText(episodeTitle));
										solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
										}
								}
									solo.sleep(1500);
									solo.sendKey(KeyEvent.KEYCODE_BACK);
									solo.sleep(500);
									assertTrue(solo.waitForActivity(TestConstants.MAIN_ACTIVITY));
									solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
									continue;
								}
							else if(currElement.getJSONObject("category").getString("level").trim().equalsIgnoreCase("SHOW")){
								solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
								solo.sleep(2000);
								assertTrue(solo.waitForActivity(TestConstants.DETAILS_ACTIVITY));

								String channelContentKey = currElement.getString("contentKey");
								JSONArray ShowContent = GetShowContent.getInstance().getShowContent(channelContentKey);
								solo.sleep(2000);

								if (ShowContent == null) {
									solo.sendKey(KeyEvent.KEYCODE_BACK);
									solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
									solo.sleep(500);
									solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
									continue;

								} else {
									// solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
									for (int p = 0; p < ShowContent.length(); p++) {
										solo.sleep(2000);

										JSONObject currShow = ShowContent
												.getJSONObject(p);
										JSONArray episodeListArray = GetEpisodesList.getInstance().getEpisodeList(currShow.getString("@id"));
										String showTitle = currShow.getString("title");
										assertTrue(solo.searchText(showTitle));
										if(episodeListArray!=null){
											
										

										if (p == 0) {
											solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);

										}
										
										solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);

										for (int k = 0; k < episodeListArray
												.length(); k++) {
											solo.sleep(200);
											JSONObject currentEpisode = episodeListArray
													.getJSONObject(k);
											String episodeTitle = currentEpisode
													.getString("title");
											assertTrue(solo
													.searchText(episodeTitle));
											solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);

										}
										solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
										solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
										}
										else
										{
											solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
											
										}
									}

								}

								solo.sleep(1500);
								solo.sendKey(KeyEvent.KEYCODE_BACK);

								assertTrue(solo
										.waitForActivity(TestConstants.MAIN_ACTIVITY));
								solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
								continue;

							}
							}
				
				}			
				else if(currElement.has("asset"))
				{	
					/*solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
					solo.sleep(500);
					assertTrue(solo.waitForActivity(TestConstants.VIDEOPLAYER_ACTIVITY));
					assertTrue(solo.searchText(currElement.getString("title").trim()));
					solo.sleep(500);
					solo.sendKey(KeyEvent.KEYCODE_BACK);
					solo.sleep(500);
					assertTrue(solo.waitForActivity(TestConstants.MAIN_ACTIVITY));*/
					solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
					continue;
				}
				solo.sleep(500);
			}
			
			
			
			
			
			
		}	

	}
		
	catch(Exception e)
	{
		Log.d("Exception from testDetailsActivityChange = ", e.getLocalizedMessage());
	}
		
		
		
		
		
		
		
		
	}

	}


