package com.moses.chat.client.handler;

import java.io.IOException;

import com.moses.chat.protocol.IMDecoder;
import com.moses.chat.protocol.IMEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Java terminal client
 * @author Moses
 *
 */
public class ChatClient {

	private ChatClientHandler clientHandler;
	private String host;
	private int port;

	public ChatClient(String nickName) {
		this.clientHandler = new ChatClientHandler(nickName);
	}

	public void connect(String host, int port) {
		this.host = host;
		this.port = port;

		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new IMDecoder());
					ch.pipeline().addLast(new IMEncoder());
					ch.pipeline().addLast(clientHandler);
				}
			});
			ChannelFuture f = b.connect(this.host, this.port).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws IOException {
		new ChatClient("Jim").connect("127.0.0.1", 80);
	}

}
