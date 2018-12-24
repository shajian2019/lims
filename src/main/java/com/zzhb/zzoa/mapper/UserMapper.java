package com.zzhb.zzoa.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zzhb.zzoa.domain.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

	@Select("SELECT * FROM sys_t_user WHERE username = #{0}")
	public User getUser(String username);
	
	@Update("UPDATE sys_t_user SET recentlogin = now() WHERE u_id = #{u_id}")
	public Integer updateRecentlogin(User user);

	public List<User> getAllUsers(Map<String, String> params);

	public Integer delUserById(Map<String,Object> map);

	public Integer addUser(Map<String,Object> map);

	public Integer updateUser(Map<String,Object> map);

}
