package com.moses.chat.client.handler;

import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.moses.chat.protocol.IMMessage;
import com.moses.chat.protocol.IMP;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChatClientHandler extends ChannelInboundHandlerAdapter {

	private static Logger LOG = Logger.getLogger(ChatClientHandler.class);
	private ChannelHandlerContext ctx;
	private String nickName;

	public ChatClientHandler(String nickName) {
		this.nickName = nickName;
	}

	/** 启动客户端控制台 */
	private void startSession() throws IOException {
		new Thread() {
			public void run() {
				LOG.info(nickName + ",你好，请在控制台输入消息内容");
				IMMessage message = null;
				Scanner scanner = new Scanner(System.in);
				do {
					if (scanner.hasNext()) {
						String input = scanner.nextLine();
						if ("exit".equals(input)) {
							message = new IMMessage(IMP.LOGOUT.getName(), System.currentTimeMillis(), nickName);
						} else {
							message = new IMMessage(IMP.CHAT.getName(), System.currentTimeMillis(), nickName, input);
						}
					}
				} while (sendMsg(message));
				scanner.close();
			}
		}.start();
	}

	/**
	 * tcp链路建立成功后调用
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
		IMMessage message = new IMMessage(IMP.LOGIN.getName(), System.currentTimeMillis(), this.nickName);
		sendMsg(message);
		LOG.info("成功连接服务器,已执行登录动作");
		startSession();
	}

	/**
	 * 发送消息
	 * 
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	private boolean sendMsg(IMMessage msg) {
		ctx.channel().writeAndFlush(msg);
		LOG.info("已发送至聊天面板,请继续输入");
		return msg.getCmd().equals(IMP.LOGOUT.getName()) ? false : true;
	}

	/**
	 * 收到消息后调用
	 * 
	 * @throws IOException
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
		if(msg == null) {
			return;
		}
		IMMessage m = (IMMessage) msg;
		IMP cmd = IMP.parseName(m.getCmd());
		String result = null;
		switch(cmd) {
			case CHAT:
				result = String.format("%s 发来消息: %s", m.getSender(), m.getContent());
				break;
			case SYSTEM:
				result = String.format("系统消息: %s" , m.getContent());
				break;
			case LOGIN:
				result = String.format("%s 已经登录!", m.getSender());
				break;
			case FLOWER:
				result = String.format("%s 发来鲜花！敬请脑补...", m.getSender());
				break;
			case LOGOUT:
				result = String.format("%s 退出登录.", m.getSender());
		}
		LOG.info(result);
	}

	/**
	 * 发生异常时调用
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LOG.info("与服务器断开连接:" + cause.getMessage());
		ctx.close();
	}
}
