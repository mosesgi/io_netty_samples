package com.moses.chat.protocol;

import java.util.List;

import org.msgpack.MessagePack;
import org.msgpack.MessageTypeException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class IMDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {
			// 先获取可读字节数
			final int length = in.readableBytes();
			final byte[] array = new byte[length];
			String content = new String(array, in.readerIndex(), length);

			// 空消息不解析
			if (!(null == content || "".equals(content.trim()))) {
				if (!IMP.isIMP(content)) {
					ctx.channel().pipeline().remove(this);
					return;
				}
			}

			in.getBytes(in.readerIndex(), array, 0, length);
			out.add(new MessagePack().read(array, IMMessage.class));
			in.clear();
		} catch (MessageTypeException e) {
			ctx.channel().pipeline().remove(this);
		}
	}

}
