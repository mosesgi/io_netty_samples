package com.moses.chat.protocol;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class IMEncoder extends MessageToByteEncoder<IMMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, IMMessage msg, ByteBuf out)
			throws Exception {
		out.writeBytes(new MessagePack().write(msg));
	}

}
