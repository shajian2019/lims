package com.zzhb.zzoa.mapper;

import java.util.Map;

import com.zzhb.zzoa.domain.activiti.UserSpr;

public interface UserSprMapper {

	public UserSpr getUserSprs(UserSpr userSpr);

	public Integer replaceSprsById(UserSpr userSpr);

	public Integer updateSprs(Map<String, String> params);

	public Map<String, String> getSprs(Map<String, String> params);

	public void delSprs(Map<String, String> params);
}
