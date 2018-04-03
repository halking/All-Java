package com.hal.quartz.demo;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hal.quartz.MisfireExample;

public class MisfiresDemo {
    
	
	public void run() throws Exception{
		
		  Logger log = LoggerFactory.getLogger(MisfiresDemo.class);

		    log.info("------- Initializing -------------------");

		    // First we must get a reference to a scheduler
		    SchedulerFactory sf = new StdSchedulerFactory();
		    Scheduler sched = sf.getScheduler();

		    log.info("------- Initialization Complete -----------");

		    log.info("------- Scheduling Jobs -----------");
		    
		    Date startTime = nextGivenSecondDate(null, 15);
		    
		    JobDetail job = JobBuilder.newJob(MisfiresJob.class).withIdentity("--job1--", "--group--").
		    		usingJobData(MisfiresJob.EXECUTION_DELAY,1000l).build();
		    
		   SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("--trigger1--","--group--").startAt(startTime)
				   .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever()).build();
		   
		   Date ft = sched.scheduleJob(job, trigger);
		   
		    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
		             + trigger.getRepeatInterval() / 1000 + " seconds");
		
		    // stateful Job2 will run every three seconds
		    // (but it will delay for ten seconds - and therefore purposely misfire after a few iterations)
		    job = newJob(MisfiresJob.class).withIdentity("statefulJob2", "group1")
		        .usingJobData(MisfiresJob.EXECUTION_DELAY, 10000L).build();

		    trigger = newTrigger()
		        .withIdentity("trigger2", "group1")
		        .startAt(startTime)
		        .withSchedule(simpleSchedule().withIntervalInSeconds(3).repeatForever()
		                          .withMisfireHandlingInstructionNowWithExistingCount()) // set misfire instructions
		        .build();

		    ft = sched.scheduleJob(job, trigger);
		    log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
		             + trigger.getRepeatInterval() / 1000 + " seconds");
		    
		    log.info("------- Starting Scheduler ----------------");

		    // jobs don't start firing until start() has been called...
		    sched.start();

		    log.info("------- waiting 1 minutes -----------------");

		    try {
		      // sleep for ten minutes for triggers to file....
		      Thread.sleep(60L * 1000L);
		    } catch (Exception e) {
		      //
		    }

		    log.info("------- Shutting Down ---------------------");

		    sched.shutdown(true);

		    log.info("------- Shutdown Complete -----------------");

		    SchedulerMetaData metaData = sched.getMetaData();
		    log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
      MisfiresDemo  misfiresDemo  = new MisfiresDemo();
      misfiresDemo.run();
	}

}
