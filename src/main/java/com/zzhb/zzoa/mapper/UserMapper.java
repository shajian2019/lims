package com.zzhb.zzoa.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zzhb.zzoa.domain.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

	@Select("SELECT * FROM sys_t_user WHERE username = #{0}")
	public User getUser(String username);

	@Select("SELECT * FROM sys_t_user WHERE u_id = #{0}")
	public User getUserById(Integer u_id);

	@Update("UPDATE sys_t_user SET recentlogin = now() WHERE u_id = #{u_id}")
	public Integer updateRecentlogin(User user);

	public List<Map<String, String>> getAllUsers(Map<String, String> params);

	@Delete("DELETE FROM sys_t_user where u_id = #{0}")
	public Integer delUser(String u_id);

	public Integer addUser(User user);

	public Integer updateUser(Map<String, Object> map);

	public Integer updateUserByUser(User user);

	public Integer addUrole(Map<String, Integer> map);

	public Integer updateUserRole(Map<String, Integer> map);

	@Delete("DELETE FROM sys_t_user_role WHERE u_id = #{0}")
	public Integer delUserRole(Integer u_id);

	public Integer resetPass(Map<String, String> map);

	@Select("SELECT COUNT(1) FROM sys_t_user WHERE username = #{0}")
	public Integer getCountByName(String username);
}
