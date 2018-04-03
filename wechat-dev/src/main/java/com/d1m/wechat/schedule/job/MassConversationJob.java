package com.d1m.wechat.schedule.job;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.d1m.wechat.model.enums.MassConversationResultStatus;
import com.d1m.wechat.pamametermodel.MassConversationModel;
import com.d1m.wechat.schedule.BaseJob;
import com.d1m.wechat.schedule.ExecResult;
import com.d1m.wechat.service.ConversationService;
import com.d1m.wechat.util.ParamUtil;

@Component
public class MassConversationJob extends BaseJob {

	private Logger log = LoggerFactory.getLogger(MassConversationJob.class);

	@Autowired
	private ConversationService conversationService;
	
	@Override
	public ExecResult run(Map<String, String> params) {
		ExecResult ret = new ExecResult();
		try {
			Integer wechatId = ParamUtil.getInt(params.get("wechatId"), null);
			Integer massId = ParamUtil.getInt(params.get("massId"), null);
			log.info("wechatId : {}, massId : {}", wechatId, massId);
			MassConversationModel massConversationModel = new MassConversationModel();
			massConversationModel.setId(massId);
			massConversationModel.setStatus(MassConversationResultStatus.WAIT_SEND.name());
			conversationService.sendMassConversation(wechatId, null,massConversationModel);
			ret.setStatus(true);
		} catch (Exception e) {
			ret.setStatus(false);
			ret.setMsg(e.getMessage());
			log.error("群发定时消息失败：" + e.getMessage());
		}
		return ret;
	}

}
