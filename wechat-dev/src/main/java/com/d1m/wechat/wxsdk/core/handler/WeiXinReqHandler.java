package com.d1m.wechat.wxsdk.core.handler;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqParam;

/**
 * 获取微信接口的信息
 * 
 * @author liguo
 *
 */
public interface WeiXinReqHandler {

	public String doRequest(WeixinReqParam weixinReqParam)
			throws WechatException;

}
