package com.comcast.xideo.firmware;

public class MockMemoryReader implements IMemoryReader {
	private byte[] memory = {
		'G', 'e', 'n', 'i', 'a', 't', 'e', 'c', 'h', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

		0x00, 0x00, 0x00, 0x20,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,

		0x00, 0x00, 0x00, 0x30,
		0x00, 0x00, 0x00, 0x08,
		'c', '2', '9', '5', 'd', 'a', '3', '0',

		0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x40,
		'7', 'a', '8', '9', '9', '6', '5', 'd',
		'3', '6', '2', '3', '9', '5', '6', 'f',
		'f', '1', '0', 'a', '6', '6', 'f', 'f',
		'a', '5', '3', '0', '9', '9', '8', 'c',
		'4', '6', 'a', '1', '6', 'a', '8', 'f',
		'c', '7', '8', '5', '6', 'c', 'c', 'c',
		'd', '6', '5', '2', '6', '1', '4', '6',
		'3', 'a', '2', '7', 'e', '5', '6', '9',
	};

	private byte getByte( int address ) {
		return address >= 0 && address < memory.length ? memory[ address ] : -1;
	}

	@Override
	public int getInt( int address ) {
		int data = 0;
		for( int i = 0; i < 4; i++ ) {
			data = data << 8 | getByte( address + i );
		}
		return data;
	}

	@Override
	public String getString( int address, int length ) {
		if( length < 0 ) {
			return null;
		}
		byte[] buffer = new byte[ length ];
		for( int i = 0; i < length; i++ ) {
			byte c = getByte( address + i );
			if( c == 0 ) {
				return new String( buffer, 0, i );
			}
			buffer[ i ] = c;
		}
		return new String( buffer );
	}
}
