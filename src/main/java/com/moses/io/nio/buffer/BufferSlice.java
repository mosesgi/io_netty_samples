package com.moses.io.nio.buffer;

import java.nio.ByteBuffer;

public class BufferSlice {
	
	public static void main(String[] args) {
		ByteBuffer buffer = ByteBuffer.allocate(10);
		
		//put 0-10 into buffer
		for(int i=0; i<buffer.capacity(); ++i) {
			buffer.put((byte)i);
		}
		
		//create buffer slice
		buffer.position(3);
		buffer.limit(7);
		ByteBuffer slice = buffer.slice();
		
		//change content in the slice
		for(int i=0; i<slice.capacity(); ++i) {
			byte b= slice.get(i);
			b*=10;
			slice.put(i,b);
		}
		
		buffer.position(0);
		buffer.limit(buffer.capacity());
		
		while(buffer.remaining() >0) {
			System.out.println(buffer.get());
		}
	}
}
