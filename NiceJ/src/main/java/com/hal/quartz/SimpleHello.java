package com.hal.quartz;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class SimpleHello {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
    SchedulerFactory factory = new StdSchedulerFactory();
    Scheduler scheduler = factory.getScheduler();
    
    JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("job-1", "jobG-1").build();
    Trigger  trigger = TriggerBuilder.newTrigger().withIdentity("trigger-1", "tri-g").startNow().build();
    scheduler.scheduleJob(jobDetail, trigger);
    scheduler.start();
    Thread.currentThread().sleep(1000L);
	}
}
