package com.d1m.wechat.schedule.job;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.d1m.wechat.dto.MemberStatusDto;
import com.d1m.wechat.model.Wechat;
import com.d1m.wechat.service.WechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.d1m.wechat.schedule.BaseJob;
import com.d1m.wechat.schedule.ExecResult;
import com.d1m.wechat.service.MemberService;
import com.d1m.wechat.util.DateUtil;

/**
 * 刷新会员活跃度任务
 * @author d1m
 */
@Component
public class UpdateMemberActivityJob extends BaseJob {
	private Logger log = LoggerFactory.getLogger(UpdateMemberActivityJob.class);
	
	@Autowired
	private MemberService memberService;

	@Autowired
	private WechatService wechatService;

	@Override
	public ExecResult run(Map<String, String> params) {
		ExecResult ret = new ExecResult();
		try {
			Date endDate = DateUtil.getDateEnd(DateUtil.parse(DateUtil.getYestoday()));
			List<Wechat> wechatList = wechatService.getWechatList();
			for (Wechat wechat:wechatList) {
				List<MemberStatusDto> list = memberService.getMemberStatus(wechat.getId(), endDate);
				for (MemberStatusDto memberStatusDto : list) {
					memberService.updateMemberActivity(memberStatusDto,endDate);
				}
			}
			ret.setStatus(true);
		} catch (Exception e) {
			ret.setStatus(false);
			ret.setMsg(e.getMessage());
			log.error("刷新会员活跃度失败："+e.getMessage());
		}
		return ret;
	}

}
