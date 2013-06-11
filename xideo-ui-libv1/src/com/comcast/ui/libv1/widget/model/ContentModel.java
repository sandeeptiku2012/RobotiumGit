package com.comcast.ui.libv1.widget.model;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class ContentModel implements Parcelable{

	public String title;
	public List<VideoInfo> videoList;
	public ContentType type = ContentType.TitleView;
	
	public String iconFile;
	
	private Drawable icon;
	
	public enum ContentType{
		TitleView,
		IconView
	}
	
	public ContentModel(String title, ContentType type){
		this.title = title;
		this.type = type;
	}
	
	public ContentModel(Parcel p){
		this.title = p.readString();
		this.videoList = p.readArrayList(VideoInfo.class.getClassLoader());
	}
	
	public void setIcon(Drawable icon){
		this.icon = icon;
	}
	
	public Drawable getIcon(){
		return this.icon;
	}
	
	@Override
    public void writeToParcel(Parcel out, int flags) {
      out.writeString(title);
      VideoInfo[] vi = new VideoInfo[videoList.size()];
      vi = videoList.toArray(vi);
      out.writeParcelableArray(vi, flags);
    }

    public static final Parcelable.Creator<ContentModel> CREATOR =
        new Parcelable.Creator<ContentModel>() {
          public ContentModel createFromParcel(Parcel in) {
            return new ContentModel(in);
          }
          public ContentModel[] newArray(int size) {
            return new ContentModel[size];
          }
    };

	@Override
	public int describeContents() {
		return 0;
	}
}
