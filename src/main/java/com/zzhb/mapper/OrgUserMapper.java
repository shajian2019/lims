package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface OrgUserMapper {

	public List<Map<String, Object>> listOrgUser2();

	@Select("SELECT COUNT(1) FROM oa_t_user_procdef WHERE u_id = #{u_id} and p_id = #{p_id}")
	public Integer countProcDefByUidAndPid(@Param("u_id") String u_id, @Param("p_id") String p_id);

}
