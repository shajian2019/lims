package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

public interface UserOrgJobMapper {

	@Delete("DELETE FROM sys_t_user_org_job WHERE u_id = #{0}")
	public Integer delByUId(String u_id);

	public Integer addUserOrgJob(@Param("list") List<Map<String, String>> params);

}
