package com.d1m.wechat.wxsdk.core.req.model.user;

import com.d1m.wechat.wxsdk.core.annotation.ReqType;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqParam;

/**
 * 删除标签
 * 
 * 
 */
@ReqType("tagDelete")
public class TagDelete extends WeixinReqParam {

	private Tag tag;

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

}
