package com.zzhb.zzoa.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.zzhb.zzoa.domain.Role;

public interface RoleMapper {

	@Select("SELECT r.* from  sys_t_role r LEFT JOIN sys_t_user_role ur on r.r_id = ur.r_id WHERE ur.u_id = #{0}")
	public Role getRol(Integer u_id);
	
	public Integer delRoleMenu(Map<String, Object> params);

}
