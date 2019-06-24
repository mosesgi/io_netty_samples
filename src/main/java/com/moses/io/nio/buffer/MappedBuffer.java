package com.moses.io.nio.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedBuffer {
	private static final int start = 0;
	private static final int size = 1024;

	public static void main(String[] args) throws IOException {
		RandomAccessFile raf = new RandomAccessFile("randomTest.txt", "rw");
		FileChannel fc = raf.getChannel();

		// 把缓冲区跟文件系统进行映射关系，只要操作缓冲区里的内容，文件内容也跟着改变
		MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, start, size);

		mbb.put(0, (byte) 97);
		mbb.put(1023, (byte) 122);

		raf.close();
	}
}
