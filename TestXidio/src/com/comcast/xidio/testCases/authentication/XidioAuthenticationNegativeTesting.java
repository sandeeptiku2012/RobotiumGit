package com.comcast.xidio.testCases.authentication;

import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.comcast.xidio.core.common.GetSolo;
import com.comcast.xidio.core.constant.TestConstants;
import com.comcast.xidio.model.GetLoginResponse;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;

public class XidioAuthenticationNegativeTesting extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;
	private JSONObject response;


	public XidioAuthenticationNegativeTesting() 
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

	public void testXidioAuthenticationNegativeTesting()
	{
		solo.waitForActivity(TestConstants.FIRST_RUN);
		solo.sleep(1000);
	
		try {

			response=GetLoginResponse.getInstance().getLoginResponse("asas", TestConstants.PASSWORD);
			
			solo.sleep(TestConstants.SLEEP_TIME_2000);
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
			Log.e(this.getClass().getCanonicalName(),"Failed to complete the testXidioAuthenticationNegativeTesting ",e);
		}

	}
	
	protected void tearDown() throws Exception
	{

		solo.finishOpenedActivities();
		super.tearDown();
	}

}
