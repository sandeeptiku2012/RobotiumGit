package no.sumo.api.service.util;

import javax.ws.rs.FormParam;

public class UserPropertyValueLoginParameters {
	@FormParam( "id" )
	private String userPropertyValue;
	@FormParam( "contract" )
	private String checksum;
	@FormParam( "timestamp" )
	private String expires;
	@FormParam( "account" )
	private String propertyKeyIdentifier;
	@FormParam( "rememberMe" )
	private boolean rememberMe = false;

	/**
	 * <p>
	 * This constructor is public to satisfy RESTEasy.
	 * </p>
	 * <p>
	 * Use the static factory methods
	 * {@link #copyOf(UserPropertyValueLoginParameters)} or {@link #create()} to
	 * create new instances of this class
	 * </p>
	 */
	@Deprecated
	public UserPropertyValueLoginParameters() {
	}

	UserPropertyValueLoginParameters( UserPropertyValueLoginParameters source ) {
		checksum = source.checksum;
		expires = source.expires;
		propertyKeyIdentifier = source.propertyKeyIdentifier;
		userPropertyValue = source.userPropertyValue;
		rememberMe = source.rememberMe;
	}

	public String propertySecretIdentifier() {
		return propertyKeyIdentifier;
	}

	public String checksum() {
		return checksum;
	}

	public String userPropertyValue() {
		return userPropertyValue;
	}

	public boolean rememberMe() {
		return rememberMe;
	}

	public String expires() {
		return expires;
	}

	public UserPropertyValueLoginParameters( String contract, String account, String code, String timestamp ) {
		this( contract, account, code, timestamp, true );
	}

	public UserPropertyValueLoginParameters( String contract, String account, String code, String timestamp, boolean rememberMe ) {
		userPropertyValue = code;
		propertyKeyIdentifier = account;
		checksum = contract;
		expires = timestamp;
		this.rememberMe = rememberMe;
	}
}