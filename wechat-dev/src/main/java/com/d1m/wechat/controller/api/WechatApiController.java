package com.d1m.wechat.controller.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.d1m.wechat.common.Constant;
import com.d1m.wechat.integration.spring.CustomerTokenCheck;
import com.d1m.wechat.model.CustomerToken;
import com.d1m.wechat.service.CustomerTokenService;
import com.d1m.wechat.service.impl.RefreshAccessTokenJob;
import com.d1m.wechat.wxsdk.core.req.model.message.IndustryTemplateMessageSend;
import com.d1m.wechat.wxsdk.core.sendmsg.JwTemplateMessageAPI;
import com.d1m.wechat.wxsdk.user.group.JwGroupAPI;
import com.d1m.wechat.wxsdk.user.tag.JwTagApi;

/**
 * OpenIdApiController
 *
 * @author f0rb on 2017-02-14.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class WechatApiController extends ApiController {

    @Resource
    private CustomerTokenService customerTokenService;

    @RequestMapping(value = "ackey", method = RequestMethod.GET)
    public Object getToken(String appid, String appkey) {
        try {
            Map<String, Object> ret = new HashMap<>();

            if (StringUtils.isBlank(appid)) {
                ret.put("errcode", 41003);
                ret.put("errmsg", "appid is required");
            } else if (StringUtils.isBlank(appkey)) {
                ret.put("errcode", 41003);
                ret.put("errmsg", "appkey is required");
            } else {
                CustomerToken customerToken = customerTokenService.createToken(appid, appkey);
                if (customerToken != null) {
                    ret.put("errcode", 0);
                    ret.put(Constant.AC_KEY, customerToken.getToken());
                    ret.put("expires_in", customerToken.getExpiresIn());
                } else {
                    ret.put("errcode", 41003);
                    ret.put("errmsg", "invalid appid or appkey");
                }
            }
            return ret;
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @CustomerTokenCheck
    @RequestMapping(value = "update-group-member", method = RequestMethod.POST)
    public Object updateGroupMember(
            @RequestBody JSONObject json,
            @RequestAttribute("customerToken") CustomerToken customerToken
    ) {
        //CustomerToken checkedToken = customerTokenService.checkToken(ackey);
        String accessToken = RefreshAccessTokenJob.getAccessTokenStr(customerToken.getWechatId());
        return JwGroupAPI.groupMemberMove0(accessToken, json.getString("openid"), json.getString("groupid"));
    }

    @CustomerTokenCheck
    @RequestMapping(value = "members-batch-tagging", method = RequestMethod.POST)
    public Object membersBatchTagging(
            @RequestBody JSONObject json,
            @RequestAttribute("customerToken") CustomerToken customerToken
    ) {
        String accessToken = RefreshAccessTokenJob.getAccessTokenStr(customerToken.getWechatId());
        return JwTagApi.memberBatchTag0(
                accessToken, json.getString("tagId"),
                JSON.parseObject(json.getString("openidList"), new TypeReference<List<String>>() {}));
    }

    @CustomerTokenCheck
    @RequestMapping(value = "members-batch-untagging", method = RequestMethod.POST)
    public Object membersBatchUnTagging(
            @RequestBody JSONObject json,
            @RequestAttribute("customerToken") CustomerToken customerToken
    ) {
        String accessToken = RefreshAccessTokenJob.getAccessTokenStr(customerToken.getWechatId());
        return JwTagApi.memberBatchUnTag0(
                accessToken, json.getString("tagId"),
                JSON.parseObject(json.getString("openidList"), new TypeReference<List<String>>() {}));
    }

    @RequestMapping(value = "batch-update-group-member", method = RequestMethod.POST)
    public Object batchUpdateGroupMember(String ackey, @RequestBody JSONObject json) {
        CustomerToken checkedToken = customerTokenService.checkToken(ackey);
        String accessToken = RefreshAccessTokenJob.getAccessTokenStr(checkedToken.getWechatId());
        List<String> openidList = Arrays.asList(json.getJSONArray("openid_list").toArray(new String[0]));
        return JwGroupAPI.batchGroupMemberMove0(accessToken, openidList, json.getString("groupid"));
    }

    @RequestMapping(value = "message-template-send", method = RequestMethod.POST)
    public Object messageTemplateSend(String ackey, @RequestBody IndustryTemplateMessageSend json) {
        CustomerToken checkedToken = customerTokenService.checkToken(ackey);
        String accessToken = RefreshAccessTokenJob.getAccessTokenStr(checkedToken.getWechatId());
        json.setAccess_token(accessToken);
        return JwTemplateMessageAPI.sendTemplateMsg0(json);
    }
}
