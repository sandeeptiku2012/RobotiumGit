package no.sumo.api.vo.profile;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "person" )
public class PersonVO {
	private String lastName = null;
	private String middleName = null;
	private String firstName = null;
	private Date dateOfBirth = null;
	private Integer gender = null;
	private String company;
	private String ssn;
	private String phoneNumber;

	@Element
	public String getLastName() {
		return lastName;
	}

	public void setLastName( String value ) {
		lastName = value;
	}

	@Element
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName( String value ) {
		firstName = value;
	}

	@Element
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName( String value ) {
		middleName = value;
	}

	@Element( required = true )
	public String getFullName() {
		if( middleName != null && middleName.length() > 0 ) {
			return new StringBuffer().append( firstName ).append( " " ).append( middleName ).append( " " ).append( lastName ).toString();
		} else {
			return new StringBuffer().append( firstName ).append( " " ).append( lastName ).toString();
		}
	}

	@Attribute
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public String getYearOfBirth() {

		if( dateOfBirth == null ) {
			return null;
		}
		SimpleDateFormat tFormat = new SimpleDateFormat( "yyyy" );
		return tFormat.format( dateOfBirth );
	}

	public void setDateOfBirth( Date newDateOfBirth ) {
		dateOfBirth = newDateOfBirth;
	}

	@Attribute
	public Integer getGender() {
		return gender;
	}

	public void setGender( Integer newGender ) {
		gender = newGender;
	}

	@Element
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber( String newPhoneNumber ) {
		phoneNumber = newPhoneNumber;
	}

	@Element
	public String getCompany() {
		return company;
	}

	public void setCompany( String newCompany ) {
		company = newCompany;
	}

	@Element
	public String getSsn() {
		return ssn;
	}

	public void setSsn( String newSsn ) {
		ssn = newSsn;
	}
}