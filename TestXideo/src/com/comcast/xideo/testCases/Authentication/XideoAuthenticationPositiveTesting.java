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

	String response = null;
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

			login();

			solo.sleep(5000);

			if (response == null) {
				assertTrue(false);

			} else {
				JSONObject loginObject = new JSONObject(response);
				assertTrue(loginObject.has("response"));
				assertTrue(!loginObject.isNull("response"));
				assertTrue(loginObject.getJSONObject("response").has("code"));
				assertTrue(!loginObject.getJSONObject("response").isNull("code"));
				assertTrue(loginObject.getJSONObject("response").getString("code").contentEquals("AUTHENTICATION_OK"));
			}

		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(),"Failed to complete the tset XideoDetailsActivityFeaturedEpisodeActivityChange ",e);
		}

	}

	private void login() 
	{
		StringBuilder login = new StringBuilder();
		login.append("username=").append(TestConstants.USERNAME).append("&password=").append(TestConstants.PASSWORD);
		loginRequest = new StringPostRequest(Method.POST,login.toString(),URLFactory.getLoginUrl(), new Listener<String>()
				{

					@Override
					public void onResponse(String arg0) {
						response = arg0;

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
					}
				});
		addRequest(loginRequest);

	}

	private void addRequest(Request request) {
		mRequestQueue.add(request);
	}

}
