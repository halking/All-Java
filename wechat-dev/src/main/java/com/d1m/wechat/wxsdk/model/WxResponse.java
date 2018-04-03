package com.d1m.wechat.wxsdk.model;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信Model的基类
 *
 * @author f0rb on 2016-12-01.
 */
@Getter
@Setter
public abstract class WxResponse {

    // 错误码
    private String errcode;
    // 错误信息
    private String errmsg;

    public boolean fail() {
        return errcode != null && !errcode.equals("0");
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
