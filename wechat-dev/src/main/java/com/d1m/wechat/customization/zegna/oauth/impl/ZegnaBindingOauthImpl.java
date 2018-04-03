package com.d1m.wechat.customization.zegna.oauth.impl;

import com.alibaba.fastjson.JSON;
import com.d1m.wechat.dto.QrcodeDto;
import com.d1m.wechat.dto.ZegnaBindDto;
import com.d1m.wechat.mapper.MemberProfileMapper;
import com.d1m.wechat.mapper.MemberScanQrcodeMapper;
import com.d1m.wechat.mapper.QrcodeMapper;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.MemberProfile;
import com.d1m.wechat.model.MemberScanQrcode;
import com.d1m.wechat.model.Qrcode;
import com.d1m.wechat.oauth.IOauth;
import com.d1m.wechat.service.MemberService;
import com.d1m.wechat.service.ZegnaBindService;
import com.d1m.wechat.util.*;
import com.d1m.wechat.wxsdk.user.user.model.Wxuser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.d1m.wechat.util.RequestUtils.getIp;

@Slf4j
@Component
public class ZegnaBindingOauthImpl implements IOauth {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ZegnaBindService zegnaBindService;

    @Autowired
    private MemberProfileMapper memberProfileMapper;

    @Autowired
    private MemberScanQrcodeMapper memberScanQrcodeMapper;

    @Autowired
    private QrcodeMapper qrcodeMapper;

    @Override
    public void execute(HttpServletRequest request,
                        HttpServletResponse response, Wxuser wuser,
                        Map<String, Object> params) {
        OutputStream outputStream = null;
        try {
            log.info("params : {}", params);
            Integer wechatId = (Integer) params.get("wechatId");
            String redirectUrl = (String) params.get("redirectUrl");
            String source = (String) params.get("source");
            String baseUrl = (String) params.get("baseUrl");
            String keyword = (String) params.get("keyword");
            Member member = memberService.getMemberByOpenId(wechatId,
                    wuser.getOpenid());
            log.info("params : {}", JSON.toJSONString(member));

            if (member == null) {
                member = new Member();
                member.setActivity((byte) 5);
                member.setBatchsendMonth(0);
                member.setIsSubscribe(false);
                member.setOpenId(wuser.getOpenid());
                member.setWechatId(wechatId);
                member.setCreatedAt(new Date());
                memberService.save(member);
            }
            if (StringUtils.isNotBlank(redirectUrl) && !redirectUrl.startsWith("http")) {
                redirectUrl = baseUrl + redirectUrl;
            }
            List<MemberScanQrcode> scanQrcodeList = null;
            if (StringUtils.equals(keyword, "QRCode") || StringUtils.equals(keyword, "keyword")) {
                scanQrcodeList = memberScanQrcodeMapper.getMemberScanQrcodeList(member.getId(), wechatId);
            }
            MemberProfile memberProfile = memberProfileMapper.getByMemberId(wechatId, member.getId());
            if (StringUtils.isNotBlank(source)) {
                if (memberProfile != null) {
                    memberProfile.setSource(keyword);
                    memberProfile.setKeyword(keyword);
                    if (scanQrcodeList != null) {
                        Qrcode qrcode = qrcodeMapper.getQrcode(wechatId, scanQrcodeList.get(0).getQrcodeId());
                        memberProfile.setKeyword(qrcode.getTicket());
                    }
                    memberProfileMapper.updateByPrimaryKey(memberProfile);
                } else {
                    memberProfile = new MemberProfile();
                    memberProfile.setSource(keyword);
                    memberProfile.setKeyword(keyword);
                    memberProfile.setMemberId(member.getId());
                    memberProfile.setWechatId(wechatId);
                    if (scanQrcodeList != null) {
                        Qrcode qrcode = qrcodeMapper.getQrcode(wechatId, scanQrcodeList.get(0).getQrcodeId());
                        memberProfile.setKeyword(qrcode.getTicket());
                    }
                    memberProfileMapper.insert(memberProfile);
                }
            }
            String ip = getIp(request);
            String token = SessionCacheUtil.addMember(member, ip);

            response.setHeader("Content-type", "application/json;charset=UTF-8");
            response.addCookie(RequestUtils.createCookie(Constants.ZEGNATOKEN, token, 60 * 60 * 24 * 30));

            log.info("redirectUrl : {}", redirectUrl);

            response.sendRedirect(redirectUrl);

        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            }
        }
    }

    private String getRedirectUrl(String redirectUrl, String defaultUrl,String source) {
        if (StringUtils.isNotBlank(redirectUrl)
                && StringUtils.equals(source,"bonus")) {
            return CommonUtils.urlDecode(redirectUrl);
        }
        return defaultUrl;
    }



}
