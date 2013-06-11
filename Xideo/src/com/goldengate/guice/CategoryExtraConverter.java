package com.goldengate.guice;

import roboguice.inject.ExtraConverter;

import com.comcast.xideo.viewmodel.Category;
import com.vimond.entity.CategoryReference;

public class CategoryExtraConverter implements ExtraConverter<Long, CategoryReference> {
	@Override
	public CategoryReference convert( Long categoryId ) {
		return new Category( categoryId );
	}
}
