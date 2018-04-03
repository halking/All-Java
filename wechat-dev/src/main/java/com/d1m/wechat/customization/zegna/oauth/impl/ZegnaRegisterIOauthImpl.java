package com.d1m.wechat.customization.zegna.oauth.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.d1m.wechat.customization.zegna.ZegnaCons;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.oauth.IOauth;
import com.d1m.wechat.service.MemberService;
import com.d1m.wechat.util.CommonUtils;
import com.d1m.wechat.util.RequestUtils;
import com.d1m.wechat.util.SessionCacheUtil;
import com.d1m.wechat.wxsdk.user.user.model.Wxuser;

import static com.d1m.wechat.util.RequestUtils.getIp;

/**
 * ZegnaRegisterIOauthImpl
 *
 * @author f0rb on 2017-02-13.
 */
@Slf4j
@Component
public class ZegnaRegisterIOauthImpl implements IOauth {

    @Resource
    private MemberService memberService;


    @Override
    public void execute(HttpServletRequest request,
                        HttpServletResponse response, Wxuser wuser,
                        Map<String, Object> params) {
        try {
            log.info("params : {}", params);
            Integer wechatId = (Integer) params.get("wechatId");
            String registerUrl = (String) params.get("registerUrl");
            String homeUrl = (String) params.get("homeUrl");
            String baseUrl = (String) params.get("baseUrl");
            String needBind = (String) params.get("needBind");
            if (StringUtils.isBlank(needBind)) {
                needBind = "true";
            }

            String redirectUrl = (String) params.get("redirectUrl");
            if (StringUtils.isNotBlank(redirectUrl) && !redirectUrl.startsWith("http")) {
                redirectUrl = baseUrl + redirectUrl;
            }
            Member member = memberService.getMemberByOpenId(wechatId,
                                                            wuser.getOpenid());
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

            String redirect;
            if ("true".equalsIgnoreCase(needBind)) {
                if (StringUtils.isNotBlank(member.getMobile())) {
                    // redirect home page
                    redirect = getRedirectUrl(redirectUrl, homeUrl);
                } else {
                    // redirect register page
                    redirect = registerUrl;
                }
            } else {
                redirect = getRedirectUrl(redirectUrl, homeUrl);
            }

            String ip = getIp(request);

            Map<String, String> tokens = SessionCacheUtil.getTokens(member.getId());
            if (tokens != null) {
                SessionCacheUtil.removeMember(member.getId());
            }
            String token = SessionCacheUtil.addMember(member, ip);

            response.addCookie(RequestUtils.createCookie(ZegnaCons.TOKEN, token, 60 * 60 * 24 * 30));

            log.info("redirectUrl : {}", redirect);
            response.sendRedirect(redirect);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    private String getRedirectUrl(String redirectUrl, String defaultUrl) {
        if (StringUtils.isNotBlank(redirectUrl)
                && !StringUtils.contains(redirectUrl, "#!/login")) {
            return CommonUtils.urlDecode(redirectUrl);
        }
        return defaultUrl;
    }

}
