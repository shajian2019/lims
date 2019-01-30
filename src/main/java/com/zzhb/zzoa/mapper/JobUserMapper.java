package com.zzhb.zzoa.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Delete;

public interface JobUserMapper {

	public Integer addUserJobs(Map<String, Object> params);

	@Delete("DELETE FROM sys_t_user_job WHERE u_id = #{0}")
	public Integer delUserJobByUid(String u_id);

}
