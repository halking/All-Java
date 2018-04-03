package com.d1m.wechat.model;

import java.util.Date;
import javax.persistence.*;

public class Task {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 创建用户ID
     */
    @Column(name = "cate_id")
    private Integer cateId;

    /**
     * 任务分类名称
     */
    @Column(name = "task_name")
    private String taskName;

    /**
     * 任务开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 任务结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 是否有状态(0:无,1:有)
     */
    @Column(name = "stateful")
    private Byte stateful;
    
    /**
     * 时间表达式
     */
    @Column(name = "cron_expression")
    private String cronExpression;

    /**
     * 最后执行状态(0:失败,1:成功)
     */
    @Column(name = "last_exec_status")
    private Byte lastExecStatus;
    
    /**
     * 最后执行错误信息
     */
    @Column(name = "last_exec_error")
    private String lastExecError;
    
    /**
     * 最后执行时间
     */
    @Column(name = "last_exec_time")
    private Date lastExecTime;

    /**
     * 下次执行时间
     */
    @Column(name = "next_exec_time")
    private Date nextExecTime;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * 创建用户ID
     */
    @Column(name = "creator_id")
    private Integer creatorId;

    /**
     * 任务上下文参数值
     */
    private String params;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取创建用户ID
     *
     * @return cate_id - 创建用户ID
     */
    public Integer getCateId() {
        return cateId;
    }

    /**
     * 设置创建用户ID
     *
     * @param cateId 创建用户ID
     */
    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    /**
     * 获取任务分类名称
     *
     * @return task_name - 任务分类名称
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * 设置任务分类名称
     *
     * @param taskName 任务分类名称
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    /**
     * 获取任务开始时间
     *
     * @return start_time - 任务开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置任务开始时间
     *
     * @param startTime 任务开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取任务结束时间
     *
     * @return end_time - 任务结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置任务结束时间
     *
     * @param endTime 任务结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取执行状态
     *
     * @return stateful - 是否有状态
     */
    public Byte getStateful() {
		return stateful;
	}

    /**
     * 设置执行状态
     *
     * @param stateful 是否有状态
     */
	public void setStateful(Byte stateful) {
		this.stateful = stateful;
	}

	/**
     * 获取时间表达式
     *
     * @return cron_expression - 时间表达式
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * 设置时间表达式
     *
     * @param cronExpression 时间表达式
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();
    }

    /**
     * 获取最后执行状态
     *
     * @return lastExecStatus - 最后执行状态
     */
    public Byte getLastExecStatus() {
		return lastExecStatus;
	}

    /**
     * 设置最后执行状态
     *
     * @param lastExecStatus 最后执行状态
     */
	public void setLastExecStatus(Byte lastExecStatus) {
		this.lastExecStatus = lastExecStatus;
	}
	
	/**
     * 获取最后执行错误信息
     *
     * @return lastExecError - 最后执行错误信息
     */
    public String getLastExecError() {
		return lastExecError;
	}

    /**
     * 设置最后执行错误信息
     *
     * @param lastExecError 最后执行错误信息
     */
	public void setLastExecError(String lastExecError) {
		this.lastExecError = lastExecError;
	}
	
    /**
     * 获取最后执行时间
     *
     * @return last_exec_time - 最后执行时间
     */
    public Date getLastExecTime() {
        return lastExecTime;
    }

    /**
     * 设置最后执行时间
     *
     * @param lastExecTime 最后执行时间
     */
    public void setLastExecTime(Date lastExecTime) {
        this.lastExecTime = lastExecTime;
    }

    /**
     * 获取下次执行时间
     *
     * @return next_exec_time - 下次执行时间
     */
    public Date getNextExecTime() {
        return nextExecTime;
    }

    /**
     * 设置下次执行时间
     *
     * @param nextExecTime 下次执行时间
     */
    public void setNextExecTime(Date nextExecTime) {
        this.nextExecTime = nextExecTime;
    }

    /**
     * 获取任务状态
     *
     * @return status - 任务状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置任务状态
     *
     * @param status 任务状态
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 获取创建时间
     *
     * @return created_at - 创建时间
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置创建时间
     *
     * @param createdAt 创建时间
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取创建用户ID
     *
     * @return creator_id - 创建用户ID
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * 设置创建用户ID
     *
     * @param creatorId 创建用户ID
     */
    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * 获取任务上下文参数值
     *
     * @return params - 任务上下文参数值
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置任务上下文参数值
     *
     * @param params 任务上下文参数值
     */
    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }
}