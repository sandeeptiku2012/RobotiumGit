package no.sumo.api.vo.paymentprovider;

import java.util.Date;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "possibleOneClickBuy" )
@Default( DefaultType.FIELD )
public class RestPaymentProvider extends RestObject {

	@Element( name = "paymentProviderId" )
	private Long id;

	@Element( name = "paymentProviderName" )
	private String name;

	@Element( name = "paymentInformation" )
	private String paymentInformation;

	@Element( name = "paymentInformationExpirationDate" )
	private Date paymentInformationExpirationDate;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getPaymentInformation() {
		return paymentInformation;
	}

	public void setPaymentInformation( String paymentInformation ) {
		this.paymentInformation = paymentInformation;
	}

	public Date getPaymentInformationExpirationDate() {
		return paymentInformationExpirationDate;
	}

	public void setPaymentInformationExpirationDate( Date paymentInformationExpirationDate ) {
		this.paymentInformationExpirationDate = paymentInformationExpirationDate;
	}
}
