package com.d1m.wechat.integration.spring;

import java.lang.reflect.Method;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.d1m.wechat.common.Constant;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.model.CustomerToken;
import com.d1m.wechat.service.CustomerTokenService;

/**
 * CustomerTokenInterceptor
 *
 * @author f0rb on 2017-02-16.
 */
@Slf4j
public class CustomerTokenInterceptor implements HandlerInterceptor {
    @Resource
    private CustomerTokenService customerTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            if (method.getAnnotation(CustomerTokenCheck.class) != null) {
                String token = request.getParameter(Constant.AC_KEY);
                try {
                    CustomerToken customerToken = customerTokenService.checkToken(token);
                    request.setAttribute(Constant.CUSTOMER_TOKEN, customerToken);
                } catch (WechatException e) {
                    // 返回json格式的错误信息
                    JSONObject json = new JSONObject();
                    json.put("errcode", e.getMessageInfo().getCode());
                    json.put("errmsg", e.getMessageInfo().getName());
                    String ret = json.toJSONString();
                    log.error(ret, e);

                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().append(ret).flush();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
