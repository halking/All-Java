package com.d1m.wechat.wxsdk.menu;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.wxsdk.core.req.WeiXinReqService;
import com.d1m.wechat.wxsdk.core.req.model.menu.ConditionalMenuDelete;
import com.d1m.wechat.wxsdk.core.req.model.menu.MatchRule;
import com.d1m.wechat.wxsdk.core.req.model.menu.MenuConfigureGet;
import com.d1m.wechat.wxsdk.core.req.model.menu.MenuCreate;
import com.d1m.wechat.wxsdk.core.req.model.menu.MenuDelete;
import com.d1m.wechat.wxsdk.core.req.model.menu.MenuGet;
import com.d1m.wechat.wxsdk.core.req.model.menu.WeixinButton;
import com.d1m.wechat.wxsdk.core.req.model.menu.config.CustomWeixinButtonConfig;
import com.d1m.wechat.wxsdk.core.req.model.menu.config.WeixinButtonExtend;
import com.d1m.wechat.wxsdk.core.sendmsg.model.WxArticleConfig;
import com.d1m.wechat.wxsdk.core.util.WeiXinConstant;
import com.d1m.wechat.wxsdk.extend.CustomJsonConfig;
import com.d1m.wechat.wxsdk.model.WxMenuHolder;

/**
 * 微信--menu
 * 
 * @author lizr
 * 
 */
public class JwMenuAPI {

	private static Logger log = LoggerFactory.getLogger(JwMenuAPI.class);

	public static String add_conditional_url = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=ACCESS_TOKEN";

