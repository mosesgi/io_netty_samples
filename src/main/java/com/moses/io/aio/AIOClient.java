package com.moses.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class AIOClient {
	private final AsynchronousSocketChannel client;
	
	public AIOClient() throws IOException {
		client = AsynchronousSocketChannel.open();
	}
	
	public void connect(String host, int port) throws Exception{
		//write operation only
		client.connect(new InetSocketAddress(host, port), null, new CompletionHandler<Void, Void>(){

			@Override
			public void completed(Void result, Void attachment) {
				try {
					client.write(ByteBuffer.wrap(("test data: " + System.currentTimeMillis()).getBytes())).get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void failed(Throwable exc, Void attachment) {
				exc.printStackTrace();
			}
			
		});
		
		//read data
		final ByteBuffer bb = ByteBuffer.allocate(200);
		client.read(bb, null, new CompletionHandler<Integer, Object>(){

			@Override
			public void completed(Integer result, Object attachment) {
				System.out.println("Get result: " + new String(bb.array()));
			}

			@Override
			public void failed(Throwable exc, Object attachment) {
				exc.printStackTrace();
			}
			
		});
		
		System.in.read();
	}
	
	public static void main(String[] args) {
		int count = 10;
		CountDownLatch latch = new CountDownLatch(count);
		for(int i=0; i<count; i++) {
			new Thread() {

				@Override
				public void run() {
					try {
						
						new AIOClient().connect("localhost", 8000);
					} catch(Exception e) {
						
					}
				}
				
			}.start();
		}
		try {
			Thread.sleep(1000*60*10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
