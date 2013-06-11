package com.comcast.xideo.firmware;

import android.os.IHardwareService;
import android.os.RemoteException;

public class GeniaTechMemoryReader implements IMemoryReader {
	private IHardwareService service;

	public GeniaTechMemoryReader( IHardwareService service ) {
		this.service = service;
	}

	@Override
	public int getInt( int address ) {
		try {
			return service.getI2cDataSize( address, 4 );
		} catch( RemoteException e ) {
		}
		return -1;
	}

	@Override
	public String getString( int address, int length ) {
		try {
			return service.getI2cData( address, length );
		} catch( RemoteException e ) {
		}
		return null;
	}
}
