package com.d1m.wechat.schedule.job;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.d1m.wechat.mapper.ConversationIndexMapper;
import com.d1m.wechat.schedule.BaseJob;
import com.d1m.wechat.schedule.ExecResult;

@Slf4j
@Component
public class ResetWeekConversationIndexJob extends BaseJob{
	
	@Autowired
	private ConversationIndexMapper conversationIndexMapper;

	@Override
	public ExecResult run(Map<String, String> params) {
		ExecResult ret = new ExecResult();
        try {
        	conversationIndexMapper.weekDelete();
        	log.info("清除一周前的会员索引成功");
            ret.setStatus(true);
        } catch (Exception e) {
            ret.setStatus(false);
            ret.setMsg(e.getMessage());
            log.error("清除一周前的会话索引" + e.getMessage());
        }
        return ret;
	}

}
