package no.sumo.api.vo.profile;

import no.sumo.api.entity.profile.enums.UserEmailFormat;
import no.sumo.api.entity.profile.enums.UserEmailStatus;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root( name = "email" )
public class EmailVO {
	private String email = null;
	private String status = null;
	private Boolean receiveEmail = null;
	private String format = null;

	@Text
	public String getEmail() {
		return email;
	}

	public void setEmail( String newEmail ) {
		email = newEmail;
	}

	@Attribute
	public String getEmailStatus() {
		return status;
	}

	public void setStatus( String newEmailStatus ) {
		status = newEmailStatus;
	}

	public Integer getStatusInt() {
		if( status == null ) {
			return new Integer( UserEmailStatus.UNKNOWN.ordinal() );
		} else if( status.equals( "invalid" ) ) {
			return new Integer( UserEmailStatus.INVALID.ordinal() );
		} else if( status.equals( "valid" ) ) {
			return new Integer( UserEmailStatus.VALID.ordinal() );
		} else {
			return new Integer( UserEmailStatus.UNKNOWN.ordinal() );
		}
	}

	public void setStatusInt( Integer newEmailStatus ) {
		if( newEmailStatus == null ) {
			status = "unknown";
		} else if( newEmailStatus.intValue() == UserEmailStatus.VALID.ordinal() ) {
			status = "valid";
		} else if( newEmailStatus.intValue() == UserEmailStatus.INVALID.ordinal() ) {
			status = "invalid";
		} else {
			status = "unknown";
		}
	}

	@Attribute
	public Boolean getReceiveEmail() {
		return receiveEmail;
	}

	public void setReceiveEmail( Boolean newReceiveEmail ) {
		receiveEmail = newReceiveEmail;
	}

	@Attribute
	public String getFormat() {
		return format;
	}

	public void setFormat( String newEmailFormat ) {
		format = newEmailFormat;
	}

	public Integer getFormatInt() {
		if( format == null || format.equals( "unknown" ) ) {
			return new Integer( UserEmailFormat.UNKNOWN.ordinal() );
		} else if( format.equals( "html" ) ) {
			return new Integer( UserEmailFormat.HTML.ordinal() );
		} else {
			return new Integer( UserEmailFormat.TEXT.ordinal() );
		}
	}

	public void setFormatInt( Integer newEmailFormat ) {
		if( newEmailFormat == null ) {
			format = "unknown";
		} else if( newEmailFormat.intValue() == UserEmailFormat.HTML.ordinal() ) {
			format = "html";
		} else if( newEmailFormat.intValue() == UserEmailFormat.TEXT.ordinal() ) {
			format = "text";
		} else {
			format = "unknown";
		}
	}

}