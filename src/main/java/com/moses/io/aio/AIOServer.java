package com.moses.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AIOServer {
	private final int port;
	
	public AIOServer(int port) {
		this.port = port;
		listen();
	}

	private void listen() {
		try {
			//线程缓冲池
			ExecutorService executorService = Executors.newCachedThreadPool();
			AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
			
			AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(threadGroup);
			
			server.bind(new InetSocketAddress(port));
			System.out.println("service start, listening port: " + port);
			
			AtomicInteger count = new AtomicInteger(0);
			server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
				ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
				
				@Override
				public void completed(AsynchronousSocketChannel result, Object attachment) {
					count.addAndGet(1);
					System.out.println(count.get());
					try {
						buffer.clear();
						result.read(buffer).get();
						buffer.flip();
						result.write(buffer);
						buffer.flip();
					} catch(Exception e) {
						e.printStackTrace();
					} finally {
						try {
							result.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						server.accept(null, this);
					}
				}

				@Override
				public void failed(Throwable exc, Object attachment) {
					System.out.println("IO操作失败: " + exc);
				}
				
			});
			
			System.in.read();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		int port = 8000;
		new AIOServer(port);
	}
}
