package com.comcast.xideo.firmware;

import java.util.ArrayList;
import java.util.List;


public class GeniaTechPlatform extends FirmwareServiceImpl implements IFirmwareService {
	private static final int HEADER_SIZE = 0x10;
	private String activationCode;
	private String secretKey;
	private String account;

	public GeniaTechPlatform( IMemoryReader reader, String account ) {
		assert account != null;
		readAll( reader );
		this.account = account;
	}

	private void readAll( IMemoryReader reader ) {
		if( reader == null ) {
			return;
		}
		String header = reader.getString( 0x0000, HEADER_SIZE );
		if( "Geniatech".equals( header ) ) {
			int address = reader.getInt( HEADER_SIZE );
			List<String> data = readStrings( reader, address );
			if( data.size() >= 2 ) {
				activationCode = data.get( 0 );
				secretKey = data.get( 1 );
			}
		}
	}

	private List<String> readStrings( IMemoryReader reader, int address ) {
		ArrayList<String> data = new ArrayList<String>();
		while( address != 0 ) {
			int next = reader.getInt( address + 0 );
			int size = reader.getInt( address + 4 );
			if( size >= 0 ) {
				data.add( reader.getString( address + 8, size ) );
			}
			address = next;
		}
		return data;
	}

	@Override
	public String getActivationCode() {
		return activationCode;
	}

	@Override
	public String getSecretKey() {
		return secretKey;
	}

	@Override
	public String getAccount() {
		return account;
	}
}
