package com.hal.quartz.demo;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerTest01 {

	public void run() throws Exception {
	    Logger log = LoggerFactory.getLogger(SimpleTriggerExample.class);
	    log.info("------- Initializing -------------------");
	    // First we must get a reference to a scheduler
	    SchedulerFactory sf = new StdSchedulerFactory();
	    Scheduler sched = sf.getScheduler();
        
	    log.info("------- Initialization Complete --------");
	    log.info("------- Scheduling Jobs ----------------");
	    
	    Date startDate = DateBuilder.nextGivenSecondDate(null, 15);
	    JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity("--job1--", "--gorup1--").build();
	    
	    SimpleTrigger  trigger = (SimpleTrigger)TriggerBuilder.newTrigger()
	    		.withIdentity("--trigger1--", "--triggerG--").startAt(startDate).build();
	    //schedule  it  to run！
	   Date firstdDate = sched.scheduleJob(jobDetail,trigger);
	    log.info(jobDetail.getKey() + " will run at: " + firstdDate + " and repeat: " + trigger.getRepeatCount() + " times, every "
	             + trigger.getRepeatInterval() / 1000 + " seconds");
	    
	    // job2 will only fire once at date/time "ts"
	    jobDetail = newJob(SimpleJob.class).withIdentity("--job2--", "--group1--").build();

	    trigger = (SimpleTrigger) newTrigger().withIdentity("--trigger2--", "group1").startAt(startDate).build();

	    firstdDate = sched.scheduleJob(jobDetail, trigger);
	    log.info(jobDetail.getKey() + " will run at: " + firstdDate + " and repeat: " + trigger.getRepeatCount() + " times, every "
	             + trigger.getRepeatInterval() / 1000 + " seconds");
	    
	    // job3 will run 11 times (run once and repeat 10 more times)
	    // job3 will repeat every 10 seconds
	    jobDetail = newJob(SimpleJob.class).withIdentity("--job3--", "--group1--").build();

	    trigger = newTrigger().withIdentity("--trigger3--", "group1").startAt(startDate)
	        .withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(10)).build();

	    firstdDate = sched.scheduleJob(jobDetail, trigger);
	    log.info(jobDetail.getKey() + " will run at: " + firstdDate + " and repeat: " + trigger.getRepeatCount() + " times, every "
	             + trigger.getRepeatInterval() / 1000 + " seconds");
	    
	    // the same job (job3) will be scheduled by a another trigger
	    // this time will only repeat twice at a 70 second interval
	    
	    trigger = TriggerBuilder.newTrigger().withIdentity("--trigger3--", "--group3--").startAt(firstdDate).withSchedule(
	    		simpleSchedule().withIntervalInSeconds(10).withRepeatCount(2)).forJob(jobDetail).build();
	    
	    firstdDate = sched.scheduleJob(trigger);
	    
	    log.info(trigger.getKey() + " will run(also) at: " + firstdDate + " and repeat: " + trigger.getRepeatCount() + " times, every "
	             + trigger.getRepeatInterval() / 1000 + " seconds");
	    
	    // job5 will run once, five minutes in the future
	    jobDetail = newJob(SimpleJob.class).withIdentity("--job5--", "--group1--").build();

	    trigger = (SimpleTrigger) newTrigger().withIdentity("trigger5", "group1")
	        .startAt(futureDate(5, IntervalUnit.MINUTE)).build();
	    
	    firstdDate = sched.scheduleJob(jobDetail, trigger);
	    log.info(jobDetail.getKey() + " will run at: " + firstdDate + " and repeat: " + trigger.getRepeatCount() + " times, every "
	             + trigger.getRepeatInterval() / 1000 + " seconds");
	    
	    // job6 will run indefinitely, every 40 seconds
	    jobDetail = newJob(SimpleJob.class).withIdentity("--job6--", "--group1--").build();

	    trigger = (SimpleTrigger) newTrigger().withIdentity("trigger6", "group1")
	        .startAt(startDate).withSchedule(simpleSchedule().withIntervalInSeconds(40).repeatForever()).build();
	    
	    firstdDate = sched.scheduleJob(jobDetail, trigger);
	    log.info(jobDetail.getKey() + " will run at: " + firstdDate + " and repeat: " + trigger.getRepeatCount() + " times, every "
	             + trigger.getRepeatInterval() / 1000 + " seconds");
	    log.info("------- Starting Scheduler ----------------");

	    // All of the jobs have been added to the scheduler, but none of the jobs
	    // will run until the scheduler has been started
	    sched.start();

	    log.info("------- Started Scheduler -----------------");
	    
	    // jobs can also be scheduled after start() has been called...
	    // job7 will repeat 20 times, repeat every five minutes
	    jobDetail = newJob(SimpleJob.class).withIdentity("--job7--", "--group1--").build();

	    trigger = newTrigger().withIdentity("--trigger7--", "--group1--").startAt(startDate)
	        .withSchedule(simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20)).build();

	    firstdDate = sched.scheduleJob(jobDetail, trigger);
	    log.info(jobDetail.getKey() + " will run at: " + firstdDate + " and repeat: " + trigger.getRepeatCount() + " times, every "
	             + trigger.getRepeatInterval() / 1000 + " seconds");
	    
	    //    // jobs can be fired directly... (rather than waiting for a trigger)
	    jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity("--job8--", "--group--").storeDurably().build();
	    sched.addJob(jobDetail, true);
	    log.info("'Manually' triggering job8...");
	    sched.triggerJob(JobKey.jobKey("--job8--","--group--"));
	    
	    log.info("------- Waiting five minutes... ------------");
	    try {
	      // wait five minutes to show jobs
	      Thread.sleep(300L * 1000L);
	      // executing...
	    } catch (Exception e) {
	      //
	    }
	    // jobs can be re-scheduled...
	    // job 7 will run immediately and repeat 10 times for every second
	    log.info("------- Rescheduling... --------------------");
	    trigger = newTrigger().withIdentity("--trigger7--", "--group1--").startAt(startDate)
	        .withSchedule(simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20)).build();

	    firstdDate = sched.rescheduleJob(trigger.getKey(), trigger);
	    log.info("job7 rescheduled to run at: " + firstdDate);
	    log.info("------- Shutting Down ---------------------");

	    sched.shutdown(true);

	    log.info("------- Shutdown Complete -----------------");
	    
	    SchedulerMetaData metaData = sched.getMetaData();
	    log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
}

	public static void main(String[] args)  throws Exception{
		// TODO Auto-generated method stub
       TriggerTest01  test01 = new TriggerTest01();
       test01.run();
	}

}
