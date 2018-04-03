package com.hal.quartz.demo;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

public class SimpleJob implements Job {

	@Override
	public void execute(JobExecutionContext jobexecutioncontext)
			throws JobExecutionException {
		// TODO Auto-generated method stub
          JobKey  jobKey = jobexecutioncontext.getJobDetail().getKey();
          System.out.println("SimpleJob says: "+jobKey+"  execute at "+new Date());
	}
}
