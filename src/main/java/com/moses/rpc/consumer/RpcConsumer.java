package com.moses.rpc.consumer;

import com.moses.rpc.api.IRpcHello;
import com.moses.rpc.consumer.proxy.RpcProxy;

public class RpcConsumer {
	
	public static void main(String[] args) {
		IRpcHello rpcHello = RpcProxy.create(IRpcHello.class);
		String result = rpcHello.hello("Jim");
		
		System.out.println(result);
	}
	
}
