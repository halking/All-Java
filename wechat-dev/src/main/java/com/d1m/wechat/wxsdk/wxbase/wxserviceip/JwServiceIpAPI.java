package com.d1m.wechat.wxsdk.wxbase.wxserviceip;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.wxsdk.core.req.WeiXinReqService;
import com.d1m.wechat.wxsdk.core.req.model.ServiceIP;
import com.d1m.wechat.wxsdk.core.util.WeiXinConstant;

/**
 * 微信--token信息
 * 
 * @author lizr
 * 
 */
public class JwServiceIpAPI {

	private static Logger logger = LoggerFactory
			.getLogger(JwServiceIpAPI.class);

	/**
	 * 返回的信息名称
	 */
	public static String RETURN_INFO_NAME = "ip_list";

	/**
	 * 获取服务的ip列表信息
	 * 
	 * @param accessToke
	 * @return
	 * @throws WechatException
	 */
	public static List<String> getServiceIpList(String accessToke)
			throws WechatException {
		ServiceIP param = new ServiceIP();
		param.setAccess_token(accessToke);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				param);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		List<String> lstServiceIp = null;
		if (error == null) {
			JSONArray infoArray = result.getJSONArray(RETURN_INFO_NAME);
			lstServiceIp = new ArrayList<String>(infoArray.size());
			for (int i = 0; i < infoArray.size(); i++) {
				lstServiceIp.add(infoArray.getString(i));
			}
		}
		return lstServiceIp;
	}

	/**
	 * 获取服务的ip列表信息
	 * 
	 * @param accessToke
	 * @return
	 * @throws WechatException
	 */
	public static boolean checkAccessTokenValid(String accessToke)
			throws WechatException {
		ServiceIP param = new ServiceIP();
		param.setAccess_token(accessToke);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				param);
		logger.info("response : {}", result);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		return error == null ? true : false;
	}
}
