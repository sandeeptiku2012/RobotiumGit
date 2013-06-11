package no.sumo.api.exception;

import no.sumo.api.exception.base.NotFoundException;

public class SectionNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 2132235773774682012L;

	public SectionNotFoundException(String message) {
		super(Errorcode.CATEGORY_SECTION_NOT_FOUND, message);
	}

}
