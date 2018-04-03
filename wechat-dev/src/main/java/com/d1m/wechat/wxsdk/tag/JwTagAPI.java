package com.d1m.wechat.wxsdk.tag;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.wxsdk.core.common.WxstoreUtils;
import com.d1m.wechat.wxsdk.wxbase.wxmedia.JwMediaAPI;
import com.d1m.wechat.wxsdk.wxbase.wxmedia.model.WxArticlesRequestByMaterial;
import com.d1m.wechat.wxsdk.wxbase.wxmedia.model.WxArticlesRespponseByMaterial;
import net.sf.json.JSONObject;
import org.aspectj.apache.bcel.generic.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwTagAPI {

    private static Logger logger = LoggerFactory.getLogger(JwTagAPI.class);

    private static String get_tag_list = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=ACCESS_TOKEN";

    private static String add_tag_to_user = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=ACCESS_TOKEN";


    public static WxTags getTagList(
            String accesstoken) throws WechatException {
        WxTags wxTags = null;
        String requestUrl = get_tag_list.replace("ACCESS_TOKEN",
                accesstoken);

        JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
                null);
        logger.info("weixin response：{}", result.toString());
        if (!result.has("errcode")) {
            // logger.error("获得消息失败！errcode=" + result.getString("errcode")
            // + ",errmsg = " + result.getString("errmsg"));
            // throw new WechatException("获得消息失败！errcode="
            // + result.getString("errcode") + ",errmsg = "
            // + result.getString("errmsg"));
            Map tagMap = new HashMap();
            tagMap.put("tags",WxTags.Tag.class);
            wxTags = (WxTags) JSONObject
                    .toBean(result, WxTags.class,tagMap);
        }
        return wxTags;
    }

    public static void addTagToUser(
            String accesstoken, List<String> openIds, Integer tagId) throws WechatException {
        WxTags wxTags = null;
        String requestUrl = add_tag_to_user.replace("ACCESS_TOKEN",
                accesstoken);
        WxTagRequest wxTagRequest = new WxTagRequest();
        wxTagRequest.setOpenid_list(openIds);
        wxTagRequest.setTagid(tagId);
        JSONObject obj = JSONObject.fromObject(wxTagRequest);

        JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
                obj.toString());
        logger.info("weixin response：{}", result.toString());
    }

}
