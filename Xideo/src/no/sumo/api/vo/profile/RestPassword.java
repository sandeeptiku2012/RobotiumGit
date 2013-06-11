package no.sumo.api.vo.profile;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Root;

@Root( name = "password" )
public class RestPassword extends RestObject {

	private long userId;

	private String oldPassword;

	private String newPassword;

	public long getUserId() {
		return userId;
	}

	public void setUserId( long userId ) {
		this.userId = userId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword( String oldPassword ) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword( String newPassword ) {
		this.newPassword = newPassword;
	}

}
