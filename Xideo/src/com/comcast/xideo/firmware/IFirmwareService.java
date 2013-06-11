package com.comcast.xideo.firmware;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface IFirmwareService {
	String getActivationCode();

	String getAccount();

	String getContract( String time ) throws NoSuchAlgorithmException, UnsupportedEncodingException;
}