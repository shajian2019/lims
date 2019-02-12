package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zzhb.zzoa.domain.common.Menu;

public interface MenuMapper {

	public Menu getMenu(Map<String, String> params);

	public List<Menu> getAllMenus(Map<String, String> params);

	public List<Map<String, Object>> getMenus(Map<String, String> params);

	public List<String> getIdByParentId(@Param("parentid") String parentid, @Param("r_id") String r_id);

	@Select("SELECT m_url FROM sys_t_menu WHERE m_url IS NOT NULL")
	public List<String> getUrls();

	public Integer updateMenu(Menu menu);

	public Integer insertMenu(Menu menu);

	public Integer delMenus(Map<String, Object> params);
}
