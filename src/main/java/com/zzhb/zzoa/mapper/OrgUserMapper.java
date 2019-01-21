package com.zzhb.zzoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zzhb.zzoa.domain.User;

public interface OrgUserMapper {

	@Select("SELECT u.* FROM sys_t_user_org org LEFT JOIN sys_t_user u on org.u_id = u.u_id WHERE org.o_id = #{0}")
	public List<User> listOrgUser(String o_id);

	@Select("SELECT COUNT(1) FROM sys_t_user_procdef WHERE u_id = #{u_id} and p_id = #{p_id}")
	public Integer countProcDefByUidAndPid(@Param("u_id") String u_id, @Param("p_id") String p_id);

}
