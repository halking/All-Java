package com.d1m.wechat.schedule.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.d1m.wechat.model.Wechat;
import com.d1m.wechat.service.WechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.d1m.wechat.model.Template;
import com.d1m.wechat.schedule.BaseJob;
import com.d1m.wechat.schedule.ExecResult;
import com.d1m.wechat.service.TemplateService;
import com.d1m.wechat.service.impl.RefreshAccessTokenJob;
import com.d1m.wechat.wxsdk.template.JwTemplateAPI;
import com.d1m.wechat.wxsdk.template.model.TemplateAnalysis;


@Component
public class FetchTemplateJob extends BaseJob{
	
	private Logger log = LoggerFactory.getLogger(FetchTemplateJob.class);

	@Autowired
	private TemplateService templateService;

	@Autowired
	private WechatService wechatService;

	@Override
	public ExecResult run(Map<String, String> params) {
		ExecResult ret = new ExecResult();
		try {
			String accessToken = null;
			List<Wechat> list = wechatService.getWechatList();
			for (Wechat wechat:list) {
				accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechat.getId());
				List<Template> oriList = templateService.list(wechat.getId());
				List<TemplateAnalysis> fetchList = JwTemplateAPI.getTemplateList(accessToken);
				outter: for (TemplateAnalysis ta : fetchList){
					for (Template t : oriList){
						if (t.getTemplateId().equals(ta.getTemplateId())){
							continue outter;
						}
					}
					Template record = new Template();
					record.setTemplateId(ta.getTemplateId());
					record.setTitle(ta.getTitle());
					record.setPrimaryIndustry(ta.getPrimaryIndustry());
					record.setDeputyIndustry(ta.getDeputyIndustry());
					record.setWechatId(wechat.getId());
					record.setParameters(parseParameter(ta.getContent()));
					record.setContent(ta.getContent());
					record.setStatus((byte)1);
					templateService.save(record);
				}
				if (oriList != null){
					outter:	for (Template t : oriList){
						for (TemplateAnalysis ta : fetchList){
							if(ta.getTemplateId().equals(t.getTemplateId())){
								t.setStatus((byte)1);
								templateService.updateNotNull(t);
								continue outter;
							}
						}
						t.setStatus((byte)0);
						templateService.updateNotNull(t);
					}
				}
			
			}
			ret.setStatus(true);
		} catch (Exception e) {
			ret.setStatus(false);
			ret.setMsg(e.getMessage());
			log.error("获取消息模板列表失败：" + e.getMessage());
		}
		return ret;
	}

	private String parseParameter(String content) {
		String[] str1 = content.split(".DATA");
		List<String> list = new ArrayList<String>();
		for(int i=0; i<str1.length-1; i++){
			String[] str2 = str1[i].split("\\{\\{");
			list.add(str2[1]);
		}
		return list.toString();
	}

}
