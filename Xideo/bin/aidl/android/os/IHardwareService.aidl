package android.os;

/** {@hide} */
interface IHardwareService {
	boolean getFlashlightEnabled();
	String getI2cData( int offset, int size );
	int getI2cDataSize( int offset, int size );
	int writeI2cData( int offset, String data );
	String getMAC();
	String getMCID();
}