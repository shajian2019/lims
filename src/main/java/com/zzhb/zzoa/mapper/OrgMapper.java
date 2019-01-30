package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zzhb.zzoa.domain.Org;

public interface OrgMapper {

	public List<Org> getOrgs(Map<String, String> params);

	@Select("SELECT count(1) FROM sys_t_org WHERE o_name = #{name} and o_id != #{id} and p_oid = #{parentid}")
	public Integer checkOrgName(Org org);

	public Integer addOrg(Org org);

	public Integer updateOrg(Org org);


	public Integer addUserOrg(Map<String, Object> params);

	public Integer delOrg(@Param("orgs") List<Org> orgs);

	public Integer delUserOrgByOid(@Param("orgs") List<Org> orgs);

	public List<Map<String, String>> getUsers(Map<String, String> params);

	public List<Org> getOrgByUid(String u_id);

	public List<Map<String, String>> getAddUsers(Map<String, String> params);

}
