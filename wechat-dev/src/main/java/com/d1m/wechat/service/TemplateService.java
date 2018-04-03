package com.d1m.wechat.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import com.d1m.wechat.model.Template;
import com.d1m.wechat.pamametermodel.TemplateEncryptModel;

public interface TemplateService extends IService<Template>{

	List<Template> list(Integer wechatId);

	JSONObject getParams(TemplateEncryptModel templateEncryptModel);

	JSONObject sendToWX(TemplateEncryptModel templateEncryptModel);

    JSONObject sendToWX(Integer wechatId, String touser, String templateId, String url, String data);

}
