package com.moses.io.nio.buffer;

import java.nio.ByteBuffer;

public class BufferWrapper {
	
	public static void main(String[] args) {
		//allocating specified buffer directly.
		ByteBuffer buff1 = ByteBuffer.allocate(10);
		
		//wrap an existing array
		byte array[] = new byte[10];
		ByteBuffer buff2 = ByteBuffer.wrap(array);
	}
}
