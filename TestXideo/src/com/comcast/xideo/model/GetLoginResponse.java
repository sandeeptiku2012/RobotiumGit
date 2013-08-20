package com.comcast.xideo.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.toolbox.custom.StringPostRequest;
import com.xfinity.xidio.core.URLFactory;

public class GetLoginResponse 
{
	private static GetLoginResponse instance;

	public static synchronized GetLoginResponse getInstance() 
	{
		if (instance == null)
			instance = new GetLoginResponse();
		return instance;
	}

	String username=null,password=null;

	private StringPostRequest loginRequest;

	public JSONObject getLoginResponse(String username,String password) {
		
		try {
			this.username=username;
			this.password=password;
			String temp=new LoginAsyncTask().execute(URLFactory.getLoginUrl()).get();
			return new JSONObject(temp);
			} 
			catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), "Failed to GetLoginResponse ", e);
		}
		return null;

	}
		
	public class LoginAsyncTask extends AsyncTask<String , Void, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			
			  BufferedReader reader=null;
			 URL url;
			try {
				url = new URL(params[0]);
				 URLConnection conn = url.openConnection();
				 conn.setDoOutput(true);
				 OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				 wr.write( "username="+username+"&password="+password );
				  wr.flush(); 
				  reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		          StringBuilder sb = new StringBuilder();
		          String line = null;
		          
		          while((line = reader.readLine()) != null)
	              {
	                   sb.append(line + "\n");
	              }
	              
	              
	              String text = sb.toString();
	              JSONObject abc= new JSONObject(text);
	              String id= abc.getJSONObject("response").getString("userId").toString();
	              Log.d("Response", text);
	             return text; 
	              
			} catch (Exception exe)
			{
				Log.e(this.getClass().getCanonicalName(), "Failed to authorize  ", exe);
			}
			return null;
		}
		
	}
	
	
	/*private void login() {
		loginRequest = new StringPostRequest(Method.POST, "username=test_151&password=Demo1111",
				URLFactory.getLoginUrl(), new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						try {
							JSONObject loginObject = new JSONObject(arg0);
							if (loginObject.has("response") && !loginObject.isNull("response")
									&& loginObject.getJSONObject("response").has("userId")
									&& !loginObject.getJSONObject("response").isNull("userId")) {

								String userId = loginObject.getJSONObject("response").getString("userId");
								String sessionId = loginRequest.getSessionId();
								TestConstants.userId=userId;
								

							}
						} catch (JSONException e) {

							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
					}
				});

	}*/
	
	
	

}
