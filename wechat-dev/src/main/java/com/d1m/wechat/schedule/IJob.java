package com.d1m.wechat.schedule;

import org.quartz.JobExecutionContext;

import com.d1m.wechat.dto.TaskDto;

public interface IJob {
	public void execute(JobExecutionContext context, TaskDto task);
}
