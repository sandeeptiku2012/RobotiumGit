package com.comcast.xideo.authentication;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import android.os.IBinder;
import android.os.IHardwareService;

import com.comcast.xideo.firmware.FirmwareServiceImpl;
import com.comcast.xideo.firmware.GeniaTechMemoryReader;
import com.comcast.xideo.firmware.GeniaTechPlatform;
import com.comcast.xideo.firmware.IFirmwareService;
import com.comcast.xideo.firmware.IMemoryReader;
import com.comcast.xideo.firmware.MockMemoryReader;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class SilentLoginModule extends AbstractModule {
	private final static String ACCOUNT_NAME = "geniatech";

	@Override
	protected void configure() {
	}

	@Provides
	IMemoryReader createMemoryReader( @Nullable IHardwareService service ) {
		return service != null ? new GeniaTechMemoryReader( service ) : new MockMemoryReader();
	}

	@Provides
	IFirmwareService createFirmwareService( IMemoryReader reader ) {
		return new GeniaTechPlatform( reader, ACCOUNT_NAME );
	}

	@Provides
	IHardwareService createHardwareService() {
		try {
			Class<?> serviceManager = Class.forName( "android.os.ServiceManager" );
			Method getServiceMethod = serviceManager.getMethod( "getService", String.class );
			IBinder binder = (IBinder)getServiceMethod.invoke( null, new Object[] { "hardware" } );
			return IHardwareService.Stub.asInterface( binder );
		} catch( Exception ex ) {
			return null;
		}
	}
}
