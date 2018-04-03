package com.d1m.wechat.customization.zegna.controller;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.d1m.wechat.controller.BaseController;
import com.d1m.wechat.customization.zegna.ZegnaCons;
import com.d1m.wechat.customization.zegna.model.ZegnaMemberProfileModel;
import com.d1m.wechat.customization.zegna.service.ZegnaMemberProfileService;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.service.MemberService;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.util.SessionCacheUtil;

/**
 * ZegnaMemberProfileController
 *
 * @author f0rb on 2017-02-10.
 */
@Slf4j
@Controller
@RequestMapping("/c/zegna/member")
public class ZegnaMemberProfileController extends BaseController {

    @Resource
    private ZegnaMemberProfileService zegnaMemberProfileService;

    @Resource
    private MemberService memberService;

    @RequestMapping(value = "/bind.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject bind(
            @RequestBody(required = false) ZegnaMemberProfileModel model,
            @CookieValue(value = ZegnaCons.TOKEN, required = false) String token
    ) {
        try {
            Member member = getZegnaMember(token);
            if (member == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            model.setMemberId(member.getId());
            model.setWechatId(member.getWechatId());
            model.setOpenId(member.getOpenId());
            zegnaMemberProfileService.bind(model);

            return representation(Message.MEMBER_PROFILE_REGISTER_SUCCESS);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return wrapException(e);
        }
    }

    //@RequestMapping(value = "/get.json", method = RequestMethod.GET)
    //@ResponseBody
    //public JSONObject get(HttpSession session, HttpServletRequest request,
    //                      HttpServletResponse response) {
    //    try {
    //        Member member = getFinishMember(request);
    //        MemberProfileDto dto = zegnaMemberProfileService.getMemberProfile(member);
    //        return representation(Message.MEMBER_PROFILE_GET_SUCCESS, dto);
    //    } catch (Exception e) {
    //        log.error(e.getMessage(),e);
    //        return wrapException(e);
    //    }
    //}
    //
    //@RequestMapping(value = "/update.json", method = RequestMethod.POST)
    //@ResponseBody
    //public JSONObject update(
    //        @RequestBody(required = false) RegisterModel model,
    //        @CookieValue(value = Constants.TOKEN, required = false) String token
    //) {
    //    try {
    //        Member member = getFinishMember(token);
    //        model.setMemberId(member.getId());
    //        model.setWechatId(member.getWechatId());
    //        model.setOpenId(member.getOpenId());
    //        zegnaMemberProfileService.update(model);
    //
    //        MemberProfileDto dto = zegnaMemberProfileService.getMemberProfile(member);
    //        return representation(Message.MEMBER_PROFILE_UPDATE_SUCCESS, dto);
    //    } catch (Exception e) {
    //        log.error(e.getMessage(),e);
    //        return wrapException(e);
    //    }
    //}

    protected Member getZegnaMember(String token) {
        log.info("token : {}", token);
        if (StringUtils.isNotBlank(token)) {
            Member member = SessionCacheUtil.getMember(token);
            log.info("token member : {}", (member != null ? member.getId() : null));
            if (member != null) {
                log.info("wechatId : {}, openId : {}, memberId : {}", member.getWechatId(), member.getOpenId(), member.getId());
                member = memberService.getMember(member.getWechatId(), member.getId());
                return member;
            }
        }
        throw new WechatException(Message.MEMBER_NOT_LOGIN);
    }

}
