package com.moses.io.nio.channel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileOutputSample {
	private final static byte message[] = {83, 111, 109, 101, 32, 98, 121, 116, 101, 115, 46 };
	
	public static void main(String[] args) throws IOException {
		FileOutputStream fos = new FileOutputStream("d:/outFile.txt");
		FileChannel channel = fos.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		for(int i=0; i<message.length; ++i) {
			buffer.put(message[i]);
		}
		
		buffer.flip();
		channel.write(buffer);
		fos.close();
	}
}
