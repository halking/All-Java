package com.d1m.wechat.wxsdk.qrcode;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.wxsdk.core.req.WeiXinReqService;
import com.d1m.wechat.wxsdk.core.req.model.account.QrcodeActionInfo;
import com.d1m.wechat.wxsdk.core.req.model.account.QrcodeCreate;
import com.d1m.wechat.wxsdk.core.req.model.account.QrcodeScene;
import com.d1m.wechat.wxsdk.core.req.model.account.ShortUrlCreate;
import com.d1m.wechat.wxsdk.core.util.WeiXinConstant;
import com.d1m.wechat.wxsdk.qrcode.model.WxQrcode;

/**
 * 微信--生成二维码和长短链接切换
 * 
 * @author lizr
 * 
 */
public class JwAccountAPI {

	private static Logger log = LoggerFactory.getLogger(JwAccountAPI.class);

	public static String SHOWQRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";

	/**
	 * 代表长链接转短链接
	 */
	public static final String SHORT_URL_ACTION = "long2short";

	/**
	 * 
	 * expire_seconds 该二维码有效时间，以秒为单位。 最大不超过1800。 action_name
	 * 二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
	 * action_info 二维码详细信息 scene_id
	 * 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000） scene_str
	 * 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64，仅永久二维码支持此字段
	 * 
	 * @param accessToken
	 * @param scene_str
	 * @param actionName
	 * @param expireSeconds
	 * @return
	 * @throws WechatException
	 */
	public static WxQrcode createQrcodeBySceneId(String accessToken,
			Integer sceneId, String actionName, String expireSeconds)
			throws WechatException {
		QrcodeCreate qrcodeCreate = new QrcodeCreate();
		qrcodeCreate.setAccess_token(accessToken);
		QrcodeActionInfo q = new QrcodeActionInfo();
		QrcodeScene ss = new QrcodeScene();
		ss.setScene_id(sceneId);
		q.setScene(ss);
		qrcodeCreate.setAction_info(q);
		qrcodeCreate.setExpire_seconds(expireSeconds);
		qrcodeCreate.setAction_name(actionName);
		log.info("action_name : {}, scene_id : {}, expire_seconds : {}",
				actionName, sceneId, expireSeconds);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				qrcodeCreate);
		log.info("qrcode create result : {}", result);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		if (error != null) {
			throw new WechatException(Message.SYSTEM_ERROR);
		}
		WxQrcode wxQrcode = (WxQrcode) JSONObject
				.toBean(result, WxQrcode.class);
		log.info("wxQrcode : {}", wxQrcode);
		return wxQrcode;
	}

	/**
	 * 
	 * expire_seconds 该二维码有效时间，以秒为单位。 最大不超过1800。 action_name
	 * 二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
	 * action_info 二维码详细信息 scene_id
	 * 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000） scene_str
	 * 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64，仅永久二维码支持此字段
	 * 
	 * @param accessToken
	 * @param scene_str
	 * @param actionName
	 * @param expireSeconds
	 * @return
	 * @throws WechatException
	 */
	public static WxQrcode createQrcodeBySceneStr(String accessToken,
			String sceneStr, String actionName, String expireSeconds)
			throws WechatException {
		QrcodeCreate qrcodeCreate = new QrcodeCreate();
		qrcodeCreate.setAccess_token(accessToken);
		QrcodeActionInfo q = new QrcodeActionInfo();
		QrcodeScene ss = new QrcodeScene();
		ss.setScene_str(sceneStr);
		q.setScene(ss);
		qrcodeCreate.setAction_info(q);
		qrcodeCreate.setExpire_seconds(expireSeconds);
		qrcodeCreate.setAction_name(actionName);
		log.info("action_name : {}, scene_str : {}, expire_seconds : {}",
				actionName, sceneStr, expireSeconds);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				qrcodeCreate);
		log.info("qrcode create result : {}", result);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		if (error != null) {
			throw new WechatException(Message.SYSTEM_ERROR);
		}
		WxQrcode wxQrcode = (WxQrcode) JSONObject
				.toBean(result, WxQrcode.class);
		log.info("wxQrcode : {}", wxQrcode);
		return wxQrcode;
	}

	/**
	 * 获取短链接信息
	 * 
	 * @param accessToken
	 * @param longUrl
	 * @return
	 * @throws WechatException
	 */
	public static String getShortUrl(String accessToken, String longUrl)
			throws WechatException {
		ShortUrlCreate uc = new ShortUrlCreate();
		uc.setAccess_token(accessToken);
		uc.setLong_url(longUrl);
		uc.setAction(SHORT_URL_ACTION);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(uc);
		Object error = result.get("short_url");
		String shortUrl = "";
		if (error != null) {
			shortUrl = result.getString("short_url");
		} else {
			shortUrl = result.getString(WeiXinConstant.RETURN_ERROR_INFO_MSG);
		}
		return shortUrl;
	}

}
