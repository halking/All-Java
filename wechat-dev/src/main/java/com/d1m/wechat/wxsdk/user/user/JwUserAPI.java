package com.d1m.wechat.wxsdk.user.user;

import java.util.*;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.service.impl.RefreshAccessTokenJob;
import com.d1m.wechat.wxsdk.core.req.WeiXinReqService;
import com.d1m.wechat.wxsdk.core.req.model.user.BatchGetUserInfo;
import com.d1m.wechat.wxsdk.core.req.model.user.UserBaseInfoGet;
import com.d1m.wechat.wxsdk.core.req.model.user.UserInfoListGet;
import com.d1m.wechat.wxsdk.user.user.model.Wxuser;

/**
 * 微信--用户
 *
 * @author lizr
 *
 */
public class JwUserAPI {

	private static Logger log = LoggerFactory.getLogger(JwUserAPI.class);

	/**
	 * 根据user_openid 获取关注用户的基本信息
	 * @param accesstoken
	 * @param user_openid
	 *
	 * @return
	 * @throws WechatException
	 */
	public static Wxuser getWxuser(String accesstoken, String user_openid)
			throws WechatException {
		if (accesstoken != null) {
			UserBaseInfoGet userBaseInfoGet = new UserBaseInfoGet();
			userBaseInfoGet.setAccess_token(accesstoken);
			userBaseInfoGet.setOpenid(user_openid);
			JSONObject result = WeiXinReqService.getInstance()
					.doWeinxinReqJson(userBaseInfoGet);
			log.info("getWxuser : {}", result);
			// 正常返回

		    Wxuser wxuser = null;
			Object error = result.get("errcode");
			wxuser = (Wxuser) JSONObject.toBean(result, Wxuser.class);

			return wxuser;
		}
		return null;
	}
	/**
	 * 根据user_openid 获取关注用户的基本信息
	 * @param wechatId
	 * @param user_openid
	 *
	 * @return
	 * @throws WechatException
	 */
	public static Wxuser getWxuser(Integer wechatId, String user_openid)
			throws WechatException {
		String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
		UserBaseInfoGet userBaseInfoGet = new UserBaseInfoGet();
		userBaseInfoGet.setAccess_token(accessToken);
		userBaseInfoGet.setOpenid(user_openid);
		JSONObject result = WeiXinReqService.getInstance()
				.doWeinxinReqJson(userBaseInfoGet);
		log.info("getWxuser : {}", result);
		// 正常返回

		Wxuser wxuser = null;
		Object error = result.get("errcode");
		wxuser = (Wxuser) JSONObject.toBean(result, Wxuser.class);

		return wxuser;
	}

	/**
	 *
	 * @param wechatId
	 * @param next_openid
	 * @return
	 * @throws WechatException
     */
	public static HashMap<String,Object> getOpenIdList(Integer wechatId,
											String next_openid) throws WechatException {
		String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
		HashMap<String,Object> resultMap=new HashMap<String,Object>();
		if (accessToken != null) {
			UserInfoListGet userInfoListGet = new UserInfoListGet();
			userInfoListGet.setAccess_token(accessToken);
			userInfoListGet.setNext_openid(next_openid);
			JSONObject result = WeiXinReqService.getInstance()
					.doWeinxinReqJson(userInfoListGet);
			Object error = result.get("errcode");
			List<String> openIdList=new ArrayList<String>();

			int total = result.getInt("total");
			int count = result.getInt("count");
			log.info("getWxuser strNextOpenId: error {},total {},count {}", error,total,count);
			String strNextOpenId = result.getString("next_openid");
			log.info("getWxuser strNextOpenId: {}", strNextOpenId);

			JSONObject data = result.getJSONObject("data");
			if (count > 0) {
				JSONArray lstOpenid = data.getJSONArray("openid");

				int iSize = lstOpenid.size();
				for (int i = 0; i < iSize; i++) {
					String openId = lstOpenid.getString(i);
					openIdList.add(openId);
				}

			}
			resultMap.put("nextOpenId",strNextOpenId);
			resultMap.put("openIdList",openIdList);
		}
		return resultMap;
	}
	/**
	 * 获取所有关注用户信息信息
	 * @param next_openid
	 * @param wechatId
	 *
	 * @return
	 * @throws WechatException
	 */
	public static List<Wxuser> getAllWxuser(Integer wechatId,
			String next_openid) throws WechatException {
		String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
		if (accessToken != null) {
			UserInfoListGet userInfoListGet = new UserInfoListGet();
			userInfoListGet.setAccess_token(accessToken);
			userInfoListGet.setNext_openid(next_openid);
			JSONObject result = WeiXinReqService.getInstance()
					.doWeinxinReqJson(userInfoListGet);
			Object error = result.get("errcode");
			List<Wxuser> lstUser = null;
			Wxuser mxuser = null;
			int total = result.getInt("total");
			int count = result.getInt("count");
			log.info("getWxuser strNextOpenId: error {},total {},count {}", error,total,count);
			String strNextOpenId = result.getString("next_openid");
			log.info("getWxuser strNextOpenId: {}", strNextOpenId);

			JSONObject data = result.getJSONObject("data");
			lstUser = new ArrayList<Wxuser>(total);
			if (count > 0) {
				JSONArray lstOpenid = data.getJSONArray("openid");

				int iSize = lstOpenid.size();
				for (int i = 0; i < iSize; i++) {
					String openId = lstOpenid.getString(i);
					mxuser = getWxuser(wechatId, openId);
					lstUser.add(mxuser);
				}

			}
			return lstUser;
		}
		return null;
	}

	public static List<String> getOpenidList(Integer wechatId, String next_openid) throws WechatException {
        List<String> ret = new LinkedList<>();

        String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
		if (accessToken != null) {
			UserInfoListGet userInfoListGet = new UserInfoListGet();
			userInfoListGet.setAccess_token(accessToken);
			userInfoListGet.setNext_openid(next_openid);
			JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(userInfoListGet);
            int count = result.getInt("count");
			if (count > 0) {
                JSONObject data = result.getJSONObject("data");
				String openidJSON = data.getString("openid");
                ret = JSON.parseArray(openidJSON, String.class);
			}
		}
		return ret;
	}

    public static List<Wxuser> batchGetWxUser(List<String> openIdList, int wechatId) throws WechatException {
        List<Wxuser> totalList = new ArrayList<>();

        // 每100个openid生成一个BatchGetUserInfo对象
        List<BatchGetUserInfo> batchGetUserInfoList = BatchGetUserInfo.prepare(openIdList);

        for (BatchGetUserInfo batchGetUserInfo : batchGetUserInfoList) {
            String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
            if (accessToken != null) {
                batchGetUserInfo.setAccess_token(accessToken);
                JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(batchGetUserInfo);

                if (result.containsKey("user_info_list")) {
                    List<Wxuser> userList = JSON.parseArray(result.getString("user_info_list"), Wxuser.class);
                    totalList.addAll(userList);
                } else {
                    log.warn("批量获取用户出错: {}\n待拉取用户的openid{}", result, openIdList);
                }
            }
        }
        return totalList;
    }

}
