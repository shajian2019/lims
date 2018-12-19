package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zzhb.zzoa.domain.common.Menu;

public interface MenuMapper {

	public List<Menu> getAllMenus(Map<String, String> params);

	public List<Menu> getMenus(Map<String, Object> params);

	public List<String> getIdByParentId(@Param("parentid") String parentid,@Param("r_id") String r_id);

	public List<String> getIdByRoleID(Map<String, Object> params);

}
