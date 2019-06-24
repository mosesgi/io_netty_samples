package com.moses.io.aio;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

public class AIOClient {
	private final AsynchronousSocketChannel client;
	
	public AIOClient() throws IOException {
		client = AsynchronousSocketChannel.open();
	}
}
