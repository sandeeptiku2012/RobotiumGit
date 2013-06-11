package com.vimond.service;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.comcast.xideo.utils.CryptString;
import com.google.inject.Inject;

public class SharedPreferencesCredentialStorage implements CredentialStorage {
	private static final String USERNAME_PREFERENCES_KEY = "usn";
	private static final String PASSWORD_PREFERENCES_KEY = "pwd";
	private static final String GOLDEN_GATE_CRYPTO_SEED = "GoldenGate";

	private SharedPreferences preferences;

	@Inject
	public SharedPreferencesCredentialStorage( SharedPreferences preferences ) {
		this.preferences = preferences;
	}

	@Override
	public void save( String username, String password ) throws Exception {
		Editor edit = preferences.edit();
		edit.putString( USERNAME_PREFERENCES_KEY, username );
		edit.putString( PASSWORD_PREFERENCES_KEY, encrypt( password ) );
		edit.commit();
	}

	@Override
	public Credentials load() throws Exception {
		String username = preferences.getString( USERNAME_PREFERENCES_KEY, null );
		String password = preferences.getString( PASSWORD_PREFERENCES_KEY, null );
		return new Credentials( username, decrypt( password ) );
	}

	@Override
	public void clear() {
		Editor edit = preferences.edit();
		edit.remove( USERNAME_PREFERENCES_KEY );
		edit.remove( PASSWORD_PREFERENCES_KEY );
		edit.commit();
	}

	private String encrypt( String value ) throws Exception {
		return CryptString.encrypt( GOLDEN_GATE_CRYPTO_SEED, value );
	}

	private String decrypt( String value ) throws Exception {
		return CryptString.decrypt( GOLDEN_GATE_CRYPTO_SEED, value );
	}
}
