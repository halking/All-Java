package com.d1m.wechat.wxsdk.core.req.model.kfaccount;

import com.d1m.wechat.wxsdk.core.annotation.ReqType;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqParam;

/**
 * 发送客服消息
 * 
 * @author sfli.sir
 * 
 */
@ReqType("customsend")
public class KfcustomSend extends WeixinReqParam {

	private String touser;

	/**
	 * mpnews,music,text,image,voice,video
	 */
	private String msgtype;

	private Customservice customservice;

	private MsgText text;

	private MsgImage image;

	private MsgVoice voice;

	private MsgVideo video;

	private MsgNews news;

	private MsgMusic music;

	private MsgMpNews mpnews;

	public Customservice getCustomservice() {
		return customservice;
	}

	public MsgImage getImage() {
		return image;
	}

	public MsgMpNews getMpnews() {
		return mpnews;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public MsgMusic getMusic() {
		return music;
	}

	public MsgNews getNews() {
		return news;
	}

	public MsgText getText() {
		return text;
	}

	public String getTouser() {
		return touser;
	}

	public MsgVideo getVideo() {
		return video;
	}

	public MsgVoice getVoice() {
		return voice;
	}

	public void setCustomservice(Customservice customservice) {
		this.customservice = customservice;
	}

	public void setImage(MsgImage image) {
		this.image = image;
	}

	public void setMpnews(MsgMpNews mpnews) {
		this.mpnews = mpnews;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public void setMusic(MsgMusic music) {
		this.music = music;
	}

	public void setNews(MsgNews news) {
		this.news = news;
	}

	public void setText(MsgText text) {
		this.text = text;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public void setVideo(MsgVideo video) {
		this.video = video;
	}

	public void setVoice(MsgVoice voice) {
		this.voice = voice;
	}

}
