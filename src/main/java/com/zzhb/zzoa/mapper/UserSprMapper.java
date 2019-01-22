package com.zzhb.zzoa.mapper;

import java.util.Map;

import com.zzhb.zzoa.domain.activiti.UserSpr;

public interface UserSprMapper {

	public UserSpr getUserSprs(Map<String, String> params);

	public Integer updateSprsById(Map<String,String> params);
}
