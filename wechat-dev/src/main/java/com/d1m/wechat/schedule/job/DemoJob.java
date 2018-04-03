package com.d1m.wechat.schedule.job;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.d1m.wechat.schedule.BaseJob;
import com.d1m.wechat.schedule.ExecResult;

@Component
public class DemoJob extends BaseJob {
	private Logger log = LoggerFactory.getLogger(DemoJob.class);
	
	@Override
	public ExecResult run(Map<String, String> params) {
		log.debug("exec DemoJob...");
		ExecResult ret = new ExecResult();
		ret.setStatus(true);
		return ret;
	}
}
