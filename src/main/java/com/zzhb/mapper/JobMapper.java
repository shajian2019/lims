package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.zzhb.domain.Job;

public interface JobMapper {

	public List<Job> getJobs(Map<String, String> params);

	public Integer addJob(Job job);

	@Select("SELECT count(1) FROM sys_t_job WHERE j_name = #{name} and j_id != #{id}")
	public Integer checkJobName(Job job);

	public Integer updateJob(Job job);

	@Delete("DELETE FROM sys_t_job WHERE j_id = #{id}")
	public Integer delJob(Job job);

}
