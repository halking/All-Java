package com.d1m.wechat.wxsdk.core.req.model.menu;

import com.d1m.wechat.wxsdk.core.annotation.ReqType;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqParam;

/**
 * 删除个性化菜单
 * 
 * @author zhenglei.wang
 * 
 */
@ReqType("conditionalMenuDelete")
public class ConditionalMenuDelete extends WeixinReqParam {

	private String menuid;

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

}
