package com.d1m.wechat.wxsdk.wxbase.wxjsapiticket;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.wxsdk.core.util.HttpRequestProxy;

/**
 * 微信--ticket信息
 * 
 * 
 */
public class JwJsApiTicketAPI {

	private static Logger logger = LoggerFactory
			.getLogger(JwJsApiTicketAPI.class);

	// 获得jsapi_ticket
	private static String get_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token"
			+ "=ACCESS_TOKEN&type=jsapi";

	/**
	 * 获取jsapi_ticket信息
	 * 
	 * @param accessToken
	 * @return bxLdikRXVbTPdHSM05e5u5sUoXNKd8-41
	 *         ZO3MhKoyN5OfkWITDGgnr2fwJ0m9E8NYzWKVZvdVtaUgWvsdshFKA
	 * @throws WechatException
	 */
	public static String getJsApiTicket(String accessToken)
			throws WechatException {
		String reqUrl = get_ticket_url.replace("ACCESS_TOKEN", accessToken);
		String result = HttpRequestProxy.doGet(reqUrl, new HashMap(), "UTF-8");
		JSONObject json = JSONObject.parseObject(result);
		logger.info("response : {}", json);
		if (!StringUtils.equals(json.getString("errcode"), "0")) {
			throw new WechatException(Message.WEIXIN_HTTPS_REQUEST_ERROR);
		}
		return json.getString("ticket");
	}

}
