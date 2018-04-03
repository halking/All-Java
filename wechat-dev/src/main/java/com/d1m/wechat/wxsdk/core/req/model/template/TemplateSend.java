package com.d1m.wechat.wxsdk.core.req.model.template;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.wxsdk.core.annotation.ReqType;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqParam;

@ReqType("sendTemplate")
public class TemplateSend extends WeixinReqParam{
	
	private String touser;
	private String template_id;
	private JSONObject data;
	private String url;
	
	public String getTouser() {
		return touser;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public JSONObject getData() {
		return data;
	}
	public String getUrl() {
		return url;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public void setData(JSONObject dataObj) {
		this.data = dataObj;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
