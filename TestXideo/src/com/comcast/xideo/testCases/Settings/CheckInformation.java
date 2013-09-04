package com.comcast.xideo.testCases.Settings;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.common.NetworkUtil;
import com.comcast.xideo.core.constant.TestConstants;
import com.comcast.xideo.model.GetLoginResponse;
import com.comcast.xideo.model.GetUserData;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.FirstRun;
import com.xfinity.xidio.MainActivity;

public class CheckInformation extends ActivityInstrumentationTestCase2<FirstRun>{

	private Solo solo;	
	
	public CheckInformation() {
		super(FirstRun.class);
	}

	@Override
	protected void setUp() throws Exception {

		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		super.setUp();
	}

	public void testCheckInformation() 
	{
		solo.waitForActivity(TestConstants.FIRST_RUN);
		
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(1000);
		
		
		solo.sleep(4000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_RIGHT);
		
		assertTrue(solo.searchText(TestConstants.SETTINGS));
		solo.sleep(5000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sleep(5000);
		String username=null;
		try {
			//JSONObject userInfo=new UserInformationAsynTask().execute(URLFactory.getUserDataUrl()).get();
			username=GetUserData.getInstance().getUserData().getJSONObject("user").getString("userName");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		assertTrue(solo.searchText("Information"));
		
		WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
		String wifi_mac_address=null;
		String ip_address=null;
		String eth_mac_address=null;
		if(wifiManager.isWifiEnabled()) {
		    WifiInfo info = wifiManager.getConnectionInfo();
		    wifi_mac_address=NetworkUtil.getMACAddress("wlan0");
		    ip_address=info.getIpAddress()+"";
		    if(!ip_address.contentEquals("0"))
		    		assertTrue(solo.searchText(wifi_mac_address));
		} else {
			 eth_mac_address = NetworkUtil.getMACAddress("eth0");
			assertTrue(solo.searchText(eth_mac_address));
		    
	  }

		ip_address = NetworkUtil.getIPAddress(true);
		assertTrue(solo.searchText(ip_address));
		assertTrue(solo.searchText(username));
		
		solo.sleep(2000);
		}
	
	protected void tearDown() throws Exception {

		solo.finishOpenedActivities();
		super.tearDown();
	}
	
}
