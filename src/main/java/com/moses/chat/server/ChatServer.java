package com.moses.chat.server;

import org.apache.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatServer {
	private static Logger LOG = Logger.getLogger(ChatServer.class);
	
	private int port = 80;
	
	public void start() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup,workerGroup)
				.channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						
					}
					
				});
			ChannelFuture future = server.bind(this.port).sync();
			LOG.info("server started, listening on port: " + this.port);
			future.channel().closeFuture().sync();
		} catch(InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
