package com.vimond.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.service.IAuthenticationService;
import no.sumo.api.service.IBaseService;
import no.sumo.api.service.IUserPropertyValueLoginService;
import no.sumo.api.service.util.UserPropertyValueLoginParameters;
import no.sumo.api.vo.profile.RestLoginResult;

import org.apache.http.auth.AuthenticationException;

import roboguice.util.Ln;

import com.comcast.xideo.firmware.IFirmwareService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vimond.entity.User;
import com.vimond.entity.UserReference;

@Singleton
public class StbAuthenticationService {
	private final IAuthenticationService authenticationService;
	private final IFirmwareService firmwareService;
	private final IBaseService baseService;
	private final IUserPropertyValueLoginService silentLoginService;

	UserReference user;

	@Inject( optional = true )
	CredentialStorage credentialStorage;

	@Inject
	public StbAuthenticationService( IAuthenticationService authenticationService, IFirmwareService firmwareService, IBaseService baseService, IUserPropertyValueLoginService silentLoginService ) {
		this.authenticationService = authenticationService;
		this.firmwareService = firmwareService;
		this.baseService = baseService;
		this.silentLoginService = silentLoginService;
	}

	StbAuthenticationService( IAuthenticationService authenticationService ) {
		this( authenticationService, null, null, null );
	}

	public UserReference login( String username, String password ) throws Exception {
		return login( username, password, true );
	}

	public UserReference login( String username, String password, boolean rememberMe ) throws Exception {
		user = null;
		final RestLoginResult result = authenticationService.login( username, password, rememberMe );

		if( result.getCode() == Errorcode.AUTHENTICATION_OK ) {
			if( credentialStorage != null ) {
				credentialStorage.save( username, password );
			}
			user = new User( result.getUserId() );
			return user;
		}
		throw new Exception( result.getDescription() );
	}

	public UserReference getUser() {
		if( user == null ) {
			final RestLoginResult result = authenticationService.isSessionAuthenticated();
			Ln.i( "getUser: %s", result.getCode() );
			if( result.getCode() == Errorcode.SESSION_AUTHENTICATED ) {
				user = new User( result.getUserId() );
			} else {
				try {
					//					if( credentialStorage != null ) {
					//						Credentials credentials = credentialStorage.load();
					//						return login( credentials.getUsername(), credentials.getPassword() );
					//					}
					return loginSilently();
				} catch( Exception e ) {
					Ln.e( "Error autologin." );
				}
			}
		}
		return user;
	}

	public UserReference loginSilently() throws AuthenticationException, NoSuchAlgorithmException, UnsupportedEncodingException {
		String timestamp = Long.toString( System.currentTimeMillis() );
		String contract = firmwareService.getContract( timestamp );
		Ln.e( "contract = %s, account = %s, code = %s, timestamp = %s", contract, getAccount(), getActivationCode(), timestamp );
		final RestLoginResult result = silentLoginService.login( new UserPropertyValueLoginParameters( contract, getAccount(), getActivationCode(), timestamp, false ) );
		if( result.getCode() == Errorcode.AUTHENTICATION_OK ) {
			return new User( result.getUserId() );
		}
		throw new AuthenticationException();
	}

	public void logout() {
		user = null;

		if( credentialStorage != null ) {
			credentialStorage.clear();
		}

		authenticationService.logout();
	}

	private String getServerTime() {
		return baseService.getServerDateTime();
	}

	public String getActivationCode() {
		return firmwareService.getActivationCode();
	}

	public String getAccount() {
		return firmwareService.getAccount();
	}
}
