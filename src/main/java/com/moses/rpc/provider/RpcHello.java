package com.moses.rpc.provider;

import com.moses.rpc.api.IRpcHello;

public class RpcHello implements IRpcHello{

	@Override
	public String hello(String name) {
		return "Hello " + name + ", this is provider.";
	}

}
