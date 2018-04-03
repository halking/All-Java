package com.d1m.wechat.wxsdk.wxbase.wxtoken;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.wxsdk.core.req.WeiXinReqService;
import com.d1m.wechat.wxsdk.core.req.model.AccessToken;

/**
 * 微信--token信息
 * 
 * @author lizr
 * 
 */
public class JwTokenAPI {

	private static Logger logger = LoggerFactory.getLogger(JwTokenAPI.class);

	/**
	 * 获取权限令牌信息
	 * 
	 * @param appid
	 * @param appscret
	 * @return 
	 *         kY9Y9rfdcr8AEtYZ9gPaRUjIAuJBvXO5ZOnbv2PYFxox__uSUQcqOnaGYN1xc4N1rI7NDCaPm_0ysFYjRVnPwCJHE7v7uF_l1hI6qi6QBsA
	 * @throws WechatException
	 */
	public static String getAccessToken(String appid, String appscret)
			throws WechatException {
		AccessToken atoken = new AccessToken();
		atoken.setAppid(appid);
		atoken.setSecret(appscret);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				atoken);
		logger.info("get token result : {}", result);
		return result.optString("access_token");
	}

}
