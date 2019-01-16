package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.zzhb.zzoa.domain.Role;

public interface RoleMapper {

	@Select("SELECT r.* from  sys_t_role r LEFT JOIN sys_t_user_role ur on r.r_id = ur.r_id WHERE ur.u_id = #{0}")
	public Role getRol(Integer u_id);

	@Select("SELECT * from sys_t_role WHERE r_id = #{0}")
	public Role getRolByRid(String r_id);

	@Delete("DELETE FROM sys_t_role WHERE r_id = #{r_id}")
	public Integer delRole(Map<String, Object> params);

	@Delete("DELETE FROM sys_t_user_role WHERE u_id = #{0}")
	public Integer delUserRoleByUId(String u_id);

	@Delete("DELETE FROM sys_t_user_role WHERE r_id = #{0}")
	public Integer delUserRoleByRId(String r_id);

	@Delete("DELETE FROM sys_t_user_role WHERE r_id = #{r_id}")
	public Integer delUserRole(Map<String, Object> params);

	public Integer delRoleMenu(Map<String, Object> params);

	public List<Role> getRoles(Map<String, String> params);

	public Integer updateRole(Map<String, String> params);

	@Select("SELECT u_id FROM sys_t_user_role WHERE r_id = #{r_id}")
	public List<String> getUserIds(Map<String, String> params);

	@Select("SELECT u_id FROM sys_t_user_role WHERE r_id = #{r_id}")
	public List<String> getUIds(Map<String, Object> params);

	@Select("SELECT m_id FROM sys_t_role_menu WHERE r_id = #{r_id}")
	public List<String> getMidsByRId(Map<String, String> params);

	public Integer addRole(Role role);

	public Integer addRoleMenus(Map<String, Object> params);

	@Select("SELECT r_id FROM sys_t_user_role WHERE u_id = #{0}")
	public Integer getRoleIds(Integer u_id);
}
