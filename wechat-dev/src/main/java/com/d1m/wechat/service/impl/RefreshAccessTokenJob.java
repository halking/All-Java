package com.d1m.wechat.service.impl;

import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.d1m.wechat.model.Wechat;
import com.d1m.wechat.model.enums.WechatStatus;
import com.d1m.wechat.pamametermodel.WechatModel;
import com.d1m.wechat.service.RefreshAccessTokenService;
import com.d1m.wechat.service.WechatService;
import com.d1m.wechat.util.AppContextUtils;
import com.d1m.wechat.util.DateUtil;
import com.d1m.wechat.wxsdk.core.thread.AccessToken;
import com.d1m.wechat.wxsdk.core.thread.JsApiTicket;
import com.d1m.wechat.wxsdk.wxbase.wxjsapiticket.JwJsApiTicketAPI;
import com.d1m.wechat.wxsdk.wxbase.wxserviceip.JwServiceIpAPI;
import com.d1m.wechat.wxsdk.wxbase.wxtoken.JwTokenAPI;

@Component
public class RefreshAccessTokenJob implements RefreshAccessTokenService {

	private static Logger logger = LoggerFactory
			.getLogger(RefreshAccessTokenJob.class);

	public static ConcurrentHashMap<Integer, AccessToken> accessTokenMap = new ConcurrentHashMap<Integer, AccessToken>();

	public static ConcurrentHashMap<String, Integer> accessTokenWechatMap = new ConcurrentHashMap<String, Integer>();

	public static ConcurrentHashMap<Integer, JsApiTicket> jsApiTicketMap = new ConcurrentHashMap<Integer, JsApiTicket>();

	public static AccessToken getAccessToken(Integer wechatId) {
		return accessTokenMap.get(wechatId);
	}

	public static JsApiTicket getJsApiTicket(Integer wechatId) {
		return jsApiTicketMap.get(wechatId);
	}


	public synchronized static AccessToken getLatestAccessToken(Integer wechatId) {
		logger.info("getLatestAccessToken" + JSONArray.fromObject(accessTokenMap.keys()));
//		for (Map.Entry<Integer, AccessToken> entry : accessTokenMap.entrySet()){
//			logger.info("======" + entry.getKey() + "===" + JSONObject.fromObject(entry.getValue()).toString());
//		}
		AccessToken accessToken = getAccessToken(wechatId);
		if(null == accessToken || accessToken.isExpired()){
			accessToken = refreshAccessToken(wechatId);
		}
		return accessToken;
	}

	public synchronized static String getAccessTokenStr(Integer wechatId) {
		AccessToken accessToken = getAccessToken(wechatId);
		if(null==accessToken||accessToken.isExpired()){
			accessToken=refreshAccessToken(wechatId);
		}
		if (accessToken != null) {
			return accessToken.getAccessToken();
		}
		return "";
	}

	public synchronized static String getJsApiTicketStr(Integer wechatId) {
		JsApiTicket jsApiTicket = getJsApiTicket(wechatId);
		if(null==jsApiTicket||jsApiTicket.isExpired()){
			jsApiTicket=refreshJsApiTicket(wechatId);
		}
		if (jsApiTicket != null) {
			return jsApiTicket.getTicket();
		}
		return "";
	}

	@Override
	// @Scheduled(fixedRate = 1000 * 60*60)
	public synchronized void checkAccessTokenIsExpired() {
		logger.info("accessTokenMap size : {}", accessTokenMap.size());
		String accessToken = null;
		for (Entry<Integer, AccessToken> entry : accessTokenMap.entrySet()) {
			accessToken = entry.getValue().getAccessToken();
			logger.info("wechat {}, token {} start checked.", entry.getKey(),
					accessToken);
			JsApiTicket jsApiTicket = jsApiTicketMap.get(entry.getKey());
			logger.info("wechat {}, ticket {}.", entry.getKey(),
					jsApiTicket != null ? jsApiTicket.getTicket() : null);
			boolean valid = JwServiceIpAPI.checkAccessTokenValid(accessToken);
			logger.info("valid : {}.", valid);
			if (valid) {
				logger.info("valid do nothing.");
				continue;
			}
			WechatService wechatService = AppContextUtils
					.getBean(WechatService.class);
			getNewAccessToken(wechatService.selectByKey(entry.getKey()));
			logger.info(
					"wechat {}, token {} get new AccessToken because of invalid.",
					entry.getKey(), accessToken);
		}
	}

