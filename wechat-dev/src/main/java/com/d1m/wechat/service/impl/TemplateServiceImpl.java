package com.d1m.wechat.service.impl;

import static com.d1m.wechat.util.IllegalArgumentUtil.notBlank;

import java.util.Date;
import java.util.List;

import com.d1m.wechat.util.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.controller.wechat.WechatController;
import com.d1m.wechat.mapper.MemberMapper;
import com.d1m.wechat.mapper.TemplateMapper;
import com.d1m.wechat.mapper.TemplateResultMapper;
import com.d1m.wechat.model.Template;
import com.d1m.wechat.model.TemplateResult;
import com.d1m.wechat.pamametermodel.TemplateEncryptModel;
import com.d1m.wechat.service.TemplateResultService;
import com.d1m.wechat.service.TemplateService;
import com.d1m.wechat.service.WechatService;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.wxsdk.template.JwTemplateAPI;

@Service
public class TemplateServiceImpl extends BaseService<Template> implements TemplateService{
	
	private Logger log = LoggerFactory.getLogger(WechatController.class);
	private static String SALT = "national";
	
	@Autowired
	private TemplateMapper templateMapper;
	
	@Autowired
	private TemplateResultService templateResultService;
	
	@Autowired
	private TemplateResultMapper templateResultMapper;
	
	@Autowired
	private WechatService wechatService;
	
	@Autowired
	private MemberMapper memberMapper;

	@Override
	public Mapper<Template> getGenericMapper() {
		// TODO Auto-generated method stub
		return templateMapper;
	}

	@Override
	public List<Template> list(Integer wechatId) {
		return templateMapper.list(wechatId);
	}

	@Override
	public JSONObject getParams(TemplateEncryptModel templateEncryptModel) {
		String decryptData = Security.decrypt(templateEncryptModel.getData(), SALT);
		log.debug("decrypt dataMap: " + decryptData);
		JSONObject data = JSON.parseObject(decryptData);
		String templateId = data.getString("template_id");
		
		notBlank(templateId, Message.TEMPLATE_ID_NOT_BLANK);
		
		JSONObject ret = new JSONObject();
		String code = "";
		String msg = "";
		String encyptStr = "";
		
		Template record = new Template();
		record.setTemplateId(templateId);
		record = templateMapper.selectOne(record);
		if (record.getStatus() == null || record.getStatus() == 0){
			code = Message.TEMPLATE_WEXIN_NOT_EXIST.getCode().toString();
			msg = Message.TEMPLATE_WEXIN_NOT_EXIST.getName();
		}else{
			String params = templateMapper.selectParamsByTemplateId(templateId);
			JSONObject paramters = new JSONObject();
			paramters.put("paramters", params);
			String paramterStr = JSON.toJSONString(paramters);
			encyptStr = Security.encrypt(paramterStr, SALT);
			code = "0";
			msg = "success";
		}
		
		ret.put("code", code);
		ret.put("msg", msg);
		ret.put("data", encyptStr);
		return ret;
	}

	@Override
	public JSONObject sendToWX(TemplateEncryptModel templateEncryptModel) {
        String decryptData = Security.decrypt(templateEncryptModel.getData(), SALT);
        log.debug("decrypt dataMap: " + decryptData);
        JSONObject jsonData = JSON.parseObject(decryptData);
        String touser = jsonData.getString("touser");
        String templateId = jsonData.getString("template_id");
        String url = jsonData.getString("url");
        String data = jsonData.getString("data");
        Integer wechatId = memberMapper.selectWechatIdByOpenId(touser);
        return sendToWX(wechatId, touser, templateId, url, data);
    }

	@Override
	public synchronized JSONObject sendToWX(Integer wechatId, String touser, String templateId, String url, String data) {
		notBlank(touser, Message.TEMPLATE_OPENID_NOT_BLANK);
		notBlank(templateId, Message.TEMPLATE_ID_NOT_BLANK);
		notBlank(data, Message.TEMPLATE_DATA_NOT_BLANK);
		
		JSONObject ret = new JSONObject();
		String code = "";
		String msg = "";
		String encyptStr = "";
		
		if(wechatId == null){
			code = Message.TEMPLATE_WEXIN_NOT_EXIST.getCode().toString();
			msg = Message.TEMPLATE_WEXIN_NOT_EXIST.getName();
		}else{
			String accesstoken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
			notBlank(accesstoken, Message.TEMPLATE_ACCESSTOKEN_NOT_BLANK);
			net.sf.json.JSONObject result = JwTemplateAPI.sendTemplate(accesstoken, touser, templateId, data, url);
			String errcode = result.getString("errcode");
			String errmsg = result.getString("errmsg");
			code = errcode;
			if(!errmsg.equals("ok")){
				msg = errmsg;
			}else{
				String dataStr = JSON.toJSONString(result);
				encyptStr = Security.encrypt(dataStr, SALT);
				msg = "success";

				TemplateResult record = new TemplateResult();
				record.setTemplateId(templateId);
				record.setOpenId(touser);
				record.setPushAt(new Date());
				record.setWechatId(wechatId);
				record.setData(data);
				if (url != null){
					record.setUrl(url);
				}
				record.setMsgId(result.getString("msgid"));
				templateResultService.save(record);
			}

		}
		
		ret.put("code", code);
		ret.put("msg", msg);
		ret.put("data", encyptStr);
		return ret;
	}

}
