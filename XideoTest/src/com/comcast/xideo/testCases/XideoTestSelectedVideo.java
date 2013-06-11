package com.comcast.xideo.testCases;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.comcast.videoplayer.ui.PlayerActivity;
import com.comcast.xideo.activity.XideoHomeActivity;
import com.comcast.xideo.events.ShowVideoEvent;
import com.jayway.android.robotium.solo.Solo;

public class XideoTestSelectedVideo extends	ActivityInstrumentationTestCase2<PlayerActivity> {
private Solo solo;
	
	public XideoTestSelectedVideo() {
		super( PlayerActivity.class );
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent( getInstrumentation().getTargetContext().getApplicationContext(), PlayerActivity.class );
		intent.putExtra( "assetId",1L );
		setActivityIntent( intent );
		solo = new Solo( getInstrumentation(), getActivity() );
	}


	// own implementation

	/*public void testSelectedVideoPlayedorNot() {

		Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), PlayerActivity.class);
		intent.putExtra("assetIds", new long[] { 2658324L, 2658324L });
		setActivityIntent(intent);
		solo = new Solo(getInstrumentation(), getActivity());

	}*/

	// own implementation

	/*public void testSubscriptionVideo() {

		Intent intent = new Intent(getInstrumentation().getTargetContext()
				.getApplicationContext(), PlayerActivity.class);
		// id's of subscription videos
		intent.putExtra("assetIds", new long[] { 2177667L, 2177667L });
		setActivityIntent(intent);
		solo = new Solo(getInstrumentation(), getActivity());

	}*/

}
