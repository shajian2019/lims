package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zzhb.domain.Org;

public interface UserOrgJobMapper {

	@Delete("DELETE FROM sys_t_user_org_job WHERE u_id = #{0}")
	public Integer delByUId(String u_id);

	public Integer updateByOId(@Param("orgs") List<Org> orgs);

	public Integer addUserOrgJob(@Param("list") List<Map<String, String>> params);

	@Delete("DELETE FROM sys_t_user_org_job WHERE o_id is null and j_id is null")
	public Integer delEmptyByOId();

	@Select("SELECT uoj.o_id,o.o_name,uoj.j_id,j.j_name FROM sys_t_user_org_job uoj LEFT JOIN sys_t_org o on uoj.o_id = o.o_id LEFT JOIN sys_t_job j on uoj.j_id = j.j_id where u_id = #{0}")
	public List<Map<String, String>> getUserOrgJobs(String u_id);

	@Select("SELECT o_id FROM sys_t_user_org_job WHERE u_id = #{0}")
	public List<String> getUserOrgs(String u_id);
	
}
