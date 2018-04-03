package com.d1m.wechat.controller.api;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.d1m.wechat.model.CustomerToken;
import com.d1m.wechat.model.Wechat;
import com.d1m.wechat.service.CustomerTokenService;
import com.d1m.wechat.service.WechatService;
import com.d1m.wechat.util.CacheUtils;

/**
 * AuthApiController
 *
 * @author f0rb on 2017-02-16.
 */
@Slf4j
@Controller
@RequestMapping("/api")
public class AuthApiController extends ApiController {

    private static final String CODE_CACHE_KEY = "wechat_access_token";

    @Resource
    private CustomerTokenService customerTokenService;

    @Resource
    private WechatService wechatService;

    private static String resolveWebUrl(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String urlStr = url.substring(0, url.length() - request.getRequestURI().length()) + "/backend";
        log.info("WEB_URL: {}", urlStr);
        return urlStr;
    }

    @RequestMapping(value = "oauth2-authorize", method = RequestMethod.GET)
    public Object oauth2Authorize(Model model, String ackey, String appid, String scope, String redirect_uri, String state, HttpServletRequest request) {
        CustomerToken checkedToken = customerTokenService.checkToken(ackey);
        if (StringUtils.isBlank(redirect_uri)) {
            //客户不传redirect_uri时, 我们自己使用code获取微信用户的access_token
            redirect_uri = resolveWebUrl(request) + "/api/oauth2-accesstoken";
        }

        String redirect = "https://open.weixin.qq.com/connect/oauth2/authorize#wechat_redirect";
        model.addAttribute("appid", appid);
        model.addAttribute("redirect_uri", resolveWebUrl(request) + "/api/oauth2-redirect?redirect=" +
                                   UriComponentsBuilder.fromHttpUrl(redirect_uri).build().encode());
        model.addAttribute("response_type", "code");
        model.addAttribute("scope", StringUtils.defaultIfEmpty(scope, "snsapi_base"));
        model.addAttribute("state", state);
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "oauth2-redirect", method = RequestMethod.GET)
    public Object oauth2Redirect(Model model, String code, String state, String redirect) {
        model.addAttribute("code", code);
        if (StringUtils.isNotBlank(state)) {
            model.addAttribute("state", state);
        }
        return "redirect:" + redirect;
    }

    @RequestMapping(value = "oauth2-accesstoken", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject oauth2AccessToken(String ackey, String code) throws IOException {
        CustomerToken checkedToken = customerTokenService.checkToken(ackey);
        Wechat wechat = wechatService.getById(checkedToken.getWechatId());
        return JSON.parseObject(getUserAccessToken(wechat.getAppid(), wechat.getAppscret(), code));
    }

    public String getUserAccessToken(String appid, String secret, String code) {
        // 缓存code的返回结果60秒
        Cache codeCache = CacheUtils.getCache(CODE_CACHE_KEY);
        if (codeCache == null) {
            synchronized (this) {
                codeCache = CacheUtils.getCache(CODE_CACHE_KEY);
                if (codeCache == null) {
                    codeCache = CacheUtils.addCache(CODE_CACHE_KEY, 1000, false, false, 60, 60);
                }
            }
        }
        Element codeElement = codeCache.get(code);
        if (codeElement != null) {
            return (String) codeElement.getObjectValue();
        }

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token"
                + "?appid=" + appid
                + "&secret=" + secret
                + "&code=" + code
                + "&grant_type=authorization_code";
        String ret = "{}";
        try {
            ret = Request.Get(url).execute().returnContent().asString();
            codeCache.put(new Element(code, ret));
        } catch (IOException e) {
            log.error("用户access_token获取失败!", e);
        }
        return ret;
    }
}
