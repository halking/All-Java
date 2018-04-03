package com.d1m.wechat.controller.api;


import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.d1m.wechat.model.Member;
import com.d1m.wechat.pamametermodel.ConversationModel;
import com.d1m.wechat.service.ConfigService;
import com.d1m.wechat.service.ConversationService;
import com.d1m.wechat.service.MemberService;
import com.d1m.wechat.util.Message;

/**
 * PushMaterialApiController2
 *
 * @author f0rb on 2017-07-27.
 */
@Slf4j
@Controller
@RequestMapping("/api/push")
public class PushMaterialApiController extends ApiController {

    @Resource
    private MemberService memberService;
    @Resource
    private ConfigService configService;
    @Resource
    private ConversationService conversationService;

    @RequestMapping(value = "/{group}/{key}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getOutlet(@PathVariable String group, @PathVariable String key, @RequestParam String openId) {
        try {
            Member member = memberService.getMemberByOpenId(null, openId);
            if (member != null) {
                Integer wechatId = member.getWechatId();
                String MATERIAL_ID = configService.getConfigValue(wechatId, group, key);
                if (StringUtils.isNotEmpty(MATERIAL_ID)) {
                    Integer materialId = Integer.parseInt(MATERIAL_ID);
                    ConversationModel conversationModel = new ConversationModel();
                    conversationModel.setMaterialId(materialId);
                    conversationModel.setMemberId(member.getId());
                    conversationService.wechatToMember(wechatId, null, conversationModel);
                } else {
                    log.warn("Config不存在: LBS:OPEN_LOCATION_MATERIAL_ID");
                }
            } else {
                log.warn("openId错误: {}", openId);
            }
            return representation(Message.CONVERSATION_CREATE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }
}
