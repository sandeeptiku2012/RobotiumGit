package com.comcast.xideo;

import com.comcast.xideo.ParentalGuidelines.TelevisionRating;
import com.vimond.entity.ImagePackReference;

public interface IShow {
	long getId();

	String getName();

	String getDescription();

	String getPublisher();

	TelevisionRating getTelevisionRating();

	String getTVRatingText();

	ImagePackReference getShowThumbnailBigImagePack();

	ImagePackReference getShowThumbnailMiniImagePack();

}