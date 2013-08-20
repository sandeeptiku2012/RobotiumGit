package com.comcast.xideo.testCases.search.Activity;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.comcast.xideo.core.common.GetCatagoryLists;
import com.comcast.xideo.core.common.GetSolo;
import com.comcast.xideo.core.constant.TestConstants;
import com.jayway.android.robotium.solo.Solo;
import com.xfinity.xidio.MainActivity;
import com.xfinity.xidio.views.MultiStateEditTextView;

public class XideoSearchActivityHint extends ActivityInstrumentationTestCase2<MainActivity>
{
	private Solo solo;

	public XideoSearchActivityHint() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {

		GetSolo.getInstance().setUpSolo(getInstrumentation(),getActivity());
		solo=GetSolo.getInstance().getSoloObject();
		GetCatagoryLists.getInstance().storeBasicLists(getActivity().getSessionId(), getActivity().getSessionId());
		super.setUp();
	}

	public void testSearchHint() 
	{
		solo.waitForActivity(TestConstants.MAIN_ACTIVITY);
		solo.sleep(2000);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_UP);
		solo.sleep(500);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_LEFT);
		solo.sleep(2000);
		MultiStateEditTextView searchText = (MultiStateEditTextView) solo.getCurrentActivity().findViewById(com.xfinity.xidio.R.id.header_search_text);
		assertTrue(searchText.getHint().toString().equalsIgnoreCase(TestConstants.SEARCH));
		solo.sleep(2000);
	}

}
