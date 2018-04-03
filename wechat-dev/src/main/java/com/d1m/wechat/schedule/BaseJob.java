package com.d1m.wechat.schedule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.d1m.wechat.dto.TaskDto;
import com.d1m.wechat.model.Task;
import com.d1m.wechat.service.TaskService;
import com.d1m.wechat.util.AppContextUtils;

public abstract class BaseJob implements IJob {
	private Logger log = LoggerFactory.getLogger(BaseJob.class);
	
	public abstract ExecResult run(Map<String, String> params);
	
	@Override
	public void execute(JobExecutionContext context, TaskDto task) {
		TaskService taskService = AppContextUtils.getBean(TaskService.class);
		Map<String,String> map = new HashMap<String,String>();
		String strParams = task.getParams();
		if(StringUtils.isNotBlank(strParams)){
			List<TaskParam> params = JSON.parseArray(strParams, TaskParam.class);
			for(TaskParam p:params){
				map.put(p.getKey(), p.getValue());
			}
		}
		ExecResult ret = null;
		try {
			long start = System.currentTimeMillis();
			ret = run(map);
			// 增加失败重试机制
			if(ret!=null && !ret.getStatus()){
				// 重试次数
				int retry = map.get("retry")==null?0:Integer.parseInt(map.get("retry"));
				// 重试间隔，单位毫秒，默认5秒
				long interval = map.get("interval")==null?5000:Long.parseLong(map.get("interval"));
				int count = 0;
				while (count<retry){
					log.debug("task-"+task.getTaskName()+" retry ... ("+(count+1)+").");
					ret = run(map);
					if(ret!=null && ret.getStatus()){
						// 重试成功，退出
						break;
					}
					count ++;
					Thread.sleep(interval);
				}
			}
			long end = System.currentTimeMillis();
			log.debug("task-"+task.getTaskName()+" exec success,cost "+(end-start)+" ms.");
		} catch (Exception e) {
			log.error("task-"+task.getTaskName()+" exec failed,msg: "+e.getMessage());
		}
		Task entity = new Task();
		entity.setId(task.getId());
		entity.setLastExecTime(new Date());
		entity.setNextExecTime(context.getNextFireTime());
		
		if(ret==null){
			ret = new ExecResult();
		}
		
		if(ret.getStatus()){
			entity.setLastExecStatus((byte) 1);
		}else{
			entity.setLastExecStatus((byte) 0);
		}
		entity.setLastExecError(ret.getMsg());
		taskService.updateNotNull(entity);
		
		if(context.getNextFireTime()==null){
			try {
				taskService.changeStatus(task.getId(), "shutdown");
			} catch (SchedulerException e) {
				log.error("shutdown task-"+task.getTaskName()+" failed,msg: "+e.getMessage());
			} 
		}
	}
}
