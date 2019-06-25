package com.moses.chat.protocol;

public enum IMP {
	/** 系统消息 */
	SYSTEM("SYSTEM"),
	/** 登录指令 */
	LOGIN("LOGIN"),
	/** 登出指令 */
	LOGOUT("LOGOUT"),
	/** 聊天消息 */
	CHAT("CHAT"),
	/** 送鲜花 */
	FLOWER("FLOWER");

	private String name;

	public static boolean isIMP(String content) {
		return content.matches("^\\[(SYSTEM|LOGIN|LOGIN|CHAT)\\]");
	}

	IMP(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		return this.name;
	}

	public static IMP parseName(String name) {
		if (name == null || name.trim().length() == 0) {
			return null;
		}
		if (name.equals(SYSTEM.getName())) {
			return SYSTEM;
		} else if (name.equals(LOGIN.getName())) {
			return LOGIN;
		} else if (name.equals(LOGOUT.getName())) {
			return LOGOUT;
		} else if (name.equals(CHAT.getName())) {
			return CHAT;
		} else if (name.equals(FLOWER.getName())) {
			return FLOWER;
		}
		return null;
	}

}