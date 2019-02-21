package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;

import com.zzhb.domain.common.Icon;

public interface IconMapper {

	public List<Icon> getIcons(Map<String, String> params);

	public Integer addIcon(Icon icon);
	
	public Integer updateIcon(Icon icon);
	
	@Delete("DELETE FROM sys_t_icon WHERE i_id = #{0}")
	public Integer delIcon(String id);

	public Icon getIcon(Map<String, String> params);
}
