package com.d1m.wechat.oauth.impl;

import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.MemberMemberTag;
import com.d1m.wechat.model.MemberProfile;
import com.d1m.wechat.oauth.IOauth;
import com.d1m.wechat.service.MemberMemberTagService;
import com.d1m.wechat.service.MemberProfileService;
import com.d1m.wechat.service.MemberService;
import com.d1m.wechat.util.Constants;
import com.d1m.wechat.util.SessionCacheUtil;
import com.d1m.wechat.wxsdk.user.user.model.Wxuser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by D1M on 2017/4/27.
 */
@Component
@Slf4j
public class BAAActivityIOauthImpl implements IOauth {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberProfileService memberProfileService;

    public static void addCookie(String ip, HttpServletRequest request,
                                 HttpServletResponse response, Member member, int maxAge) {
        Map<String, String> tokens = SessionCacheUtil.getTokens(member.getId());
        if (tokens != null) {
            SessionCacheUtil.removeMember(member.getId());
        }
        String token = SessionCacheUtil.addMember(member, ip);
        Cookie cookie = new Cookie(Constants.ZEGNATOKEN, token);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    protected String getReallyIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip)
                || StringUtils.equalsIgnoreCase("unknown", ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Override
    public void execute(HttpServletRequest request,
                        HttpServletResponse response, Wxuser wuser,
                        Map<String, Object> params) {
        try {
            log.info("params : {}", params);
            Integer wechatId = (Integer) params.get("wechatId");
            String baseUrl = (String) params.get("baseUrl");
            String bindedUrl = (String) params.get("bindedUrl");
            String notBindedUrl = (String) params.get("notBindedUrl");

            String redirectUrl = (String) params.get("redirectUrl");
            if (StringUtils.isNotBlank(redirectUrl) && !redirectUrl.startsWith("http")) {
                redirectUrl = baseUrl + redirectUrl;
            }

            Member member = memberService.getMemberByOpenId(wechatId, wuser.getOpenid());
            if(member != null) {
                final MemberProfile memberProfile = memberProfileService.getByMemberId(member.getWechatId(), member.getId());
                if(memberProfile != null) {
                    redirectUrl = baseUrl + bindedUrl;
                } else {
                    redirectUrl = baseUrl + notBindedUrl;
                }
                String ip = getReallyIp(request);
                addCookie(ip, request, response, member, 60 * 60 * 24 * 30);
                SessionCacheUtil.addMember(member, ip);
            } else {

            }


            log.info("redirectUrl : {}", redirectUrl);
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }


}