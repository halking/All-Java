package com.d1m.wechat.controller;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.MemberProfile;
import com.d1m.wechat.model.enums.MemberProfileStatus;
import com.d1m.wechat.util.Constants;
import com.d1m.wechat.util.SessionCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.d1m.wechat.model.User;
import com.d1m.wechat.service.MemberProfileService;
import com.d1m.wechat.service.MemberService;
import com.d1m.wechat.service.WechatService;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.util.MessageUtil;

public class BaseController {

    @Autowired
    private MemberProfileService memberProfileService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private WechatService wechatService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected Integer getWechatId(HttpSession session) {
        User user = getUser(session);
        return user.getWechatId();
        // return (Integer) session.getAttribute("wechatId");
    }

    protected User getUser(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
        // return (User) session.getAttribute("user");
    }

    protected Integer getWechatId() {
        User user = getUser();
        return user.getWechatId();
    }

    protected User getUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }

    protected Integer getCompanyId(HttpSession session) {
        User user = getUser(session);
        return user.getCompanyId();
    }

    protected Integer getIsSystemRole(HttpSession session) {
        User user = getUser(session);

        return wechatService.getIsSystemRole(user);
    }

    protected MessageUtil getMessageUtil() {
        return MessageUtil.getInstance();
    }

    protected Integer getResultCode(String msg) {
        return getMessageUtil().getResultCode(msg);
    }

    protected JSONObject representation(Message msg) {
        JSONObject json = new JSONObject();
        json.put("resultCode", msg.getCode().toString());
        json.put("msg", msg.name());
        return json;
    }

    protected JSONObject representation(Message msg, Object data) {
        JSONObject json = new JSONObject();
        json.put("resultCode", msg.getCode().toString());
        json.put("msg", msg.name());
        json.put("data", data);
        return json;
    }

    protected JSONObject representation(Message msg, Object data,
                                        Integer pageSize, Integer pageNum, long count) {
        JSONObject json = new JSONObject();
        json.put("resultCode", msg.getCode().toString());
        json.put("msg", msg.name());
        JSONObject subJson = new JSONObject();
        subJson.put("result", data);
        subJson.put("pageSize", pageSize);
        subJson.put("pageNum", pageNum);
        subJson.put("count", count < 0 ? 0 : count);
        json.put("data", subJson);
        return json;
    }

    protected JSONObject wrapException(Exception e) {
        JSONObject json = new JSONObject();
        Integer resultCode = getResultCode(e.getMessage());
        if (resultCode == null) {
            resultCode = Message.SYSTEM_ERROR.getCode();
        }
        json.put("resultCode", resultCode.toString());
        json.put("msg", e.getMessage());
        logger.error("[" + resultCode + "]" + e.getMessage(), e);
        return json;
    }

    protected JSONObject wrapBookingException(WechatException e) {
        JSONObject json = new JSONObject();
        Integer resultCode = getResultCode(e.getMessage());
        if (resultCode == null) {
            resultCode = Message.SYSTEM_ERROR.getCode();
        }
        json.put("resultCode", resultCode.toString());
        json.put("msg", e.getMessageInfo().getName());
        logger.error("[" + resultCode + "]" + e.getMessage(), e);
        return json;
    }

    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public Locale getLocale(HttpServletRequest request) {
        return RequestContextUtils.getLocale(request);

    }

    protected Member getZegnaMember(HttpServletRequest request) {
        String token = getCookie(request, Constants.ZEGNATOKEN);
        logger.info("token : {}", token);
        if (StringUtils.isNotBlank(token)) {
            Member member = SessionCacheUtil.getMember(token);
            logger.info("token member : {}", (member != null ? member.getId()
                    : null));
            if (member != null) {
                logger.info("wechatId : {}, openId : {}, memberId : {}",
                        member.getWechatId(), member.getOpenId(),
                        member.getId());
                member = memberService.getMember(member.getWechatId(),member.getId());
                MemberProfile memberProfile = memberProfileService
                        .getByMemberId(member.getWechatId(), member.getId());
                logger.info("memberProfile : {}",
                        (memberProfile != null ? memberProfile.getId() : ""));
                if (memberProfile != null) {
                    if (memberProfile.getStatus() == MemberProfileStatus.MULTI_CARD
                            .getValue()) {
                        throw new WechatException(Message.MEMBER_WAIT_BIND_CARD);
                    }
                }
                return member;
            }
        }
        throw new WechatException(Message.MEMBER_NOT_LOGIN);
    }


    protected Member getMember(HttpServletRequest request) {
        String token = getCookie(request, Constants.ZEGNATOKEN);
        logger.info("token : {}", token);
        if (StringUtils.isNotBlank(token)) {
            Member member = SessionCacheUtil.getMember(token);
            logger.info("token member : {}", (member != null ? member.getId()
                    : null));
            if (member != null) {
                logger.info("wechatId : {}, openId : {}, memberId : {}",
                        member.getWechatId(), member.getOpenId(),
                        member.getId());
                member = memberService.getMember(member.getWechatId(),member.getId());
                return member;
            }
        }
        throw new WechatException(Message.MEMBER_NOT_LOGIN);
    }
}
