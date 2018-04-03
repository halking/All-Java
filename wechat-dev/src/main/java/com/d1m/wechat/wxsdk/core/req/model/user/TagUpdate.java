package com.d1m.wechat.wxsdk.core.req.model.user;

import com.d1m.wechat.wxsdk.core.annotation.ReqType;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqParam;

/**
 * 编辑标签
 * 
 * 
 */
@ReqType("tagUpdate")
public class TagUpdate extends WeixinReqParam {

	private Tag tag;

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

}
