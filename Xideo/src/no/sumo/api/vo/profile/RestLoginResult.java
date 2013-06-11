package no.sumo.api.vo.profile;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.service.exception.mapper.ErrorCategory;
import no.sumo.api.vo.error.ErrorResponse;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "response" )
@Default( DefaultType.FIELD )
public class RestLoginResult extends ErrorResponse {
	@Element( name = "userId", required = false )
	private Long userId;

	public RestLoginResult() {
		super( Errorcode.AUTHENTICATION_FAILED, ErrorCategory.UNAUTHORIZED );
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId( Long userId ) {
		this.userId = userId;
	}

}
