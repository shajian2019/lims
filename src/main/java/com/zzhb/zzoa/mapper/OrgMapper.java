package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;

import com.zzhb.zzoa.domain.Org;

public interface OrgMapper {

	public List<Org> getOrgs(Map<String, String> params);

	public Integer addOrg(Org org);

	public Integer updateOrg(Org org);

	@Delete("DELETE FROM sys_t_user_org WHERE u_id = #{0}")
	public Integer delUserOrgByUid(String u_id);

	public Integer addUserOrg(Map<String, Object> params);

	@Delete("DELETE FROM sys_t_org WHERE o_id = #{0}")
	public Integer delOrg(String o_id);

	@Delete("DELETE FROM sys_t_user_org WHERE o_id = #{0}")
	public Integer delUserOrgByOid(String o_id);

	public List<Map<String, String>> getUsers(Map<String, String> params);
	
	public Org getOrgByUid(String u_id);
	
	public List<Map<String, String>> getAddUsers(Map<String, String> params);

}
