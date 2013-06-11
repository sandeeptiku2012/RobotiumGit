package com.comcast.xideo.events;

import com.vimond.entity.CategoryReference;

public class ShowCategoryEvent {
	private final Object source;
	private final CategoryReference category;

	public ShowCategoryEvent( Object source, CategoryReference category ) {
		this.source = source;
		this.category = category;
	}

	public Object getSource() {
		return source;
	}

	public CategoryReference getCategory() {
		return category;
	}
}
