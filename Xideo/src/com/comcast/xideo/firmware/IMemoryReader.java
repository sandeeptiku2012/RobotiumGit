package com.comcast.xideo.firmware;

public interface IMemoryReader {
	public abstract int getInt( int address );
	public abstract String getString( int address, int size );
}