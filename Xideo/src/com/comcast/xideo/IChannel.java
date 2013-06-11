package com.comcast.xideo;

import com.comcast.xideo.ParentalGuidelines.TelevisionRating;
import com.vimond.entity.ChannelReference;
import com.vimond.entity.ImagePackReference;

public interface IChannel extends ChannelReference {
	String getTitle();

	Long getLikes();

	String getChannelLogoUrl();

	String getDescription();

	String getPublisher();

	TelevisionRating getTelevisionRating();

	ImagePackReference getImagePack();

	boolean isEditorsPick();

	long getParentCategoryId();

	String getCategoryName();

	String getCategoryPath();
}