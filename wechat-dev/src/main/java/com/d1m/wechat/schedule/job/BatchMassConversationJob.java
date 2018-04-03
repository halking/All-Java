package com.d1m.wechat.schedule.job;

import com.d1m.wechat.mapper.MassConversationBatchMemberMapper;
import com.d1m.wechat.mapper.MassConversationBatchResultMapper;
import com.d1m.wechat.mapper.MemberMapper;
import com.d1m.wechat.model.MassConversationBatchMember;
import com.d1m.wechat.model.MassConversationBatchResult;
import com.d1m.wechat.model.enums.MassConversationResultStatus;
import com.d1m.wechat.model.enums.MsgType;
import com.d1m.wechat.schedule.BaseJob;
import com.d1m.wechat.schedule.ExecResult;
import com.d1m.wechat.service.ConfigService;
import com.d1m.wechat.service.impl.RefreshAccessTokenJob;
import com.d1m.wechat.util.ParamUtil;
import com.d1m.wechat.wxsdk.core.sendmsg.JwSendMessageAPI;
import com.d1m.wechat.wxsdk.core.sendmsg.model.SendMessageResponse;
import com.d1m.wechat.wxsdk.core.sendmsg.model.WxMassMessage;
import com.d1m.wechat.wxsdk.user.user.model.Wxuser;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 批量群发任务类
 */
@Component
public class BatchMassConversationJob extends BaseJob {

	private Logger log = LoggerFactory.getLogger(BatchMassConversationJob.class);

	@Resource
	private MassConversationBatchResultMapper massConversationBatchResultMapper;

	@Resource
	private MassConversationBatchMemberMapper massConversationBatchMemberMapper;

	@Resource
	private ConfigService configService;

	@Resource
	private MemberMapper memberMapper;
	
	@Override
	public ExecResult run(Map<String, String> params) {
		ExecResult ret = new ExecResult();
		StringBuilder sb = new StringBuilder();
		try {
			Integer wechatId = ParamUtil.getInt(params.get("wechatId"), null);
			Integer batchMassId = ParamUtil.getInt(params.get("batchMassId"), null);
			log.info("wechatId : {}, batchMassId : {}", wechatId, batchMassId);

			MassConversationBatchResult param = new MassConversationBatchResult();
			param.setWechatId(wechatId);
			param.setConversationId(batchMassId);
			param.setStatus(MassConversationResultStatus.WAIT_SEND.getValue());
			List<MassConversationBatchResult> batchList = massConversationBatchResultMapper.select(param);
			for(MassConversationBatchResult batchResult : batchList){
				MsgType msgType = MsgType.getByValue(batchResult.getMsgType());

				MassConversationBatchMember batchMember = new MassConversationBatchMember();
				batchMember.setBatchId(batchResult.getId());
				batchMember.setWechatId(batchResult.getWechatId());
				List<MassConversationBatchMember> list = massConversationBatchMemberMapper.select(batchMember);

				Wxuser[] wxusers = getWxusers(batchResult,list);
				WxMassMessage wxMassMessage = (WxMassMessage) JSONObject.toBean(JSONObject.fromObject(batchResult.getMsgContent()),WxMassMessage.class);
				log.info("wxusers size : {}", wxusers.length);
				String accesstoken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);

				SendMessageResponse sendMessageResponse = null;
				if (msgType == MsgType.IMAGE) {
					sendMessageResponse = JwSendMessageAPI.sendMessageToOpenidsWithMedia(accesstoken, wxusers, wxMassMessage);
				} else if (msgType == MsgType.MPNEWS) {
					sendMessageResponse = JwSendMessageAPI.sendMessageToOpenidsWithArticles(accesstoken, wxusers, wxMassMessage);
				} else if (msgType == MsgType.TEXT) {
					sendMessageResponse = JwSendMessageAPI.sendMessageToOpenidsWithText(accesstoken, wxusers, wxMassMessage);
				}
				log.info("Batch sendMessageRespontse : {}", sendMessageResponse);
				if (!StringUtils.equals(sendMessageResponse.getErrcode(), "0")) {
					sb.append(batchResult.getId()).append(":").append(sendMessageResponse.getErrmsg()).append(";");
				}else{
					batchResult.setMsgId(sendMessageResponse.getMsg_id());
					batchResult.setStatus(MassConversationResultStatus.SENDING.getValue());
					massConversationBatchResultMapper.updateByPrimaryKey(batchResult);

					// 分组推送时，更新会员本月群发数
					List<Integer> idList = new ArrayList<Integer>();
					for(MassConversationBatchMember bm:list){
						idList.add(bm.getMemberId());
					}
					if(!idList.isEmpty()){
						memberMapper.updateBatchSendMonth(idList);
					}
				}
				int batchInterval = 20;//秒
				String batchIntervalStr = configService.getConfigValue(wechatId, "MASS_CONVERSATION", "BATCH_INTERVAL");
				if(StringUtils.isNotBlank(batchIntervalStr)){
					batchInterval = Integer.parseInt(batchIntervalStr);
				}
				TimeUnit.SECONDS.sleep(batchInterval);
			}
			ret.setStatus(true);
			ret.setMsg(sb.toString());
		} catch (Exception e) {
			ret.setStatus(false);
			ret.setMsg(e.getMessage());
			log.error("批量群发定时消息失败",e);
		}
		return ret;
	}

	private Wxuser[] getWxusers(MassConversationBatchResult batchResult, List<MassConversationBatchMember> list){
		Wxuser[] wxusers = null;
		if(list!=null&&!list.isEmpty()){
			wxusers = new Wxuser[list.size()];
			for(int i=0; i<list.size();i++){
				Wxuser user = new Wxuser();
				user.setOpenid(list.get(i).getOpenId());
				wxusers[i] = user;
			}
		}
		return wxusers;
	}

}
