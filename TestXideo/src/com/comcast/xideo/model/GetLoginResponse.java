package com.comcast.xideo.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.custom.StringPostRequest;
import com.comcast.cim.httpcomponentsandroid.client.entity.UrlEncodedFormEntity;
import com.comcast.xideo.core.constant.TestConstants;
import com.xfinity.xidio.core.URLFactory;

public class GetLoginResponse {
	private static GetLoginResponse instance;

	public static GetLoginResponse getInstance() {
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
		//	String temp=new Login().execute(URLFactory.getLoginUrl()+"?username=test_151&password=Demo1111").get();
			String temp=new Login().execute(URLFactory.getLoginUrl()).get();
			return new JSONObject(temp);
			//String rsponse=new Login().execute(new String("http://api.stage2.xidio.com/api/authentication/user/login.json?username=test_151&password=Demo1111")).get();
		//	login();
			//return null;
		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), "Failed to GetLoginResponse ", e);
		}
		return null;

	}
	
	
	public class Login extends AsyncTask<String , Void, String>
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
	                     // Append server response in string
	                     sb.append(line + "\n");
	              }
	              
	              
	              String text = sb.toString();
	              JSONObject abc= new JSONObject(text);
	              String id= abc.getJSONObject("response").getString("userId").toString();
	              //GetSubscriptionList.getInstance().getSubscriptionList(id);
	              
	              
	              Log.d("Response", text);
	             return text; 
	              
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
		
	}
	
	
	private void login() {
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

	}
	
	
	

}
