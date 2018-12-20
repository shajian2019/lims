package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import com.zzhb.zzoa.domain.common.Icon;

public interface IconMapper {
	
	public List<Icon> getIcons(Map<String, String> params);

}
