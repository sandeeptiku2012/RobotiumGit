package no.sumo.api.service.exception.mapper;

import javax.ws.rs.core.Response.Status;

/**
 * Maps an error category to a HTTP response code
 */
public enum ErrorCategory {
	UNKNOWN( Status.INTERNAL_SERVER_ERROR.getStatusCode() ),
	NOT_FOUND( Status.NOT_FOUND.getStatusCode() ),
	BAD_REQUEST( Status.BAD_REQUEST.getStatusCode() ),
	UNAUTHORIZED( Status.UNAUTHORIZED.getStatusCode() ),
	FORBIDDEN( Status.FORBIDDEN.getStatusCode() ),
	CONFLICT( Status.CONFLICT.getStatusCode() ),
	INTERNAL_ERROR( Status.INTERNAL_SERVER_ERROR.getStatusCode() ),
	GATEWAY_TIMEOUT( 504 );

	private int status;

	ErrorCategory( int status ) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}