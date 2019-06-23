package com.moses.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {
	ServerSocket server;

	public BIOServer(int port) {
		try {
			server = new ServerSocket(port);
			System.out.println("BIO server started, port: " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listen() throws IOException {
		while (true) {
			// block here
			Socket client = server.accept();
			InputStream is = client.getInputStream();
			byte[] buff = new byte[1024];
			int len = is.read(buff);

			if (len > 0) {
				String msg = new String(buff, 0, len);
				System.out.println("received: " + msg);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new BIOServer(8080).listen();
	}
}
