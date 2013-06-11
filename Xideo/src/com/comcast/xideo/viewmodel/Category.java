package com.comcast.xideo.viewmodel;

import com.vimond.entity.MainCategory;
import com.vimond.entity.SubCategory;

public class Category implements MainCategory, SubCategory {
	public long id;
	public String title;

	public Category( long id ) {
		this.id = id;
	}

	public Category() {
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
}
