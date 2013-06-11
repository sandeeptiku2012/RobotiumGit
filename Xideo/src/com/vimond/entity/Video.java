package com.vimond.entity;

import java.util.Date;

public abstract class Video implements VideoReference {
	public abstract String getTitle();

	public abstract String getDescription();

	public abstract ImagePackReference getImagePack();

	public abstract Long getDuration();

	public abstract Date getDate();

	public abstract String getPublisher();

	public abstract Long getLikes();

	public abstract ChannelReference getChannel();

	public abstract String getRating();
}