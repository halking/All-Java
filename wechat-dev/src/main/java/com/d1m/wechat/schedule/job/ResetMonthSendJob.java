package com.d1m.wechat.schedule.job;

import com.d1m.wechat.mapper.MemberMapper;
import com.d1m.wechat.schedule.BaseJob;
import com.d1m.wechat.schedule.ExecResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by d1m on 2016/9/23.
 * 每月月初清空本月群发数
 */
@Component
public class ResetMonthSendJob extends BaseJob {
    private Logger log = LoggerFactory.getLogger(ResetMonthSendJob.class);
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public ExecResult run(Map<String, String> params) {
        ExecResult ret = new ExecResult();
        try {
            memberMapper.resetMonthSend();
            ret.setStatus(true);
        } catch (Exception e) {
            ret.setStatus(false);
            ret.setMsg(e.getMessage());
            log.error("清空本月群发数失败：" + e.getMessage());
        }
        return ret;
    }
}
