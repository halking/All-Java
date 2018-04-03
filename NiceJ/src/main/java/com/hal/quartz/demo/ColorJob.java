package com.hal.quartz.demo;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ColorJob implements Job {
    private static Logger log = LoggerFactory.getLogger(ColorJob.class);
    
    // parameter names specific to this job
    public static final String FAVORITE_COLOR = "favorite color";
    public static final String EXECUTION_COUNT = "count";
  
     public  int  counter =1;
     public ColorJob() {
		// TODO Auto-generated constructor stub
	}
     
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
     JobKey jobKey = context.getJobDetail().getKey();
     
     JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
     
     String  favoriteColor = jobDataMap.getString(FAVORITE_COLOR);
     int count = jobDataMap.getInt(EXECUTION_COUNT);
     
     log.info("ColorJob: " + jobKey + " executing at " + new Date() + "\n" +
             "  favorite color is " + favoriteColor + "\n" + 
             "  execution count (from job map) is " + count + "\n" + 
             "  execution count (from job member variable) is " + counter);
     
     count++;
     jobDataMap.put(EXECUTION_COUNT, count);
     
     counter++;
	}

}
