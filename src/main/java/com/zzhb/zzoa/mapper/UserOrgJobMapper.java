package com.zzhb.zzoa.mapper;

import org.apache.ibatis.annotations.Delete;

public interface UserOrgJobMapper {

	@Delete("DELETE FROM sys_t_user_org_job WHERE u_id = #{0}")
	public Integer delByUId(String u_id);

}
