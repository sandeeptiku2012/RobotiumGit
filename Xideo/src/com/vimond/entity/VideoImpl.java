package com.vimond.entity;

import java.util.Date;

public class VideoImpl extends Video implements VideoReference {
	private long id;
	private String title;
	private String publisher;
	private long likes;
	private String description;
	private ImagePackReference imagePack;
	private Long duration;
	private Date date;
	private ChannelReference channel;
	private String rating;

	public VideoImpl( long id ) {
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

	public void setTitle( String title ) {
		this.title = title;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	@Override
	public Long getLikes() {
		return likes;
	}

	public void setLikes( long likes ) {
		this.likes = likes;
	}

	@Override
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher( String publisher ) {
		this.publisher = publisher;
	}

	@Override
	public Date getDate() {
		return date;
	}

	public void setDate( Date date ) {
		this.date = date;
	}

	@Override
	public Long getDuration() {
		return duration;
	}

	public void setDuration( Long duration ) {
		this.duration = duration;
	}

	@Override
	public ImagePackReference getImagePack() {
		return imagePack;
	}

	public void setImagePack( ImagePackReference imagePack ) {
		this.imagePack = imagePack;
	}

	@Override
	public ChannelReference getChannel() {
		return channel;
	}

	public void setChannel( ChannelReference channel ) {
		this.channel = channel;
	}

	@Override
	public String getRating() {
		return rating;
	}

	public void setRating( String rating ) {
		this.rating = rating;
	}
}
