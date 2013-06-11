package no.sumo.api.vo.product;

import no.sumo.api.entity.sumo.enums.PaymentMethod;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root( name = "payment" )
@Default( value = DefaultType.FIELD, required = false )
public class RestPaymentObject extends RestObject {
	private RestObject callbackUri;

	private RestObject initializeUri;

	private String providerReturnUrl;

	private String provider;

	private String url;

	private String name;

	protected PaymentMethod paymentMethod;

	/* The following are credit card fields, and should ideally be held in a separate subclass. However, unmarshalling this stopped working
	 * at some point.
	 */
	private RestObject redirectUrl;

	private String cardNumber;

	private String expireMonth;

	private String expireYear;

	private String cvc;

	/*
	 * Following fields are used for SMS payment, and, as with credit card fields, ideally should be held in separate subclass.
	 */
	private String mobilenumber;

	private String countrycode;

	private String verificationCode;

	/*
	 * Following fields are used for One Click Buy, ideally should be held in separate subclass.
	 */
	private String password;

	/*
	 * Contains hash of mobilenumer + salt, used to prevent hacking of pre-validated sms purchases
	 */
	private String authString;

	private String voucher;

	private Long pinCode;

	public Long getPinCode() {
		return pinCode;
	}

	public void setPinCode( Long pinCode ) {
		this.pinCode = pinCode;
	}

	public RestObject getCallbackUri() {
		if( callbackUri == null ) {
			callbackUri = new RestObject();
		}
		return callbackUri;
	}

	public RestObject getInitializeUri() {
		if( initializeUri == null ) {
			initializeUri = new RestObject();
		}
		return initializeUri;
	}

	public String getProviderReturnUrl() {
		return providerReturnUrl;
	}

	public void setProviderReturnUrl( String providerReturnUrl ) {
		this.providerReturnUrl = providerReturnUrl;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider( String provider ) {
		this.provider = provider;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl( String url ) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod( PaymentMethod paymentMethod ) {
		this.paymentMethod = paymentMethod;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber( String cardNumber ) {
		this.cardNumber = cardNumber;
	}

	public String getExpireMonth() {
		return expireMonth;
	}

	public void setExpireMonth( String expireMonth ) {
		this.expireMonth = expireMonth;
	}

	public String getExpireYear() {
		return expireYear;
	}

	public void setExpireYear( String expireYear ) {
		this.expireYear = expireYear;
	}

	public String getCvc() {
		return cvc;
	}

	public void setCvc( String cvc ) {
		this.cvc = cvc;
	}

	public RestObject getRedirectUrl() {
		if( redirectUrl == null ) {
			redirectUrl = new RestObject();
		}
		return redirectUrl;
	}

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber( String mobilenumber ) {
		this.mobilenumber = mobilenumber;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode( String countrycode ) {
		this.countrycode = countrycode;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode( String verificationCode ) {
		this.verificationCode = verificationCode;
	}

	public String getAuthString() {
		return authString;
	}

	public void setAuthString( String authString ) {
		this.authString = authString;
	}

	public String getVoucher() {
		return voucher;
	}

	public void setVoucher( String voucher ) {
		this.voucher = voucher;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( String password ) {
		this.password = password;
	}
}
