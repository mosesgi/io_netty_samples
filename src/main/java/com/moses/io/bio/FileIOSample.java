package com.moses.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileIOSample {
	public void useInputStreamReader() throws IOException {
		int result;
		InputStream is = this.getClass().getResourceAsStream("/sample.txt");
		InputStreamReader isr = new InputStreamReader(is);
		while((result=isr.read()) != -1) {
			System.out.print((char)result);
		}
		isr.close();
	}
	
	public void useBufferedReader() throws IOException{
		InputStream is = this.getClass().getResourceAsStream("/sample.txt");
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String tmp;
		while((tmp=br.readLine()) != null) {
			System.out.println(tmp);
		}
	}
	
	public static void main(String[] args) throws IOException {
//		new FileIOSample().useInputStreamReader();
		new FileIOSample().useBufferedReader();
	}
}
