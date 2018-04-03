package com.d1m.wechat.oauth.impl;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.d1m.wechat.oauth.IOauth;
import com.d1m.wechat.util.FileUploadConfigUtil;
import com.d1m.wechat.wxsdk.user.user.model.Wxuser;

@Component
public class ActivityIOauthImpl implements IOauth {

	private Logger log = LoggerFactory.getLogger(ActivityIOauthImpl.class);

	@Override
	public void execute(HttpServletRequest request,
			HttpServletResponse response, Wxuser wuser,
			Map<String, Object> params) {
		try {
			log.info("params : {}", params);
			Integer activityId = (Integer) params.get("activity_id");
			String short_url = (String) params.get("short_url");
			FileUploadConfigUtil config = FileUploadConfigUtil.getInstance();
			String activityFrontUrl = String.format(
					config.getValue("activity_front_url"), activityId,
					wuser.getOpenid(), short_url);
			log.info("activityFrontUrl : {}", activityFrontUrl);
			response.sendRedirect(activityFrontUrl);
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}
	}
}
