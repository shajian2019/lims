package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

import com.zzhb.domain.Task;

public interface TaskMapper {

	public List<Task> getTasks(Map<String, Object> params);

	public Integer updateTask(Task task);

	public Integer addTask(Task task);

}
