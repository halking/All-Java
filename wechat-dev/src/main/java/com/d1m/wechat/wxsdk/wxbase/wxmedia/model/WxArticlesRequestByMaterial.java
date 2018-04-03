package com.d1m.wechat.wxsdk.wxbase.wxmedia.model;

/**
 * 上传图文消息素材id
 * 
 * @author lihongxuan
 *
 */
public class WxArticlesRequestByMaterial {

	private String media_id;

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	@Override
	public String toString() {
		return "WxArticlesRequestByMaterial [mediaId=" + media_id + "]";
	}

}
