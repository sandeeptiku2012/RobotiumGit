package com.comcast.xideo.testCases.search.Activity;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetCatagoryLists;
import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.MainActivity;
import com.xfinity.xidio.views.MultiStateEditTextView;

public class XideoSearchActivityText extends ActivityInstrumentationTestCase2<MainActivity>
{
	private Solo solo;

	public XideoSearchActivityText() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		
		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(getActivity().getSessionId(), getActivity().getSessionId());
		super.setUp();
	}

	public void testSearchText() 
	{
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_A);
		solo.sendKey(KeyEvent.KEYCODE_B);
		MultiStateEditTextView searchText = (MultiStateEditTextView) solo.getCurrentActivity().findViewById(com.xfinity.xidio.R.id.header_search_text);
		assertTrue(searchText.getText().toString().equalsIgnoreCase("ab"));
		solo.sleep(2000);

	}

}
