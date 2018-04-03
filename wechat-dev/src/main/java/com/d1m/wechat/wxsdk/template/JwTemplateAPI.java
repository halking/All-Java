package com.d1m.wechat.wxsdk.template;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.wxsdk.core.req.WeiXinReqService;
import com.d1m.wechat.wxsdk.core.req.model.template.TemplateList;
import com.d1m.wechat.wxsdk.core.req.model.template.TemplateSend;
import com.d1m.wechat.wxsdk.report.JwReportAPI;
import com.d1m.wechat.wxsdk.template.model.TemplateAnalysis;

public class JwTemplateAPI {
	private static Logger log = LoggerFactory.getLogger(JwReportAPI.class);
	
	/**
	 * 获取消息模板列表
	 * 
	 * @param accesstoken
	 * @return
	 * @throws WechatException
	 */
	public static List<TemplateAnalysis> getTemplateList(String accesstoken) throws WechatException{
		List<TemplateAnalysis> list = new ArrayList<TemplateAnalysis>();
		if (accesstoken != null){
			TemplateList templateReq = new TemplateList();
			templateReq.setAccess_token(accesstoken);
			JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(templateReq);
			log.info("getTemplateList return:{}",result.toString());
			if(result.containsKey("template_list")) {
				JSONArray array = result.getJSONArray("template_list");
				for (int i = 0; i < array.size(); i++) {
					JSONObject obj = array.getJSONObject(i);
					TemplateAnalysis ta = new TemplateAnalysis();
					ta.setTitle(obj.getString("title"));
					ta.setTemplateId(obj.getString("template_id"));
					ta.setPrimaryIndustry(obj.getString("primary_industry"));
					ta.setDeputyIndustry(obj.getString("deputy_industry"));
					ta.setContent(obj.getString("content"));
					list.add(ta);
				}
			}
		}
		return list;
	}
	
	/**
	 * 发送模板消息
	 * 
	 * @param accesstoken-接口调用凭证
	 * @param touser-接收者openid
	 * @param templateId-模板ID
	 * @param data-模板数据
	 * @param url-模板跳转链接
	 * @return
	 */
	public static JSONObject sendTemplate(String accesstoken, String touser, 
		String templateId, String data, String url){
		TemplateSend send = new TemplateSend();
		send.setAccess_token(accesstoken);
		send.setTouser(touser);
		send.setTemplate_id(templateId);
		com.alibaba.fastjson.JSONObject dataObj = JSON.parseObject(data);
		send.setData(dataObj);
		if(url !=null){
			send.setUrl(url);
		}
		
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(send);
		return result;
	}

}
