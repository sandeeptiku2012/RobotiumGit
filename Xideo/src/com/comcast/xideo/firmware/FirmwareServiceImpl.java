package com.comcast.xideo.firmware;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.vimond.util.Hex;

public class FirmwareServiceImpl implements IFirmwareService {
	final static String ACTIVATION_CODE = "8587116854797768006";
	final static String SECRET = "TTi9emRlcozc2qf/Q5ddBwTx5KFwZOjn75+7EzB6gLY=";
	final static String ACCOUNT = "geniatec";

	@Override
	public String getActivationCode() {
		return ACTIVATION_CODE;
	}

	@Override
	public String getAccount() {
		return ACCOUNT;
	}

	protected String getSecretKey() {
		return SECRET;
	}

	@Override
	public String getContract( String timestamp ) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String source = getActivationCode() + timestamp + getSecretKey();
		MessageDigest sha256 = MessageDigest.getInstance( "SHA-256" );
		byte[] data = sha256.digest( source.getBytes( "UTF-8" ) );
		return new String( Hex.encode( data ) );
	}
}