package com.comcast.ui.libv1.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.comcast.ui.libv1.R;
import com.comcast.ui.libv1.widget.BlobSurfaceView;
import com.comcast.ui.libv1.widget.AnimatedFocusHopperView;
import com.comcast.ui.libv1.widget.IFocusHopperListener;
import com.comcast.ui.libv1.widget.MenuLayout;
import com.comcast.ui.libv1.widget.VideoStripLayout;
import com.comcast.ui.libv1.widget.MenuLayout.MenuItem;
import com.comcast.ui.libv1.widget.model.ContentModel;
import com.comcast.ui.libv1.widget.model.ContentModelCollection;
import com.comcast.ui.libv1.widget.model.VideoInfo;
import com.comcast.ui.libv1.widget.model.ContentModel.ContentType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;

public class BlobDemoActivity extends Activity implements IFocusHopperListener {

	String[] menuLabels = { "SEARCH", "HOME", "SUBSCRIPTIONS", "SETTINGS", "FAVORITES", "POPULAR", "SPOTLIGHT", "OTHERS" };
	MenuLayout menu1, menu2;
	BlobSurfaceView blob;
	ContentModelCollection model;
	AnimatedFocusHopperView blobvw1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blob_layout);
//		blob = (BlobSurfaceView) findViewById(R.id.blob1);
		blobvw1 = (AnimatedFocusHopperView) findViewById(R.id.blobvw1);
	}

	@Override
	protected void onStart() {
		super.onStart();
		List<MenuItem> items = new ArrayList<MenuItem>();
		for (int i = 0; i < menuLabels.length; i++) {
			MenuItem item = new MenuItem();
			item.itemLabel = menuLabels[i];
			item.iconDrawable = getResources().getDrawable(R.drawable.ic_launcher);
			items.add(item);
		}

		menu1 = (MenuLayout) findViewById(R.id.menu1);
		menu1.addMenuItems(items);

		menu1.setBlobListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setupModelData();
		DelayedKeyPressThread thread = new DelayedKeyPressThread();
		thread.start();
		menu1.requestFocus();
	}


	private void setupModelData(){
		model = new ContentModelCollection();
		model.type = ContentType.IconView;

		com.google.gson.JsonParser parser = new JsonParser();

		InputStream is = null;
		try {
			is = getAssets().open(model.type == ContentType.IconView?"content_iconview.json":"content_titleview.json");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			JsonElement elem = parser.parse(br);
			br.close();
			elem = elem.getAsJsonObject().get("entries");
			fillModel(model, elem);
		}catch (JsonSyntaxException jps){
			jps.printStackTrace();
		}catch (JsonIOException jpi){
			jpi.printStackTrace();
		}catch (JsonParseException jpe){
			jpe.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		VideoStripLayout vsl = (VideoStripLayout) findViewById(R.id.vs1);
		vsl.setContentModel(model.get(0));
		vsl.setBlobListener(this);

		VideoStripLayout vsl2 = (VideoStripLayout) findViewById(R.id.vs2);
		vsl2.setContentModel(model.get(1));
		vsl2.setBlobListener(this);

		VideoStripLayout vsl3 = (VideoStripLayout) findViewById(R.id.vs3);
		vsl3.setContentModel(model.get(2));
		vsl3.setBlobListener(this);

	}

	private void fillModel(ContentModelCollection model, JsonElement obj){
		if(obj.isJsonArray()){
			JsonArray array = obj.getAsJsonArray();
			for(JsonElement elem:array){
				JsonObject o = elem.getAsJsonObject();
				ContentModel cm = new ContentModel(o.get("title").getAsString(), ContentType.valueOf(o.get("type").getAsString()));
				cm.iconFile = o.get("icon").getAsString();

				ArrayList<VideoInfo> viList = new ArrayList<VideoInfo>();

				JsonArray videos = o.get("videos").getAsJsonArray();
				for(JsonElement velem:videos){
					JsonObject vObj = velem.getAsJsonObject();
					VideoInfo vi = new VideoInfo();
					vi.title = vObj.get("title").getAsString();
					vi.description = vObj.get("desc").getAsString();
					vi.duration = vObj.get("dur").getAsString();
					vi.rating = vObj.get("rat").getAsString();
					viList.add(vi);
				}
				cm.videoList = viList;
				model.add(cm);
			}
		}
	}

	ArrayList<Point> mPoints = new ArrayList<Point>();
	boolean reInit = true;

	@Override
	public void onFocusChanged(boolean hasFocus, final Point[] points, Drawable d, ArrayList<Animator> set) {
		animateBlob(points, d, set);
	}

	@Override
	public void onChildFocusChanged(final Point[] points, Drawable d, ArrayList<Animator> set) {
		animateBlob(points, d, set);
	}

	private void animateBlob(Point[] points, Drawable d, ArrayList<Animator> set){
//		blob.setFocussedBitmapOverlay(d);
//		blob.animateBlob(points);
//		blobvw1.setFocussedBitmapOverlay(d);
		blobvw1.animateBlob(points);
	}

	class DelayedKeyPressThread extends Thread{
		int delay = 2000;
		Random rnd = new Random();
		public void run(){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			while(true){
				int keycode = 0;
				int r = rnd.nextInt(100) % 5;

				switch(r){
					case 1:
						keycode = KeyEvent.KEYCODE_DPAD_RIGHT;
						break;
					case 2:
						keycode = KeyEvent.KEYCODE_DPAD_UP;
						break;
					case 3:
						keycode = KeyEvent.KEYCODE_DPAD_DOWN;
						break;
					default:
						keycode = KeyEvent.KEYCODE_DPAD_LEFT;
						break;
				}

				Log.d(this.getClass().getName(), "key:"+r);

				final int keyCode = keycode;
				new Thread(new Runnable() {
			        @Override
			        public void run() {
			            Instrumentation inst = new Instrumentation();
		                inst.sendKeyDownUpSync(keyCode);
			        }
			    }).start();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
