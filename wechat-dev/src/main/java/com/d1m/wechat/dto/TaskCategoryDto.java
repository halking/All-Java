package com.d1m.wechat.dto;

import java.util.Date;

public class TaskCategoryDto {
	private Integer id;
    private String cateName;
    private String taskClass;
    private Date createdAt;
    private Integer creatorId;
    private String params;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCateName() {
		return cateName;
	}
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	public String getTaskClass() {
		return taskClass;
	}
	public void setTaskClass(String taskClass) {
		this.taskClass = taskClass;
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
}
