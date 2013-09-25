package com.comcast.xidio.testCases.authentication;

import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.comcast.xidio.core.common.GetSolo;
import com.comcast.xidio.core.constant.TestConstants;
import com.comcast.xidio.model.GetLoginResponse;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;

public class XidioAuthenticationPositiveTesting extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;
	private JSONObject response;


	public XidioAuthenticationPositiveTesting()
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

	public void testXidioAuthenticationPositiveTesting()
	{

		solo.waitForActivity(TestConstants.FIRST_RUN);
		try {
			response=GetLoginResponse.getInstance().getLoginResponse(TestConstants.USERNAME, TestConstants.PASSWORD);
			

			solo.sleep(TestConstants.SLEEP_TIME_1000);
			if(response==null)
			{
				assertTrue(false);
			}
			else{
				assertTrue(response.has("response"));
				assertTrue(!response.isNull("response"));
				assertTrue(response.getJSONObject("response").has("code"));
				assertTrue(!response.getJSONObject("response").isNull("code"));
				assertTrue(response.getJSONObject("response").getString("code").contentEquals("AUTHENTICATION_OK"));
			}

		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(),"Failed to complete the test XidioAuthenticationPositiveTesting ",e);
		}

	}
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}


}
