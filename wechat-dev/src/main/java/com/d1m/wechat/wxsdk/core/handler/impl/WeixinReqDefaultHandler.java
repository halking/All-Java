package com.d1m.wechat.wxsdk.core.handler.impl;

import java.util.Map;

import org.apache.log4j.Logger;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.wxsdk.core.annotation.ReqType;
import com.d1m.wechat.wxsdk.core.handler.WeiXinReqHandler;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqConfig;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqParam;
import com.d1m.wechat.wxsdk.core.util.HttpRequestProxy;
import com.d1m.wechat.wxsdk.core.util.WeiXinConstant;
import com.d1m.wechat.wxsdk.core.util.WeiXinReqUtil;

public class WeixinReqDefaultHandler implements WeiXinReqHandler {

	private static Logger logger = Logger
			.getLogger(WeixinReqDefaultHandler.class);

	@SuppressWarnings("rawtypes")
	public String doRequest(WeixinReqParam weixinReqParam)
			throws WechatException {
		String strReturnInfo = "";
		if (weixinReqParam.getClass().isAnnotationPresent(ReqType.class)) {
			ReqType reqType = weixinReqParam.getClass().getAnnotation(
					ReqType.class);
			WeixinReqConfig objConfig = WeiXinReqUtil
					.getWeixinReqConfig(reqType.value());
			if (objConfig != null) {
				String reqUrl = objConfig.getUrl();
				String method = objConfig.getMethod();
				String datatype = objConfig.getDatatype();
				Map parameters = WeiXinReqUtil
						.getWeixinReqParam(weixinReqParam);
				if (WeiXinConstant.JSON_DATA_TYPE.equalsIgnoreCase(datatype)) {
					parameters.clear();
					parameters.put("access_token",
							weixinReqParam.getAccess_token());
					weixinReqParam.setAccess_token(null);
					String jsonData = WeiXinReqUtil
							.getWeixinParamJson(weixinReqParam);
					logger.info("wechat request :"+jsonData);
					strReturnInfo = HttpRequestProxy.doJsonPost(reqUrl,
							parameters, jsonData);
					// 请求后写回access_token，防止access_token被抢后重试时为NULL
					weixinReqParam.setAccess_token((String) parameters.get("access_token"));
				} else {
					if (WeiXinConstant.REQUEST_GET.equalsIgnoreCase(method)) {
						strReturnInfo = HttpRequestProxy.doGet(reqUrl,
								parameters, "UTF-8");
					} else {
						strReturnInfo = HttpRequestProxy.doPost(reqUrl,
								parameters, "UTF-8");
					}
				}
			}
		} else {
			logger.info("没有找到对应的配置信息");
		}
		return strReturnInfo;
	}

}
