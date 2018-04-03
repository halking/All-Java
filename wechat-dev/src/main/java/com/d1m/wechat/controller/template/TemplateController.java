package com.d1m.wechat.controller.template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.controller.BaseController;
import com.d1m.wechat.controller.wechat.WechatController;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.pamametermodel.TemplateEncryptModel;
import com.d1m.wechat.service.TemplateService;
import com.d1m.wechat.util.Message;


@Controller
@RequestMapping("/template")
public class TemplateController extends BaseController{
	
	private Logger log = LoggerFactory.getLogger(WechatController.class);
	
	@Autowired
	private TemplateService templateService;
	
	@RequestMapping(value="hola-get.json", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject get(
			@RequestBody(required = true) TemplateEncryptModel templateEncryptModel,
			HttpServletRequest request,HttpServletResponse response,
			HttpSession session){
		try {
			return templateService.getParams(templateEncryptModel);
		} catch (Exception e) {
			log.error(e.getMessage());
			return exception(e);
		}
		
	}
	
	@RequestMapping(value="hola-send.json", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject send(
			@RequestBody(required = true) TemplateEncryptModel templateEncryptModel,
			HttpServletRequest request,HttpServletResponse response,
			HttpSession session){
		try {
			return templateService.sendToWX(templateEncryptModel);
		} catch (Exception e) {
			log.error(e.getMessage());
			return exception(e);
		}
		
	}
	
	protected JSONObject exception(Exception e) {
		JSONObject json = new JSONObject();
		Integer resultCode = getResultCode(e.getMessage());
		if (resultCode == null) {
			resultCode = Message.SYSTEM_ERROR.getCode();
		}
		json.put("code", resultCode.toString());
		json.put("msg", e.getMessage());
		return json;
	}

}
