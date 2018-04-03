package com.d1m.wechat.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d1m.wechat.dto.TaskDto;
import com.d1m.wechat.util.AppContextUtils;

/**
 * 无状态，可能并发
 * @author d1m
 */
public class QuartzJob implements Job {
	private Logger log = LoggerFactory.getLogger(QuartzStatefulJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		TaskDto task = (TaskDto) context.getMergedJobDataMap().get("jobBean");
		String className = task.getTaskCategory().getTaskClass();
		try {
			Class<?> clazz = Class.forName(className);
			// 统一使用spring容器管理，方便注入
			Object object = AppContextUtils.getBean(clazz);
			if(object == null){
				object = clazz.newInstance();
			}
			
			if(object instanceof IJob){
				((IJob) object).execute(context, task);
			}else{
				log.error("JobBean must implements BaseJob!");
			}
		} catch (Exception e) {
			log.error("JobBean must be set!");
		}
	}
}
