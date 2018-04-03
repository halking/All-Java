package com.d1m.wechat.wxsdk.core.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.wxsdk.core.annotation.ReqType;
import com.d1m.wechat.wxsdk.core.handler.WeiXinReqHandler;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqConfig;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqParam;
import com.d1m.wechat.wxsdk.core.req.model.menu.MenuCreate;
import com.d1m.wechat.wxsdk.core.req.model.menu.WeixinButton;
import com.d1m.wechat.wxsdk.core.util.HttpRequestProxy;
import com.d1m.wechat.wxsdk.core.util.WeiXinReqUtil;
import com.d1m.wechat.wxsdk.menu.JwMenuAPI;

/**
 * 菜单创建的处理
 * 
 * @author sfli.sir
 *
 */
public class WeixinReqMenuCreateHandler implements WeiXinReqHandler {

	private static Logger logger = Logger
			.getLogger(WeixinReqMenuCreateHandler.class);

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
				MenuCreate mc = (MenuCreate) weixinReqParam;
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("access_token", mc.getAccess_token());
				String jsonData = null;
				if (mc.getMatchRule() == null) {
					jsonData = "{"
							+ getMenuButtonJson("button", mc.getButton()) + "}";
					logger.info("处理创建菜单" + jsonData);
				} else {
					reqUrl = JwMenuAPI.add_conditional_url.replace(
							"ACCESS_TOKEN", mc.getAccess_token());
					jsonData = "{"
							+ getMenuButtonJson("button", mc.getButton())
							+ ",\"matchrule\" : "
							+ JSONObject.toJSONString(mc.getMatchRule()) + "}";
					logger.info("处理创建个性化菜单" + jsonData);
				}
				strReturnInfo = HttpRequestProxy.doJsonPost(reqUrl, parameters,
						jsonData);
			}
		} else {
			logger.info("没有找到对应的配置信息");
		}
		return strReturnInfo;
	}

	/**
	 * 单独处理菜单 json信息
	 * 
	 * @param name
	 * @param b
	 * @return
	 */
	private String getMenuButtonJson(String name, List<WeixinButton> b) {
		StringBuffer json = new StringBuffer();
		json.append("\"" + name + "\":[");
		if (b == null || b.size() == 0) {
			return json.append("]").toString();
		}
		List<WeixinButton> sub_button = null;
		String objJson = "";
		for (WeixinButton m : b) {
			sub_button = m.getSub_button();
			m.setSub_button(null);
			objJson = net.sf.json.JSONObject.fromObject(m).toString();
			json.append(objJson);
			if (sub_button != null && sub_button.size() > 0) {
				json.setLength(json.length() - 1);
				json.append(",");
				objJson = getMenuButtonJson("sub_button", sub_button);
				json.append(objJson);
				json.append("}");
			}
			m.setSub_button(sub_button);
			json.append(",");
		}
		json.setLength(json.length() - 1);
		json.append("]");
		logger.info("button json : " + json.toString());
		return json.toString();
	}

}