	/**
	 * 刷新wechatId 对应的AccessToken
	 * @param wechatId
     * @return
     */
	public synchronized static AccessToken refreshAccessToken(Integer wechatId){
		AccessToken accessToken=null;
		try {
			WechatService wechatService = AppContextUtils.getBean(WechatService.class);
			Wechat wechat= wechatService.getById(wechatId);
			if(null!=wechat){
				accessToken = getNewAccessToken(wechat);
			}
		} catch (Exception e) {
			logger.error("refreshAccessToken err: {}", e);
		}
		return accessToken;
	}
	/**
	 * 刷新wechatId JsApiTicket
	 * @param wechatId
	 * @return
	 */
	public synchronized static JsApiTicket refreshJsApiTicket(Integer wechatId){
		JsApiTicket jsApiTicket=null;
		try {
			WechatService wechatService = AppContextUtils
					.getBean(WechatService.class);
			WechatModel wechatModel = new WechatModel();
			wechatModel.setStatus(WechatStatus.INUSED.getValue());
			Wechat wechat= wechatService.getById(wechatId);
			if(null!=wechat){
				jsApiTicket=getNewJsApiTicket(wechat,getAccessTokenStr(wechatId));
			}
		} catch (Exception e) {
			logger.error("refreshJsApiTicket err: {}", e);
		}
		return jsApiTicket;
	}
	public synchronized static AccessToken getNewAccessToken(Wechat wechat) {
		String token = JwTokenAPI.getAccessToken(wechat.getAppid(),
				wechat.getAppscret());
		if (StringUtils.isBlank(token)) {
			logger.error("wechat {} can not get access token.", wechat.getId());
			return null;
		}
		AccessToken accessToken = getAccessToken(wechat.getId());
		if(null==accessToken){
			accessToken = new AccessToken(token, 7200);
			accessToken.setLastRefreshTimeMillis(System.currentTimeMillis());
			accessTokenMap.put(wechat.getId(),accessToken);
		}else{
			accessToken.setAccessToken(token);
			accessToken.setLastRefreshTimeMillis(System.currentTimeMillis());
			accessTokenMap.put(wechat.getId(),accessToken);
		}
		//getNewJsApiTicket(wechat, token);
		accessTokenWechatMap.put(token, wechat.getId());
		logger.info("当前时间 {}, wechat {}, 获取access_token成功, 有效时长{}秒 ,token:{}",
				DateUtil.formatYYYYMMDDHHMMSSS(new Date()), wechat.getId(),
				accessToken.getExpired(), accessToken.getAccessToken());
		return accessToken;
	}

	private synchronized static JsApiTicket getNewJsApiTicket(Wechat wechat,
			String accessToken) {
		String jsApiTicket = JwJsApiTicketAPI.getJsApiTicket(accessToken);
		if (StringUtils.isBlank(jsApiTicket)) {
			logger.error("wechat {} can not get js api ticket.", wechat.getId());
			return null;
		}
		JsApiTicket jat = getJsApiTicket(wechat.getId());
		if(null==jat){
			jat=new JsApiTicket(jsApiTicket, 7200);
			jsApiTicketMap.put(wechat.getId(), jat);
		}else{
			jat.setTicket(jsApiTicket);
		}

		logger.info(
				"当前时间 {}, wechat {}, 获取js_api_ticket成功, 有效时长{}秒 ,ticket:{}",
				DateUtil.formatYYYYMMDDHHMMSSS(new Date()), wechat.getId(),
				jat.getExpired(), jat.getTicket());
		return jat;
	}

}
