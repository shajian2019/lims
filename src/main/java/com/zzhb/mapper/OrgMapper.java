package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zzhb.domain.Org;

public interface OrgMapper {

	public List<Org> getOrgs(Map<String, String> params);

	public List<Map<String, Object>> getOrgForTree();

	@Select("SELECT count(1) FROM sys_t_org WHERE o_name = #{name} and o_id != #{id} and p_oid = #{parentid}")
	public Integer checkOrgName(Org org);

	public Integer addOrg(Org org);

	public Integer updateOrg(Org org);

	public Integer addUserOrg(Map<String, Object> params);

	public Integer delOrg(@Param("orgs") List<Org> orgs);

	@Select("SELECT o.o_name name FROM sys_t_user_org_job uoj LEFT JOIN sys_t_org o ON uoj.o_id = o.o_id WHERE uoj.u_id = #{0}")
	public List<Org> getUserOrgByUid(String u_id);
}
