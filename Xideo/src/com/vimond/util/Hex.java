package com.vimond.util;

public class Hex {
	private final static char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String encodeToString( byte[] bytes ) {
		char[] encodedChars = encode( bytes );
		return new String( encodedChars );
	}

	public static char[] encode( byte[] data ) {
		int l = data.length;
		char[] out = new char[ l << 1 ];
		// two characters form the hex value.
		for( int i = 0, j = 0; i < l; i++ ) {
			out[ j++ ] = DIGITS[ (0xF0 & data[ i ]) >>> 4 ];
			out[ j++ ] = DIGITS[ 0x0F & data[ i ] ];
		}
		return out;
	}
}