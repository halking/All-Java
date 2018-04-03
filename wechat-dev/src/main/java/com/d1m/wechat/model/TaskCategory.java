package com.d1m.wechat.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "task_category")
public class TaskCategory {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 任务分类名称
     */
    @Column(name = "cate_name")
    private String cateName;

    /**
     * 任务执行类
     */
    @Column(name = "task_class")
    private String taskClass;

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
     * 任务上下文参数定义
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
     * 获取任务分类名称
     *
     * @return cate_name - 任务分类名称
     */
    public String getCateName() {
        return cateName;
    }

    /**
     * 设置任务分类名称
     *
     * @param cateName 任务分类名称
     */
    public void setCateName(String cateName) {
        this.cateName = cateName == null ? null : cateName.trim();
    }

    /**
     * 获取任务执行类
     *
     * @return task_class - 任务执行类
     */
    public String getTaskClass() {
        return taskClass;
    }

    /**
     * 设置任务执行类
     *
     * @param taskClass 任务执行类
     */
    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass == null ? null : taskClass.trim();
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
     * 获取任务上下文参数定义
     *
     * @return params - 任务上下文参数定义
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置任务上下文参数定义
     *
     * @param params 任务上下文参数定义
     */
    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }
}