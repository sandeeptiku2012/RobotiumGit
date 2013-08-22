package com.comcast.xideo.testCases.Authentication;

import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.custom.StringPostRequest;
import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.comcast.xideo.model.GetLoginResponse;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;
import com.xfinity.xidio.core.URLFactory;

public class XideoAuthenticationPositiveTesting extends ActivityInstrumentationTestCase2<FirstRun>
{
	private Solo solo;
	private StringPostRequest loginRequest;

	public XideoAuthenticationPositiveTesting() {
		super(FirstRun.class);

	}

	JSONObject response = null;
	private RequestQueue mRequestQueue;

	@Override
	protected void setUp() throws Exception {
		GetSolo.getInstance().setUpSolo(getInstrumentation(), getActivity());
		solo = GetSolo.getInstance().getSoloObject();
		super.setUp();
	}

	public void testXideoAuthenticationPositiveTesting() {

		solo.waitForActivity(TestConstants.FIRST_RUN);
		mRequestQueue = Volley.newRequestQueue(getActivity());
		try {
			response=GetLoginResponse.getInstance().getLoginResponse(TestConstants.USERNAME, TestConstants.PASSWORD);
			

			solo.sleep(5000);

			//	JSONObject response = new JSONObject(response);
				assertTrue(response.has("response"));
				assertTrue(!response.isNull("response"));
				assertTrue(response.getJSONObject("response").has("code"));
				assertTrue(!response.getJSONObject("response").isNull("code"));
				assertTrue(response.getJSONObject("response").getString("code").contentEquals("AUTHENTICATION_OK"));

		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(),"Failed to complete the tset XideoDetailsActivityFeaturedEpisodeActivityChange ",e);
		}

	}


}
