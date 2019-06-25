package com.moses.chat.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.moses.chat.protocol.IMMessage;
import com.moses.chat.protocol.IMP;

public class MessageConverter {
	// 解析IM写一下请求内容的正则
	private Pattern pattern = Pattern.compile("^\\[(.*)\\](\\s\\-\\s(.*))?");
	
	/**
	 * 字符串解析成自定义即时通信协议
	 * 
	 * @param msg
	 * @return
	 */
	public IMMessage decode(String msg) {
		if (null == msg || "".equals(msg.trim())) {
			return null;
		}
		try {
			Matcher m = pattern.matcher(msg);
			String header = "";
			String content = "";
			if (m.matches()) {
				header = m.group(1);
				content = m.group(3);
			}

			String[] heards = header.split("\\]\\[");
			long time = 0;
			try {
				time = Long.parseLong(heards[1]);
			} catch (Exception e) {
			}
			String nickName = heards[2];
			// 昵称最多十个字
			nickName = nickName.length() < 10 ? nickName : nickName.substring(0, 9);

			if (msg.startsWith("[" + IMP.LOGIN.getName() + "]")) {
				return new IMMessage(heards[0], time, nickName);
			} else if (msg.startsWith("[" + IMP.CHAT.getName() + "]")) {
				return new IMMessage(heards[0], time, nickName, content);
			} else if (msg.startsWith("[" + IMP.FLOWER.getName() + "]")) {
				return new IMMessage(heards[0], time, nickName);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String encode(IMMessage msg){
		if(null == msg){ return ""; }
		String prex = "[" + msg.getCmd() + "]" + "[" + msg.getTime() + "]";
		if(IMP.LOGIN.getName().equals(msg.getCmd()) ||
		   IMP.CHAT.getName().equals(msg.getCmd()) ||
		   IMP.FLOWER.getName().equals(msg.getCmd())){
			prex += ("[" + msg.getSender() + "]");
		}else if(IMP.SYSTEM.getName().equals(msg.getCmd())){
			prex += ("[" + msg.getOnline() + "]");
		}
		if(!(null == msg.getContent() || "".equals(msg.getContent()))){
			prex += (" - " + msg.getContent());
		}
		return prex;
	}
}
