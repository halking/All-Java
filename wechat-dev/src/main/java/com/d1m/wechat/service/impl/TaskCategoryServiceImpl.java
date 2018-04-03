package com.d1m.wechat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.common.Mapper;

import com.d1m.wechat.mapper.TaskCategoryMapper;
import com.d1m.wechat.model.TaskCategory;
import com.d1m.wechat.service.TaskCategoryService;

@Service
public class TaskCategoryServiceImpl extends BaseService<TaskCategory> implements TaskCategoryService {
	@Autowired
	private TaskCategoryMapper taskCategoryMapper;
	
	@Override
	public Mapper<TaskCategory> getGenericMapper() {
		return taskCategoryMapper;
	}

}
