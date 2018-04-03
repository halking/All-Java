package com.d1m.wechat.wxsdk.core.req.model.user;

import java.util.List;

import com.d1m.wechat.wxsdk.core.annotation.ReqType;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqParam;

/**
 * 批量为用户打标签
 * 
 * 
 */
@ReqType("memberBatchTag")
public class MemberBatchTag extends WeixinReqParam {

	private List<String> openid_list;

	private String tagid;

	public List<String> getOpenid_list() {
		return openid_list;
	}

	public String getTagid() {
		return tagid;
	}

	public void setOpenid_list(List<String> openid_list) {
		this.openid_list = openid_list;
	}

	public void setTagid(String tagid) {
		this.tagid = tagid;
	}

}
