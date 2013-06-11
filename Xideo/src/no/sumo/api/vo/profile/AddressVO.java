package no.sumo.api.vo.profile;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "address" )
@Default( DefaultType.FIELD )
public class AddressVO {
	private String address;
	private String postalCode;
	private String city;
	private String country;

	@Element
	public String getAddress() {
		return address;
	}

	public void setAddress( String newAddress ) {
		address = newAddress;
	}

	@Element
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode( String newPostalCode ) {
		postalCode = newPostalCode;
	}

	@Element
	public String getCity() {
		return city;
	}

	public void setCity( String newCity ) {
		city = newCity;
	}

	@Element
	public String getCountry() {
		return country;
	}

	public void setCountry( String newCountry ) {
		country = newCountry;
	}
}