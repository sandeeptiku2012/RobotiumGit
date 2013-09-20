package com.comcast.xideo.testCases.Authentication;

import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.comcast.xideo.model.GetLoginResponse;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;

public class XideoAuthenticationNegetiveTesting extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;
	private JSONObject response;


	public XideoAuthenticationNegetiveTesting() 
	{
		super(FirstRun.class);

	}

	@Override
	protected void setUp() throws Exception
	{
		GetSolo.getInstance().setUpSolo(getInstrumentation(), getActivity());
		solo = GetSolo.getInstance().getSoloObject();
		super.setUp();
	}

	public void testXideoAuthenticationNegetiveTesting()
	{
		solo.waitForActivity(TestConstants.FIRST_RUN);
		solo.sleep(1000);
		try {

			response=GetLoginResponse.getInstance().getLoginResponse("asas", TestConstants.PASSWORD);
			
			solo.sleep(2000);
			if(response==null)
				assertTrue(false);
			else{
			
				assertTrue(response.has("response"));
				assertTrue(!response.isNull("response"));
				assertTrue(response.getJSONObject("response").has("code"));
				assertTrue(!response.getJSONObject("response").isNull("code"));
				assertTrue(response.getJSONObject("response").getString("code").contentEquals("AUTHENTICATION_FAILED"));
			}

		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(),"Failed to complete the tset XideoDetailsActivityFeaturedEpisodeActivityChange ",e);
		}

	}
	
	protected void tearDown() throws Exception
	{

		solo.finishOpenedActivities();
		super.tearDown();
	}

}
