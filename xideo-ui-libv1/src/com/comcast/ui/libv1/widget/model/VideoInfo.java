package com.comcast.ui.libv1.widget.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoInfo implements Parcelable{
	public enum WatchStatus{
		unwatched,
		watched,
		partial
	}

	public String title;
	public String description;
	public String duration;
	public String rating;
	public WatchStatus watchstatus = WatchStatus.unwatched;

	public VideoInfo(){}

	public VideoInfo(Parcel p){
		this.title = p.readString();
		this.description = p.readString();
		this.duration = p.readString();
		this.rating = p.readString();
		this.watchstatus = getWatchStatus(p.readInt());
	}

	public static WatchStatus getWatchStatus(int i){
		WatchStatus status = WatchStatus.unwatched;
		switch(i){
			case 1:
				status = WatchStatus.watched;
				break;

			case 2:
				status = WatchStatus.partial;
				break;
		}
		return status;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(description);
		dest.writeString(duration);
		dest.writeString(rating);
		dest.writeInt(watchstatus.ordinal());
	}
	
    public static final Parcelable.Creator<VideoInfo> CREATOR =
        new Parcelable.Creator<VideoInfo>() {
          public VideoInfo createFromParcel(Parcel in) {
            return new VideoInfo(in);
          }
          public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
          }
    };

}
