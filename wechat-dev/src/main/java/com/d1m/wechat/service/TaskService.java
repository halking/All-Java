package com.d1m.wechat.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.SchedulerException;

import com.d1m.wechat.dto.TaskDto;
import com.d1m.wechat.model.Task;

public interface TaskService extends IService<Task> {
	/**
	 * 从数据库中获取所有任务列表
	 * @return
	 */
	public List<TaskDto> getAllTask();

	/**
	 * 添加新任务到数据库中
	 */
	public void addTask(Task task);

	/**
	 * 从数据库中查询task
	 */
	public TaskDto getTaskById(int taskId);

	/**
	 * 更改任务状态
	 * @throws SchedulerException
	 */
	public void changeStatus(int taskId, String cmd) throws SchedulerException;

	/**
	 * 更改任务 cron表达式
	 * @throws SchedulerException
	 */
	public void updateCron(int taskId, String cron) throws SchedulerException;

	/**
	 * 添加任务
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void addJob(TaskDto task) throws SchedulerException;

	@PostConstruct
	public void init() throws Exception;

	/**
	 * 获取所有计划中的任务列表
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<TaskDto> getAllJob() throws SchedulerException;

	/**
	 * 所有正在运行的job
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<TaskDto> getRunningJob() throws SchedulerException;

	/**
	 * 暂停一个job
	 * 
	 * @param task
	 * @throws SchedulerException
	 */
	public void pauseJob(TaskDto task) throws SchedulerException;

	/**
	 * 恢复一个job
	 * 
	 * @param task
	 * @throws SchedulerException
	 */
	public void resumeJob(TaskDto task) throws SchedulerException;

	/**
	 * 删除一个job
	 * 
	 * @param task
	 * @throws SchedulerException
	 */
	public void deleteJob(TaskDto task) throws SchedulerException;

	/**
	 * 立即执行job
	 * 
	 * @param task
	 * @throws SchedulerException
	 */
	public void runAJobNow(TaskDto task) throws SchedulerException;

	/**
	 * 更新job时间表达式
	 * 
	 * @param task
	 * @throws SchedulerException
	 */
	public void updateJobCron(TaskDto task) throws SchedulerException;
}
