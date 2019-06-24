package com.moses.io.nio.buffer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DirectBuffer {
	public static void main(String[] args) throws IOException {
		URL url = DirectBuffer.class.getResource("/sample.txt");
		System.out.println(url.getPath());
		FileInputStream fis = new FileInputStream(url.getFile());
		FileChannel fileInChannel = fis.getChannel();
		
		//write content to a new file
		FileOutputStream fout = new FileOutputStream("sampleCopy.txt");
		FileChannel fileOutChannel = fout.getChannel();
		
		//use allocateDirect instead of allocate
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		
		while(true) {
			buffer.clear();
			int r = fileInChannel.read(buffer);
			
			if(r == -1) {
				break;
			}
			buffer.flip();
			fileOutChannel.write(buffer);
		}
	}
}
