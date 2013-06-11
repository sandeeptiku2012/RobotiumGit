package no.sumo.api.exception.generic;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.GeneralFailureException;

public class RemoteGatewayException extends GeneralFailureException {

	private static final long serialVersionUID = -6029730427172284302L;

	public RemoteGatewayException (String message)
	{
		super (Errorcode.REMOTE_GATEWAY, message);
	}

	public RemoteGatewayException(String message, Throwable cause) {
		super(Errorcode.REMOTE_GATEWAY, message, cause);
	}
}
