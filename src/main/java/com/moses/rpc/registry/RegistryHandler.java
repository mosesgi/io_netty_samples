package com.moses.rpc.registry;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.moses.rpc.core.RpcMsg;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RegistryHandler extends ChannelInboundHandlerAdapter{
	private Map<String, Object> container = new ConcurrentHashMap<String, Object>();
	
	private List<String> classCache = new ArrayList<String>();
	
	//Scan all classes under package "com.moses.rpc.provider"
	public RegistryHandler() {
		scanClass("com.moses.rpc.provider");
		doRegister();
	}


	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Object result = new Object();
		
		RpcMsg message = (RpcMsg)msg;
		if(container.containsKey(message.getClassName())) {
			Object clazz = container.get(message.getClassName());
			Method m = clazz.getClass().getMethod(message.getMethodName(), message.getParams());
			result = m.invoke(clazz, message.getValues());
		}
		
		ctx.writeAndFlush(result);
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	private void scanClass(String packageName) {
		URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
		File dir = new File(url.getFile());
		for(File file : dir.listFiles()) {
			if(file.isDirectory()) {
				scanClass(packageName + "." + file.getName());
			}else {
				classCache.add(packageName + "." + file.getName().replace(".class", "").trim());
			}
		}
	}
	
	//initialize all classes found, and use interface name as provider name
	private void doRegister() {
		if(classCache.size() == 0) return;
		for(String className: classCache) {
			try {
				Class<?> clazz = Class.forName(className);
				Class<?> interf = clazz.getInterfaces()[0];
				container.put(interf.getName(), clazz.newInstance());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
