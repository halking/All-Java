package com.d1m.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.common.Mapper;

import com.d1m.wechat.dto.TaskDto;
import com.d1m.wechat.mapper.TaskMapper;
import com.d1m.wechat.model.Task;
import com.d1m.wechat.model.enums.TaskStatus;
import com.d1m.wechat.schedule.QuartzJob;
import com.d1m.wechat.schedule.QuartzStatefulJob;
import com.d1m.wechat.service.TaskService;

@Service
public class TaskServiceImpl extends BaseService<Task> implements TaskService {
	private Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Autowired
	private TaskMapper taskMapper;
	
	@Override
	public Mapper<Task> getGenericMapper() {
		return taskMapper;
	}

	/**
	 * 从数据库中获取所有任务列表
	 * @return
	 */
	public List<TaskDto> getAllTask() {
		return taskMapper.getAllTask();
	}

	/**
	 * 添加新任务到数据库中
	 */
	public void addTask(Task task) {
		taskMapper.insertSelective(task);
	}

	/**
	 * 从数据库中查询task
	 */
	public TaskDto getTaskById(int taskId) {
		return taskMapper.getTaskById(taskId);
	}

	/**
	 * 更改任务状态
	 * @throws SchedulerException
	 */
	public void changeStatus(int taskId, String cmd) throws SchedulerException {
		TaskDto task = getTaskById(taskId);
		if (task == null) {
			return;
		}
		if ("shutdown".equals(cmd)) {
			deleteJob(task);
			task.setStatus(TaskStatus.SHUTDOWN.getValue());
		} else if ("startup".equals(cmd)) {
			task.setStatus(TaskStatus.STARTUP.getValue());
			addJob(task);
		}
		Task taskEntity = new Task();
		taskEntity.setId(task.getId());
		taskEntity.setStatus(task.getStatus());
		taskMapper.updateByPrimaryKeySelective(taskEntity);
	}

	/**
	 * 更改任务 cron表达式
	 * @throws SchedulerException
	 */
	public void updateCron(int taskId, String cron) throws SchedulerException {
		TaskDto task = getTaskById(taskId);
		if (task == null) {
			return;
		}
		task.setCronExpression(cron);
		if (TaskStatus.STARTUP.getValue().equals(task.getStatus())) {
			updateJobCron(task);
		}
		Task taskEntity = new Task();
		taskEntity.setId(task.getId());
		taskEntity.setCronExpression(task.getCronExpression());
		taskMapper.updateByPrimaryKeySelective(taskEntity);
	}

	/**
	 * 添加任务
	 * 
	 * @param task
	 * @throws SchedulerException
	 */
	public void addJob(TaskDto task) throws SchedulerException {
		if (task == null || !TaskStatus.STARTUP.getValue().equals(task.getStatus())) {
			return;
		}

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(task.getId()+"", task.getCateId()+"");

		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		// 如果不存在，创建一个
		if (null == trigger) {
			Class<? extends Job> clazz = task.getStateful()==(byte)1 ? QuartzStatefulJob.class : QuartzJob.class;
			JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(task.getId()+"", task.getCateId()+"").build();
			JobDataMap map = jobDetail.getJobDataMap();
			map.put("jobBean", task);
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
			trigger = TriggerBuilder.newTrigger().withIdentity(task.getId()+"", task.getCateId()+"").withSchedule(scheduleBuilder).build();
			scheduler.scheduleJob(jobDetail, trigger);
		} else {
			// Trigger已存在，那么更新相应的定时设置
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		}
	}

	@PostConstruct
	public void init() throws Exception {
		// 从数据库加载任务列表
		List<TaskDto> taskList = taskMapper.getAllTask();
		for (TaskDto task : taskList) {
			log.debug("加载任务："+task.getTaskName());
			addJob(task);
		}
	}

	/**
	 * 获取所有计划中的任务列表
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<TaskDto> getAllJob() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		List<TaskDto> jobList = new ArrayList<TaskDto>();
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				int id = Integer.parseInt(jobKey.getName());
				TaskDto job = taskMapper.getTaskById(id);
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				job.setStatus(triggerState.name());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					job.setCronExpression(cronExpression);
				}
				jobList.add(job);
			}
		}
		return jobList;
	}

	/**
	 * 所有正在运行的job
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<TaskDto> getRunningJob() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
		List<TaskDto> jobList = new ArrayList<TaskDto>(executingJobs.size());
		for (JobExecutionContext executingJob : executingJobs) {
			Trigger trigger = executingJob.getTrigger();
			JobDetail jobDetail = executingJob.getJobDetail();
			JobKey jobKey = jobDetail.getKey();
			int id = Integer.parseInt(jobKey.getName());
			TaskDto job = taskMapper.getTaskById(id);
			Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
			job.setStatus(triggerState.name());
			if (trigger instanceof CronTrigger) {
				CronTrigger cronTrigger = (CronTrigger) trigger;
				String cronExpression = cronTrigger.getCronExpression();
				job.setCronExpression(cronExpression);
			}
			jobList.add(job);
		}
		return jobList;
	}

	/**
	 * 暂停一个job
	 * 
	 * @param task
	 * @throws SchedulerException
	 */
	public void pauseJob(TaskDto task) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(task.getId()+"", task.getCateId()+"");
		scheduler.pauseJob(jobKey);
	}

	/**
	 * 恢复一个job
	 * 
	 * @param task
	 * @throws SchedulerException
	 */
	public void resumeJob(TaskDto task) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(task.getId()+"", task.getCateId()+"");
		scheduler.resumeJob(jobKey);
	}

	/**
	 * 删除一个job
	 * 
	 * @param task
	 * @throws SchedulerException
	 */
	public void deleteJob(TaskDto task) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(task.getId()+"", task.getCateId()+"");
		scheduler.deleteJob(jobKey);

	}

	/**
	 * 立即执行job
	 * 
	 * @param task
	 * @throws SchedulerException
	 */
	public void runAJobNow(TaskDto task) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(task.getId()+"", task.getCateId()+"");
		scheduler.triggerJob(jobKey);
	}

	/**
	 * 更新job时间表达式
	 * 
	 * @param task
	 * @throws SchedulerException
	 */
	public void updateJobCron(TaskDto task) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(task.getId()+"", task.getCateId()+"");
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
		scheduler.rescheduleJob(triggerKey, trigger);
	}
}
