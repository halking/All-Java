package com.d1m.wechat.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.d1m.wechat.controller.BaseController;
import com.d1m.wechat.exception.WechatException;

/**
 * Created on 16/11/25.
 */
@Slf4j
public class ApiController extends BaseController {

    @ExceptionHandler(WechatException.class)
    public ModelAndView handleWechatException(WechatException e) {
        JSONObject json = new JSONObject();
        json.put("errcode", e.getMessageInfo().getCode());
        json.put("errmsg", e.getMessageInfo().getName());
        log.error(json.toJSONString(), e);
        return new ModelAndView(new FastJsonJsonView(), json);
    }

}
