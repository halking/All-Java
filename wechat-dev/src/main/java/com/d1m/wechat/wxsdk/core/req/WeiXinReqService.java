package com.d1m.wechat.wxsdk.core.req;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.service.WechatService;
import com.d1m.wechat.service.impl.RefreshAccessTokenJob;
import com.d1m.wechat.util.AppContextUtils;
import com.d1m.wechat.wxsdk.core.annotation.ReqType;
import com.d1m.wechat.wxsdk.core.handler.WeiXinReqHandler;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqConfig;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqParam;
import com.d1m.wechat.wxsdk.core.util.WeiXinReqUtil;

/**
 * 获取微信接口的信息
 * 
 * @author liguo
 * 
 */
public class WeiXinReqService {

	private static Logger logger = LoggerFactory
			.getLogger(WeiXinReqService.class);

	private static WeiXinReqService weiXinReqUtil = null;

	private WeiXinReqService() {
		try {
			WeiXinReqUtil.initReqConfig("weixin-reqcongfig.xml");
		} catch (JDOMException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 获取公共的调用处理
	 * 
	 * @return
	 */
	public static WeiXinReqService getInstance() {
		if (weiXinReqUtil == null) {
			// 同步块，线程安全的创建实例
			synchronized (WeiXinReqService.class) {
				// 再次检查实例是否存在，如果不存在才真正的创建实例
				if (weiXinReqUtil == null) {
					weiXinReqUtil = new WeiXinReqService();
				}
			}
		}
		return weiXinReqUtil;
	}

	/**
	 * 传入请求的参数，获取对应额接口返回信息
	 * 
	 * @param weixinReqParam
	 * @return
	 * @throws WechatException
	 */
	public String doWeinxinReq(WeixinReqParam weixinReqParam)
			throws WechatException {
		String strReturnInfo = "";
		if (weixinReqParam.getClass().isAnnotationPresent(ReqType.class)) {
			ReqType reqType = weixinReqParam.getClass().getAnnotation(
					ReqType.class);
			WeixinReqConfig objConfig = WeiXinReqUtil
					.getWeixinReqConfig(reqType.value());
			WeiXinReqHandler handler = WeiXinReqUtil.getMappingHander(objConfig
					.getMappingHandler());
			strReturnInfo = handler.doRequest(weixinReqParam);
		}
		return strReturnInfo == null ? "" : strReturnInfo;
	}

	/**
	 * 返回json对象
	 * 
	 * @param weixinReqParam
	 * @return
	 * @throws WechatException
	 */
	public JSONObject doWeinxinReqJson(WeixinReqParam weixinReqParam)
			throws WechatException {
		JSONObject obj = JSONObject.fromObject(this
				.doWeinxinReq(weixinReqParam));
		String errcode = null;
		if (obj.has("errcode")) {
			errcode = obj.getString("errcode");
		}
		if (errcode != null && !"0".equals(errcode)) {
            logger.error("req weixin error:{}", obj.toString());
			if (StringUtils.equals(errcode, "40001")
					|| StringUtils.equals(errcode, "40014")
					|| StringUtils.equals(errcode, "42001")) {
				logger.error("access_token error, Refresh AccessToken.");
				WechatService wechatService = AppContextUtils
						.getBean(WechatService.class);
				RefreshAccessTokenJob.getNewAccessToken(wechatService
						.getById(RefreshAccessTokenJob.accessTokenWechatMap
								.get(weixinReqParam.getAccess_token())));
				RefreshAccessTokenJob.accessTokenWechatMap
						.remove(weixinReqParam.getAccess_token());

                // token过期, 重试一次
                return JSONObject.fromObject(this.doWeinxinReq(weixinReqParam));
			}
		}
		return obj;
	}
}
