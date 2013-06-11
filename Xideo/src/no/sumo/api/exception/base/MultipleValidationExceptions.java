package no.sumo.api.exception.base;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.exception.Errorcode;
import android.text.TextUtils;

public class MultipleValidationExceptions extends ValidationException {
	private static final long serialVersionUID = -6925169002266791982L;

	final List<ValidationException> exceptions;

	public MultipleValidationExceptions(Iterable<ValidationException> exceptions){
		super(Errorcode.USER_MULTIPLE_VALIDATION_ERRORS);
		this.exceptions = new ArrayList<ValidationException>();
		for( ValidationException exception : exceptions ) {
			this.exceptions.add( exception );
		}
	}

	public List<ValidationException> getExceptions() {
		return exceptions;
	}

	public void throwIfHasExceptions() throws MultipleValidationExceptions {
		if(!exceptions.isEmpty()){
			throw this;
		}
	}

	@Override
	public String getMessage() {
		List<String> messages = new ArrayList<String>();

		for(ValidationException e : exceptions){
			messages.add(e.getMessage());
		}

		return TextUtils.join( ",", messages );
	}
}
