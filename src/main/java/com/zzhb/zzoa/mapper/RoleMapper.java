package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zzhb.zzoa.domain.Role;

public interface RoleMapper {

	@Select("SELECT r.* from  sys_t_role r LEFT JOIN sys_t_user_role ur on r.r_id = ur.r_id WHERE ur.u_id = #{0}")
	public Role getRol(Integer u_id);

	public Integer delRoleMenu(Map<String, Object> params);

	public List<Role> getRoles(Map<String, String> params);

	public Integer updateRole(Map<String, String> params);

	@Select("SELECT u_id FROM sys_t_user_role WHERE r_id = #{r_id}")
	public List<String> getUserIds(Map<String, String> params);
}
