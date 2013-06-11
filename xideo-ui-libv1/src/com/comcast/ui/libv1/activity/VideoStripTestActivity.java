package com.comcast.ui.libv1.activity;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.comcast.ui.libv1.R;
import com.comcast.ui.libv1.widget.VideoStripLayout;
import com.comcast.ui.libv1.widget.model.*;
import com.comcast.ui.libv1.widget.model.ContentModel.ContentType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.app.Activity;
import android.app.Instrumentation;

public class VideoStripTestActivity extends Activity {

	ContentModelCollection model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_strip_test);
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		VideoStripLayout vg = (VideoStripLayout) findViewById(R.id.vs1);
		vg.requestFocus();
		LoadModelDataAsyncTask task = new LoadModelDataAsyncTask();
		task.execute();

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
		
//		ContentView cv = (ContentView) findViewById(R.id.contentview1);
//		cv.setContentModel(model);
		
		VideoStripLayout vsl = (VideoStripLayout) findViewById(R.id.vs1);
		vsl.setContentModel(model.get(0));

		VideoStripLayout vsl2 = (VideoStripLayout) findViewById(R.id.vs2);
		vsl2.setContentModel(model.get(1));

		VideoStripLayout vsl3 = (VideoStripLayout) findViewById(R.id.vs3);
		vsl3.setContentModel(model.get(2));

		vsl.requestFocus();
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
	
	class LoadModelDataAsyncTask extends AsyncTask<Void,Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try{
				Thread.sleep(200);
			}catch(Exception ex){
				
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			setupModelData();
		}
	}
}
