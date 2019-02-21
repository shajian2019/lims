package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface OrgUserMapper {

	@Select("SELECT u.u_id,u.nickname,j.j_name FROM sys_t_user_org org LEFT JOIN sys_t_user u on org.u_id = u.u_id LEFT JOIN sys_t_user_job uj on u.u_id = uj.u_id LEFT JOIN sys_t_job j on j.j_id = uj.j_id WHERE org.o_id = #{0}")
	public List<Map<String, String>> listOrgUser(String o_id);

	@Select("SELECT o.o_id id,o.o_name name,o.p_oid parentid,u.u_id,u.nickname,j.j_name FROM sys_t_org o LEFT JOIN sys_t_user_org_job uoj on o.o_id = uoj.o_id LEFT JOIN sys_t_user u on uoj.u_id = u.u_id LEFT join sys_t_job j on j.j_id = uoj.j_id ORDER BY o.o_sort, j.j_sort")
	public List<Map<String, Object>> listOrgUser2();

	@Select("SELECT COUNT(1) FROM oa_t_user_procdef WHERE u_id = #{u_id} and p_id = #{p_id}")
	public Integer countProcDefByUidAndPid(@Param("u_id") String u_id, @Param("p_id") String p_id);

}
