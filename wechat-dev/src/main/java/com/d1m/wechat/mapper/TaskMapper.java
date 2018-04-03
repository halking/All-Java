package com.d1m.wechat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.d1m.wechat.dto.TaskDto;
import com.d1m.wechat.model.Task;
import com.d1m.wechat.util.MyMapper;

public interface TaskMapper extends MyMapper<Task> {
	List<TaskDto> getAllTask();
	
	TaskDto getTaskById(@Param("id") Integer id);
}