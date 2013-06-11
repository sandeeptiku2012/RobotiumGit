package com.vimond.entity;

import com.comcast.xideo.IShow;
import com.comcast.xideo.ParentalGuidelines.TelevisionRating;

public class ShowImpl implements IShow {
	public static final String NAME = "title";
	public static final String PUBLISHER_ID = "publisher-id";
	public static final String PUBLISHER = "publisher";
	public static final String THUMBNAIL_BIG = "image-pack-big";
	public static final String THUMBNAIL_MINI = "image-pack-mini";
	public static final String SHORT_DESC = "description-short";
	public static final String PARENTAL_GUIDANCE = "parental-guidance";

	private long id;
	private String name;
	private String description;
	private String publisher;
	private String tvRating;
	private TelevisionRating rating;
	private ImagePackReference showThumbnailBigImagePack;
	private ImagePackReference showThumbnailMiniImagePack;
	private long publisherid;

	public long getPublisherid() {
		return publisherid;
	}

	public void setPublisherid( long publisherid ) {
		this.publisherid = publisherid;
	}

	public ShowImpl( long id ) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	@Override
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher( String publisher ) {
		this.publisher = publisher;
	}

	public TelevisionRating getTelevisionRating() {
		return rating;
	}

	public void setTelevisionRating( TelevisionRating rating ) {
		this.rating = rating;
	}

	public String getTVRatingText() {
		return tvRating;
	}

	public void setTVRatingText(String rating) {
		this.tvRating = rating;
	}

	public final ImagePackReference getShowThumbnailBigImagePack() {
		return showThumbnailBigImagePack;
	}

	public final ImagePackReference getShowThumbnailMiniImagePack() {
		return showThumbnailMiniImagePack;
	}

	public void setBigThumbnailImagePack(ImagePackReference imagePackReference) {
		showThumbnailBigImagePack = imagePackReference;
	}

	public void setMiniThumbnailImagePack(ImagePackReference imagePackReference) {
		showThumbnailMiniImagePack = imagePackReference;
	}

	public void setName(String name) {
		this.name = name;
	}
}