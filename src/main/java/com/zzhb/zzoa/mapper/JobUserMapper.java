package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

public interface JobUserMapper {

	public Integer addUserJobs(Map<String, Object> params);

	@Delete("DELETE FROM sys_t_user_job WHERE u_id = #{0}")
	public Integer delUserJobByUid(String u_id);

	@Select("SELECT l.j_id,r.j_name FROM sys_t_user_job l LEFT JOIN sys_t_job r on l.j_id = r.j_id where l.u_id = #{0}")
	public List<Map<String, String>> getUserJob(String u_id);

}
