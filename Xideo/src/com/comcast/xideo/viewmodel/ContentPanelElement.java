package com.comcast.xideo.viewmodel;

import com.vimond.entity.CategoryReference;
import com.vimond.entity.ChannelReference;
import com.vimond.entity.ChannelReferenceImpl;
import com.vimond.entity.ImagePackReference;
import com.vimond.entity.VideoImpl;
import com.vimond.entity.VideoReference;

public class ContentPanelElement {
	public enum ContentPanelTargetType {
		UNKNOWN,
		CATEGORY,
		CHANNEL,
		VIDEO
	};

	private String title;
	private String url;
	private String type;
	private String key;
	private Integer likes;
	private ImagePackReference imagePack;
	private ContentPanelTargetType targetType;

	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl( String url ) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType( String type ) {
		this.type = type;
	}

	public boolean isTargetCategory() {
		return "category".equalsIgnoreCase( type ) || "main_category".equalsIgnoreCase( type );
	}

	public String getKey() {
		return key;
	}

	public void setKey( String key ) {
		this.key = key;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes( Integer likes ) {
		this.likes = likes;
	}

	public CategoryReference getCategoryReference() {
		return getTargetType() == ContentPanelTargetType.CATEGORY ? new Category( Long.parseLong( key ) ) : null;
	}

	public ChannelReference getChannelReference() {
		return getTargetType() == ContentPanelTargetType.CHANNEL ? new ChannelReferenceImpl( Long.parseLong( key ) ) : null;
	}

	public VideoReference getVideoReference() {
		return getTargetType() == ContentPanelTargetType.VIDEO ? new VideoImpl( Long.parseLong( key ) ) : null;
	}

	public ImagePackReference getImagePack() {
		return imagePack;
	}

	public void setImagePack( ImagePackReference imagePack ) {
		this.imagePack = imagePack;
	}

	public ContentPanelTargetType getTargetType() {
		return targetType;
	}

	public void setTargetType( ContentPanelTargetType targetType ) {
		this.targetType = targetType;
	}

	public void setTargetType( String contentType ) {
		if( "main_category".equals( contentType ) ) {
			setTargetType( ContentPanelTargetType.CATEGORY );
		} else if( "category".equals( contentType ) ) {
			setTargetType( ContentPanelTargetType.CATEGORY );
		} else if( "content_category".equals( contentType ) ) {
			setTargetType( ContentPanelTargetType.CHANNEL );
		} else if( "asset".equals( contentType ) ) {
			setTargetType( ContentPanelTargetType.VIDEO );
		} else {
			setTargetType( ContentPanelTargetType.UNKNOWN );
		}
	}
}
