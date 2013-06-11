package no.sumo.api.vo.profile;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root( name = "mobile" )
public class MobileVO {
	private Boolean receiveSMS = null;
	private String number;
	private Integer status;
	private String pin;

	@Attribute
	public Boolean getReceiveSMS() {
		return receiveSMS;
	}

	public void setReceiveSMS( Boolean newReceiveSMS ) {
		receiveSMS = newReceiveSMS;
	}

	@Text
	public String getNumber() {
		return number;
	}

	public void setNumber( String newMobileNumber ) {
		number = newMobileNumber;
	}

	@Attribute
	public Integer getStatus() {
		return status;
	}

	public void setStatus( Integer integer ) {
		status = integer;
	}

	@Attribute
	public String getPin() {
		return pin;
	}

	public void setPIN( String newMobilePIN ) {
		pin = newMobilePIN;
	}
}