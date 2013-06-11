package no.sumo.api.exception.generic;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.SuperException;

public class MethodNotAllowedException extends SuperException {
	private static final long serialVersionUID = -102799860484813792L;

	public MethodNotAllowedException(String message) {
		super(Errorcode.ASSET_NO_ACCESS, message);
	}

	public MethodNotAllowedException(Errorcode code, String message) {
		super(code, message);
	}

}
