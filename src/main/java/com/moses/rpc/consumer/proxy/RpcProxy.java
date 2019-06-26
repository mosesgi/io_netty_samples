package com.moses.rpc.consumer.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.moses.rpc.core.RpcMsg;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class RpcProxy {

	public static <T> T create(Class<T> clazz) {
		MethodProxy methodProxy = new MethodProxy(clazz);
		@SuppressWarnings("unchecked")
		T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, methodProxy);
		return result;
	}

}

class MethodProxy implements InvocationHandler {
	private Class<?> clazz;

	public MethodProxy(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//ignore if the method is from an instance instead of interface class
		if(Object.class.equals(method.getDeclaringClass())) {
			return null;
		} else {
			return rpcInvoke(method,args);
		}
		
	}

	private Object rpcInvoke(Method method, Object[] args) {
		RpcMsg msg = new RpcMsg();
		msg.setClassName(this.clazz.getName());
		msg.setMethodName(method.getName());
		msg.setParams(method.getParameterTypes());
		msg.setValues(args);
		
		EventLoopGroup group = new NioEventLoopGroup();
		
		final RpcProxyHandler handler = new RpcProxyHandler();
		
		try {
			Bootstrap b = new Bootstrap();
			
			b.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					//处理拆包、粘包的解码编码器
					pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
					pipeline.addLast(new LengthFieldPrepender(4));
					
					//JDK serialization encoder and decoder.
					pipeline.addLast("encoder",new ObjectEncoder());
					pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE,ClassResolvers.cacheDisabled(null)));
					
					pipeline.addLast(handler);
				}
			});
			
			ChannelFuture f = b.connect("localhost",8080).sync();
			
			f.channel().writeAndFlush(msg).sync();
			f.channel().closeFuture().sync();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
		
		return handler.getResult();
	}
	
	

}