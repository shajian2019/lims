package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.zzhb.zzoa.domain.Job;

public interface JobMapper {

	public List<Job> getJobs(Map<String, String> params);

	public Integer addJob(Job job);

	@Select("SELECT count(1) FROM sys_t_job WHERE j_name = #{name} and j_id != #{id}")
	public Integer checkJobName(Job job);

	public Integer updateJob(Job job);

	public List<Map<String, String>> getUsers(Map<String, String> params);

	@Delete("DELETE FROM sys_t_user_job WHERE u_id = #{0}")
	public Integer delUserJobByUid(String u_id);

	public Integer addUserJob(Map<String, Object> params);

	@Delete("DELETE FROM sys_t_user_job WHERE j_id = #{id}")
	public Integer delUserJobByJid(Job job);

	@Delete("DELETE FROM sys_t_job WHERE j_id = #{id}")
	public Integer delJob(Job job);

	public List<Map<String, String>> getAddUsers(Map<String, String> params);

}
