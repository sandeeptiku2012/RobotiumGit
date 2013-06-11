package no.sumo.api.vo.error;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.service.exception.mapper.ErrorCategory;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "error" )
public class ErrorResponse {
	@Transient
	int status;

	@Element( name = "code" )
	private Errorcode code;

	@Element( name = "description" )
	private String description;

	public ErrorResponse() {
		this( Errorcode.UNKNOWN, ErrorCategory.UNKNOWN );
	}

	public ErrorResponse( Errorcode code, ErrorCategory category ) {
		this( code, category, null );
	}

	public ErrorResponse( Errorcode code, ErrorCategory category, String description ) {
		this( code, category.getStatus(), description );
	}

	public ErrorResponse( Errorcode code, int status, String description ) {
		setCode( code );
		setDescription( description );
		setStatus( status );
	}

	public int getStatus() {
		return status;
	}

	public void setStatus( int status ) {
		this.status = status;
	}

	public Errorcode getCode() {
		return code;
	}

	public void setCode( Errorcode code ) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}
}