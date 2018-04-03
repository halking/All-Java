//package com.d1m.wechat.wechatclient;
//
//import java.io.IOException;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.client.fluent.Request;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
///**
// * WechatHttp
// *
// * @author f0rb on 2017-05-12.
// */
//@Slf4j
//@Component
//public class WechatTokenClientUtil {
//
//    private static String ACCESS_TOKEN_URL;
//    private static String ACCESS_TOKEN_REFRESH_URL;
//    private static String JSAPI_TICKET_URL;
//    private static String WXCARD_TICKET_URL;
//
//    private static String get(String url) {
//        String ret;
//        try {
//            ret = Request.Get(url).execute().returnContent().asString();
//            if (ret.isEmpty()) {
//                ret = "{\"code\":-2, \"info\":\"response has empty content\"}";
//                log.error("HTTP请求异常! url:" + url);
//            }
//        } catch (IOException e) {
//            log.error("HTTP请求异常! url:" + url, e);
//            JSONObject error = new JSONObject();
//            error.put("code", -1);
//            error.put("info", e.getMessage());
//            ret = error.toJSONString();
//        }
//        log.debug("\nrequest : {}\nresponse: {} ", url, ret);
//
//        JSONObject json = JSON.parseObject(ret);
//        String data = json.getString("data");
//        if (data == null) {// appid, secret错误
//            log.info("Wechat response: [{}]", json.getString("info"));
//            throw new RuntimeException(json.getString("info"));
//        }
//        return data;
//    }
//
//    public static String getAccessToken(String appid, String secret) {
//        return get(String.format(ACCESS_TOKEN_URL, appid, secret));
//    }
//
//    public static String refreshAccessToken(String appid, String secret) {
//        return get(String.format(ACCESS_TOKEN_REFRESH_URL, appid, secret));
//    }
//
//    public static String getJsApiTicket(String appid, String secret) {
//        return get(String.format(JSAPI_TICKET_URL, appid, secret));
//    }
//
//    public static String getCardApiTicket(String appid, String secret) {
//        return get(String.format(WXCARD_TICKET_URL, appid, secret));
//    }
//
//    @Value("${weixin.access_token.url}")
//    void setAccessTokenUrl(String accessTokenUrl) {
//        ACCESS_TOKEN_URL = accessTokenUrl;
//    }
//
//    @Value("${weixin.access_token.refresh.url}")
//    void setAccessTokenRefreshUrl(String accessTokenRefreshUrl) {
//        ACCESS_TOKEN_REFRESH_URL = accessTokenRefreshUrl;
//    }
//
//    @Value("${weixin.ticket.jsapi.url}")
//    void setJsapiTicketUrl(String jsapiTicketUrl) {
//        JSAPI_TICKET_URL = jsapiTicketUrl;
//    }
//
//    @Value("${weixin.ticket.wx_card.url}")
//    void setWxCardTicketUrl(String wxCardTicketUrl) {
//        WXCARD_TICKET_URL = wxCardTicketUrl;
//    }
//}
