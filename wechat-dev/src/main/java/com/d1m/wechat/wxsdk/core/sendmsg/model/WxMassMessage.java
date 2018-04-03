package com.d1m.wechat.wxsdk.core.sendmsg.model;

public class WxMassMessage {

	private String media_id;

	private String text;

	private String msgtype;

	public String getMedia_id() {
		return media_id;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public String getText() {
		return text;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public void setText(String text) {
		this.text = text;
	}

}
