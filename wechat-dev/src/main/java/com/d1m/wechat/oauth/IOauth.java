package com.d1m.wechat.oauth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.d1m.wechat.wxsdk.user.user.model.Wxuser;

public interface IOauth {
	public void execute(HttpServletRequest request, HttpServletResponse response,Wxuser wuser, Map<String,Object> params);
}