	/**
	 * 创建菜单 button 是 一级菜单数组，个数应为1~3个 sub_button 否 二级菜单数组，个数应为1~5个 type 是
	 * 菜单的响应动作类型 name 是 菜单标题，不超过16个字节，子菜单不超过40个字节 key click等点击类型必须
	 * 菜单KEY值，用于消息接口推送，不超过128字节 url view类型必须 网页链接，用户点击菜单可打开链接，不超过256字节
	 * 
	 * @param accessToken
	 * @param button
	 *            的json字符串
	 * @throws WechatException
	 */
	public static String createMenu(String accessToken,
			List<WeixinButton> button, MatchRule matchRule)
			throws WechatException {
		MenuCreate menuCreate = new MenuCreate();
		menuCreate.setAccess_token(accessToken);
		menuCreate.setButton(button);
		menuCreate.setMatchRule(matchRule);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				menuCreate);
		log.info("create menu result : {}", result);
		if (matchRule == null) {// default menu
			Object errorCode = result
					.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
			if (!StringUtils.equals(errorCode.toString(), "0")) {
				throw new WechatException(Message.WEIXIN_HTTPS_REQUEST_ERROR);
			}
			return null;
		} else {// additional menu
			if (!result.containsKey("menuid")) {
				throw new WechatException(Message.WEIXIN_HTTPS_REQUEST_ERROR);
			}
			return result.getString("menuid");
		}
	}

	/**
	 * 获取所有的菜单
	 * 
	 * @param accessToken
	 * @return
	 * @throws WechatException
	 */
	public static List<WeixinButton> getAllMenu(String accessToken)
			throws WechatException {
		MenuGet g = new MenuGet();
		g.setAccess_token(accessToken);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(g);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		List<WeixinButton> lstButton = null;
		JSONObject menu = result.getJSONObject("menu");
		JSONArray buttons = menu.getJSONArray("button");
		JSONArray subButtons = null;
		lstButton = new ArrayList<WeixinButton>();
		WeixinButton btn = null;
		WeixinButton subBtn = null;
		List<WeixinButton> lstSubButton = null;
		for (int i = 0; i < buttons.size(); i++) {
			btn = (WeixinButton) JSONObject.toBean(buttons.getJSONObject(i),
					WeixinButton.class);
			subButtons = buttons.getJSONObject(i).getJSONArray("sub_button");
			if (subButtons != null) {
				lstSubButton = new ArrayList<WeixinButton>();
				for (int j = 0; j < subButtons.size(); j++) {
					subBtn = (WeixinButton) JSONObject.toBean(
							subButtons.getJSONObject(j), WeixinButton.class);
					lstSubButton.add(subBtn);
				}
				btn.setSub_button(lstSubButton);
			}
			lstButton.add(btn);
		}
		return lstButton;
	}

	/**
	 * 获取所有的菜单
	 *
	 * @param accessToken
	 * @return WxMenuGroup
	 */
	public static WxMenuHolder getWxMenuHolder(String accessToken) {
		MenuGet g = new MenuGet();
		g.setAccess_token(accessToken);
		String result = WeiXinReqService.getInstance().doWeinxinReq(g);
        log.debug("查询微信菜单结果: {}", result);
        WxMenuHolder wxMenuHolder = com.alibaba.fastjson.JSON.parseObject(result, WxMenuHolder.class);
        log.debug("查询微信菜单JSON: {}", com.alibaba.fastjson.JSON.toJSONString(wxMenuHolder));
        return wxMenuHolder;
	}

	/**
	 * 删除所有的菜单
	 * 
	 * @param accessToken
	 * @return
	 * @throws WechatException
	 */
	public static String deleteMenu(String accessToken) throws WechatException {
		MenuDelete m = new MenuDelete();
		m.setAccess_token(accessToken);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(m);
		log.info("deleteMenu result {}", result);
		String msg = result.getString(WeiXinConstant.RETURN_ERROR_INFO_MSG);
		return msg;
	}

	/**
	 * 删除指定个性化菜单
	 * 
	 * @param accessToken
	 * @return
	 * @throws WechatException
	 */
	public static String deleteConditionalMenu(String accessToken, String menuId)
			throws WechatException {
		ConditionalMenuDelete cm = new ConditionalMenuDelete();
		cm.setAccess_token(accessToken);
		cm.setMenuid(menuId);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(cm);
		log.info("deleteConditionalMenu result {}", result);
		String msg = result.getString(WeiXinConstant.RETURN_ERROR_INFO_MSG);
		return msg;
	}

	// update-begin--Author:luobaoli Date:20150714 for：增加“获取自定义菜单配置接口”功能接口
	// update-begin--Author:luobaoli Date:20150715 for：优化该方法的处理逻辑
	/**
	 * 获取自定义接口配置
	 * 
	 * @param accessToken
	 * @return
	 * @throws WechatException
	 */
	public static CustomWeixinButtonConfig getAllMenuConfigure(
			String accessToken) throws WechatException {
		MenuConfigureGet cmcg = new MenuConfigureGet();
		cmcg.setAccess_token(accessToken);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				cmcg);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);

		CustomWeixinButtonConfig customWeixinButtonConfig = (CustomWeixinButtonConfig) JSONObject
				.toBean(result, new CustomJsonConfig(
						CustomWeixinButtonConfig.class, "selfmenu_info"));

		JSONObject selfmenuInfo = result.getJSONObject("selfmenu_info");
		if (selfmenuInfo != null && !JSONUtils.isNull(selfmenuInfo)) {
			/** 处理父类菜单 */
			JSONArray buttons = selfmenuInfo.getJSONArray("button");
			List<WeixinButtonExtend> listButton = new ArrayList<WeixinButtonExtend>();
			for (int i = 0; i < buttons.size(); i++) {
				WeixinButtonExtend weixinButtonExtend = (WeixinButtonExtend) JSONObject
						.toBean(buttons.getJSONObject(i), new CustomJsonConfig(
								WeixinButtonExtend.class, "sub_button"));
				/** 处理子类菜单 */
				JSONObject subButtonJsonObj = buttons.getJSONObject(i)
						.getJSONObject("sub_button");
				if (subButtonJsonObj != null
						&& !JSONUtils.isNull(subButtonJsonObj)) {
					JSONArray subButtons = subButtonJsonObj
							.getJSONArray("list");
					if (subButtons != null) {
						List<WeixinButtonExtend> listSubButton = new ArrayList<WeixinButtonExtend>();
						for (int j = 0; j < subButtons.size(); j++) {
							WeixinButtonExtend subBtn = (WeixinButtonExtend) JSONObject
									.toBean(subButtons.getJSONObject(j),
											new CustomJsonConfig(
													WeixinButtonExtend.class,
													"news_info"));
							/** 处理菜单关联的图文消息 */
							JSONObject newsInfoJsonObj = subButtons
									.getJSONObject(j)
									.getJSONObject("news_info");
							if (newsInfoJsonObj != null
									&& !JSONUtils.isNull(newsInfoJsonObj)) {
								JSONArray newsInfos = newsInfoJsonObj
										.getJSONArray("list");
								List<WxArticleConfig> listNewsInfo = new ArrayList<WxArticleConfig>();
								for (int k = 0; k < newsInfos.size(); k++) {
									WxArticleConfig wxArticleConfig = (WxArticleConfig) JSONObject
											.toBean(newsInfos.getJSONObject(k),
													WxArticleConfig.class);
									listNewsInfo.add(wxArticleConfig);
								}
								subBtn.setNews_info(listNewsInfo);
							}
							listSubButton.add(subBtn);
						}
						weixinButtonExtend.setSub_button(listSubButton);
					}
				}
				listButton.add(weixinButtonExtend);
			}
			customWeixinButtonConfig.setSelfmenu_info(listButton);
		}
		return customWeixinButtonConfig;
	}

	// update-end--Author:luobaoli Date:20150715 for：优化该方法的处理逻辑
	// update-end--Author:luobaoli Date:20150714 for：增加“获取自定义菜单配置接口”功能接口

	public static void main(String[] args) {
		String s = "";
		try {
			s = "3DGIfJqqupzTPxvq_P-0ATDC2MDjFLqaz8S41SPmRIqLaA3PSb8FgN_PuhpZ5jEB4D6w7ZNeX3gbC3CfSOAz2wt4DxVKi2HD5BCjoecrB0Q";
			// s =
			// JwTokenAPI.getAccessToken("wx00737224cb9dbc7d","b9479ebdb58d1c6b6efd4171ebe718b5");
			// s =
			// JwTokenAPI.getAccessToken("wx298c4cc7312063df","fbf8cebf983c931bd7c1bee1498f8605");
			System.out.println(s);
			// WeixinButton button = new WeixinButton();
			CustomWeixinButtonConfig cb = JwMenuAPI.getAllMenuConfigure(s);

			// for(WeixinButton bb : b){
			// System.out.println(bb.toString());
			// }
			// List<WeixinButton> sub_button = new ArrayList<WeixinButton>();
			// List<WeixinButton> testsUb = new ArrayList<WeixinButton>();
			// WeixinButton w = new WeixinButton();
			// w.setName("测试菜单");
			// testsUb.add(w);
			//
			// WeixinButton w1 = new WeixinButton();
			// /*
			// "type": "scancode_waitmsg",
			// "name": "扫码带提示",
			// "key": "rselfmenu_0_0",
			// */
			// w1.setName("测试sub菜单");
			// w1.setKey("rselfmenu_0_0");
			// w1.setType("scancode_waitmsg");
			// sub_button.add(w1);
			// w.setSub_button(sub_button);
			//
			//
			// //s = getMenuButtonJson("button",b);
			// /*Gson gson = new Gson();
			// System.out.println(json);*/
			// s= JwMenuAPI.createMenu(s,testsUb);
			// System.out.println(s);
		} catch (WechatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
