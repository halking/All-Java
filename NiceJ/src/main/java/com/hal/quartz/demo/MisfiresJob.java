package com.hal.quartz.demo;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MisfiresJob implements Job {
	
	  public static final String NUM_EXECUTIONS  = "NumExecutions";
	  public static final String EXECUTION_DELAY = "ExecutionDelay";
	  
	  public MisfiresJob() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		System.err.println("--"+context.getJobDetail().getKey()+"executed["+new Date()+"]");
		
		JobDataMap map = context.getJobDetail().getJobDataMap();
		int executeCount = 0;
		if (map.containsKey(NUM_EXECUTIONS)) {
			 executeCount = map.getInt(NUM_EXECUTIONS);
		}
		executeCount++;
		map.put(NUM_EXECUTIONS, executeCount);
       
		
		//延时设置
		long delay = 5000l;
		if (map.containsKey(EXECUTION_DELAY)) {
			delay = map.getLong(EXECUTION_DELAY);
		}
		
		try {
			Thread.sleep(delay);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		System.out.println("--"+context.getJobDetail().getKey()+" , complete ("+executeCount+")");
	}

}
