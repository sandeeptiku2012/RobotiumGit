package no.sumo.api.exception.category;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;


public class CategoryNotFoundException extends NotFoundException {

	private static final long serialVersionUID = -1971339713675222377L;

	public CategoryNotFoundException(Long categoryId) {
		this(categoryId, String.format("Category with id '%s' was not found", categoryId));
	}
	
	public CategoryNotFoundException(Long categoryId, Integer platformId) {
		this(categoryId, String.format("Category with id '%s' on platformId '%s' was not found", categoryId, platformId));
	}

	public CategoryNotFoundException(Long categoryId, String message) {
		super(Errorcode.CATEGORY_NOT_FOUND, message, categoryId);
	}
	
	public CategoryNotFoundException(Long categoryId, Errorcode errorcode, String message) {
		super(errorcode, message, categoryId);
	}
}
