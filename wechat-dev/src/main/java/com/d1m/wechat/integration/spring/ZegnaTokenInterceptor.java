package com.d1m.wechat.integration.spring;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.common.Constant;
import com.d1m.wechat.common.ExceptionCode;
import com.d1m.wechat.service.ConfigService;
import com.d1m.wechat.util.MD5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.TreeMap;

@Slf4j
public class ZegnaTokenInterceptor extends HandlerInterceptorAdapter {

    private String arvatoWechatId;

    public void setArvatoWechatId(String arvatoWechatId) {
        this.arvatoWechatId = arvatoWechatId;
    }

    @Autowired
    private ConfigService configService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            log.info("Request from arvato,url:{}, timestamp:{}", request.getRequestURL(), System.currentTimeMillis());
            String token = request.getParameter(Constant.TOKEN);
            String timestamp = request.getParameter(Constant.TIMESTAMP);
            String secret = configService.getConfigValue(Integer.parseInt(arvatoWechatId), "ARVATO", "SECRECT");

            String sign = request.getParameter(Constant.SIGN);
            Assert.notNull(token, "token of zegna is null");
            Assert.notNull(timestamp, "timestamp of zegna is null");
            Assert.notNull(sign, "sign of zegna is null");

            if(timestamp.length() != 10){
                throw new IllegalArgumentException("Timestamp为10位数字");
            }

            int currentMills = (int)(System.currentTimeMillis()/1000L);

            if ((currentMills - Integer.parseInt(timestamp)) > Constant.EXPIRED_MINS * 60){
                throw new Exception("Time expired");
            }

            TreeMap<String,String> treeMap = new TreeMap<String, String>();
            treeMap.put(Constant.TIMESTAMP,timestamp);
            treeMap.put(Constant.TOKEN,token);
            String mySign = MD5.md5Sign(treeMap,"&secret="+secret,Constant.CHARSET_UTF_8);
            mySign = mySign.substring(0,8);
            if (!StringUtils.equals(sign,mySign)) {
                throw new IllegalArgumentException("sign is error");
            }
        } catch (Exception e) {
            // 返回json格式的错误信息
            JSONObject json = new JSONObject();
            json.put("errcode", ExceptionCode.PARAMA_IS_INVALID);
            json.put("errmsg", e.getMessage());
            String ret = json.toJSONString();
            log.error(ret, e);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().append(ret).flush();
            return false;
        }

        return true;
    }

}
