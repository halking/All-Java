package com.d1m.wechat.wxsdk.user.tag;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.wxsdk.core.req.WeiXinReqService;
import com.d1m.wechat.wxsdk.core.req.model.user.*;
import com.d1m.wechat.wxsdk.core.util.WeiXinConstant;

/**
 * 微信--标签
 *
 */
public class JwTagApi {
	private static Logger log = LoggerFactory.getLogger(JwTagApi.class);

	/**
	 * 创建标签
	 * 
	 * @param accesstoken
	 * @param name
	 * @return
	 * @throws WechatException
	 */
	public static TagCreate createTag(String accesstoken, String name)
			throws WechatException {
		TagCreate tagCreate = new TagCreate();
		tagCreate.setAccess_token(accesstoken);
		Tag tag = new Tag();
		tag.setName(name);
		tagCreate.setTag(tag);

		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				tagCreate);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		if (error != null) {
			return null;
		}
		TagCreate tc = (TagCreate) JSONObject.toBean(result, TagCreate.class);
		return tc;
	}

	/**
	 * 获取公众号已创建的标签
	 * 
	 * @param accesstoken
	 * @return
	 * @throws WechatException
	 */
	public static List<Tag> getAllTag(String accesstoken)
			throws WechatException {
		TagGet tagGet = new TagGet();
		tagGet.setAccess_token(accesstoken);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				tagGet);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		if (error != null) {
			return null;
		}
		List<Tag> tags = null;
		JSONArray tagArray = result.getJSONArray("tags");
		tags = new ArrayList<Tag>(tagArray.size());
		Tag tag = null;
		for (int i = 0; i < tagArray.size(); i++) {
			tag = (Tag) JSONObject.toBean(tagArray.getJSONObject(i), Tag.class);
			tags.add(tag);
		}
		return tags;
	}

	/**
	 * 编辑标签
	 * 
	 * @param accesstoken
	 * @param tagId
	 * @param tagNewName
	 * @return
	 * @throws WechatException
	 */
	public static String updateTag(String accesstoken, String tagId,
			String tagNewName) throws WechatException {
		TagUpdate tagUpdate = new TagUpdate();
		tagUpdate.setAccess_token(accesstoken);
		Tag tag = new Tag();
		tag.setId(tagId);
		tag.setName(tagNewName);
		tagUpdate.setTag(tag);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				tagUpdate);
		return result.getString("errmsg");
	}

	/**
	 * 删除标签
	 * 
	 * @param accesstoken
	 * @param tagId
	 * @return
	 * @throws WechatException
	 */
	public static String deleteTag(String accesstoken, String tagId)
			throws WechatException {
		TagDelete tagDelete = new TagDelete();
		tagDelete.setAccess_token(accesstoken);
		Tag tag = new Tag();
		tag.setId(tagId);
		tagDelete.setTag(tag);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				tagDelete);
		return result.getString("errmsg");
	}

	/**
	 * 获取标签下粉丝列表
	 * 
	 * @param accesstoken
	 * @param tagId
	 * @param nextOpenId
	 * @return
	 * @throws WechatException
	 */
	public static List<String> getUserByTag(String accesstoken, String tagId,
			String nextOpenId) throws WechatException {
		TagGetUser tagGetUser = new TagGetUser();
		tagGetUser.setTagid(tagId);
		tagGetUser.setNext_openid(nextOpenId);
		tagGetUser.setAccess_token(accesstoken);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				tagGetUser);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		if (error != null) {
			return null;
		}
		log.info("JwTag result:{}",result);
		List<String> openIdList = new ArrayList<String>();
		Integer count = result.getInt("count");
		if (count > 0) {
			String nextOpenIdStr = result.getString("next_openid");
			JSONObject openIdJson = JSONObject.fromObject(result.getString("data"));
			JSONArray openIdArray = openIdJson.getJSONArray("openid");
			int size = openIdArray.size();
			for (int i = 0; i < size; i++) {
				openIdList.add(openIdArray.getString(i));
			}
			if (nextOpenIdStr != null) {
				List<String> openIdNextList=getUserByTag(accesstoken, tagId,
						nextOpenIdStr);
				if(null!=openIdNextList){
					openIdList.addAll(openIdNextList);
				}

			}
		}else{
			return null;
		}
		return openIdList;
	}

	/**
	 * 批量为用户打标签
	 * 
	 * @param accesstoken
	 * @param tagId
	 * @param openIdList
	 * @return
	 */
	public static String memberBatchTag(String accesstoken, String tagId,
			List<String> openIdList) {
		return memberBatchTag0(accesstoken, tagId, openIdList).getString("errcode");
	}

	public static JSONObject memberBatchTag0(String accesstoken, String tagId,
			List<String> openIdList) {
		MemberBatchTag memberBatchTag = new MemberBatchTag();
		memberBatchTag.setAccess_token(accesstoken);
		memberBatchTag.setOpenid_list(openIdList);
		memberBatchTag.setTagid(tagId);
		return WeiXinReqService.getInstance().doWeinxinReqJson(memberBatchTag);
	}

	/**
	 * 批量为用户取消标签
	 * 
	 * @param accesstoken
	 * @param tagId
	 * @param openIdList
	 * @return
	 */
	public static String memberBatchUnTag(String accesstoken, String tagId,
			List<String> openIdList) {
		return memberBatchUnTag0(accesstoken, tagId, openIdList).getString("errcode");
	}

	public static JSONObject memberBatchUnTag0(String accesstoken, String tagId,
			List<String> openIdList) {
		MemberBatchUnTag memberBatchUnTag = new MemberBatchUnTag();
		memberBatchUnTag.setAccess_token(accesstoken);
		memberBatchUnTag.setOpenid_list(openIdList);
		memberBatchUnTag.setTagid(tagId);
        return WeiXinReqService.getInstance().doWeinxinReqJson(memberBatchUnTag);
	}

	/**
	 * 获取用户身上的标签列表
	 * 
	 * @param accesstoken
	 * @param openId
	 * @return
	 */
	public static List<String> getTagByUser(String accesstoken, String openId) {
		UserGetTag userGetTag = new UserGetTag();
		userGetTag.setAccess_token(accesstoken);
		userGetTag.setOpenid(openId);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				userGetTag);
		JSONArray arr = result.getJSONArray("tagid_list");
		List<String> tagIds = new ArrayList<String>();
		for (int i = 0; i < arr.size(); i++) {
			tagIds.add(arr.getString(i));
		}
		return tagIds;
	}

}
