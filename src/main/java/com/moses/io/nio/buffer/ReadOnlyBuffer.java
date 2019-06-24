package com.moses.io.nio.buffer;

import java.nio.ByteBuffer;

public class ReadOnlyBuffer {
	
	public static void main(String[] args) {
		ByteBuffer buffer = ByteBuffer.allocate(10);
		
		//put 0-9 into buffer
		for(int i=0; i<buffer.capacity(); ++i) {
			buffer.put((byte)i);
		}
		
		//create read-only buffer
		ByteBuffer readOnlyBuf = buffer.asReadOnlyBuffer();
		
		for(int i=0; i<buffer.capacity(); ++i) {
			byte b = buffer.get(i);
			b*=10;
			buffer.put(i,b);
		}
		
		readOnlyBuf.position(0);
		readOnlyBuf.limit(buffer.capacity());
		
		//readonly buffer content changes too
		while(readOnlyBuf.hasRemaining()) {
			System.out.println(readOnlyBuf.get());
		}
	}
}
