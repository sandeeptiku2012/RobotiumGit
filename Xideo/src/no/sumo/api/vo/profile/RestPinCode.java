package no.sumo.api.vo.profile;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created with IntelliJ IDEA. User: ogi Date: 04.07.12 Time: 15:42 To change
 * this template use File | Settings | File Templates.
 */
@Root( name = "pinCode" )
public class RestPinCode extends RestObject {
	@Element
	private Long userId;

	@Element
	private String password;

	@Element
	private Long pinCode;

	public Long getUserId() {
		return userId;
	}

	public void setUserId( Long userId ) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( String password ) {
		this.password = password;
	}

	public Long getPinCode() {
		return pinCode;
	}

	public void setPinCode( Long pinCode ) {
		this.pinCode = pinCode;
	}
}
