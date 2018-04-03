package com.d1m.wechat.dto;

import java.util.Date;

public class TaskDto {
	private Integer id;
    private Integer cateId;
    private String taskName;
    private Date startTime;
    private Date endTime;
    private Byte stateful;
    private String cronExpression;
    private Byte lastExecStatus;
    private String lastExecError;
    private Date lastExecTime;
    private Date nextExecTime;
    private String status;
    private Date createdAt;
    private Integer creatorId;
    private String params;
    private TaskCategoryDto taskCategory;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCateId() {
		return cateId;
	}
	public void setCateId(Integer cateId) {
		this.cateId = cateId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Byte getStateful() {
		return stateful;
	}
	public void setStateful(Byte stateful) {
		this.stateful = stateful;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public Byte getLastExecStatus() {
		return lastExecStatus;
	}
	public void setLastExecStatus(Byte lastExecStatus) {
		this.lastExecStatus = lastExecStatus;
	}
	public String getLastExecError() {
		return lastExecError;
	}
	public void setLastExecError(String lastExecError) {
		this.lastExecError = lastExecError;
	}
	public Date getLastExecTime() {
		return lastExecTime;
	}
	public void setLastExecTime(Date lastExecTime) {
		this.lastExecTime = lastExecTime;
	}
	public Date getNextExecTime() {
		return nextExecTime;
	}
	public void setNextExecTime(Date nextExecTime) {
		this.nextExecTime = nextExecTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Integer getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public TaskCategoryDto getTaskCategory() {
		return taskCategory;
	}
	public void setTaskCategory(TaskCategoryDto taskCategory) {
		this.taskCategory = taskCategory;
	}
}
