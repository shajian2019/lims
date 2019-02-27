package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zzhb.domain.User;

public interface UserMapper {

	@Select("SELECT * FROM sys_t_user WHERE username = #{0}")
	public User getUser(String username);

	@Select("SELECT * FROM sys_t_user WHERE u_id = #{0}")
	public User getUserById(String u_id);

	@Update("UPDATE sys_t_user SET recentlogin = now() WHERE u_id = #{u_id}")
	public Integer updateRecentlogin(User user);

	public List<Map<String, String>> getAllUsers(Map<String, Object> params);

	@Delete("DELETE FROM sys_t_user where u_id = #{0}")
	public Integer delUser(String u_id);

	public Integer addUser(User user);

	public Integer updateUserLogin(User user);

	public Integer updateUserStatus(User user);

	public Integer updateUser(User user);

	public Integer updateUserByUser(User user);

	public Integer addUrole(Map<String, Integer> map);

	public Integer updateUserRole(Map<String, Integer> map);

	@Delete("DELETE FROM sys_t_user_role WHERE u_id = #{0}")
	public Integer delUserRole(Integer u_id);

	public Integer resetPass(Map<String, String> map);

	public Integer countUserByUserName(Map<String, String> params);

	public List<String> getUsernames(Map<String, List<String>> u_ids);

	public List<String> getUsersIdByPId(@Param("p_id") String p_id);

	public void delUserProcdef(@Param("p_id") String p_id);

	public Integer addUserProcdef(Map<String, Object> params);

	@Update("UPDATE oa_t_user_procdef set p_id = #{newpid} WHERE p_id = #{oldpid}")
	public Integer updateUserProcdef(Map<String, String> params);

	@Delete("DELETE FROM oa_t_user_procdef WHERE u_id like #{0}")
	public Integer delUserProcdefByUid(String u_id);

}
