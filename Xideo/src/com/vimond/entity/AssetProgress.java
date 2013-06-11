package com.vimond.entity;

public interface AssetProgress {
	public abstract void setProgress( VideoReference video, long position );

	public abstract Long getProgress( VideoReference video );

	public abstract void removeProgress( VideoReference video );

	public abstract boolean hasSeen( Video video );
}
