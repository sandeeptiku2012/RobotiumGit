package com.vimond.entity;

import com.comcast.xideo.IChannel;
import com.comcast.xideo.ParentalGuidelines.TelevisionRating;

public class ChannelImpl implements IChannel {
	private long id;
	private String title;
	private Long likes;
	private String channelLogoUrl;
	private String description;
	private String publisher;
	private TelevisionRating rating;
	private ImagePackReference imagePack;
	private boolean isEditorsPick;
	private boolean trending;
	private long publisherid;
	private long parentCategoryId;
	private long categoryId;
	private String categoryName;
	private String categoryPath;

	public long getPublisherid() {
		return publisherid;
	}

	public void setPublisherid( long publisherid ) {
		this.publisherid = publisherid;
	}

	public boolean isTrending() {
		return trending;
	}

	public void setTrending( boolean trending ) {
		this.trending = trending;
	}

	public ChannelImpl( long id ) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Long getLikes() {
		return likes;
	}

	public void setLikes( Long likes ) {
		this.likes = likes;
	}

	@Override
	public String getChannelLogoUrl() {
		return channelLogoUrl;
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

	public long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setPublisher( String publisher ) {
		this.publisher = publisher;
	}

	@Override
	public TelevisionRating getTelevisionRating() {
		return rating;
	}

	public void setTelevisionRating( TelevisionRating rating ) {
		this.rating = rating;
	}

	@Override
	public boolean isEditorsPick() {
		return isEditorsPick;
	}

	public void setEditorsPick( boolean value ) {
		isEditorsPick = value;
	}

	@Override
	public final ImagePackReference getImagePack() {
		return imagePack;
	}

	public void setImagePack( ImagePackReference imagePackReference ) {
		imagePack = imagePackReference;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public void setParentCategoryId( long id ) {
		this.parentCategoryId = id;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryId( long id ) {
		categoryId = id;
	}

	public void setCategoryName( String name ) {
		categoryName = name;
	}

	public void setCategoryPath( String path ) {
		this.categoryPath = path;
	}

	public String getCategoryPath() {
		return categoryPath;
	}
}