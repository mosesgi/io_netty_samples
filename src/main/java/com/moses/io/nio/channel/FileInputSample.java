package com.moses.io.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileInputSample {
	
	public void inputSample() throws IOException {
		URL resource = this.getClass().getResource("/sample.txt");
		File file = new File(resource.getFile());
		FileInputStream fis = new FileInputStream(file);
		//Open channel
		FileChannel channel = fis.getChannel();
		//create buffer
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		//Read data to buffer
		channel.read(buffer);
		buffer.flip();
		while(buffer.remaining()>0) {
			byte b = buffer.get();
			System.out.print((char)b);
		}
		
		fis.close();
	}
	
	public static void main(String[] args) throws IOException {
		new FileInputSample().inputSample();
	}
}
