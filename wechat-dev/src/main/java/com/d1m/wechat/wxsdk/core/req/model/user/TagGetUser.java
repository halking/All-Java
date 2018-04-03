package com.d1m.wechat.wxsdk.core.req.model.user;

import com.d1m.wechat.wxsdk.core.annotation.ReqType;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqParam;

/**
 * 获取标签下粉丝列表
 * 
 * 
 */
@ReqType("tagGetUser")
public class TagGetUser extends WeixinReqParam {

	private String tagid;

	private String next_openid;

	public String getNext_openid() {
		return next_openid;
	}

	public String getTagid() {
		return tagid;
	}

	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}

	public void setTagid(String tagid) {
		this.tagid = tagid;
	}

}
